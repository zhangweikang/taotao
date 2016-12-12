package com.taotao.store.order.util;

import org.apache.hadoop.hbase.util.Bytes;

public class Constants {
	//订单信息
	public static final byte[] O_TABLE_NAME=Bytes.toBytes("o");//表名
	public static final byte[] O_FAMILY_NAME=Bytes.toBytes("oi");//列族名
	public static final byte[] O_ORDER_ID=Bytes.toBytes("oid");//id,rowKye:id+时间错
	public static final byte[] O_PICPATH=Bytes.toBytes("opp");//图片路径
	public static final byte[] O_PAYMENT=Bytes.toBytes("opm");//实付金额
	public static final byte[] O_POSTFEE=Bytes.toBytes("opf");//邮费
	public static final byte[] O_STATUS=Bytes.toBytes("os");//状态
	public static final byte[] O_CREATE_TIME=Bytes.toBytes("oct");//创建时间
	public static final byte[] O_UPDATE_TIME=Bytes.toBytes("out");//更新时间
	public static final byte[] O_PAYMENT_TIME=Bytes.toBytes("opt");//付款时间
	public static final byte[] O_CONSIGN_TIME=Bytes.toBytes("ocot");//发货时间
	public static final byte[] O_END_TIME=Bytes.toBytes("oet");//交易结束时间
	public static final byte[] O_CLOSE_TIME=Bytes.toBytes("oclt");//交易关闭时间
	public static final byte[] O_SHIPPING_NAME=Bytes.toBytes("osn");//物流名称
	public static final byte[] O_SHIPPING_CODE=Bytes.toBytes("osc");//物流单号
	public static final byte[] O_USERID=Bytes.toBytes("ou");//用户id
	public static final byte[] O_BUYER_MESSAGE=Bytes.toBytes("obm");//买家留言
	public static final byte[] O_BUYER_NICK=Bytes.toBytes("obn");//买家昵称
	public static final byte[] O_BUYER_RATE=Bytes.toBytes("obr");//买家是否已经评价
	public static final byte[] O_OEDER_ITEMS=Bytes.toBytes("ooi");//商品详情


	//商品信息
	public static final byte[] OT_TABLE_NAME=Bytes.toBytes("ot");//表名
	public static final byte[] OT_FAMLIY_NAME=Bytes.toBytes("oti");//列族1：
	public static final byte[] OT_ITEMID=Bytes.toBytes("otid");//商品id
	public static final byte[] OT_NUM=Bytes.toBytes("otn");//商品购买数量
	public static final byte[] OT_TITLE=Bytes.toBytes("ott");//商品标题
	public static final byte[] OT_PRICE=Bytes.toBytes("otp");//商品单价
	public static final byte[] OT_TOTALFEE=Bytes.toBytes("ottf");//商品总价
	
	
	//收件人信息
public static final byte[] OS_RECEIVER_NAME=Bytes.toBytes("osrn"); // 收货人全名
public static final byte[] OS_RECEIVER_PHONE=Bytes.toBytes("osrp"); // 固定电话
public static final byte[] OS_RECEIVER_MOBILE=Bytes.toBytes("osrm"); // 移动电话
public static final byte[] OS_RECEIVER_STATE=Bytes.toBytes("osrs"); // 省份
public static final byte[] OS_RECEIVER_CITY=Bytes.toBytes("osrc"); // 城市
public static final byte[] OS_RECEIVER_DISTRICT=Bytes.toBytes("osrr"); // 区/县
public static final byte[] OS_RECEIVER_ADDRESS=Bytes.toBytes("osra"); // 收货地址，如：xx路xx号
public static final byte[] OS_RECEIVER_ZIP=Bytes.toBytes("osrz"); // 邮政编码,如：310001
public static final byte[] OS_RECEIVER_CREATE=Bytes.toBytes("osrct");//创建时间
public static final byte[] OS_RECEIVER_UPDATED=Bytes.toBytes("osru");//修改时间

	
}