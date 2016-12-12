package com.taotao.sso.mapper;

import org.apache.ibatis.annotations.Param;

import com.taotao.sso.pojo.User;
import com.taotao.sso.pojo.UserVo;

public interface UserMapper {

    User queryByWhere(@Param("type") Integer type, @Param("param") String param);

    void saveUser(User user);

    //通过手机号查找用户
    User queryUserByPhone(@Param("phone")String phone);

    //通过用户id修改密码
    Integer updatePasswordById(@Param("password")String password, @Param("id")String id);

    void updateUserInfo(UserVo userVo);

    UserVo queryInfoByUserId(Long id);

    void saveUserInfo(UserVo userVo);
    
}
