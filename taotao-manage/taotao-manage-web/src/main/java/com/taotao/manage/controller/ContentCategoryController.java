package com.taotao.manage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@RequestMapping(value = "content/category")
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 查询内容分类列表
     * 
     * @param parentId
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public List<ContentCategory> queryList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        return this.contentCategoryService.queryByWhere(contentCategory);
    }

    /**
     * 创建新的分类
     * 
     * @param contentCategory
     * @return
     */
    @RequestMapping(value = "create")
    @ResponseBody
    public TaotaoResult create(ContentCategory contentCategory) {
        // 初始化数据
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(contentCategory.getCreated());
        contentCategory.setIsParent(false);
        this.contentCategoryService.save(contentCategory);

        // 判断父节点的isParent是否为true
        ContentCategory parentCategory = this.contentCategoryService.queryById(contentCategory.getParentId());
        if (!parentCategory.getIsParent()) {
            // 修改isParent为true
            parentCategory.setIsParent(true);
            parentCategory.setUpdated(new Date());
            this.contentCategoryService.update(parentCategory);
        }

        return TaotaoResult.ok(contentCategory);
    }

    /**
     * 重命名
     * 
     * @param contentCategory
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public TaotaoResult update(ContentCategory contentCategory) {
        contentCategory.setUpdated(new Date());
        this.contentCategoryService.updateSelective(contentCategory);
        return TaotaoResult.ok();
    }

    @RequestMapping("delete")
    @ResponseBody
    public TaotaoResult delete(ContentCategory param) {
        // 判断当前节点是否为父节点，如果是父节点要级联删除所有的子节点
        ContentCategory contentCategory = this.contentCategoryService.queryById(param.getId());
        if (contentCategory.getIsParent()) {
            // 级联删除 递归实现
            List<Long> ids = new ArrayList<Long>();
            findContentCategoryByParnetId(ids, param.getId());
            this.contentCategoryService.deleteByIds(ids.toArray(new Long[] {}));
        }

        // 删除当前节点
        this.contentCategoryService.deleteById(param.getId());

        // 判断当前节点的父节点是否有子节点，如果没有，设置isparent为false
        ContentCategory where = new ContentCategory();
        where.setParentId(param.getParentId());
        List<ContentCategory> list = this.contentCategoryService.queryByWhere(where);
        if (null == list || list.isEmpty()) {
            ContentCategory parent = new ContentCategory();
            parent.setId(param.getParentId());
            parent.setIsParent(false);
            parent.setUpdated(new Date());
            this.contentCategoryService.updateSelective(parent);
        }
        return TaotaoResult.ok();
    }

    /**
     * 根据parentId递归查找子节点
     * 
     * @param ids
     * @param parentId
     */
    private void findContentCategoryByParnetId(List<Long> ids, Long parentId) {
        ContentCategory where = new ContentCategory();
        where.setParentId(parentId);
        List<ContentCategory> contentCategories = this.contentCategoryService.queryByWhere(where);
        if (contentCategories != null && !contentCategories.isEmpty()) {
            for (ContentCategory contentCategory : contentCategories) {
                ids.add(contentCategory.getId());
                // 递归
                findContentCategoryByParnetId(ids, contentCategory.getId());
            }
        }
    }

}
