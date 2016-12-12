package com.taotao.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.service.ApiService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.vo.Region;

@Service
public class RegionService {

    @Autowired
    private ApiService apiService;

    public TaotaoResult queryBycity(String city) {
        // TODO Auto-generated method stub
        String api = "http://order.taotao.com/region/list";
        System.out.println(city);
        Map<String,String> map = new HashMap<String,String>(1);
        map.put("city", city);
        try {
            String jsonData = this.apiService.doPost(api, map);
             return TaotaoResult.formatToList(jsonData, Region.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return TaotaoResult.build(201, "异常");
        }
    }
    
}
