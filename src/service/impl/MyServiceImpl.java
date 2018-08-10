package service.impl;

import annotation.Service;
import service.MyService;

/**
 * @author zhangguoli.cn
 * @date 2018/8/10
 */
@Service("myServiceImpl")
public class MyServiceImpl implements MyService {

    @Override
    public String get() {
        return "di success";
    }
}
