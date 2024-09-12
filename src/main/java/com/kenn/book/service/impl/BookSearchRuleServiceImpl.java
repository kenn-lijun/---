package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenn.book.domain.entity.BookSearchRule;
import com.kenn.book.mapper.BookSearchRuleMapper;
import com.kenn.book.service.BookSearchRuleService;
import com.kenn.book.service.BookSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @Description TODO
 * @ClassName BookSearchRuleServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:11:00
 */
@Service
@RequiredArgsConstructor
public class BookSearchRuleServiceImpl extends ServiceImpl<BookSearchRuleMapper, BookSearchRule> implements BookSearchRuleService {

    private final BookSourceService bookSourceService;

    @Override
    public BookSearchRule getBySourceId(Long sourceId) throws JsonProcessingException {
        // 书源初始化
        bookSourceService.init(sourceId);
        // 获取书籍搜索规则配置
        LambdaQueryWrapper<BookSearchRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookSearchRule::getSourceId, sourceId);
        BookSearchRule searchRule = getOne(queryWrapper);
        Assert.notNull(searchRule, "未获取到书籍搜索规则");
        return searchRule;
    }

}
