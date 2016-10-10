package ar.com.olx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by jegutierrez on 08/10/16.
 */

@Configuration
public class TransactionPool {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${threadpool.corepoolsize}")
    int corePoolSize;

    @Value("${threadpool.maxpoolsize}")
    int maxPoolSize;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(corePoolSize);
        pool.setMaxPoolSize(maxPoolSize);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }
}
