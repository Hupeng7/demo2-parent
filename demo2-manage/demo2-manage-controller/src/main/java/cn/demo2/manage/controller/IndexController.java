package cn.demo2.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by admin on 2017/7/24.
 */
@Controller
public class IndexController {
    @RequestMapping("/page/{index}")
    public String toIndex(@PathVariable String index){
        return index;
    }
}
