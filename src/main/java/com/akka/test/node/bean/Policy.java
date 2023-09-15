package com.akka.test.node.bean;

public class Policy {

    private int qos; // qos级别
    private int expiryTime; // 消息有效时间
    private int delayedTime; // 消息发送延时时间

    public Policy() {

    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public int getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(int expiryTime) {
        this.expiryTime = expiryTime;
    }

    public int getDelayedTime() {
        return delayedTime;
    }

    public void setDelayedTime(int delayedTime) {
        this.delayedTime = delayedTime;
    }
}