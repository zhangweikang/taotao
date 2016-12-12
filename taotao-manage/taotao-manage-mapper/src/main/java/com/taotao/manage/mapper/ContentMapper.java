package com.taotao.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.manage.base.mapper.TaotaoMapper;
import com.taotao.manage.pojo.Content;

public interface ContentMapper extends TaotaoMapper<Content> {

    List<Content> queryContentList(@Param("categoryId") Long categoryId);

}
