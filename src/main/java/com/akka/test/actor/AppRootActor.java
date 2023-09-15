package com.akka.test.actor;

import akka.actor.*;
import com.akka.test.callback.RuleCallBack;
import com.akka.test.dao.RuleDO;
import com.akka.test.exception.RuleEngineException;
import com.akka.test.message.*;
import com.akka.test.service.LogService;
import com.akka.test.service.RuleInfoService;
import com.akka.test.service.ServiceContext;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * AppRootActor
 */
@Slf4j
public class AppRootActor extends ActorContext {

    private final BiMap<RuleInfo, ActorRef> rootRuleActor;
    private LogService logService;
    private RuleInfoService ruleInfoService;

    public AppRootActor(Long parentId, String actorName, ServiceContext serviceContext) {
        super(parentId, actorName, serviceContext);
        this.rootRuleActor = HashBiMap.create();
        this.logService = serviceContext.getLogService();
        this.ruleInfoService = serviceContext.getRuleInfoService();
    }

    @Override
    public void onReceive(ActorMsg message) {
        log.info("I am AppRootActor, I receive message");
        switch (message.getMsgType()) {
            case RULE_ENGINE:
                ToRuleMsg nodeMsg = (ToRuleMsg) message;
                rootRuleActor.forEach((nodeInfo, actorRef) -> {
                    ToRuleMsg msg = nodeMsg;
                    sendMessageToNode(1, actorRef, msg, nodeInfo);
                });
                break;
            default:
                break;

        }
    }

//========================== AppRootActor的方法，包括了发送消息到节点、重新加载规则。 ==========================

    private void sendMessageToNode(int triggerType, ActorRef actorRef, ToRuleMsg nodeMsg, RuleInfo nodeInfo) {
        Metadata metadata = nodeMsg.getMetadata();
        LogNode logNode = new LogNode(triggerType, metadata == null ? null : metadata.getDeviceId());
        ToRuleChainMsg toRuleChainMsg = new ToRuleChainMsg(nodeMsg, nodeInfo.getNodeId(), nodeInfo.getRuleId(), new RuleCallBack() {
            @Override
            public void onSuccess() {
                logService.saveDB();
            }

            @Override
            public void onFailure(RuleEngineException e) {
                logService.saveDB();
            }
        }, logNode);

        actorRef.tell(toRuleChainMsg, self());
    }


    protected void initNodeContext() {
        log.info("I am AppRootActor, I am going to init node context");
//        数据库查询node
        Optional<List<RuleDO>> nodes = ruleInfoService.findAllRootRule();
//        根据node创建actor
        nodes.ifPresent(rules -> rules.forEach(ruleInfo -> reload(ruleInfo.getRuleId(), ruleInfo.getNodeId())));
    }


    private boolean reload(long ruleId, long nodeId) {
        rootRuleActor.computeIfAbsent(new RuleInfo(ruleId, nodeId), k -> {
            ActorRef actor = context().actorOf(Props.create(RuleChainActor.class, ruleId, nodeId, this.serviceContext));
            this.context().watch(actor);
            return actor;
        });
        return false;
    }
}
