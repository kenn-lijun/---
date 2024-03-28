package com.kenn.book.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.User;
import com.kenn.book.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Description TODO
 * @ClassName UserController
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月29日 15:18:00
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(tags = "用户")
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    @ApiOperation("保存历史")
    @Transactional
    public Result save(@RequestBody User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", user.getOpenid());
        User isExit = userService.getOne(wrapper);
        if (ObjectUtil.isNotNull(isExit)) {
            isExit.setAvatar(user.getAvatar());
            isExit.setNickName(user.getNickName());
            isExit.setPhone(user.getPhone());
            isExit.setCreateTime(new Date());
            userService.updateById(isExit);
            return Result.success("更新成功");
        }

        Date operateTime = new Date();
        user.setCreateTime(operateTime);
        user.setUpdateTime(operateTime);
        boolean result = userService.save(user);
        return result ? Result.success("保存成功") : Result.error("保存失败");
    }

    @GetMapping("/first")
    @ApiOperation("根据openid获取用户是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query"),
    })
    public Result<User> first(String openid) {
        User result = userService.getOne(new QueryWrapper<User>().eq("openid", openid));
        return Result.success(result);
    }

}
