package com.kenn.book.interceptor;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.properties.KennProperties;
import com.kenn.book.filter.RepeatedlyRequestWrapper;
import com.kenn.book.utils.CollectionUtils;
import com.kenn.book.utils.JavaUtils;
import com.kenn.book.utils.ServletUtils;
import com.kenn.book.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SignatureInterceptor implements HandlerInterceptor {

    private final KennProperties kennProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String requestURI = request.getRequestURI();
        if (StringUtils.matches(requestURI, Arrays.asList(kennProperties.getExcludes()))) {
            return true;
        }

        Map<String, String[]> parameterMap = null;
        if (request instanceof RepeatedlyRequestWrapper) {
            RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
            String body = StreamUtils.copyToString(repeatedlyRequest.getInputStream(), StandardCharsets.UTF_8);
            if (StringUtils.isNotEmpty(body)) {
                JsonNode jsonNode = objectMapper.readTree(body);
                if (!jsonNode.isArray()) {
                    Map<String, Object> requestBodyParams = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {});
                    parameterMap = new HashMap<>(requestBodyParams.size());
                    for (Map.Entry<String, Object> entry : requestBodyParams.entrySet()) {
                        if (entry.getValue() instanceof String) {
                            parameterMap.put(entry.getKey(), new String[]{(String) entry.getValue()});
                        } else if (entry.getValue() instanceof Collection) {
                            Collection<?> valueCollection = (Collection<?>) entry.getValue();
                            parameterMap.put(entry.getKey(), valueCollection.stream().map(Object::toString).toArray(String[]::new));
                        } else {
                            parameterMap.put(entry.getKey(), new String[] {entry.getValue().toString()});
                        }
                    }
                } else {
                    // todo json是list的情况
                }
            }
        }

        // body参数为空，获取Parameter的数据
        if (CollUtil.isEmpty(parameterMap)) {
            parameterMap = request.getParameterMap();
        }

        // 从请求头中获取签名
        String requestSign = request.getHeader(Constants.SIGN_KEY);

        // 该项目必要的请求参数: 时间戳timestamp
        if (CollUtil.isEmpty(parameterMap) || !parameterMap.containsKey(Constants.TIMESTAMP_KEY) || StringUtils.isEmpty(requestSign)) {
            Result<?> result = Result.error(HttpServletResponse.SC_UNAUTHORIZED, "请求未授权");
            ServletUtils.renderString(response, objectMapper.writeValueAsString(result));
            return false;
        }

        // 验证签名
        String signature = CollectionUtils.generateSignature(parameterMap) + kennProperties.getSignKey();
        if (!requestSign.equalsIgnoreCase(JavaUtils.md5Encode(signature))) {
            Result<?> result = Result.error(HttpServletResponse.SC_UNAUTHORIZED, "请求未授权");
            ServletUtils.renderString(response, objectMapper.writeValueAsString(result));
            return false;
        }

        // 验证请求是否已过期
        String timestampStr = parameterMap.get(Constants.TIMESTAMP_KEY)[0];
        if (isMoreThanTenMinutes(Long.parseLong(timestampStr))) {
            Result<?> result = Result.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "请求已过期");
            ServletUtils.renderString(response, objectMapper.writeValueAsString(result));
            return false;
        }

        return true; // 如果签名有效，继续处理请求
    }

    public static boolean isMoreThanTenMinutes(long timestamp) {
        // 计算两个时间戳的差值（单位：毫秒）
        long difference = Math.abs(System.currentTimeMillis() - timestamp);
        // 将10分钟转换为毫秒
        long tenMinutesInMilliseconds = 10 * 60 * 1000;
        // 判断差值是否大于10分钟
        return difference > tenMinutesInMilliseconds;
    }

}
