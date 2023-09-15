package com.akka.test.node.handler;


import com.akka.test.message.KeyValue;
import com.akka.test.message.Metadata;
import com.akka.test.message.MsgPushInfo;
import com.akka.test.node.bean.DeviceMsg;
import com.akka.test.node.handler.bean.HandlerType;
import com.akka.test.node.handler.bean.ResultDesc;
import com.akka.test.service.DeviceService;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;


/**
 * 节点处理器
 */
public class SendMsgToDeviceHandler implements ChainHandler {
    private List<DeviceMsg> deviceMsgs;
    private DeviceService deviceService;
    private Long ruleId;
    private MsgPushInfo msgPushInfo;
    private HandlerType handlerType;


    public SendMsgToDeviceHandler(HandlerType handlerType, List<DeviceMsg> deviceMsgs, DeviceService deviceService, MsgPushInfo msgPushInfo) {
        this.handlerType = handlerType;
        this.deviceMsgs = deviceMsgs;
        this.deviceService = deviceService;
        this.msgPushInfo = msgPushInfo;
    }

    @Override
    public Pair<Boolean, ResultDesc> filter(Metadata metadata, Map<String, KeyValue> params, String logNodeRequestId) {
        deviceService.sendMsgToDevice(logNodeRequestId, deviceMsgs, msgPushInfo);
        return Pair.of(true, null);
    }

}
