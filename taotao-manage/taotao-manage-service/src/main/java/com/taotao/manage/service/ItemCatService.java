package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.common.vo.ItemCatData;
import com.taotao.common.vo.ItemCatResult;
import com.taotao.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCatService.class);

    // @Autowired
    // private ItemCatMapper itemCatMapper;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    public static final String REDIS_KEY_CAT_ALL = "MANAGE_TAOTAO_ITEM_CAT_WEB_ALL_TREE";
    
    public static final Integer TIME = 60 * 60 * 24 * 30;

    public List<ItemCat> queryList(Long parentId) {
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);
        return super.queryByWhere(itemCat);
    }

    // @Override
    // public TaotaoMapper<ItemCat> getTaotaoMapper() {
    // return this.itemCatMapper;
    // }

    /**
     * 全部查询，并且生成树状结构
     * 
     * @return
     */
    public ItemCatResult queryAllToTree() {

        // 先到缓存中命中
        // 命名规范：项目名_模块名_自定义key
        try {
            String json = this.redisService.get(REDIS_KEY_CAT_ALL);
            if (StringUtils.isNotBlank(json)) {
                return MAPPER.readValue(json, ItemCatResult.class);// 返回对象
            }
        } catch (Exception e) {
            LOGGER.error("从redis中获取商品类目数据出错!", e);
        }

        ItemCatResult result = new ItemCatResult();
        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> cats = super.queryAll();

        // 转为map存储，key为父节点ID，value为数据集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
        for (ItemCat itemCat : cats) {
            if (!itemCatMap.containsKey(itemCat.getParentId())) {
                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装一级对象
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setNname("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
            result.getItemCats().add(itemCatData);
            if (!itemCat.getIsParent()) {
                continue;
            }

            // 封装二级对象
            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
            List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
            itemCatData.setItems(itemCatData2);
            for (ItemCat itemCat2 : itemCatList2) {
                ItemCatData id2 = new ItemCatData();
                id2.setNname(itemCat2.getName());
                id2.setUrl("/products/" + itemCat2.getId() + ".html");
                itemCatData2.add(id2);
                if (itemCat2.getIsParent()) {
                    // 封装三级对象
                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
                    List<String> itemCatData3 = new ArrayList<String>();
                    id2.setItems(itemCatData3);
                    for (ItemCat itemCat3 : itemCatList3) {
                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }
        }

        // 将结果集写入懂啊redis中
        try {
            this.redisService.set(REDIS_KEY_CAT_ALL, MAPPER.writeValueAsString(result), TIME);
        } catch (Exception e) {// 这里必须使用Exception捕获异常
            LOGGER.error("添加商品类目数据到redis出错!", e);
        }
        return result;
    }

}
