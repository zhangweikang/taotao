package com.taotao.store.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.store.order.service.PickViewService;

@Controller
@RequestMapping("pickview")
public class PickViewController {

    @Autowired
    private PickViewService pickViewService;
    
    @RequestMapping("list")
    @ResponseBody
    public TaotaoResult list(@RequestParam("regionId")Long regionId){
        return this.pickViewService.queryByregionId(regionId);
    }
}
