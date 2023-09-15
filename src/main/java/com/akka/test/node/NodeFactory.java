package com.akka.test.node;

import com.akka.test.node.process.INodeProcessor;
import lombok.extern.slf4j.Slf4j;


/**
 * Ndoe简单工厂
 */
// TODO 后续考虑是否使用工厂方法模式，每个Node一个工厂
@Slf4j
public class NodeFactory {

    public static INodeProcessor getProcessor(String nodeName) throws InstantiationException, IllegalAccessException {
        Class nodeClass = null;
        if ("triger".equals(nodeName)) {
            nodeClass = TriggerAndAsNode.class;
        } else if ("filter".equals(nodeName)) {
            nodeClass = GobalFilterNode.class;
        } else if ("action".equals(nodeName)) {
            nodeClass = ActionNode.class;
        } else {
            return null;
        }

        return (INodeProcessor) nodeClass.newInstance();
    }
}
