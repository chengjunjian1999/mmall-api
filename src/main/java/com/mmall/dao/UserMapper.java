package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectLogin(@Param("username") String username,@Param("password")String password);

    int checkUsername(String username);

    int checkEmail(String email);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    String selectQuestionByUsername(String question);

    //获取答案是否正确
    int checkAnswer(String username,String question,String answer);

    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew") String passwordNew);

    int checkPassword(@Param(value="password")String password,@Param(value="userId") Integer userId);

    int checkEmailByUserId(@Param(value = "email")String email,@Param(value = "userId") Integer userId);
}