package com.akka.test.actor.process;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.akka.test.exception.RuleEngineException;
import com.akka.test.service.ServiceContext;
import com.akka.test.actor.NodeActor;
import com.akka.test.dao.RuleNodeDO;
import com.akka.test.message.RuleNodeCtx;
import com.akka.test.message.RuleNodeRelation;
import com.akka.test.message.ToRuleChainMsg;
import com.akka.test.message.enums.NodeExecState;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * 规则引擎链路处理器
 */
@Slf4j
public class RuleChainProcessor implements IProcess {

    private boolean started;
    private final Long ruleId;
    private final Long nodeId;
    private final ActorRef self;
    private ServiceContext serviceContext;
    private Map<Long /**规则节点ID  */, RuleNodeCtx> nodeActors;    // 获取该条规则下面所有的规则节点
    private Map<Long /**规则节点ID  */, Map<NodeExecState, RuleNodeRelation>> ruleNodeRelation; // 所有规则节点的关联关系

    public RuleChainProcessor(Long ruleId, Long nodeId, ServiceContext serviceContext, ActorRef self) {
        this.ruleId = ruleId;
        this.nodeId = nodeId;
        this.self = self;
        this.serviceContext = serviceContext;
        this.nodeActors = new HashMap<>();
        this.ruleNodeRelation = new HashMap<>();
    }

    @Override
    public void start(ActorContext context) {
        if (!started) {
            // 获取该条规则下面所有的规则节点
            Optional<List<RuleNodeDO>> list = serviceContext.getRuleInfoService().getAllChildNodeAndSelfByNodeId(nodeId);
            list.ifPresent(nodes -> nodes.forEach(node -> loadActor(context, node)));
            log.info("I am RuleChainActor, nodeId={}, list={}", nodeId, list);
            started = true;
        }
    }


    @Override
    public void toRuleChain(ToRuleChainMsg message) {
        Long nodeId = message.getNodeId();
        if (null == nodeId) {
            message.getRuleCallBack().onSuccess();
        } else {
            RuleNodeCtx targetCtx = nodeActors.get(nodeId);
            if (targetCtx != null) {
                targetCtx.getSelfActor().tell(message, self);
            } else {
                message.getRuleCallBack().onFailure(new RuleEngineException("Node is empty,nodeId=" + nodeId));
            }
        }
    }


    @Override
    public void toRuleNode(ToRuleChainMsg message) {
        Long nodeId = message.getNodeId();
        NodeExecState state = message.getState();
        Map<NodeExecState, RuleNodeRelation> relationMap = ruleNodeRelation.get(nodeId);
        if (!hasNextNode(relationMap, state)) {
            log.info("message to next node, has not next node");
            if (null == state || state == NodeExecState.SUCCESS) {
                message.getRuleCallBack().onSuccess();
            } else {
                message.getRuleCallBack().onFailure(new RuleEngineException(message.getResultMsg()));
            }
        } else {
            RuleNodeRelation relation = relationMap.get(state);
            RuleNodeCtx targetCtx = nodeActors.get(relation.getNodeId());

            log.info("message to next node, nodeId={}, state={}, relation={}", relation.getNodeId(), relation.getState(), relation);

            if (targetCtx != null) {
                message.setNodeId(relation.getNodeId());
                targetCtx.getSelfActor().tell(message, self);
            } else {
                message.getRuleCallBack().onFailure(new RuleEngineException("Node is empty,nodeId=" + nodeId));
            }
        }
    }


//========================== 规则引擎链路处理器的方法，包括了加载Actor、获取有关联的规则引擎节点、判断是否还有下一个节点。 ==========================

    private void loadActor(ActorContext context, RuleNodeDO node) {
        nodeActors.putIfAbsent(node.getNodeId(), new RuleNodeCtx(self, context.actorOf(Props.create(NodeActor.class, node, ruleId, this.serviceContext).withDispatcher(serviceContext.getNodeWithDispatcher())), node));
        ruleNodeRelation.putIfAbsent(node.getNodeId(), getRuleNodeRelation(node.getNodeId()));
    }

    private Map<NodeExecState, RuleNodeRelation> getRuleNodeRelation(Long nodeId) {
        Map<NodeExecState, RuleNodeRelation> refMap = new HashMap<>();

        // 获取与自身关联的节点
        // TODO 为什么使用状态来当key
        // TODO 因为是使用状态来表示节点之间关系
        Optional<List<RuleNodeRelation>> relationList = serviceContext.getRuleInfoService().findNodeRelationByNodeId(nodeId);
        relationList.ifPresent(nodeRelationNodes -> nodeRelationNodes.forEach(e -> refMap.computeIfAbsent(e.getState(), g -> e)));

        // 为空释放内存,直接返回空
        return refMap.isEmpty() ? null : refMap;
    }

    private boolean hasNextNode(Map<NodeExecState, RuleNodeRelation> relationMap, NodeExecState state) {
        if (null == relationMap || relationMap.size() < 1 || null == relationMap.get(state)) {
            return false;
        }
        return true;
    }

}
