package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;

@RequestMapping("item/desc")
@Controller
public class ItemDescController {

    @Autowired
    private ItemDescService itemDescService;

    @RequestMapping("{itemId}")
    @ResponseBody
    public TaotaoResult queryItemDescByItemId(@PathVariable("itemId") Long itemId) {
        ItemDesc itemDesc = this.itemDescService.queryById(itemId);
        return TaotaoResult.ok(itemDesc);
    }

}
