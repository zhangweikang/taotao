package com.taotao.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.vo.ItemCatResult;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("web/itemcat")
@Controller
public class WebItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 查询所有的商品类目转化为前端json结构
     * 
     * @return
     */
    @RequestMapping("all")
    @ResponseBody
    public ItemCatResult queryAll() {
        ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
        return itemCatResult;
    }
    
//    @RequestMapping("all")
//    @ResponseBody
//    public String queryAll(@RequestParam("callback") String callback) {
//        try {
//            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
//            String json = MAPPER.writeValueAsString(itemCatResult);
//            return callback + "(" + json + ");";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
