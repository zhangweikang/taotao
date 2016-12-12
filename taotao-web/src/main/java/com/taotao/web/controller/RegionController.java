package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.RegionService;

@RequestMapping("region")
@Controller
public class RegionController {
    
    @Autowired
    private RegionService regionService;
    
    @RequestMapping("list")
    @ResponseBody
    public TaotaoResult list(@RequestParam("city")String city){
        return this.regionService.queryBycity(city);
    }

}
