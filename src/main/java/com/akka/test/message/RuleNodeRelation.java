package com.akka.test.message;

import com.akka.test.message.enums.NodeExecState;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class RuleNodeRelation {
    private Long nodeId;
    private NodeExecState state;
}
