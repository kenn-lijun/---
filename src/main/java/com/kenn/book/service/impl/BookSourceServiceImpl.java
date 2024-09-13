package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.entity.BookSource;
import com.kenn.book.mapper.BookSourceMapper;
import com.kenn.book.service.BookSourceService;
import com.kenn.book.utils.JsUtils;
import com.kenn.book.utils.RegexUtils;
import com.kenn.book.utils.StringUtils;
import com.kenn.book.utils.ThreadLocalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @ClassName BookSourceServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月27日 16:33:00
 */
@Service
@RequiredArgsConstructor
public class BookSourceServiceImpl extends ServiceImpl<BookSourceMapper, BookSource> implements BookSourceService {

    @Override
    public void init(Long sourceId) throws JsonProcessingException {
        // 获取书源基本信息
        BookSource source = getById(sourceId);
        Assert.notNull(source, "未获取到书源基本信息");
        // ThreadLocal设置书源baseUrl变量
        ThreadLocalUtils.setBaseUrlCache(source.getBaseUrl());
        if (StringUtils.isNotEmpty(source.getHeader())) {
            String jsCode = RegexUtils.getRegexCode(source.getHeader(), Constants.JS_REGEX_TAG);
            if (StringUtils.isNotEmpty(jsCode)) {
                JsUtils.execute(jsCode, null);
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> headerMap = objectMapper.readValue(source.getHeader(), new TypeReference<Map<String, String>>() {});
                headerMap.forEach(ThreadLocalUtils::addHeader);
            }
        }
    }

    /**
     * 查询所有未删除的书源列表
     * @param :
     * @return: java.util.List<com.kenn.book.domain.entity.BookSource>
     * @author: kenn
     * @date: 2024/6/20 11:52
     */
    @Override
    public List<BookSource> listAll() {
        LambdaQueryWrapper<BookSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(BookSource::getSort);
        return list(queryWrapper);
    }

}
