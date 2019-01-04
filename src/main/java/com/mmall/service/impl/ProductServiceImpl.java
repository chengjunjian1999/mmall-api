package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product != null ){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String [] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length>0){
                    product.setMainImage(subImageArray[0]);
                }
            }
            //idb不是null代表是更新
            if(product.getId()!=null){
                Integer row = productMapper.updateByPrimaryKeySelective(product);
                if(row>0){
                    return ServerResponse.createBySuccessMessage("产品更新成功");
                }else{
                    return ServerResponse.createByErrorMessage("产品更新失败");
                }
            }else{
                Integer row = productMapper.insertSelective(product);
                if(row>0){
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                }else{
                    return ServerResponse.createByErrorMessage("新增产品失败");
                }
            }

        }

        return ServerResponse.createByErrorMessage("新增或修改产品参数不正确");
    }

    @Override
    public ServerResponse setStatus(Product product) {
        if (product.getId() == null || product.getStatus() == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int row = productMapper.updateByPrimaryKeySelective(product);
        if(row>0){
            return ServerResponse.createBySuccessMessage("修改产品状态成功");
        }else{
            return ServerResponse.createByErrorMessage("修改产品状态失败");
        }
    }

    @Override
    public ServerResponse manageProductDetail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);

        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSizes) {
        PageHelper.startPage(pageNum,pageSizes);
        List<Product> productList = productMapper.getProductList();

        List<ProductListVo> productListVoList = new ArrayList<>();
        for(Product productItem : productList){
            ProductListVo productListVo =assembleProductListVo(productItem) ;
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){

        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());


        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        Category category =categoryMapper.selectByPrimaryKey(productDetailVo.getCategoryId());
        //空是根节点
        if(category == null){
            productDetailVo.setCategoryId(0);
        }else{
            productDetailVo.setCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }
}
