package com.kenn.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenn.book.domain.entity.InfoSearchRule;

/**
 * @Description TODO
 * @ClassName InfoSearchRuleService
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:12:00
 */
public interface InfoSearchRuleService extends IService<InfoSearchRule> {

    InfoSearchRule getBySourceId(Long sourceId) throws JsonProcessingException;

}
