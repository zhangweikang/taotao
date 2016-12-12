package com.taotao.store.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.store.order.pojo.PickView;

public interface PickViewMapper {

    List<PickView> queryByregionId(@Param("regionId")Long regionId);

}
