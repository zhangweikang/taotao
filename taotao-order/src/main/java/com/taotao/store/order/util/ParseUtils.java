package com.taotao.store.order.util;

import java.util.Date;

import org.apache.hadoop.hbase.util.Bytes;


public class ParseUtils {
	
	//不同类型向byte[]转换
	public static byte[] dateToByte( Date time ){
		return Bytes.toBytes(Long.toString(time.getTime()));
	}
	public static byte[] stringToByte( String string ){
		return Bytes.toBytes(string);
	}
	public static byte[] integerToByte( Integer integer ){
		return Bytes.toBytes(String.valueOf(integer));
	}
	public static byte[] longToByte( Long lg ){
		return Bytes.toBytes(Long.toString(lg));
	}
	//byte[]向不同类型转换
	public static Date byteToDate( byte[] bt ){
		return new Date(Long.parseLong(new String(bt)));
	}
	public static String byteToString( byte[] bt ){
		return new String(bt);
	}
	public static Integer byteToInteger( byte[] bt ){
		return Integer.parseInt(new String(bt));
	}
	public static Long byteToLong( byte[] bt ){
		return Long.parseLong(new String(bt));
	}
	
	
}
