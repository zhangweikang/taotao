package com.taotao.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("page/{pageNum}")
    public String getPage(@PathVariable("pageNum") String pageNum){
        
        return pageNum;
    }
    
}
