package com.taotao.web.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;

@Component
public class DeleteRedisItem {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRedisItem.class);

    @Autowired
    private RedisService redisService;

    public void execute(Long itemId) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("接受到MQ消息，内容为：" + itemId);
        }
        this.redisService.del(ItemService.REDIS_KEY_ITEM + itemId);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("成功删除商品缓存数据 itemId = " + itemId);
        }
    }

}
