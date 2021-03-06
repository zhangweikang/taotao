package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;

/**
 * 首页
 * 
 */
@Controller
public class IndexController {
    
    @Autowired
    private IndexService indexService;
    
    @RequestMapping("index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("index");
        // 获取大广告为数据
        String ad1Json = this.indexService.getIndexAD1();
        mv.addObject("indexAD1", ad1Json);//添加到模型数据中
        
        // 获取淘淘快报
        mv.addObject("indexTaotaoNews", this.indexService.getIndexTaotaoNews());
        
        //导航条后台管理
        mv.addObject("navigation",this.indexService.getNavigation());
        
        //获取1楼小广告
        mv.addObject("adf1",this.indexService.getIndexADf1());
        
        //获取1楼广告中间位置
        String adf1zJson = this.indexService.getIndexAdf1z();
        mv.addObject("adf1z",adf1zJson);
        
        return mv;
    }

}
