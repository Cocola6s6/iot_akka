package com.akka.test.node.handler.bean;

import lombok.Builder;
import lombok.Data;
import com.akka.test.message.KeyValue;
import com.akka.test.message.enums.MsgType;

import java.util.Map;

@Data
@Builder
public class RequestInfo {
    private Map<String, KeyValue> params;
    private MsgType msgType;
    private Long productId;
    private Long deviceId;

}
