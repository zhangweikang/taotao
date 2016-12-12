package com.taotao.store.order.dao;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.Where;
import com.taotao.store.order.mapper.OrderMapper;
import com.taotao.store.order.pojo.Order;
import com.taotao.store.order.pojo.PageResult;
import com.taotao.store.order.pojo.ResultMsg;

public class OrderDAO implements IOrder {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void createOrder(Order order) {
        this.orderMapper.save(order);
    }

    @Override
    public Order queryOrderById(String orderId) {
        return this.orderMapper.queryByID(orderId);
    }

    @Override
    public PageResult<Order> queryOrderByUserNameAndPage(String buyerNick, Integer page, Integer count) {
        PageBounds bounds = new PageBounds();
        bounds.setContainsTotalCount(true);
        bounds.setLimit(count);
        bounds.setPage(page);
        bounds.setOrders(com.github.miemiedev.mybatis.paginator.domain.Order.formString("create_time.desc"));
        PageList<Order> list = this.orderMapper
                .queryListByWhere(bounds, Where.build("buyer_nick", buyerNick));
        return new PageResult<Order>(list.getPaginator().getTotalCount(), list);
    }

    @Override
    public ResultMsg changeOrderStatus(Order order) {
        try {
            order.setUpdateTime(new Date());
            this.orderMapper.update(order);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMsg("500", "更新订单出错!");
        }
        return new ResultMsg("200", "更新成功!");
    }

	@Override
	public void deleteOrderById(Long orderId) {
		orderMapper.deleteByID(orderId);
		
	}

	@Override
	public PageResult<Order> queryOrders(String itemName, String itemId,
			String orderId, Date updateTime, Integer status, String userId, Integer page, Integer count) {
		
		List<Order> orders = orderMapper.queryOrders(itemName,itemId,orderId,updateTime,status,userId,count+(page-1)*count,count);
		
		
		return new PageResult<Order>(orders.size(),orders);
	}

	@Override
	public void deleteOrderByIds(Long[] orderIDS) {
		orderMapper.deleteByIDS(orderIDS);
		
	}

}
