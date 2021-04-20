package com.lnjecit.springboothelloworld.controller;

import com.lnjecit.springboothelloworld.bean.CDPlayer;
import com.lnjecit.springboothelloworld.bean.CDPlayerConfig;
import com.lnjecit.springboothelloworld.bean.CompactDisc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://localhost:8080/helloWorld/sayHello?name=xxg
 * http://localhost:8080/helloWorld/sayHello2?name=xxg
 * @author
 * @create 2018-04-16 16:06
 **/
@RequestMapping("/helloWorld")
@RestController
public class HelloWorldController {
    
    @Autowired
    private CDPlayerConfig cdPlayerConfig;
    
    
    @RequestMapping("/sayHello")
    public String sayHello(@RequestParam("name") String name) {
        return "Hi, " + name;
    }
    
    @RequestMapping("/sayHello2")
    public String sayHello2(@RequestParam("name") String name) {
        
        CompactDisc compactDisc = cdPlayerConfig.sgtPeppers();
        compactDisc.play();
    
        System.out.println("---------------------------");
        CDPlayer cdPlayer =  cdPlayerConfig.cdPlayer(compactDisc);
        cdPlayer.play();
        return "Hi, " + name;
    }
}
