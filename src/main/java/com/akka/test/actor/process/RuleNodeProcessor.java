package com.akka.test.actor.process;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.akka.test.message.enums.NodeExecState;
import com.akka.test.message.enums.RuleEngineMsgType;
import com.akka.test.node.NodeFactory;
import lombok.extern.slf4j.Slf4j;
import com.akka.test.callback.RuleNodeCallBack;
import com.akka.test.message.KeyValue;
import com.akka.test.dao.RuleNodeDO;
import com.akka.test.message.ToRuleChainMsg;
import com.akka.test.exception.RuleEngineException;
import com.akka.test.node.process.INodeProcessor;
import com.akka.test.service.ServiceContext;

import java.util.List;


/**
 * 规则引擎节点处理器
 */
@Slf4j
public class RuleNodeProcessor implements IProcess {

    private final ActorRef chainActor;
    private final ActorRef selfActor;
    private final RuleNodeDO node;
    private final ServiceContext serviceContext;
    protected INodeProcessor nodeProcessor;
    private final Long ruleId;

    public RuleNodeProcessor(Long ruleId, ActorRef chainActor, ActorRef selfActor, RuleNodeDO node, ServiceContext serviceContext) {
        this.ruleId = ruleId;
        this.chainActor = chainActor;
        this.selfActor = selfActor;
        this.node = node;
        this.serviceContext = serviceContext;
    }

    @Override
    public void start(ActorContext context) throws Exception {
        nodeProcessor = NodeFactory.getProcessor(node.getNodeName());
        if (nodeProcessor == null) {
            log.error("初始化规则引擎节点失败, 节点定义的名称不能识别, nodeName={}", node.getNodeName());
            return;
        }
        nodeProcessor.init(ruleId, node, serviceContext);
    }

    @Override
    public void toRuleChain(ToRuleChainMsg message) {
        chainActor.tell(message, selfActor);
    }

    @Override
    public void toRuleNode(ToRuleChainMsg message) {
        try {
            nodeProcessor.process(message.getToRuleMsg(), message.getLogNode(), serviceContext, new RuleNodeCallBack() {
                @Override
                public void onSuccess(List<KeyValue> params) {
                    message.setMsgType(RuleEngineMsgType.TO_RULE_NODE);
                    message.setState(NodeExecState.SUCCESS);
                    message.setKeyValue(params);

                    log.info("NodeProcessor.onSuccess, message to rulechain to find next node");
                    toRuleChain(message);
                }

                @Override
                public void onFailure(RuleEngineException e) {
                    message.setMsgType(RuleEngineMsgType.TO_RULE_NODE);
                    message.setState(NodeExecState.FAILURE);
                    message.setResultMsg(e.getMessage());

                    log.info("NodeProcessor.onFailure, message to rulechain to find next node");
                    toRuleChain(message);
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

//========================== 规则引擎节点处理器的方法，包括了获取处理器类。 ==========================

//    private Class getClass(String nodeName) {
//        if ("triger".equals(nodeName)) {
//            return TriggerAndAsNode.class;
//        } else if ("filter".equals(nodeName)) {
//            return GobalFilterNode.class;
//        } else if ("action".equals(nodeName)) {
//            return ActionNode.class;
//        } else {
//            return null;
//        }
//    }
}
