package com.lnjecit.springboo.elastic.job.controller;

import com.lnjecit.springboo.elastic.job.JobMainDriver;
import com.lnjecit.springboo.elastic.job.service.FileService;
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
    FileService fileService;
    
    @RequestMapping("/sayHello")
    public String sayHello(@RequestParam("name") String name) {
        fileService.println();
        return "Hi, " + name;
    }
    
    @RequestMapping("/sayHello2")
    public String sayHello2(@RequestParam("name") String name) {
        JobMainDriver.main(null);
        return "Hi, " + name;
    }
}
