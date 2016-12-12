package com.taotao.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.common.util.CookieUtils;
import com.taotao.web.vo.Item;
import com.taotao.web.vo.ItemCart;

@Service
public class CartCookieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartCookieService.class);

    public static final String TT_CART = "TT_CART";

    public static final Integer TT_CART_TIME = 60 * 60 * 24 * 7;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    @Autowired
    private ItemService itemService;

    /**
     * 添加商品到购物车
     * 
     * @param itemId
     * @param num
     * @param request
     * @param response void
     */
    // 将商品信息写入到cookie中
    public void addItem(Long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        // 从cookie中获取 redis标示
        String key = CookieUtils.getCookieValue(request, TT_CART);
        Map<String, String> map = null;
        // 判断请求中是否带有cookie数据
        if (StringUtils.isBlank(key)) {
            // cookie中没有数据 创建一个
            map = new HashMap<String, String>();
        } else {
            try {
                map = redisService.hgetAll(1, key);
                if (map.isEmpty()) {
                    map = new HashMap<String, String>();
                }
            } catch (Exception e) {
                LOGGER.error("从缓存中命中购物车数据失败,购物车缓存以失效");
            }

        }
        // 判断商品是否存在redis中
        if (!map.isEmpty() && redisService.hexists(1, key, String.valueOf(itemId))) {
            // 说明redis中有保存该商品 那么就修改其数量

            try {
                // 从redis中获取商品数据
                String jsonItem = redisService.hget(1, key, String.valueOf(itemId));
                // 将json串转换成itemcart对像
                ItemCart itemCart = MAPPER.readValue(jsonItem, ItemCart.class);
                // 重新给itemcart数量设置值
                itemCart.setNum(itemCart.getNum() + num);
                // 再将itemcart对像转换成json串保存在redis中 并重新设置生存时间
                String itemCart2 = MAPPER.writeValueAsString(itemCart);
                redisService.hset(1, key, String.valueOf(itemId), itemCart2, TT_CART_TIME);
                map.put(String.valueOf(itemId), itemCart2);
            } catch (Exception e) {
                LOGGER.error("修改商品数量失败");
            }
        } else {
            // 说明redis中没有改商品信息
            Item item = itemService.getItemById(itemId);
            ItemCart itemCart = new ItemCart();
            itemCart.setId(itemId);
            itemCart.setImage(item.getImages()[0]);
            itemCart.setNum(num);
            itemCart.setPrice(item.getPrice());
            itemCart.setTitle(item.getTitle());
            try {
                map.put(String.valueOf(itemId), MAPPER.writeValueAsString(itemCart));
            } catch (JsonProcessingException e1) {
                LOGGER.error("数据格式转换失败");
            }
        }
        // 删除cookie中的数据
        CookieUtils.deleteCookie(request, response, TT_CART);
        // 删除redis缓存
        if (StringUtils.isNotBlank(key)) {
            redisService.del(1, key);
        }
        try {
            // 生成一个新的key
            String uuidKey = UUID.randomUUID().toString().replace("-", "");
            // 将数据写入redis中
            redisService.hmset(1, uuidKey, map, TT_CART_TIME);
            // 重新将数据写入到cookie中
            CookieUtils.setCookie(request, response, TT_CART, uuidKey, TT_CART_TIME);
        } catch (Exception e) {
            LOGGER.error("添加购物车信息到redishash中失败");
        }
    }

    /**
     * 获取redis中的商品数据
     * 
     * @param request
     * @return List<ItemCart>
     */
    public List<ItemCart> queryCartList(HttpServletRequest request) {
        // 从cookie中获取 redis标示
        String key = CookieUtils.getCookieValue(request, TT_CART);

        List<ItemCart> itemCarts = new ArrayList<ItemCart>();

        Map<String, String> map = null;

        if (StringUtils.isBlank(key)) {
            // 如果为空没有获取到数据
            return new ArrayList<ItemCart>(0);
        }
        // 判断redis中的hash值存储的商品是否已超时不存在 不存在则将商品id 从数组中删除
        try {
            map = redisService.hgetAll(1, key);
        } catch (Exception e) {
            LOGGER.error("从缓存中命中购物车数据失败,购物车缓存以失效");
        }
        // 判断map是否为空
        if (!map.isEmpty()) {
            // 遍历map
            for (String jsonItemCart : map.values()) {
                try {
                    ItemCart itemCart = MAPPER.readValue(jsonItemCart, ItemCart.class);
                    // 将商品数据添加到 购物车集合
                    itemCarts.add(itemCart);
                } catch (Exception e) {
                    LOGGER.error("数据格式转换失败");
                }
            }
        } else {
            // 从缓存中命中购物车数据失败,购物车缓存以失效
            return new ArrayList<ItemCart>(0);
        }
        return itemCarts;
    }

    /**
     * 修改商品数量
     * 
     * @param itemId
     * @param num
     * @param request
     * @param response void
     */
    public int updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 获取cookie中的标示
        String key = CookieUtils.getCookieValue(request, TT_CART);
        //从后台获取商品总数量
        Item item = itemService.getItemById(itemId);
        try {
            // 从redis中获取商品数据
            String jsonItem = redisService.hget(1, key, String.valueOf(itemId));
            // 将json串转换成itemcart对像
            ItemCart itemCart = MAPPER.readValue(jsonItem, ItemCart.class);
            
            if(item.getNum()>= num){
                // 重新给itemcart数量设置值
                itemCart.setNum(num);
            }else{
                // 重新给itemcart数量设置值
                itemCart.setNum(item.getNum());
            }
            // 再将itemcart对像转换成json串保存在redis中 并重新设置生存时间
            String itemCart2 = MAPPER.writeValueAsString(itemCart);
            redisService.hset(1, key, String.valueOf(itemId), itemCart2, TT_CART_TIME);
            return item.getNum();
        } catch (Exception e) {
            LOGGER.error("修改商品数量失败");
        }
        return item.getNum();
    }

    /**
     * 删除商品
     * 
     * @param itemId
     * @param request
     * @param response void
     */
    public void delete(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        // 获取cookie中的标示
        String key = CookieUtils.getCookieValue(request, TT_CART);
        // 还需要删除 cookie中的id
        try {
            redisService.hdel(1, key, String.valueOf(itemId));
        } catch (Exception e) {
            LOGGER.error("从购物车中删除商品失败");
        }
    }

    public void deleteIds(String[] idss, HttpServletRequest request, HttpServletResponse response) {
     // 获取cookie中的标示
        String key = CookieUtils.getCookieValue(request, TT_CART);
        // 还需要删除 cookie中的id
        try {
            for (String itemId : idss) {
                redisService.hdel(1, key, String.valueOf(itemId));
            }
        } catch (Exception e) {
            LOGGER.error("从购物车中批量删除商品失败");
        }
    }

}
