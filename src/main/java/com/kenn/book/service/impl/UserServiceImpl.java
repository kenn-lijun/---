package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.entity.User;
import com.kenn.book.mapper.UserMapper;
import com.kenn.book.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @ClassName UserServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月29日 15:16:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
