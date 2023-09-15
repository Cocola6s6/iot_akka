package com.akka.test.dao;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RuleDO {

    /**
     * 主键
     */
    private Long ruleId;

    /**
     * 节点名称
     */
    private Long nodeId;

    /**
     * 状态
     */
    private Integer status;

}
