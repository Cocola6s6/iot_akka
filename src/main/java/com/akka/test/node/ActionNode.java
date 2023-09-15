package com.akka.test.node;

import com.akka.test.dao.RuleNodeDO;
import com.akka.test.message.*;
import com.akka.test.message.enums.RelationSymbol;
import com.akka.test.node.bean.DeviceMsg;
import com.akka.test.node.handler.HandlerChain;
import com.akka.test.node.handler.SendMsgToDeviceHandler;
import com.akka.test.node.handler.bean.HandlerType;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import com.akka.test.callback.RuleNodeCallBack;
import com.akka.test.exception.RuleEngineException;

import com.akka.test.node.bean.ActionRootBean;
import com.akka.test.node.process.AbsNodeProcessor;
import com.akka.test.service.ServiceContext;

import java.util.List;
import java.util.Map;


/**
 * ActionNdoe节点
 */
@Slf4j
public class ActionNode extends AbsNodeProcessor {

    private HandlerChain handlerChain;

    @Override
    protected String getNodeName() {
        return "ActionNode";
    }

    @Override
    public void initNode(RuleNodeDO node) throws RuleEngineException {
        log.info("initNode, node name is : {}", getNodeName());
        convertToHandlerChain(convertNodeBean(node.getContext(), ActionRootBean.class));
    }


    @Override
    public void processNodeMessage(ToRuleMsg msg, LogNode logNode, ServiceContext serviceContext, RuleNodeCallBack callback) {
        log.info("ActionNode processNodeMessage");
        process(serviceContext, msg, logNode);

        // 处理完成需要回调，用来标记可以将消息发送到下一个节点了
        callback.onSuccess(null);
    }


//============================ ActionNode节点的方法，包括了转换处理器链、处理消息。============================

    private void convertToHandlerChain(ActionRootBean rootBean) {
        if (null == rootBean) {
            return;
        }

        List<DeviceMsg> deviceMsgs = rootBean.getDeviceMsg();
        if (deviceMsgs != null && deviceMsgs.size() > 0) {
            if (handlerChain == null) {
                handlerChain = new HandlerChain();
            }

            SendMsgToDeviceHandler handler = new SendMsgToDeviceHandler(HandlerType.ACTION_SEND_DEVICEMSG, deviceMsgs, serviceContext.getDeviceService(), MsgPushInfo.builder().ruleId(ruleId).nodeId(nodeId).nodeName(nodeName).build());

            handlerChain.addLast(HandlerType.ACTION_SEND_DEVICEMSG.toString(), handler);
        }
    }


    private ListenableFuture<List<KeyValue>> process(ServiceContext serviceContext, ToRuleMsg msg, LogNode logNode) {
        try {
            if (handlerChain == null) {
                return null;
            }

            List<KeyValue> keyValues = msg.getKeyValues();
            Map<String, KeyValue> paramsMap = convertMap(keyValues);
            handlerChain.filter(HandlerType.ACTION_SEND_DEVICEMSG.toString(), paramsMap, msg.getMetadata(), RelationSymbol.OR, logNode);
            log.info("ActionNode.process succeed, ruleId={}, nodeId={}", ruleId, nodeId);

            return (ListenableFuture<List<KeyValue>>) keyValues;
        } catch (Exception e) {
            log.error("ActionNode.process failed, ruleId={}, nodeId={}", ruleId, nodeId, e);
        }

        return null;
    }
}
