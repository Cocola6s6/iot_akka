package com.akka.test.message.enums;

/**
 * KV支持的value类型，包括boolean、long、double、string、json、byte
 */
public enum ValueType {

    BOOLEAN(0),
    LONG(1),
    DOUBLE(2),
    STRING(3),
    JSON(4),
    BYTE(5),


    ;

    private Integer type;

    private ValueType(Integer type) {
        this.type = type;
    }
}
