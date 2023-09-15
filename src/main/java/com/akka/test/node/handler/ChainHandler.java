package com.akka.test.node.handler;


import com.akka.test.message.KeyValue;
import com.akka.test.message.Metadata;
import com.akka.test.node.handler.bean.ResultDesc;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * 节点处理器
 */
public interface ChainHandler {
    Pair<Boolean, ResultDesc> filter(Metadata metadata, Map<String, KeyValue> params, String logId);
}
