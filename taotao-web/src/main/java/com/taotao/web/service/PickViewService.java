package com.taotao.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.service.ApiService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.vo.PickView;

@Service
public class PickViewService {
    
    @Autowired
    private ApiService apiService;

    public TaotaoResult queryByregionId(Long regionId) {
        String api = "http://order.taotao.com/pickview/list";
        Map<String,String> map = new HashMap<String,String>(1);
        map.put("regionId", String.valueOf(regionId));
        try {
            String jsonData = this.apiService.doPost(api, map);
            return TaotaoResult.formatToList(jsonData, PickView.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return TaotaoResult.build(201, "异常");
    }

}
