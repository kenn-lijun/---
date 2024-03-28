package com.kenn.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kenn.book.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @ClassName UserMapper
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月29日 15:11:00
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
