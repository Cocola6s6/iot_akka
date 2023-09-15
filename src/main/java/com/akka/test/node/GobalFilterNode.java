package com.akka.test.node;

import com.akka.test.callback.RuleNodeCallBack;
import com.akka.test.exception.RuleEngineException;
import com.akka.test.message.LogNode;
import com.akka.test.dao.RuleNodeDO;
import com.akka.test.message.ToRuleMsg;
import com.akka.test.node.process.AbsNodeProcessor;
import com.akka.test.service.ServiceContext;
import lombok.extern.slf4j.Slf4j;

/**
 * GobalFilterNode节点
 */
@Slf4j
public class GobalFilterNode extends AbsNodeProcessor {
    @Override
    protected String getNodeName() {
        return "GobalFilterNode";
    }

    @Override
    protected void initNode(RuleNodeDO node) throws RuleEngineException {
        log.info("initNode, node name is : {}", getNodeName());
    }

    @Override
    protected void processNodeMessage(ToRuleMsg msg, LogNode logNode, ServiceContext serviceContext, RuleNodeCallBack callback) {
        log.info("GobalFilterNode processNodeMessage");
    }
}
