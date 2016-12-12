package com.taotao.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.manage.pojo.Content;
import com.taotao.web.util.TaoTaoProperties;

@Service
public class IndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Autowired
    private ApiService apiService;

    @Autowired
    private TaoTaoProperties taoTaoProperties;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String REDIS_KEY_INDEX_AD1 = "WEB_TATAO_INDEX_INDEX_AD1";

    public static final Integer REDIS_KEY_INDEX_AD1_TIME = 60 * 60 * 24;

    @SuppressWarnings("unchecked")
    private List<Content> getDataFromManage(String subUrl) throws ClientProtocolException, IOException {
        // 从后台系统中获取数据
        String api = taoTaoProperties.TAOTAO_MANAGE_URL + subUrl;
        // 获取json数据
        String jsonData = this.apiService.doGet(api);
        // 将json转化为EasyUIResult
        EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Content.class);
        return (List<Content>) easyUIResult.getRows();
    }

    public String getIndexAD1() {
        try {
            // 从缓存中命中
            String json = this.redisService.get(REDIS_KEY_INDEX_AD1);
            if (StringUtils.isNotBlank(json)) {
                return json;
            }
        } catch (Exception e) {
            LOGGER.error("从缓存中命中首页大广告位数据出错!", e);
        }

        // 从后台系统中获取数据
        try {
            List<Content> contents = this.getDataFromManage(taoTaoProperties.INDEX_AD1_URL);
            // 封装前端所需要的json格式
            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(6);
            for (Content content : contents) {
                Map<String, Object> map = new HashMap<String, Object>(8);
                map.put("srcB", content.getPic());
                map.put("height", 240);
                map.put("alt", "");
                map.put("width", 670);
                map.put("src", content.getPic());
                map.put("widthB", 550);
                map.put("href", content.getUrl());
                map.put("heightB", 240);
                resultList.add(map);
            }

            String result = MAPPER.writeValueAsString(resultList);

            try {
                // 将结果写入到缓存中
                this.redisService.set(REDIS_KEY_INDEX_AD1, result, REDIS_KEY_INDEX_AD1_TIME);
            } catch (Exception e) {
                LOGGER.error("写入大广告位数据到缓存出错!", e);
            }

            return result;
        } catch (Exception e) {
            LOGGER.error("获取首页大广告位出错!", e);
        }
        return null;
    }

    public List<Content> getIndexTaotaoNews() {
        try {
            return this.getDataFromManage(this.taoTaoProperties.INDEX_NEWS_URL);
        } catch (Exception e) {
            LOGGER.error("获取首页资讯出错!", e);
        }
        return null;
    }

}
