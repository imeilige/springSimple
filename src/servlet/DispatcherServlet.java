package servlet;

import annotation.Controller;
import annotation.Qualifier;
import annotation.RequestMapping;
import annotation.Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.*;

/**
 * @author zhangguoli.cn
 * @WebServlet也可以通过web.xml配置Servlet实现 此处用到大量反射的内容
 * @date 2018/8/10
 */


@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    /**
     * 保存basePackage目录下扫描出来的各个类
     */
    private List<String> classNames = new ArrayList<>();

    /**
     * 保存带有特定注解(@Controller、@Service)的class对象
     */
    private Map<String,Object> instanceMap = new HashMap<>();

    /**
     * 保存和URL相关的方法
     */
    private Map<String,Method> methodMap = new HashMap<>();

    /**
     * 在这里完成Spring IOC的3个过程
     * 1、找到bean
     * 2、注册bean
     * 3、注入bean
     *
     * mvc()用来处理URL
     * 简单起见均用Exception代替了
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {

        try {
            scanBean("");
            filterAndInstance();
            springDi();
            mvc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     *  1、扫描basePackage目录下的class保存到classNames
     *  @param basePackage
     */
    private void scanBean(String basePackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + basePackage.replaceAll("\\.", "/"));
        if(url == null){
            return;
        }
        String path = url.getFile();
        File file = new File(path);
        String[] files = file.list();
        for (String strFile : files) {
            File f = new File(path + strFile);
            String fName = f.getName();
            if(basePackage.length()!=0){
                fName = basePackage + "." + fName;
            }
            if (f.isDirectory()) {
                scanBean(fName);
            } else {
                classNames.add(fName);
            }
        }
    }

    /**
     * 2、通过反射生成bean、将bean注册到instanceMap
     *  简单起见此处只注册带有@Controller、@Service的注解的class
     */
    private void filterAndInstance() throws Exception {
        if (classNames.size() == 0) {
            return;
        }
        for (String className : classNames) {
            Class clazz = Class.forName(className.replace(".class",""));
            //clazz类上存在@Controller注解
            if(clazz.isAnnotationPresent(Controller.class)){
                //通过反射生成对象
                Object instance = clazz.newInstance();
                //获取注解的value
                String key = ((Controller)clazz.getAnnotation(Controller.class)).value();
                instanceMap.put(key,instance);
            }else if(clazz.isAnnotationPresent(Service.class)){
                Object instance = clazz.newInstance();
                String key = ((Service)clazz.getAnnotation(Service.class)).value();
                instanceMap.put(key,instance);
            }else{
                continue;
            }
        }
    }

    /**
     * 3、注册bean
     * @throws IllegalAccessException
     */
    private void springDi() throws IllegalAccessException{
        if(instanceMap.size()==0){
            return;
        }
        for(Map.Entry<String,Object> entry:instanceMap.entrySet()){
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for(Field field:fields){
                if(field.isAnnotationPresent(Qualifier.class)){
                    String key = field.getAnnotation(Qualifier.class).value();
                    field.setAccessible(true);
                    field.set(entry.getValue(),instanceMap.get(key));
                }
            }
        }

    }

    /**
     * 4、处理带有@Controller的类和带有@RequestMapping的方法拼接到URL
     */
    private void mvc() throws Exception{
        if(instanceMap.size()==0){
            return;
        }
        for(Map.Entry<String,Object> entry: instanceMap.entrySet()){
            if(entry.getValue().getClass().isAnnotationPresent(Controller.class)){
                String ctrUrl = entry.getValue().getClass().getAnnotation(Controller.class).value();
                Method[] methods = entry.getValue().getClass().getMethods();
                for(Method method:methods){
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        String reqUrl = method.getAnnotation(RequestMapping.class).value();
                        String disPatchUrl = "/" + ctrUrl + "/" + reqUrl;
                        methodMap.put(disPatchUrl,method);
                    }
                }
            }else{
                continue;
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        String requestDispatcherPath =  req.getRequestURI().replaceAll(req.getContextPath(),"");
        Method method = methodMap.get(requestDispatcherPath);
        Object instance = instanceMap.get(requestDispatcherPath.split("/")[1]);
        try {
            method.invoke(instance,req,resp);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        doGet(req,resp);
    }
}
