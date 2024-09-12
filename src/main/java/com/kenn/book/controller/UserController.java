package com.kenn.book.controller;

import com.kenn.book.domain.Constants;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.User;
import com.kenn.book.service.UserService;
import com.kenn.book.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

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
    public Result<?> save(@RequestBody User user) {
        RLock lock = RedisUtils.getLock(String.format(Constants.USER_LOCK_KEY, user.getOpenid()));
        boolean flag = false;
        try {
            // 尝试拿锁10s后停止重试,返回false
            // 具有Watch Dog 自动延期机制 默认续30s
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                flag = userService.saveUser(user);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return Result.error(exception.getMessage());
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return flag ? Result.success("保存成功") : Result.error("保存失败");
    }

    @GetMapping("/first")
    @ApiOperation("获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query"),
    })
    public Result<User> first(String openid) {
        return Result.success(userService.getByOpenid(openid));
    }

}
