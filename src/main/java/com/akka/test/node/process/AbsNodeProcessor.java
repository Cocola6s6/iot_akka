package com.akka.test.node.process;

import com.akka.test.dao.RuleNodeDO;
import com.akka.test.message.*;
import lombok.extern.slf4j.Slf4j;

import com.akka.test.message.enums.AttributeType;
import com.akka.test.message.enums.MsgType;
import com.akka.test.message.enums.NodeExecState;
import com.akka.test.callback.RuleNodeCallBack;
import com.akka.test.exception.RuleEngineException;
import com.akka.test.message.enums.OperationSymbol;
import com.akka.test.service.ServiceContext;
import com.akka.test.utils.JsonConfigurationTools;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public abstract class AbsNodeProcessor implements INodeProcessor {

    protected ServiceContext serviceContext;
    protected Long ruleId;
    protected Long nodeId;
    protected String nodeName;

    @Override
    public void init(Long ruleId, RuleNodeDO node, ServiceContext serviceContext) throws RuleEngineException {
        this.serviceContext = serviceContext;
        this.ruleId = ruleId;
        this.nodeId = node.getNodeId();
        this.nodeName = node.getNodeName();
        initNode(node);
    }


    @Override
    public void process(ToRuleMsg msg, LogNode logNode, ServiceContext serviceContext, RuleNodeCallBack callback) {
        try {
            processNodeMessage(msg, logNode, serviceContext, callback);
        } catch (Exception e) {
            log.error("AbsNodeProcessor.process.Exception", e);
            callback.onFailure(new RuleEngineException("执行失败,nodeId=" + nodeId));
        }

    }



    protected Map<String, KeyValue> convertMap(List<KeyValue> keyValues) {
        return (null != keyValues && keyValues.size() > 0) ? keyValues.stream().collect(Collectors.toMap(KeyValue::getKey, v -> v)) : null;

    }


    protected AttributeType toAttributeType(String v) throws RuleEngineException {
        AttributeType[] types = AttributeType.values();
        for (AttributeType type : types) {
            if (type.toString().equalsIgnoreCase(v)) {
                return type;
            }
        }
        throw new RuleEngineException("Only 'product' or 'device' ");
    }

    protected <T> T convertNodeBean(String context, Class<T> classz) throws RuleEngineException {
        try {
            if (isEmptyContext(context)) {
                log.warn("节点的数据为空,nodeId={}", nodeId);
                return null;
            }
            T t = JsonConfigurationTools.readValue(context, classz);
            if (t == null) {
                throw new RuleEngineException("Data conversion failed,e");
            }
            return t;
        } catch (IOException e) {
            log.error("初始化规则节点失败,nodeId={}", nodeId, e);
            throw new RuleEngineException("Failed to initialize rule node,nodeId=" + nodeId);
        }

    }


    protected OperationSymbol toOptSymbol(String optSymbol) throws RuleEngineException {
        OperationSymbol[] types = OperationSymbol.values();
        for (OperationSymbol type : types) {
            if (type.toString().equalsIgnoreCase(optSymbol)) {
                return type;
            }
        }
        throw new RuleEngineException("optSymbol must in( GT, GTorEQ, LT,LTorQE,EQ,noEQ) ");
    }

    protected MsgType toMsgType(String msgType) throws RuleEngineException {
        MsgType[] types = MsgType.values();
        for (MsgType type : types) {
            if (type.toString().equalsIgnoreCase(msgType)) {
                return type;
            }
        }
        throw new RuleEngineException("msgType must in( post_properties, post_telemetry, post_service,post_event,off_line,on_line) ");

    }

    protected void addLog(LogNode logNode, long useTime, NodeExecState state, String cause) {
        logNode.addLog(LogInfo.builder().eventTime(System.currentTimeMillis()).useTime(useTime).nodeId(this.nodeId).ruleId(this.ruleId)
                .nodeName(nodeName).state(state).cause(cause).build());
    }

    private boolean isEmptyContext(String context) {
        if (StringUtils.isEmpty(context)) {
            return Boolean.TRUE;
        }
        if (StringUtils.equalsIgnoreCase("null", context)) {
            return Boolean.TRUE;
        }
        String afterText = context.replace("\"", "");
        if (StringUtils.equalsIgnoreCase("{}", afterText)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }




    protected abstract String getNodeName();

    protected abstract void initNode(RuleNodeDO node) throws RuleEngineException;


    // 处理完成需要回调，用来标记可以将消息发送到下一个节点了
    protected abstract void processNodeMessage(ToRuleMsg msg, LogNode logNode, ServiceContext serviceContext, RuleNodeCallBack callback);

}
