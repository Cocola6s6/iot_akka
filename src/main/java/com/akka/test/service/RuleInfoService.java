package com.akka.test.service;


import com.akka.test.dao.RuleDO;
import com.akka.test.dao.RuleNodeDO;
import com.akka.test.message.RuleNodeRelation;
import com.akka.test.message.enums.NodeExecState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * 从DB获取规则引擎相关信息
 */
@Service
public class RuleInfoService {


    /**
     * 获取规则引擎对应的首节点
     *
     * @return
     */
    public Optional<List<RuleDO>> findAllRootRule() {
        // TODO 后续改成从数据库查询
        // 这里定义了三个链路节点
        List<RuleDO> ruleList = new ArrayList<>();
        RuleDO rule1 = new RuleDO();
        rule1.setRuleId(1L).setNodeId(1L);

        RuleDO rule2 = new RuleDO();
        rule2.setRuleId(2L).setNodeId(2L);

        RuleDO rule3 = new RuleDO();
        rule3.setRuleId(3L).setNodeId(3L);

        ruleList.add(rule1);
        ruleList.add(rule2);
        ruleList.add(rule3);

        return Optional.ofNullable(ruleList);
    }

    /**
     * 获取规则信息
     *
     * @param ruleId
     * @return
     */
    public Optional<Object> getRuleById(Long ruleId) {
        return Optional.empty();
    }


    /**
     * 获取自身节点信息及下级节点
     *
     * @param nodeId
     * @return
     */
    public Optional<List<RuleNodeDO>> getAllChildNodeAndSelfByNodeId(Long nodeId) {
        // TODO 后续改成从数据库查询 1）查询节点表，获取自身节点 2）查询关联表，获取下级节点

        List<RuleNodeDO> nodeList = new ArrayList<>();

        if (1L == nodeId) {
            RuleNodeDO node1 = new RuleNodeDO();
            node1.setNodeId(1L).setNodeName("action").setContext("{\"_comment\":\"action1_self\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            RuleNodeDO node2 = new RuleNodeDO();
            node2.setNodeId(11L).setNodeName("action").setContext("{\"_comment\":\"action11_success\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            RuleNodeDO node3 = new RuleNodeDO();
            node3.setNodeId(12L).setNodeName("action").setContext("{\"_comment\":\"action12_failure\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            RuleNodeDO node4 = new RuleNodeDO();
            node4.setNodeId(111L).setNodeName("action").setContext("{\"_comment\":\"action111_success\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            RuleNodeDO node5 = new RuleNodeDO();
            node5.setNodeId(112L).setNodeName("action").setContext("{\"_comment\":\"action112_failure\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            nodeList.add(node1);
            nodeList.add(node2);
            nodeList.add(node3);
            nodeList.add(node4);
            nodeList.add(node5);
        } else if (2L == nodeId) {
            RuleNodeDO node1 = new RuleNodeDO();
            node1.setNodeId(2L).setNodeName("action").setContext("{\"_comment\":\"action2_self\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            RuleNodeDO node2 = new RuleNodeDO();
            node2.setNodeId(21L).setNodeName("action").setContext("{\"_comment\":\"action21_success\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            RuleNodeDO node3 = new RuleNodeDO();
            node3.setNodeId(22L).setNodeName("action").setContext("{\"_comment\":\"action22_failure\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            nodeList.add(node1);
            nodeList.add(node2);
            nodeList.add(node3);
        } else if (3L == nodeId) {
            RuleNodeDO node1 = new RuleNodeDO();
            node1.setNodeId(3L).setNodeName("action").setContext("{\"_comment\":\"action3_self\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            RuleNodeDO node2 = new RuleNodeDO();
            node2.setNodeId(31L).setNodeName("action").setContext("{\"_comment\":\"action31_success\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            RuleNodeDO node3 = new RuleNodeDO();
            node3.setNodeId(32L).setNodeName("action").setContext("{\"_comment\":\"action32_failure\",\"deviceMsg\":[{\"key\":\"1\",\"value\":\"A\"},{\"key\":\"2\",\"value\":\"A\"},{\"key\":\"3\",\"value\":\"A\"}]}");

            nodeList.add(node1);
            nodeList.add(node2);
            nodeList.add(node3);
        } else {
            return Optional.empty();
        }

        return Optional.ofNullable(nodeList);
    }


    /**
     * 获取与自身关联的节点
     *
     * @param nodeId
     * @return
     */
    public Optional<List<RuleNodeRelation>> findNodeRelationByNodeId(Long nodeId) {
        // TODO 后续改成从数据库查询 1）查询关联表获取关联节点
        // 1-->11-->111
        // 1-->11-->112
        // 1-->12
        // 2-->21
        // 2-->22
        // 2-->31
        // 3-->32
        List<RuleNodeRelation> relationList = new ArrayList<>();
        if (1L == nodeId) {
            RuleNodeRelation relation1 = new RuleNodeRelation();
            relation1.setNodeId(11L).setState(NodeExecState.SUCCESS);

            RuleNodeRelation relation2 = new RuleNodeRelation();
            relation2.setNodeId(12L).setState(NodeExecState.FAILURE);

            relationList.add(relation1);
            relationList.add(relation2);
        } else if (2L == nodeId) {
            RuleNodeRelation relation1 = new RuleNodeRelation();
            relation1.setNodeId(21L).setState(NodeExecState.SUCCESS);

            RuleNodeRelation relation2 = new RuleNodeRelation();
            relation2.setNodeId(22L).setState(NodeExecState.FAILURE);

            relationList.add(relation1);
            relationList.add(relation2);
        } else if (3L == nodeId) {
            RuleNodeRelation relation1 = new RuleNodeRelation();
            relation1.setNodeId(31L).setState(NodeExecState.SUCCESS);

            RuleNodeRelation relation2 = new RuleNodeRelation();
            relation2.setNodeId(32L).setState(NodeExecState.FAILURE);

            relationList.add(relation1);
            relationList.add(relation2);
        } else if (11L == nodeId){
            RuleNodeRelation relation1 = new RuleNodeRelation();
            relation1.setNodeId(111L).setState(NodeExecState.SUCCESS);

            RuleNodeRelation relation2 = new RuleNodeRelation();
            relation2.setNodeId(112L).setState(NodeExecState.FAILURE);

            relationList.add(relation1);
            relationList.add(relation2);
        } else {
            return Optional.empty();
        }

        return Optional.ofNullable(relationList);
    }

}
