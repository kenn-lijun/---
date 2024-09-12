package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenn.book.domain.entity.InfoSearchRule;
import com.kenn.book.mapper.InfoSearchRuleMapper;
import com.kenn.book.service.BookSourceService;
import com.kenn.book.service.InfoSearchRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @Description TODO
 * @ClassName InfoSearchRuleServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:13:00
 */
@Service
@RequiredArgsConstructor
public class InfoSearchRuleServiceImpl extends ServiceImpl<InfoSearchRuleMapper, InfoSearchRule> implements InfoSearchRuleService {

    private final BookSourceService bookSourceService;

    @Override
    public InfoSearchRule getBySourceId(Long sourceId) throws JsonProcessingException {
        // 书源初始化
        bookSourceService.init(sourceId);

        // 获取阅读页搜索规则配置
        LambdaQueryWrapper<InfoSearchRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InfoSearchRule::getSourceId, sourceId);
        InfoSearchRule searchRule = getOne(queryWrapper);
        Assert.notNull(searchRule, "未获取到阅读页搜索规则");
        return searchRule;
    }

}
