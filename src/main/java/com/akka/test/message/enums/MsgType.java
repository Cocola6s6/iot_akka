package com.akka.test.message.enums;

/**
 * 消息类型，包括属性、遥测、事件
 */
public enum MsgType {

    POST_PROPERTIES(1),//设备事件-属性请求消息
    POST_TELEMETRY(2),//设备事件-遥测数据
    POST_SERVICE(3),//设备事件-服务
    POST_EVENT(4),//设备事件-事件
    POST_AUTH(5),//认证请求消息
    ON_LINE(6),//上线请求消息消息
    OFF_LINE(7),//下线请求消息消息
    ADD_DEVICE(8),//新增设备消息
    DEL_DEVICE(9),//删除设备消息
    DEVICE_LOG(10),//设备日志消息

    ;


    private Integer value;

    private MsgType(Integer value) {
        this.value = value;
    }

}
