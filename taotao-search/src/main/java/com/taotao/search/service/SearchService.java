package com.taotao.search.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.search.pojo.Item;

@Service
public class SearchService {

    @Autowired
    private HttpSolrServer httpSolrServer;

    /**
     * 搜索商品
     * 
     * @param wd 关键字
     * @param page 页数
     * @param rows 记录数
     * @return
     * @throws Exception
     */
    public TaotaoResult search(String wd, Integer page, Integer rows) throws Exception {
        SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
        solrQuery.setQuery(wd); // 搜索关键词
        // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
        solrQuery.setStart((Math.max(page, 1) - 1) * rows);
        solrQuery.setRows(rows);

        // 设置高亮
        solrQuery.setHighlight(true); // 开启高亮组件
        solrQuery.addHighlightField("title");// 高亮字段
        solrQuery.setHighlightSimplePre("<em>");// 标记，高亮关键字前缀
        solrQuery.setHighlightSimplePost("</em>");// 后缀

        // 执行查询
        QueryResponse queryResponse = this.httpSolrServer.query(solrQuery);
        List<Item> items = queryResponse.getBeans(Item.class);
        // 将高亮的标题数据写回到数据对象中
        Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
        for (Map.Entry<String, Map<String, List<String>>> highlighting : map.entrySet()) {
            for (Item item : items) {
                if (!highlighting.getKey().equals(item.getId().toString())) {
                    continue;
                }
                item.setTitle(StringUtils.join(highlighting.getValue().get("title"), ""));
                break;
            }
        }
        return TaotaoResult.build(200, String.valueOf(queryResponse.getResults().getNumFound()), items);
    }

}
