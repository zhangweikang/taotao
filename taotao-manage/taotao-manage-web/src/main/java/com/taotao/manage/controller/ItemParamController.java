package com.taotao.manage.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamService;

/**
 * 商品规格参数模板
 *
 */
@RequestMapping(value = "item/param")
@Controller
public class ItemParamController {
    
    @Autowired
    private ItemParamService itemParamService;
    
    /**
     * 根据商品类目id查询模板
     * @param itemCatId
     * @return
     */
    @RequestMapping(value = "query/itemcatid/{itemcatid}")
    @ResponseBody
    public TaotaoResult queryItemParamByItemCatId(@PathVariable("itemcatid")Long itemCatId){
        ItemParam itemParam = new ItemParam();
        itemParam.setItemCatId(itemCatId);
        List<ItemParam> itemParams = this.itemParamService.queryByWhere(itemParam);
        if(itemParams == null || itemParams.isEmpty()){
            return TaotaoResult.ok();
        }
        return TaotaoResult.ok(itemParams.get(0));
    }
    
    /**
     * 新增商品规格参数模板数据
     * @return
     */
    @RequestMapping(value = "save/{itemcatid}",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult saveItemParam(ItemParam itemParam,@PathVariable("itemcatid")Long itemCatId){
        itemParam.setItemCatId(itemCatId);
        itemParam.setCreated(new Date());
        itemParam.setUpdated(itemParam.getCreated());
        this.itemParamService.save(itemParam);
        return TaotaoResult.ok();
    }
    
    @RequestMapping("list")
    @ResponseBody
    public EasyUIResult queryList(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows){
       PageInfo<ItemParam> pageInfo = this.itemParamService.queryPageList(page, rows);
       return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

}
