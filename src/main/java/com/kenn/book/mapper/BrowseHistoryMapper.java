package com.kenn.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kenn.book.domain.entity.BrowseHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @ClassName BrowseHistoryMapper
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月28日 09:52:00
 */
@Mapper
public interface BrowseHistoryMapper extends BaseMapper<BrowseHistory> {
}
