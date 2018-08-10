package controller;

import annotation.Controller;
import annotation.Qualifier;
import annotation.RequestMapping;
import service.MyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * parseUrl对应的URL为:
 *      project/@Controller/@RequestMapping
 *      即SpringSimple/myController/get
 *
 * @author zhangguoli.cn
 * @date 2018/8/10
 */
@Controller("myController")
public class MyController {

    @Qualifier("myServiceImpl")
    MyService myService;

    @RequestMapping("get")
    public void parseUrl(HttpServletRequest request, HttpServletResponse response){
        //myService不为null且能输出"di success"，则说明注入成功
        System.out.println(myService.get());
    }
}
