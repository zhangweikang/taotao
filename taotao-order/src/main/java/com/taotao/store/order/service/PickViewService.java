package com.taotao.store.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.store.order.mapper.PickViewMapper;
import com.taotao.store.order.pojo.PickView;

@Service
public class PickViewService {

    @Autowired
    private PickViewMapper pickViewMapper;

    public TaotaoResult queryByregionId(Long regionId) {
        try {
            List<PickView> pickViews = this.pickViewMapper.queryByregionId(regionId);
            return TaotaoResult.ok(pickViews);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return TaotaoResult.build(201, "查询数据异常");
    }
}
