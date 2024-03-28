package com.kenn.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kenn.book.domain.entity.BookSearchRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @ClassName BookSearchRuleMapper
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:09:00
 */
@Mapper
public interface BookSearchRuleMapper extends BaseMapper<BookSearchRule> {
}
