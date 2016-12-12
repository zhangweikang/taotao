package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.web.service.ItemService;
import com.taotao.web.vo.Item;

@RequestMapping("item")
@Controller
public class ItemController {
    
    @Autowired
    private ItemService itemService;

    /**
     * 商品详情页功能
     * 
     * @param id
     * @return
     */
    @RequestMapping("{id}")
    public ModelAndView show(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("item");
        
        //获取商品详情数据
        Item item = this.itemService.getItemById(id);
        mv.addObject("item", item);
        //获取商品详情数据
        ItemDesc itemDesc = this.itemService.queryItemDescByItemId(id);
        mv.addObject("itemDesc", itemDesc);
        
        //获取商品规格参数数据
        String itemParamItem = this.itemService.queryItemParamItemByItemId(id);
        mv.addObject("itemParam", itemParamItem);
        
        return mv;
    }
}
