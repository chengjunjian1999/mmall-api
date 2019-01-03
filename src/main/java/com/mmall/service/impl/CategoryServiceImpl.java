package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.spi.ServiceRegistry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if(parentId == null || StringUtils.isBlank(categoryName)){
            ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setStatus(true);
        category.setParentId(parentId);
        category.setName(categoryName);

        int row =categoryMapper.insertSelective(category);
        if(row<0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }else{
            return ServerResponse.createBySuccessMessage("添加品类失败");
        }
    }

    @Override
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId) {
        if(categoryName==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        Integer rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerResponse.createBySuccessMessage("更新品类成功");
        }else{
            return ServerResponse.createByErrorMessage("更新品类失败");
        }
    }

    @Override
    public ServerResponse<List<Category>> getChildrenCategory(Integer categoryId) {
        List<Category> category = categoryMapper.selectChildrenCategory(categoryId);
        if(CollectionUtils.isEmpty(category)){
            logger.info(categoryId+"：未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(category);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChild(Integer categoryId) {
        Set<Category> categorySet = new HashSet<>();
        findChildCategory(categorySet,categoryId);

        List<Integer> categoryIdList = new ArrayList<>();
        for(Category categoryItem : categorySet){
            categoryIdList.add(categoryItem.getId());
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }
    //递归子节点
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){

        Category category = categoryMapper.selectByPrimaryKey(categoryId);

        if(category!=null){
            categorySet.add(category);
        }
        //查询到的然后递归
        List<Category> categoryList = categoryMapper.selectChildrenCategory(categoryId);
        for(Category categoryItem : categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
