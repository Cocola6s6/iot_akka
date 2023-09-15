package com.akka.test.dao;


import lombok.Data;;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RuleNodeDO {

    /**
     * 主键
     */
    private Long nodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点内容
     */
    private String context;

}
