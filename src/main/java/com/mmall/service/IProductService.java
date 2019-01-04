package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setStatus(Product product);

    ServerResponse manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum,int pageSizes);
}
