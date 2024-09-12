package com.kenn.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenn.book.domain.entity.ExploreSearchRule;

/**
 * @Description TODO
 * @ClassName ExploreSearchRuleService
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年03月14日 15:50:00
 */
public interface ExploreSearchRuleService extends IService<ExploreSearchRule> {

    ExploreSearchRule getBySourceId(Long sourceId) throws JsonProcessingException;

}

