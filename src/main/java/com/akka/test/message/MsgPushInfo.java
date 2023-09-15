package com.akka.test.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MsgPushInfo {
    private Long ruleId;
    private Long nodeId;
    private String nodeName;
}
