package cn.huimin.erpplat.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by guochun on 2016/7/7.
 */
@Controller
@RequestMapping("start")
@SpringBootApplication
public class HelloWordController {


    @RequestMapping(value = "hello/{id}", method = RequestMethod.GET)
    public String helloWorld(@PathVariable("id") String id, Model model){
        model.addAttribute("username", id);
        return "hello";
    }

}
