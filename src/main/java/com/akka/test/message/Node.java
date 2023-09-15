package com.akka.test.message;

import lombok.Data;

@Data
public class Node {
    Node next = null;//下一个结点
    LogInfo data;

    public Node(LogInfo data) {
        this.data = data;
    }
}
