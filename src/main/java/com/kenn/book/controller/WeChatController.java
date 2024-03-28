package com.kenn.book.controller;


import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.res.WeChatResponse;
import com.kenn.book.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Description: 微信登录注册
 * date: 2021/6/10 10:09
 *
 * @author 18305
 */
@RestController
@RequestMapping("/weChat")
public class WeChatController {

    @Value("${appId}")
    private String appId;

    @Value("${secret}")
    private String secret;

    @Value("${grantType}")
    private String grantType;

    private final String WECHAT_LOGIN_BASE_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * @Description 登录判断微信用户是否存在于后台系统，存在则登录，不存在根据手机号作为昵称注册到后台
     */
    @PostMapping("/login")
    public Result<WeChatResponse> login(@RequestParam("code") String code) throws JsonProcessingException {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("appid",appId);
        paramMap.put("secret",secret);
        paramMap.put("js_code",code);
        paramMap.put("grant_type",grantType);
        ObjectMapper objectMapper = new ObjectMapper();
        //解析返回字符串
        String resultJson = HttpRequest.get(WECHAT_LOGIN_BASE_URL).form(paramMap).execute().body();
        WeChatResponse weChatResponse = objectMapper.readValue(resultJson, WeChatResponse.class);

        return StringUtils.isEmpty(weChatResponse.getErrmsg()) ? Result.success(weChatResponse) : Result.error(weChatResponse);
    }

}
