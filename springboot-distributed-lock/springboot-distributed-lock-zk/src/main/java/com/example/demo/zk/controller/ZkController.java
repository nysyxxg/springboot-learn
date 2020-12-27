package com.example.demo.zk.controller;

import com.example.demo.zk.ZKApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("Zk")
public class ZkController {


    @Autowired
    private ZKApi zkApi;

    @GetMapping("/CreateN")
    public void CreateN(){

        zkApi.createNode("name","1");

    }
}
