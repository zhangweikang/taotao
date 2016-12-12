package com.taotao.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.service.ApiService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.vo.Item;

@Service
public class SearchService {

    @Autowired
    private ApiService apiService;

    public TaotaoResult search(String q, Integer page, Integer rows) {
        String url = "http://search.taotao.com/search";
        Map<String, String> params = new HashMap<String, String>(3);
        params.put("wd", q);
        params.put("page", String.valueOf(page));       
        params.put("rows", String.valueOf(rows));
        try {
            String jsonData = this.apiService.doPost(url, params);
            TaotaoResult taotaoResult = TaotaoResult.formatToList(jsonData, Item.class);
            if(taotaoResult.getStatus().intValue() == 200){
                return taotaoResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
