package com.akka.test.actor;

import com.akka.test.actor.process.IProcess;
import com.akka.test.actor.process.RuleChainProcessor;
import com.akka.test.message.ActorMsg;
import com.akka.test.message.ToRuleChainMsg;
import com.akka.test.service.ServiceContext;
import lombok.extern.slf4j.Slf4j;


/**
 * RuleChainActor
 */
@Slf4j
public class RuleChainActor extends ActorContext {

    private IProcess process;

    protected RuleChainActor(Long ruleId, Long rootNodeId, ServiceContext serviceContext) {
        super(rootNodeId, "rootRule_Id_" + ruleId, serviceContext);
        process = new RuleChainProcessor(ruleId, rootNodeId, serviceContext, context().self());
    }


    @Override
    protected void onReceive(ActorMsg message) {
        log.info("I am RuleChainActor, I receive message");
        switch (message.getMsgType()) {
            case TO_RULE_CHAIN_NODE:
                process.toRuleChain((ToRuleChainMsg) message);
                break;
            case TO_RULE_NODE:
                process.toRuleNode((ToRuleChainMsg) message);
                break;
            default:
                break;

        }
    }

    @Override
    protected void initNodeContext() {
        log.info("I am RuleChainActor, I am going to init node context");
        try {
            process.start(this.getContext());
        } catch (Exception e) {
            log.error("ChainNodeContextActor.initNodeContext.error", e);
        }

    }

}
