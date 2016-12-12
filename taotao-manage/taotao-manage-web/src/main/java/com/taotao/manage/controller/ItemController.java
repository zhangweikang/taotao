package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.EasyUIResult;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;
import com.taotao.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDescService itemDescService;

    @RequestMapping("query")
    @ResponseBody
    public EasyUIResult queryItemList(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {
        return this.itemService.queryItemList(page, rows);
    }

    /**
     * 新增商品
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult saveItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams") String itemParams) {
        Long id = this.itemService.saveItem(item, desc, itemParams);
        if (id == null) {
            return TaotaoResult.build(201, "新增商品出错!");
        }
        return TaotaoResult.ok(id);
    }

    /**
     * 根据id查找商品数据
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "query/id/{id}", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult queryById(@PathVariable("id") Long id) {
        // TODO 完善逻辑
        return TaotaoResult.ok(this.itemService.queryById(id));
    }

    /**
     * 根据商品ID查询商品描述数据
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "query/item/desc/{itemId}", method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult queryItemDescByItemId(@PathVariable("itemId") Long itemId) {
        ItemDesc itemDesc = this.itemDescService.queryById(itemId);
        return TaotaoResult.ok(itemDesc);
    }

    /**
     * 更新商品数据
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams") String itemParams) {
        this.itemService.updateItem(item, desc, itemParams);
        return TaotaoResult.ok();
    }

}
