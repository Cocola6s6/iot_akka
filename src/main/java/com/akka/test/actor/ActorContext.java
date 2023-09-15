package com.akka.test.actor;

import akka.actor.UntypedAbstractActor;
import com.akka.test.callback.CallBack;
import com.akka.test.callback.RuleCallBack;
import com.akka.test.exception.RuleEngineException;
import com.akka.test.message.ActorMsg;
import com.akka.test.service.ServiceContext;
import lombok.extern.slf4j.Slf4j;


/**
 * Actor的上下文，包括了父Actor的ID、服务上下文、Actor的名称。
 */
@Slf4j
public abstract class ActorContext extends UntypedAbstractActor {
    protected final Long parentId;
    protected final String actorName;
    protected final ServiceContext serviceContext;

    protected ActorContext(Long parentId, String actorName, ServiceContext serviceContext) {
        this.parentId = parentId;
        this.actorName = actorName;
        this.serviceContext = serviceContext;
    }

//========================== Actor的上下文的方法，包括了Actor的初始化、接收消息、Actor的启动。 ==========================

    // 创建 Actor 的时候触发
    @Override
    public void preStart() throws Exception {
        initNodeContext();
    }


    // 接收到消息的时候触发
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ActorMsg) {
            try {
                onReceive((ActorMsg) message);
            } catch (Exception e) {
                ActorMsg msg = (ActorMsg) message;
                CallBack callBack = msg.getCallBack();
                if (null != callBack) {
                    if (callBack instanceof RuleCallBack) {
                        ((RuleCallBack) callBack).onFailure(new RuleEngineException(e.getMessage()));
                    }
                } else {
                    throw e;
                }

            }
        }
    }


    protected abstract void onReceive(ActorMsg message);

    protected abstract void initNodeContext();

}
