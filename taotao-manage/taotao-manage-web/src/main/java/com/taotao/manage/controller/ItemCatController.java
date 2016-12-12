package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("item/cat")
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 查询商品类目
     * 
     * @param parentId 0：查询一级类型
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public List<ItemCat> queryList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        return this.itemCatService.queryList(parentId);
    }

}
