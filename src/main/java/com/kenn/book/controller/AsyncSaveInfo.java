package com.kenn.book.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kenn.book.domain.InfoResult;
import com.kenn.book.domain.entity.BookSource;
import com.kenn.book.utils.JsoupUtils;
import com.kenn.book.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @ClassName AsyncSaveInfo
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年04月18日 14:51:00
 */
@Component
public class AsyncSaveInfo {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JsoupUtils jsoupUtils;

    @Async("threadPoolTaskExecutor")
    public void saveInfo(InfoResult infoResult, BookSource source) throws Exception {
        if (ObjectUtil.isNotNull(infoResult)) {
            String nextInfoLink = infoResult.getNextInfoLink();
            if (StrUtil.isNotEmpty(nextInfoLink)) {
                InfoResult nextInfoResult = jsoupUtils.getInfo(source.getId(), nextInfoLink);
                redisCache.setCacheObject("Info:" + source.getName() + ":" + nextInfoLink, nextInfoResult);
            }
        }
    }

}
