package com.taotao.store.order.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.taotao.common.service.RedisService;
import com.taotao.store.order.pojo.Order;
import com.taotao.store.order.pojo.OrderItem;
import com.taotao.store.order.pojo.PageResult;
import com.taotao.store.order.pojo.ResultMsg;
import com.taotao.store.order.util.Constants;
import com.taotao.store.order.util.ParseUtils;

public class OrderDAOHbase implements IOrder {
    // 注入redis
    @Autowired
    private RedisService redisService;

    private static final Integer TIME = 60 * 60 * 24;

    static Configuration config = null;

    static HTablePool hTablePool = null;
    static {
        // 创建配置
        config = HBaseConfiguration.create();
        // 添加zookeeper的ip端口
        config.set("hbase.zookeeper.quorum", "slave1,slave2,slave3");// zookeeper地址
        config.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口
        hTablePool = new HTablePool(config, 10000);

    }

    // 创建订单
    @Override
    public void createOrder(Order order) {
        // 添加订单信息

        HTableInterface table = hTablePool.getTable(Constants.O_TABLE_NAME);
        HTableInterface table1 = hTablePool.getTable(Constants.OT_TABLE_NAME);
        //添加到订单表单
        Put put = new Put(Bytes.toBytes(order.getOrderId()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_ORDER_ID, Bytes.toBytes(order.getOrderId()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_ORDER_ID, Bytes.toBytes(order.getOrderId()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_PAYMENT, Bytes.toBytes(order.getPayment()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_POSTFEE, Bytes.toBytes(order.getPostFee()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_STATUS, Bytes.toBytes(order.getStatus()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_CREATE_TIME,
                ParseUtils.dateToByte(order.getCreateTime()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_UPDATE_TIME,
                ParseUtils.dateToByte(order.getUpdateTime()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_PAYMENT_TIME,
                ParseUtils.dateToByte(order.getPaymentTime()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_CONSIGN_TIME,
                ParseUtils.dateToByte(order.getConsignTime()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_END_TIME, ParseUtils.dateToByte(order.getEndTime()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_CLOSE_TIME, ParseUtils.dateToByte(order.getCloseTime()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_SHIPPING_NAME, Bytes.toBytes(order.getShippingName()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_SHIPPING_CODE, Bytes.toBytes(order.getShippingCode()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_USERID, Bytes.toBytes(order.getUserId()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_BUYER_MESSAGE, Bytes.toBytes(order.getBuyerMessage()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_BUYER_NICK, Bytes.toBytes(order.getBuyerNick()));
        put.add(Constants.O_FAMILY_NAME, Constants.O_BUYER_RATE, Bytes.toBytes(order.getBuyerRate()));
        // 获取订单中的商品信息
        List<OrderItem> items = order.getOrderItems();
        // 存放put信息
        List<Put> list = new ArrayList<Put>();
        // 定义字符缓冲区
        StringBuffer sb = new StringBuffer();
        // 遍历并添加商品信息到oti表
        for (OrderItem orderItem : items) {
            Put put1 = new Put(Bytes.toBytes(orderItem.getItemId()));
            //添加商品详情信息
            put1.add(Constants.OT_FAMLIY_NAME, Constants.OT_ITEMID, Bytes.toBytes(orderItem.getItemId()));
            put1.add(Constants.OT_FAMLIY_NAME, Constants.OT_NUM, Bytes.toBytes(orderItem.getNum()));
            put1.add(Constants.OT_FAMLIY_NAME, Constants.OT_TITLE, Bytes.toBytes(orderItem.getTitle()));
            put1.add(Constants.OT_FAMLIY_NAME, Constants.OT_PRICE, Bytes.toBytes(orderItem.getPrice()));
            put1.add(Constants.OT_FAMLIY_NAME, Constants.OT_TOTALFEE, Bytes.toBytes(orderItem.getTotalFee()));
            sb.append(orderItem.getItemId()).append("|");
            list.add(put1);
        }
        // 将订单中包含的商品id拼成字符串添加到字段
        put.add(Constants.O_FAMILY_NAME, Constants.O_OEDER_ITEMS, Bytes.toBytes(sb.toString()));
        try {
            // 将数据存入redis中
            redisService.rpush(2, order.getBuyerNick(), order.getOrderId(), TIME);
            table.put(put);
            table1.put(list);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果出现异常 将数据删除3次
            redisService.lrem(2, order.getBuyerNick(), 3L, order.getOrderId());
        } finally {
            try {
                // 真正的提交数据
                table.flushCommits();
                table1.flushCommits();
                // 关闭资源 并 添加到map
                table.close();
                table1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Order queryOrderById(String orderId) {
        return null;
    }

    @Override
    public PageResult<Order> queryOrderByUserNameAndPage(String buyerNick, Integer page, Integer count) {
        PageBounds bounds = new PageBounds();
        bounds.setContainsTotalCount(true);
        bounds.setLimit(count);
        bounds.setPage(page);
        bounds.setOrders(com.github.miemiedev.mybatis.paginator.domain.Order.formString("create_time.desc"));
        return null;
    }

    @Override
    public ResultMsg changeOrderStatus(Order order) {
        try {
            order.setUpdateTime(new Date());
            // this.orderMapper.update(order);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMsg("500", "更新订单出错!");
        }
        return new ResultMsg("200", "更新成功!");
    }

    @Override
    public void deleteOrderById(Long orderId) {
        // orderMapper.deleteByID(orderId);

    }

    @Override
    public PageResult<Order> queryOrders(String itemName, String itemId, String orderId, Date updateTime,
            Integer status, String userId, Integer page, Integer count) {

        // List<Order> orders =
        // orderMapper.queryOrders(itemName,itemId,orderId,updateTime,status,userId,count+(page-1)*count,count);

        return null;
    }

    @Override
    public void deleteOrderByIds(Long[] orderIDS) {
        // orderMapper.deleteByIDS(orderIDS);

    }

}
