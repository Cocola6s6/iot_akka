package com.akka.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 服务的上下文，包括了日志服务、规则信息服务等。
 */
@Component
@RequiredArgsConstructor
public class ServiceContext {
    private static final String nodeWithDispatcher = "node-dispatcher";
    private final LogService logService;
    private final RuleInfoService ruleInfoService;
    private final DeviceService deviceService;


    public String getNodeWithDispatcher() {
        return nodeWithDispatcher;
    }

    public LogService getLogService() {
        return logService;
    }

    public RuleInfoService getRuleInfoService() {
        return ruleInfoService;
    }

    public DeviceService getDeviceService() {
        return deviceService;
    }
}
