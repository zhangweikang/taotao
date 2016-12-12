package com.taotao.manage.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

/**
 * 
 * 处理商品相关的业务逻辑
 * 
 */
@Service
public class ItemService extends BaseService<Item> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public EasyUIResult queryItemList(Integer page, Integer rows) {
        PageInfo<Item> pageInfo = super.queryPageList(page, rows);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

    public Long saveItem(Item item, String desc, String itemParams) {
        // 保存商品数据到数据库
        // 数据初始化
        item.setCreated(new Date());
        item.setUpdated(item.getCreated());
        item.setStatus(1);
        super.save(item);

        Item item2 = super.queryById(item.getId());
        System.out.println(item2);

        // 保存商品描述数据到数据库
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(itemDesc.getCreated());
        this.itemDescService.save(itemDesc);

        // 保存商品规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(itemParamItem.getCreated());
        this.itemParamItemService.save(itemParamItem);

        return item.getId();
    }

    /**
     * 更新商品数据
     * 
     * @param item
     * @param desc
     */
    public void updateItem(Item item, String desc, String itemParams) {
        item.setUpdated(new Date());

        item.setCreated(null);// 强制设置为null
        super.updateSelective(item);

        // 更新商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setUpdated(new Date());
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.updateSelective(itemDesc);

        // 更新规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        itemParamItem.setUpdated(new Date());

        this.itemParamItemService.updateSelective(itemParamItem);

        // 调用前台的修改接口，通知前台系统删除缓存中的数据(之前实现)

        // 调用search中的接口，通知search修改solr中的数据(之前实现)

        try {
            // 使用MQ实现
            // 要处理异常信息，发送出错要进行重试，一般情况下重试三次 , 每次重试要停留不同的时间，第一次，1S，第二次，5S，第三次，30S
            sendMQ("update", item.getId());
        } catch (Exception e) {
            LOGGER.error("发送更新商品消息失败! itemId = " + item.getId(), e);
            // 错误重试
            for (int i = 1; i < 4; i++) {
                try {
                    Thread.sleep(i * 1000);
                    sendMQ("update", item.getId());
                    break;
                } catch (Exception e1) {
                    LOGGER.error("发送更新商品消息失败! 已重试 " + i+" 次. itemId = " + item.getId(), e);
                }
            }
            
            // 重试依然失败，如何处理？
            // 通知相关人员，发邮件、发短信
        }

    }
    
    private void sendMQ(String key,Object message){
        this.rabbitTemplate.setRoutingKey(key);
        this.rabbitTemplate.convertAndSend(message);
    }

}
