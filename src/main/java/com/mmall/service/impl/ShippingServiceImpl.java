package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount>0){
            Map result = new HashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("增加收货地址成功",result);
        }
        return ServerResponse.createBySuccess("新增收货地址失败");
    }

    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {
        Integer countRow = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(countRow>0){
            return ServerResponse.createBySuccessMessage("地址删除成功");
        }
        return ServerResponse.createByErrorMessage("地址删除失败");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        Integer countRow = shippingMapper.updateByPrimaryKey(shipping);
        if(countRow > 0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
