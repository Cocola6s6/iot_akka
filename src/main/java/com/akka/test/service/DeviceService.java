package com.akka.test.service;

import com.akka.test.message.MsgPushInfo;
import com.akka.test.node.bean.DeviceMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class DeviceService {

    public Pair<Boolean, List<DeviceMsg>> sendMsgToDevice(String logNodeRequestId, List<DeviceMsg> deviceMsgs, MsgPushInfo msgPushInfo) {
        log.info("sendMsgToDevice success");
        return null;
    }
}
