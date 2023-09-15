package com.akka.test.actor.process;

import akka.actor.ActorContext;
import com.akka.test.message.ToRuleChainMsg;


/**
 * 规则引擎处理器，包括了启动、发送消息到规则链、发送消息到规则节点。
 */
public interface IProcess {

    void start(ActorContext context) throws Exception;

    void toRuleChain(ToRuleChainMsg message);

    //    TODO 消息发送到规则节点是用的数据结构也是ToRuleChainMsg，后续看是否需要修改名称，或者是否需要新建一个数据结构
    void toRuleNode(ToRuleChainMsg message);
}
