package com.mmall.controller.backed;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    private static Logger logger = LoggerFactory.getLogger(CategoryManageController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse<String> addCategory(String categoryName, @RequestParam(value="parentId",defaultValue = "0") Integer parentId, HttpSession session){
        logger.info("接口","接收前端参数：categoryName"+categoryName+",parentId"+parentId);
        UserVo userVo = (UserVo) session.getAttribute(Const.CURRENT_USER);
        if(userVo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录请登录");
        }
        if(userService.checkAdminRole(userVo).isSuccess()){
            return categoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    @RequestMapping("setCategory_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(String categoryName,Integer categoryId,HttpSession session){
        UserVo userVo = (UserVo) session.getAttribute(Const.CURRENT_USER);
        if(userVo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录请登录");
        }
        if(userService.checkAdminRole(userVo).isSuccess()){
            return categoryService.updateCategoryName(categoryName,categoryId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId ){
        UserVo userVo = (UserVo) session.getAttribute(Const.CURRENT_USER);
        if(userVo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录请登录");
        }
        if(userService.checkAdminRole(userVo).isSuccess()){
            return categoryService.getChildrenCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作请联系管理员");
        }
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getDeepCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue = "0")Integer capacityId){
        UserVo userVo = (UserVo) session.getAttribute(Const.CURRENT_USER);
        if(userVo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录请登录");
        }
        if(userService.checkAdminRole(userVo).isSuccess()){
            return categoryService.selectCategoryAndChild(capacityId);
        }else{
            return ServerResponse.createByErrorMessage("无权限，请联系管理员");
        }
    }
}
