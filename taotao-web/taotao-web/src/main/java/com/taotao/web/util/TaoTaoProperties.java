package com.taotao.web.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
// 注册为Spring组件
public class TaoTaoProperties {

    

	@Value("${TAOTAO_MANAGE_URL}")
    public String TAOTAO_MANAGE_URL;

    @Value("${INDEX_AD1_URL}")
    public String INDEX_AD1_URL;
    
    @Value("${INDEX_NEWS_URL}")
    public String INDEX_NEWS_URL;
    
    @Value("${INDEX_NV_URL}")
    public String INDEX_NV_URL;
    
    @Value("${INDEX_ADF1_URL}")
    public String INDEX_ADF1_URL;
    
    @Value("${INDWX_ADF1Z_URL}")
    public String INDWX_ADF1Z_URL;
}
