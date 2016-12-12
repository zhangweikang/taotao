package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.vo.Order;

@Service
public class OrderService {

    @Autowired
    private ApiService apiService;

    @Value("${ORDER_CREATE_URL}")
    private String ORDER_CREATE_URL;

    @Value("${ORDER_QUERY_URL}")
    private String ORDER_QUERY_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 调用订单系统提交订单
     * 
     * @param order
     * @return
     */
    public String submitOrder(Order order) {
        try {
            String jsonData = this.apiService.doPostJson(ORDER_CREATE_URL, MAPPER.writeValueAsString(order));
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, String.class);
            return taotaoResult.getData().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据订单ID查询订单（从订单系统）
     * @param orderId
     * @return
     */
    public Order queryOrderById(Long orderId) {
        try {
            String jsonData = this.apiService.doGet(ORDER_QUERY_URL + orderId);
            if(StringUtils.isNotBlank(jsonData)){
                return MAPPER.readValue(jsonData, Order.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
