package com.kenn.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenn.book.domain.entity.ChapterSearchRule;

/**
 * @Description TODO
 * @ClassName ChapterSearchRuleService
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:11:00
 */
public interface ChapterSearchRuleService extends IService<ChapterSearchRule> {

    ChapterSearchRule getBySourceId(Long sourceId) throws JsonProcessingException;

}
