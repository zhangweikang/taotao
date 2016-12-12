package com.taotao.store.order.mapper;

import java.util.Date;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;

import com.taotao.store.order.pojo.Order;

public interface OrderMapper extends IMapper<Order>{
	
	public void paymentOrderScan(@Param("date") Date date);

	public List<Order> queryOrders(@Param("itemName")String itemName,
			@Param("itemId")String itemId,
			@Param("orderId")String orderId, 
			@Param("updateTime")Date updateTime, 
			@Param("status")Integer status, 
			@Param("userId")String userId,
			@Param("page")Integer page,
			@Param("count")Integer count);

}
