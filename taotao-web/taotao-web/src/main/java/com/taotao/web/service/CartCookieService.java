package com.taotao.web.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ItemService itemService;

    public void addItem(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 读取cookie中的数据
        String cookieData = CookieUtils.getCookieValue(request, TT_CART, "UTF-8");

        List<ItemCart> itemCarts = null;

        if (StringUtils.isBlank(cookieData)) {
            // 如果不存在，创建新的数据到cookie
            itemCarts = new ArrayList<ItemCart>();
        } else {
            // 如果存在，将商品合并到cookie的数据中
            try {
                itemCarts = MAPPER.readValue(cookieData,
                        MAPPER.getTypeFactory().constructCollectionType(List.class, ItemCart.class));
            } catch (Exception e) {
                LOGGER.error("解析cookie中的购物车数据出错!", e);
            }
            if (null == itemCarts) {
                itemCarts = new ArrayList<ItemCart>();
            }
        }

        ItemCart itemCart = null;
        // 判断商品是否存在购物车中
        for (ItemCart ic : itemCarts) {
            if (ic.getId().longValue() == itemId.longValue()) {
                // 存在,传递的商品数量
                ic.setNum(ic.getNum() + num);
                itemCart = ic;
                break;
            }
        }

        if (itemCart == null) {
            itemCart = new ItemCart();
            // 查询商品数据
            itemCart.setId(itemId);
            Item item = this.itemService.getItemById(itemId);
            itemCart.setTitle(item.getTitle());
            itemCart.setImage(item.getImages()[0]);
            itemCart.setNum(num);
            itemCart.setPrice(item.getPrice());
            itemCarts.add(itemCart);
        }

        // 将全部购物车数据写入到cookie中
        try {
            CookieUtils.setCookie(request, response, TT_CART, MAPPER.writeValueAsString(itemCarts),
                    TT_CART_TIME, "UTF-8");
        } catch (JsonProcessingException e) {
            LOGGER.error("保存cookie中的购物车数据出错!", e);
        }
    }

    /**
     * 从cookie中查询数据
     * 
     * @param request
     * @return
     */
    public List<ItemCart> queryCartList(HttpServletRequest request) {
        // 读取cookie中的数据
        String cookieData = CookieUtils.getCookieValue(request, TT_CART, "UTF-8");
        if (StringUtils.isNotBlank(cookieData)) {
            try {
                return MAPPER.readValue(cookieData,
                        MAPPER.getTypeFactory().constructCollectionType(List.class, ItemCart.class));
            } catch (Exception e) {
                LOGGER.error("解析cookie中的购物车数据出错!", e);
            }
        }
        return new ArrayList<ItemCart>(0);
    }

    public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        List<ItemCart> itemCarts = this.queryCartList(request);
        if (itemCarts.isEmpty()) {
            return;
        }
        for (ItemCart itemCart : itemCarts) {
            if (itemCart.getId().longValue() == itemId.longValue()) {
                itemCart.setNum(num);
                break;
            }
        }
        // 将全部购物车数据写入到cookie中
        try {
            CookieUtils.setCookie(request, response, TT_CART, MAPPER.writeValueAsString(itemCarts),
                    TT_CART_TIME, "UTF-8");
        } catch (JsonProcessingException e) {
            LOGGER.error("保存cookie中的购物车数据出错!", e);
        }
    }

    public void delete(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<ItemCart> itemCarts = this.queryCartList(request);
        if (itemCarts.isEmpty()) {
            return;
        }
        for (ItemCart itemCart : itemCarts) {
            if (itemCart.getId().longValue() == itemId.longValue()) {
                itemCarts.remove(itemCart);
                break;
            }
        }
        // 将全部购物车数据写入到cookie中
        try {
            CookieUtils.setCookie(request, response, TT_CART, MAPPER.writeValueAsString(itemCarts),
                    TT_CART_TIME, "UTF-8");
        } catch (JsonProcessingException e) {
            LOGGER.error("保存cookie中的购物车数据出错!", e);
        }
    }

}
