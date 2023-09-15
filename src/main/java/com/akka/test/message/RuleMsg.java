package com.akka.test.message;

import com.akka.test.callback.RuleCallBack;
import com.akka.test.message.enums.NodeExecState;
import lombok.Data;

@Data
public abstract class RuleMsg implements ActorMsg {

    private NodeExecState state;
    private String resultMsg;
    private RuleCallBack ruleCallBack;
    private Long ruleId;
    private Long nodeId;

    public RuleMsg(long nodeId, long ruleId, RuleCallBack ruleCallBack) {
        this.nodeId = nodeId;
        this.ruleId = ruleId;
        this.ruleCallBack = ruleCallBack;
    }

}
