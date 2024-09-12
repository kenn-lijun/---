package com.kenn.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenn.book.domain.entity.BookSearchRule;

/**
 * @Description TODO
 * @ClassName BookSearchRuleService
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:10:00
 */
public interface BookSearchRuleService extends IService<BookSearchRule> {

    BookSearchRule getBySourceId(Long sourceId) throws JsonProcessingException;

}
