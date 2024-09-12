package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenn.book.domain.entity.ChapterSearchRule;
import com.kenn.book.mapper.ChapterSearchRuleMapper;
import com.kenn.book.service.BookSourceService;
import com.kenn.book.service.ChapterSearchRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @Description TODO
 * @ClassName ChapterSearchRuleServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:12:00
 */
@Service
@RequiredArgsConstructor
public class ChapterSearchRuleServiceImpl extends ServiceImpl<ChapterSearchRuleMapper, ChapterSearchRule> implements ChapterSearchRuleService {

    private final BookSourceService bookSourceService;

    @Override
    public ChapterSearchRule getBySourceId(Long sourceId) throws JsonProcessingException {
        // 书源初始化
        bookSourceService.init(sourceId);

        // 获取章节搜索规则配置
        LambdaQueryWrapper<ChapterSearchRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChapterSearchRule::getSourceId, sourceId);
        ChapterSearchRule searchRule = getOne(queryWrapper);
        Assert.notNull(searchRule, "未获取到章节列表规则");
        return searchRule;
    }

}
