package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.service.RedisService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.IndexService;

@RequestMapping("redis")
@Controller
public class RedisController {

    @Autowired
    private RedisService redisService;

    /**
     * 删除大广告位缓存数据
     * 
     * @return
     */
    @RequestMapping("del/index/ad1")
    @ResponseBody
    public TaotaoResult delIndexAd1RedisData() {
        this.redisService.del(IndexService.REDIS_KEY_INDEX_AD1);
        return TaotaoResult.ok();
    }

}
