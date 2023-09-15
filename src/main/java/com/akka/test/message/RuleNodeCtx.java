package com.akka.test.message;

import akka.actor.ActorRef;
import com.akka.test.dao.RuleNodeDO;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public final class RuleNodeCtx {

    private ActorRef chainActor;
    private ActorRef selfActor;
    private RuleNodeDO selfNode;

    public RuleNodeCtx(ActorRef chainActor, ActorRef selfActor, RuleNodeDO selfNode) {
        this.chainActor = chainActor;
        this.selfActor = selfActor;
        this.selfNode = selfNode;
    }
}
