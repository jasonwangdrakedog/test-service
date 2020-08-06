package com.teg.analysis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author wangyuan
 * @date 2020/8/6 17:39
 */
@Configuration
public class ThreadPoolConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(ThreadPoolConfig.class);


    @Bean
    public ExecutorService getThreadPool() {
        LOGGER.info("ExecutorService getThreadPool()...");
        return new TraceThreadPoolExecutor(50, 50, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>());
    }

}
