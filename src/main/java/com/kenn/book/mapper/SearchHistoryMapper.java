package com.kenn.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kenn.book.domain.entity.SearchHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @ClassName SearchHistoryMapper
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月26日 16:37:00
 */
@Mapper
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {
}
