package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyOrderSercice {

    @Value("${MYORDER_DELETE_URL}")
    public String MYORDER_DELETE_URL;
    
    @Value("${MYORDER_QUERY_URL}")
    public String MYORDER_QUERY_URL;
    
//    @Value("${MYORDER_QUERY_ORDER_URL}")
//    public String MYORDER_QUERY_ORDER_URL;
//
//    @Value("${MYORDER_CART_ADD_URL}")
//    public String MYORDER_CART_ADD_URL;
}
