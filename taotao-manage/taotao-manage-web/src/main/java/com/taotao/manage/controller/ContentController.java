package com.taotao.manage.controller;

import java.io.IOException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.taotao.common.service.ApiService;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

@RequestMapping("content")
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;
    
    @Autowired
    private ApiService apiService;

    /**
     * 新增内容
     * 
     * @param content
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public TaotaoResult saveContent(Content content) {
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        this.contentService.save(content);
        
        if(content.getCategoryId().intValue() == 42){
            try {
                // 通知更新首页大广告位数据
                // TODO
                String url = "http://www.taotao.com/redis/del/index/ad1.html";
                this.apiService.doGet(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TaotaoResult.ok();
    }

    @RequestMapping("query/list")
    @ResponseBody
    public EasyUIResult queryContentList(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows,
            @RequestParam("categoryId") Long categoryId) {
        return this.contentService.queryContentList(categoryId, page, rows);
    }

}
