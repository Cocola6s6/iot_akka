package com.akka.test.message;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * 规则信息，包括了规则ID、节点ID。
 */
@Data
@Builder
@ToString
public class RuleInfo {
    private long ruleId;
    private long nodeId;

    public RuleInfo(long ruleId, long nodeId) {
        this.ruleId = ruleId;
        this.nodeId = nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleInfo ruleInfo = (RuleInfo) o;
        return Objects.equals(ruleId, ruleInfo.ruleId) &&
                Objects.equals(nodeId, ruleInfo.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, nodeId);
    }
}
