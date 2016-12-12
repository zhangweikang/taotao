package com.taotao.search.mq;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.taotao.common.service.ApiService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.search.pojo.Item;

@Component
public class UpdateItem {

    @Autowired
    private HttpSolrServer httpSolrServer;

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    public void execute(Long itemId) {
        try {
            // 从后台系统的接口中获取商品数据
            String url = TAOTAO_MANAGE_URL + "/rest/item/query/id/" + itemId;
            String jsonData = this.apiService.doPost(url);
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, Item.class);
            //更新到solr中
            this.httpSolrServer.addBean(taotaoResult.getData());
            this.httpSolrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
