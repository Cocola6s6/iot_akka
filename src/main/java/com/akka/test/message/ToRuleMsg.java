package com.akka.test.message;

import com.akka.test.callback.SimpleServiceCallback;
import com.akka.test.message.enums.RuleEngineMsgType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;


/**
 * 规则引擎节点消息
 */
@Data
@Builder
@ToString
public class ToRuleMsg implements ActorMsg {
    private List<KeyValue> keyValues;
    private Metadata metadata;
    private SimpleServiceCallback<Void> callback;
    private TriggerInfo triggerInfo;
    private boolean hasCalled = false;
    private String ruleXId;

    @Override
    public RuleEngineMsgType getMsgType() {
        return RuleEngineMsgType.RULE_ENGINE;
    }

    @Override
    public SimpleServiceCallback<Void> getCallBack() {
        return callback;
    }
}
