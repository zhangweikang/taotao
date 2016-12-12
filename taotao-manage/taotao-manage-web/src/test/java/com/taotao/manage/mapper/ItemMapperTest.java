package com.taotao.manage.mapper;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.manage.pojo.Item;

public class ItemMapperTest {
    
    private ItemMapper itemMapper;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext*.xml");
        this.itemMapper = applicationContext.getBean(ItemMapper.class);
    }

    @Test
    public void testSelect() {
        List<Item> items = this.itemMapper.select(null);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    @Test
    public void testSelectCount() {
        Item item = new Item();
        item.setTitle("aaa");//添加查询条件
        item.setSellPoint("ccc");//添加查询条件
        Integer count = this.itemMapper.selectCount(item);
        System.out.println(count);
    }

    @Test
    public void testSelectByPrimaryKey() {
       Item item =  this.itemMapper.selectByPrimaryKey(40L);
       System.out.println(item);
    }

    @Test
    public void testInsert() {
        Item item = new Item();
        item.setTitle("aaa2");
        item.setSellPoint("ccc2");
        item.setPrice(200L);
        item.setNum(100);
        item.setCid(1L);
        item.setStatus(1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        this.itemMapper.insert(item);
    }

    @Test
    public void testInsertSelective() {
        Item item = new Item();
        item.setTitle("aaa2");
        //item.setSellPoint("ccc2");
        item.setPrice(200L);
        item.setNum(100);
        item.setCid(1L);
        item.setStatus(1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        this.itemMapper.insertSelective(item);
    }

    @Test
    public void testDelete() {
       // this.itemMapper.de
    }
    
    @Test
    public void testDeleteByIDS() {
       this.itemMapper.deleteByIDS(new Object[]{40L,41L});
    }

    @Test
    public void testDeleteByPrimaryKey() {
        fail("Not yet implemented");
        
    }

    @Test
    public void testUpdateByPrimaryKey() {
        fail("Not yet implemented");
//        this.itemMapper.u
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        fail("Not yet implemented");
    }

}
