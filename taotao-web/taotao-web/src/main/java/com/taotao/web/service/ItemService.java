package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.web.util.TaoTaoProperties;
import com.taotao.web.vo.Item;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TaoTaoProperties taoTaoProperties;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String REDIS_KEY_ITEM = "WEB_TATAO_ITEM_";

    public static final Integer REDIS_KEY_ITEM_TIME = 60 * 60 * 24;

    public Item getItemById(Long id) {
        String url = taoTaoProperties.TAOTAO_MANAGE_URL + "/rest/item/query/id/" + id;
        try {
            String key = REDIS_KEY_ITEM + id;

            String cacheData = null;
            String jsonData = null;
            try {
                // 从缓存中命中
                cacheData = this.redisService.get(key);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if (StringUtils.isNotBlank(cacheData)) {
                // 命中
                jsonData = cacheData;
            } else {
                // 未命中
                jsonData = this.apiService.doPost(url);
                try {
                    // 将结果集写入缓存中
                    this.redisService.set(key, jsonData, REDIS_KEY_ITEM_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, Item.class);
            return (Item) taotaoResult.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemDesc queryItemDescByItemId(Long id) {
        String url = taoTaoProperties.TAOTAO_MANAGE_URL + "/rest/item/desc/" + id;
        try {
            String jsonData = this.apiService.doGet(url);
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, ItemDesc.class);
            return (ItemDesc) taotaoResult.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryItemParamItemByItemId(Long id) {
        String url = taoTaoProperties.TAOTAO_MANAGE_URL + "/rest/item/param/item/" + id;
        try {
            String jsonData = this.apiService.doGet(url);
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, ItemParamItem.class);
            ItemParamItem itemParamItem = (ItemParamItem) taotaoResult.getData();
            // 拼接html代码块
            StringBuilder html = new StringBuilder();
            html.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");

            String data = itemParamItem.getParamData();
            ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(data);
            for (JsonNode jsonNode : arrayNode) {
                String group = jsonNode.get("group").asText();
                html.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + group + "</th></tr>");

                ArrayNode params = (ArrayNode) jsonNode.get("params");
                for (JsonNode param : params) {
                    html.append("<tr><td class=\"tdTitle\">" + param.get("k").asText() + "</td><td>"
                            + param.get("v").asText() + "</td></tr>");
                }
            }

            html.append("</tbody></table>");
            return html.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
