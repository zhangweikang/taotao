package com.taotao.store.order.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.store.order.mapper.RegionMapper;
import com.taotao.store.order.pojo.Region;

@Service
public class RegionService {
    
    @Autowired
    private RegionMapper regionMapper;

    public TaotaoResult queryByCity(String city) {
        // TODO Auto-generated method stub
        if(StringUtils.isNotBlank(city)){
            List<Region> regions = this.regionMapper.queryByCity(city);
            return TaotaoResult.ok(regions);
        }
        return null;
    }

}
