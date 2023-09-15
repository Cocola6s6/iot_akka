package com.akka.test.message;

import com.akka.test.message.enums.MsgPushState;
import com.akka.test.message.enums.NodeExecState;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class LogInfo {
    private Long eventTime;
    private Long ruleId;
    private Long nodeId;
    private Long useTime;
    private String nodeName;
    private NodeExecState state;
    private String cause;
    private MsgPushState pushState;
}
