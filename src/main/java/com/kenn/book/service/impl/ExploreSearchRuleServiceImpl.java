package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenn.book.domain.entity.ExploreSearchRule;
import com.kenn.book.mapper.ExploreSearchRuleMapper;
import com.kenn.book.service.BookSourceService;
import com.kenn.book.service.ExploreSearchRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @Description TODO
 * @ClassName ExploreSearchRuleServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年03月14日 15:50:00
 */
@Service
@RequiredArgsConstructor
public class ExploreSearchRuleServiceImpl extends ServiceImpl<ExploreSearchRuleMapper, ExploreSearchRule> implements ExploreSearchRuleService {

    private final BookSourceService bookSourceService;

    @Override
    public ExploreSearchRule getBySourceId(Long sourceId) throws JsonProcessingException {
        // 书源初始化
        bookSourceService.init(sourceId);

        LambdaQueryWrapper<ExploreSearchRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExploreSearchRule::getSourceId, sourceId);
        ExploreSearchRule searchRule = getOne(queryWrapper);
        Assert.notNull(searchRule, "未配置发现页搜索规则");
        return searchRule;
    }

}
