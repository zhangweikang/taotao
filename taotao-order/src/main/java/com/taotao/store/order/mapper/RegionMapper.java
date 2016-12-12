package com.taotao.store.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.store.order.pojo.Region;

public interface RegionMapper {

    List<Region> queryByCity(@Param("city")String city);

}
