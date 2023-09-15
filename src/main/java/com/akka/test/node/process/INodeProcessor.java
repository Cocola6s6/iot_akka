package com.akka.test.node.process;

import com.akka.test.callback.RuleNodeCallBack;
import com.akka.test.message.LogNode;
import com.akka.test.service.ServiceContext;
import com.akka.test.dao.RuleNodeDO;
import com.akka.test.message.ToRuleMsg;

/**
 * 节点处理器，包括了初始化、处理消息。
 */
public interface INodeProcessor {

    void init(Long ruleId, RuleNodeDO node, ServiceContext serviceContext) throws Exception;

    void process(ToRuleMsg msg, LogNode logNode, ServiceContext serviceContext, RuleNodeCallBack callback);
}
