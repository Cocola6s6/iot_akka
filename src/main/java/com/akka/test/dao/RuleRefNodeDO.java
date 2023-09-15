package com.akka.test.dao;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RuleRefNodeDO {

    /**
     * 主键
     */
    private Long chainId;

    /**
     * 头结点
     */
    private Long nodeId;

    /**
     * 尾节点
     */
    private Long refNodeId;

    /**
     * 流向条件，如succeed,failed
     */
    private String exeTag;

}
