package com.akka.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class LogService {
    private static final String RULE_LOG_LOCK = "RULE_LOG";
    private ThreadPoolExecutor ruleLogExecutor;

    /**
     * 记录日志到DB中
     */
    public void saveDB() {
        if (ruleLogExecutor == null) {
            initRuleLog();
        }
        ruleLogExecutor.execute(() -> {
            try {
                log.info("记录日志到DB中");
            } catch (RejectedExecutionException e) {
                log.warn("rule log pool is busy", e);

            } catch (Exception e) {
                log.warn("rule log pool Exception ", e);
            }

        });
    }


    /**
     * 初始化规则日志线程池
     */
    private void initRuleLog() {
        synchronized (RULE_LOG_LOCK) {
            if (ruleLogExecutor == null) {
                ruleLogExecutor = new ThreadPoolExecutor(
                        50,
                        50,
                        10l,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(500),
                        (r) -> new Thread(r, "rule_log"));
            }
        }

    }
}
