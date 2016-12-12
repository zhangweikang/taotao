package com.taotao.manage.mapper;

import com.taotao.manage.base.mapper.TaotaoMapper;
import com.taotao.manage.pojo.ItemCat;

public interface ItemCatMapper extends TaotaoMapper<ItemCat> {

    /**
     * 根据父节点ID查询商品类目
     * 
     * @param parentId
     * @return
     */
//    List<ItemCat> queryList(@Param("parentId") Long parentId);

}
