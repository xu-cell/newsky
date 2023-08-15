package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.WeChatPayUtil;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    WeChatProperties weChatProperties;

    public static final String WECHATAPI = "https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User login(UserLoginDTO userLoginDTO) {

        // 调用微信接口获取openid

        String openid = getOpenId(userLoginDTO);


        // 查询数据库，完成自动注册功能

        User user = userMapper.getUser(openid);
        if(user == null) {
            user = User.builder().build();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insertUser(user);
        }



        //  返回用户对象
        return user;
    }

    private String  getOpenId(UserLoginDTO userLoginDTO) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appid",weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", userLoginDTO.getCode());
        paramMap.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WECHATAPI,paramMap);

        JSONObject jsonObject = JSON.parseObject(json);

        String openid = jsonObject.getString("openid");
        return openid;
    }
}
