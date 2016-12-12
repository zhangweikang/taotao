package com.taotao.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.vo.Order;
import com.taotao.web.vo.User;
import com.taotao.web.vo.UserVo;

@Service
public class UserService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ApiService apiService;

    @Value("${SSO_REGISTER_URL}")
    private String SSO_REGISTER_URL;

    @Value("${SSO_LOGIN_URL}")
    private String SSO_LOGIN_URL;

    @Value("${SSO_QUERY_URL}")
    private String SSO_QUERY_URL;
    
    @Value("${SSO_UPDATE_URL}")
    private String SSO_UPDATE_URL;

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

    public User queryUserByPhone(String phone) {
        // TODO Auto-generated method stub
        try {
            String jsonData=apiService.doPost(SSO_QUERY_URL+phone);
            //System.out.println("这是userService doPost"+jsonData);
            TaotaoResult result = TaotaoResult.formatToPojo(jsonData, User.class);
            if(result == null || result.getStatus() != 200){
                return null;
            }
            return (User) result.getData();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return null;
        
        
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


    //发送验证码
    public String sendCode(String phone, String checkCode) {
        // TODO Auto-generated method stub
        String url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit"
                + "&account=cf_didi&password=didi123"+"&mobile="+phone
                + "&content=您的验证码是："+ checkCode+"。请不要把验证码泄露给其他人。";
        try {
            apiService.doGet(url);
            
            //底下有问题
            /*
             * <SubmitResult xmlns="http://106.ihuyi.cn/">
            <code>2</code>
            <msg>提交成功</msg>
            <smsid>70955286</smsid>
            </SubmitResult>*/
            /*TaotaoResult taotaoResult = TaotaoResult.format(jsonData);
            System.out.println(jsonData);
            System.out.println(taotaoResult);
            if (null != taotaoResult && taotaoResult.getStatus() == 200) {
                
                return taotaoResult.getMsg();
            }*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; 
        
        //return null;
    }

    //修改密码
    public String updatePassword(String password, String id) {
        // TODO Auto-generated method stub
        Map<String, String> params  = new HashMap<String, String>();
        params.put("password", password);
        params.put("id", id);
        try {
            String jsonData = apiService.doGet(SSO_UPDATE_URL,params);
            System.out.println(jsonData);
            TaotaoResult taotaoResult = TaotaoResult.format(jsonData);
            System.out.println(taotaoResult);
            if (null != taotaoResult && taotaoResult.getStatus() == 200) {
                
                return taotaoResult.getData().toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
           return null;
    }

    


    public TaotaoResult updateUserInfo(UserVo userVo) {

        try {

            String json = this.apiService.doPostJson("http://sso.taotao.com/user/update/userinfo",
                    MAPPER.writeValueAsString(userVo));
            if (null != json) {
                return TaotaoResult.ok();
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return TaotaoResult.build(201, "用户修改资料失败");
        }

    }

    public UserVo queryInfoByUserId(Long id) {

        String url = "http://sso.taotao.com/user/queryinfo/"+id;
        try {

            String json =this.apiService.doGet(url);
            
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, UserVo.class);
            return (UserVo) taotaoResult.getData();
            
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("查询用户资料失败~",e);
        }

        return null;
    }


}
