package com.taotao.wen.httpclient.test;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.common.service.ApiService;

public class TestHttpclient {
    
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext*.xml");
        
        System.out.println(applicationContext.getBean(CloseableHttpClient.class));
        System.out.println(applicationContext.getBean(CloseableHttpClient.class));
        
        System.out.println(applicationContext.getBean(ApiService.class).getHttpClient());
        System.out.println(applicationContext.getBean(ApiService.class).getHttpClient());
        System.out.println(applicationContext.getBean(ApiService.class).getHttpClient());
    }

}
