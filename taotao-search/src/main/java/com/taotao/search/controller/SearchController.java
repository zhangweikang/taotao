package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.search.service.SearchService;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    // /search?wd=java
    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult search(@RequestParam("wd") String wd,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {
        try {
            return this.searchService.search(wd, page, rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.build(500, "查询出错，请稍候再试!");
    }

}
