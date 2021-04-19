package com.lnjecit.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SessionController {
    
    @RequestMapping("/trs")
    public void testRedisSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> list = (List<String>) request.getSession().getAttribute("list");
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add("xxx");
        request.getSession().setAttribute("list", list);
        response.getWriter().println("Session:" + request.getSession().getId());
        response.getWriter().println("counts:" + list.size());
    }
}
