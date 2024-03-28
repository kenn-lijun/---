package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.entity.BookSearchRule;
import com.kenn.book.mapper.BookSearchRuleMapper;
import com.kenn.book.service.BookSearchRuleService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @ClassName BookSearchRuleServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:11:00
 */
@Service
public class BookSearchRuleServiceImpl extends ServiceImpl<BookSearchRuleMapper, BookSearchRule> implements BookSearchRuleService {
}
