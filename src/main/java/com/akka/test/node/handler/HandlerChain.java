package com.akka.test.node.handler;

import com.akka.test.node.handler.bean.RequestInfo;
import org.apache.commons.lang3.tuple.Pair;
import com.akka.test.message.KeyValue;
import com.akka.test.message.LogNode;
import com.akka.test.message.Metadata;
import com.akka.test.message.enums.RelationSymbol;
import com.akka.test.node.handler.bean.ResultDesc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 节点处理器链
 */
public class HandlerChain {

    private Map<String, List<ChainHandler>> map = new ConcurrentHashMap<>();


//=============================== 节点处理器链方法，包括了过滤、添加到处理器链最后。=============================================

    public Pair<Boolean, ResultDesc> filter(String groupName,
                                            Map<String, KeyValue> params,
                                            Metadata metadata,
                                            RelationSymbol symbol,
                                            LogNode logNode) throws Exception {
        List<ChainHandler> mapFilters = map.get(groupName);
        if (mapFilters == null || mapFilters.size() < 1) {
            return Pair.of(true, ResultDesc.builder().requestInfo(RequestInfo.builder().productId(metadata.getProductId()).deviceId(metadata.getDeviceId())
                    .msgType(metadata.getMsgType()).params(params).build()).build());
        }

        Pair<Boolean, ResultDesc> isPass = null;
        String requestId = null == logNode ? null : logNode.getRequestId();
        for (ChainHandler handler : mapFilters) {
            isPass = handler.filter(metadata, params, requestId);
            if ((isPass.getLeft() && symbol == RelationSymbol.OR) || (symbol == RelationSymbol.AND && !isPass.getLeft())) {
                break;
            }
        }

        return isPass;
    }

    public void addLast(String groupName, ChainHandler handler) {
        List<ChainHandler> mapFilters = map.get(groupName);
        if (mapFilters == null) {
            mapFilters = new ArrayList<>(2);
            map.put(groupName, mapFilters);
        }
        mapFilters.add(handler);
    }
}
