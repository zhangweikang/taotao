package com.taotao.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.print.attribute.standard.PageRanges;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.vo.Order;
import com.taotao.web.vo.PageResult;

@Service
public class OrderService {

    
    @Autowired
    private ApiService apiService;

    @Value("${ORDER_CREATE_URL}")
    private String ORDER_CREATE_URL;

    @Value("${ORDER_QUERY_URL}")
    private String ORDER_QUERY_URL;
    
    @Value("${ORDER_PAGE_QUERY_URL}")
    private String ORDER_PAGE_QUERY_URL;

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

    public EasyUIResult queryOrdersByUserId(Long userId, Long page, Long count) {
        // TODO Auto-generated method stub

        HashMap<String, String> params = new HashMap<String ,String>();
        EasyUIResult easyUIResult = new EasyUIResult();
        params.put("userId", String.valueOf(userId));
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
         
        try {
            String jsonData = this.apiService.doPost(ORDER_PAGE_QUERY_URL, params);
           PageResult pageresult = MAPPER.readValue(jsonData, PageResult.class);
           List rows = pageresult.getData();
           Integer totle = pageresult.getTotle();
           easyUIResult.setRows(rows);
           easyUIResult.setTotal(totle);
           return easyUIResult;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
        
        
    }

}
