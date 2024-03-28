package com.kenn.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kenn.book.domain.entity.MyBookShelf;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @ClassName MyBookshelfMapper
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月21日 14:44:00
 */
@Mapper
public interface MyBookshelfMapper extends BaseMapper<MyBookShelf> {
}
