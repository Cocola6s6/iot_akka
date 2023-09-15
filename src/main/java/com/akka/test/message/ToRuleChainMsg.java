package com.akka.test.message;

import com.akka.test.callback.CallBack;
import com.akka.test.callback.RuleCallBack;
import com.akka.test.message.enums.RuleEngineMsgType;
import lombok.Data;

import java.util.List;


/**
 * 规则引擎链路消息，包括了规则引擎节点消息、消息类型、日志节点。
 */
@Data
public class ToRuleChainMsg extends RuleMsg {
    private RuleEngineMsgType msgType = RuleEngineMsgType.TO_RULE_CHAIN_NODE;
    private ToRuleMsg toRuleMsg;
    private LogNode logNode;

    public ToRuleChainMsg(ToRuleMsg nodeMsg, long nodeId, long ruleId, RuleCallBack ruleCallBack, LogNode logNode) {
        super(nodeId, ruleId, ruleCallBack);
        this.toRuleMsg = nodeMsg;
        super.setRuleCallBack(ruleCallBack);
        this.logNode = logNode;
    }

    @Override
    public RuleEngineMsgType getMsgType() {
        return msgType;
    }

    @Override
    public CallBack getCallBack() {
        return null != toRuleMsg ? toRuleMsg.getCallBack() : null;
    }

    public void setMsgType(RuleEngineMsgType msgType) {
        this.msgType = msgType;
    }

    public void setKeyValue(List<KeyValue> params) {
        if (toRuleMsg != null) {
            toRuleMsg.setKeyValues(params);
        }
    }
}
