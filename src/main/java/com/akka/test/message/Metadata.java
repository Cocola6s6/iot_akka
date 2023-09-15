package com.akka.test.message;


import com.akka.test.message.enums.MsgType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Map;


@Data
@Builder
@ToString
public class Metadata {
    private Long orgId;
    private Long productId;
    private Long deviceType;
    private Long deviceId; //终端
    private String deviceName;
    private String requestId;     //平台产生的请求ID
    private String transactionId; //事务Id
    private Long eventTime; //事件生产时间
    private MsgType msgType;
    private String topic;
    private volatile Map<String, Object> params;
}
