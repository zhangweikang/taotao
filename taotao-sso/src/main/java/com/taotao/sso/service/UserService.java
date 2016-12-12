package com.taotao.sso.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import com.taotao.sso.pojo.UserVo;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String TICKET = "TICKET_";

    public static final Integer TICKET_TIME = 60 * 60 * 2;

    public TaotaoResult check(String param, Integer type) {
        if (type < 1 || type > 3) {
            return TaotaoResult.build(400, "非法参数! type in [1,2,3]");
        }
        try {
            User user = this.userMapper.queryByWhere(type, param);
            return TaotaoResult.ok(user != null);
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(400, "非法参数!");
        }
    }

    public TaotaoResult saveUser(User user) {
        try {
            // 设置加密之后密码，采用MD5加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            this.userMapper.saveUser(user);
            return TaotaoResult.ok(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(201, "注册失败!");
        }
    }

    public TaotaoResult login(String username, String passwd) {
        // 根据用户名从数据库中查询数据
        User user = this.userMapper.queryByWhere(1, username);

        // 对比密码
        if (null == user || !StringUtils.equals(user.getPassword(), DigestUtils.md5Hex(passwd))) {
            return TaotaoResult.build(201, "用户名或密码错误!");
        }

        // 生成ticket，生成规则：md5(username + date)
        String ticket = DigestUtils.md5Hex(user.getUsername() + "_" + System.currentTimeMillis());

        try {
            // 将用户的数据保存到redis中
            this.redisService.set(TICKET + ticket, MAPPER.writeValueAsString(user), TICKET_TIME);
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(202, "登录出错!请稍候再试!");
        }

        // 登录成功返回生成的ticket
        return TaotaoResult.ok(ticket);
    }

    public TaotaoResult queryByTicket(String ticket) {
        try {
            String key = TICKET + ticket;
            String jsonData = this.redisService.get(key);
            if (StringUtils.isBlank(jsonData)) {
                return TaotaoResult.build(202, "ticket已失效!");
            }
            // 重置ticket生存时间
            this.redisService.expire(key, TICKET_TIME);
            return TaotaoResult.ok(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(201, "查询用户信息失败! ticket = " + ticket);
        }
    }

    //t通过手机号查找用户
    public TaotaoResult queryByPhone(String phone) {
        // TODO Auto-generated method stub
        try {
            User user = userMapper.queryUserByPhone(phone);
            //System.out.println(user);
            return TaotaoResult.ok(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return TaotaoResult.build(400, "用户名不存在!"); 
        }
    }

    //通过用户id更改密码
    public TaotaoResult updatePasswordById(String password, String id) {
        // TODO Auto-generated method stub
        password = DigestUtils.md5Hex(password);
        try {
            Integer count = userMapper.updatePasswordById(password,id);
            //System.out.println(count);
            return TaotaoResult.ok(count);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return TaotaoResult.build(400, "修改密码失败");
        }
    }
    
    //修改用户资料
    public TaotaoResult updateUserInfo(UserVo userVo) {

        try {
            Long userInfoId = userVo.getUserId();
            UserVo exsitUserVo = this.userMapper.queryInfoByUserId(userInfoId);
            if(null==exsitUserVo)
            {
                this.userMapper.saveUserInfo(userVo);
                return TaotaoResult.ok(userVo.getUserId());
            }
            this.userMapper.updateUserInfo(userVo);
            return TaotaoResult.ok(userVo.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(201, "修改用户资料失败!");
        }
        
    }
    

    //查询用户资料
    public UserVo queryInfoByUserId(Long id) {

        return this.userMapper.queryInfoByUserId(id);
        
    }
    
    
    
   

}
