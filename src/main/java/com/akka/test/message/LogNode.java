package com.akka.test.message;

import lombok.Data;

import java.util.UUID;

@Data
public class LogNode {
    private volatile boolean saveDB = false;
    /**
     * 链表的头结点
     */
    Node head = null;//下一个结点
    private String requestId;
    private Long deviceId;
    private Integer triggerType;  //1 设备消息  2定时器

    public LogNode(Integer triggerType, Long deviceId) {
        this.triggerType = triggerType;
        this.requestId = UUID.randomUUID().toString();
        this.deviceId = deviceId;
    }

    public void addLog(LogInfo data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }


    public static void main(String[] args) {
        LogNode logNode = new LogNode(1, 1l);
    }

    public LogInfo getData() {
        Node rHead = head;
        head = head.next;
        return rHead.data;
    }

    public LogInfo getLastData() {
        if (head == null) {
            return null;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        return temp.data;
    }

    public LogInfo getFirstData() {
        if (head == null) {
            return null;
        }
        return head.data;
    }
}
