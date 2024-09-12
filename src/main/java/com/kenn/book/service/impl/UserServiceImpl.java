package com.kenn.book.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.entity.User;
import com.kenn.book.mapper.UserMapper;
import com.kenn.book.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description TODO
 * @ClassName UserServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月29日 15:16:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByOpenid(String openid) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenid, openid);
        return getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(User user) {
        User isExit = getByOpenid(user.getOpenid());
        Date operateTime = new Date();
        if (ObjectUtil.isNotNull(isExit)) {
            isExit.setAvatar(user.getAvatar());
            isExit.setNickName(user.getNickName());
            isExit.setPhone(user.getPhone());
            isExit.setUpdateTime(operateTime);
            return updateById(isExit);
        }
        user.setCreateTime(operateTime);
        user.setUpdateTime(operateTime);
        return save(user);
    }

}
