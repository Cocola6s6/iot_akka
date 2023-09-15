package com.akka.test.message.enums;

/**
 * 规则引擎消息类型，分为规则引擎管理器、规则引擎、规则链节点、规则节点、规则引擎任务。
 */
public enum RuleEngineMsgType {

    RULE_ENGINE_MANAGER,
    RULE_ENGINE,
    TO_RULE_CHAIN_NODE,
    TO_RULE_NODE,
    UPDATE_RULE_NODE,
    RULE_ENGINE_TASK,

    ;
}
