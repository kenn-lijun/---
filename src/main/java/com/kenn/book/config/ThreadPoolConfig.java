package com.kenn.book.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author ruoyi
 **/
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "threadPoolExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小 线程池会一直维护这么多的线程 即使有些线程处于空闲状态
        executor.setCorePoolSize(4);
        // 队列最大长度 当核心线程都在忙时 新任务会放在这个队列中等待
        executor.setQueueCapacity(100);
        // 最大可创建的线程数 当工作队列满了之后 线程池会创建新线程 直到达到这个最大值。
        executor.setMaxPoolSize(8);
        // 线程池维护线程所允许的空闲时间 当线程池中的线程数量超过核心线程数时 这些多余的线程会在空闲了keepAliveSeconds秒后被终止
        executor.setKeepAliveSeconds(300);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
