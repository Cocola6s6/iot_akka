package com.akka.test.node.bean;

import java.util.List;

public class ActionRootBean {

    private String _comment;
    private List<DeviceMsg> deviceMsg;

    public ActionRootBean() {
    }

    public String get_comment() {
        return _comment;
    }

    public void set_comment(String _comment) {
        this._comment = _comment;
    }

    public List<DeviceMsg> getDeviceMsg() {
        return deviceMsg;
    }

    public void setDeviceMsg(List<DeviceMsg> deviceMsg) {
        this.deviceMsg = deviceMsg;
    }
}