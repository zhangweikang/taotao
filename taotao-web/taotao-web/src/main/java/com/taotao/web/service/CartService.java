package com.taotao.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.web.vo.Item;
import com.taotao.web.vo.ItemCart;
import com.taotao.web.vo.User;

@Service
public class CartService {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ItemService itemService;

    @Value("${CART_URL}")
    private String CART_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void addItem(User user, Long itemId, Integer num) {
        String url = CART_URL + "/rest/cart/save";
        // 查询商品数据
        Item item = this.itemService.getItemById(itemId);
        Map<String, String> params = new HashMap<String, String>(6);
        params.put("userId", String.valueOf(user.getId()));
        params.put("itemId", String.valueOf(itemId));
        params.put("num", String.valueOf(num));
        params.put("itemTitle", item.getTitle());
        params.put("itemImage", item.getImages()[0]);
        params.put("itemPrice", String.valueOf(item.getPrice()));
        try {
            this.apiService.doPost(url, params);
            // TODO 暂时不处理返回结果，如果需要给用户提示添加成功，需要处理结果
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ItemCart> queryCartList(User user) {
        String url = CART_URL + "/rest/cart/query/" + user.getId();
        try {
            String jsonData = this.apiService.doGet(url);
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            if (jsonNode.get("status").asInt() == 200) {
                ArrayNode arrayNode = (ArrayNode) jsonNode.get("data");
                List<ItemCart> result = new ArrayList<ItemCart>();
                for (JsonNode jn : arrayNode) {
                    ItemCart itemCart = new ItemCart();
                    itemCart.setId(jn.get("itemId").asLong());
                    itemCart.setImage(jn.get("itemImage").asText());
                    itemCart.setNum(jn.get("num").asInt());
                    itemCart.setPrice(jn.get("itemPrice").asLong());
                    itemCart.setTitle(jn.get("itemTitle").asText());
                    result.add(itemCart);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateNum(User user, Long itemId, Integer num) {
        try {
            String url = CART_URL + "/rest/cart/update/num/" + user.getId() + "/" + itemId + "/" + num;
            this.apiService.doPost(url);
            // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(User user, Long itemId) {
        try {
            String url = CART_URL + "/rest/cart/delete/" + user.getId() + "/" + itemId;
            this.apiService.doPost(url);
            // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
