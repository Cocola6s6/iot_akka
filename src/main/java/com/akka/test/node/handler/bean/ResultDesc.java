package com.akka.test.node.handler.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultDesc {
    private RequestInfo requestInfo;
    private NodeCondition nodeCondition;
    private String cause;
    private HandlerType handlerType;
}
