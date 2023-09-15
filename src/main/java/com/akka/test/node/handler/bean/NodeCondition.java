package com.akka.test.node.handler.bean;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodeCondition {

    private ObjectNode jsonNode;
}
