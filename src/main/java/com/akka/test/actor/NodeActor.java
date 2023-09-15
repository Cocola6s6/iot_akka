package com.akka.test.actor;

import com.akka.test.actor.process.IProcess;
import com.akka.test.actor.process.RuleNodeProcessor;
import com.akka.test.message.ActorMsg;
import com.akka.test.dao.RuleNodeDO;
import com.akka.test.message.ToRuleChainMsg;
import com.akka.test.service.ServiceContext;
import lombok.extern.slf4j.Slf4j;


/**
 * NodeActor
 */
@Slf4j
public class NodeActor extends ActorContext {

    private IProcess process;

    protected NodeActor(RuleNodeDO node, Long ruleId, ServiceContext serviceContext) {
        super(node.getNodeId(), "node_actor" + node.getNodeId(), serviceContext);
        process = new RuleNodeProcessor(ruleId, context().parent(), self(), node, serviceContext);
    }


    @Override
    protected void onReceive(ActorMsg message) {
        log.info("I am NodeActor, I receive message");
        process.toRuleNode((ToRuleChainMsg) message);
    }

    @Override
    protected void initNodeContext() {
        log.info("I am NodeActor, I am going to init node context");
        try {
            process.start(this.getContext());
        } catch (Exception e) {
            log.error("NodeRunActor.initSelfActorContext.error", e);
        }
    }
}
