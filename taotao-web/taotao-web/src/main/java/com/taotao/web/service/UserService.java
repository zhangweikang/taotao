package com.taotao.web.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.service.ApiService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.vo.User;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ApiService apiService;

    @Value("${SSO_REGISTER_URL}")
    private String SSO_REGISTER_URL;

    @Value("${SSO_LOGIN_URL}")
    private String SSO_LOGIN_URL;

    @Value("${SSO_QUERY_URL}")
    private String SSO_QUERY_URL;

    public TaotaoResult register(String username, String password, String phone) {
        Map<String, String> params = new HashMap<String, String>(3);
        params.put("username", username);
        params.put("password", password);
        params.put("phone", phone);
        try {
            String jsonData = this.apiService.doPost(SSO_REGISTER_URL, params);
            TaotaoResult taotaoResult = TaotaoResult.format(jsonData);
            if (taotaoResult.getStatus() == 200) {
                // 注册成功
                return TaotaoResult.ok();
            }
        } catch (Exception e) {
            LOGGER.error("用户注册出错!", e);
        }
        return TaotaoResult.build(201, null, " 请联系客服!");
    }

    /**
     * 实现登录
     * 
     * @param username
     * @param password
     * @return 返回ticket
     */
    public String login(String username, String password) {
        Map<String, String> params = new HashMap<String, String>(2);
        params.put("u", username);
        params.put("p", password);
        try {
            String jsonData = this.apiService.doPost(SSO_LOGIN_URL, params);
            TaotaoResult taotaoResult = TaotaoResult.format(jsonData);
            if (null != taotaoResult && taotaoResult.getStatus() == 200) {
                // 登录成功
                return taotaoResult.getData().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据ticket查询用户信息
     * 
     * @param ticket
     * @return
     */
    public User queryUserByTicket(String ticket) {
        try {
            String jsonData = this.apiService.doGet(SSO_QUERY_URL + ticket);
            TaotaoResult result = TaotaoResult.formatToPojo(jsonData, User.class);
            if (result == null || result.getStatus() != 200) {
                return null;
            }
            return (User) result.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
