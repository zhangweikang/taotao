package com.taotao.store.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.store.order.service.RegionService;

@RequestMapping("region")
@Controller
public class RegionController {
    
    @Autowired
    private RegionService regionService;

    @RequestMapping(value="list",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult queryByCity(@RequestParam("city")String city){
        return this.regionService.queryByCity(city);
    }
}
