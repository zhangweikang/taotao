package com.taotao.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.SearchService;
import com.taotao.web.vo.Item;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    private static final Integer ROWS = 30;

    @RequestMapping("search")
    public ModelAndView search(@RequestParam("q") String q,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {

        ModelAndView mv = new ModelAndView("search");
        
        try {
            q = new String(q.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            mv.addObject("query", "未知");// 关键字
        }
        
        TaotaoResult taotaoResult = this.searchService.search(q, page, ROWS);
        if (null != taotaoResult) {
            mv.addObject("itemList", taotaoResult.getData());
            Integer total = Integer.valueOf(taotaoResult.getMsg());
            mv.addObject("pages", (total + ROWS - 1) / ROWS);// 总页数
        } else {
            mv.addObject("itemList", new ArrayList<Item>(0));
        }

        mv.addObject("query", q);// 关键字
        mv.addObject("page", page);// 当前页
        return mv;
    }

}
