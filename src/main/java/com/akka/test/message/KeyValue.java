package com.akka.test.message;


import com.akka.test.message.enums.ValueType;

/**
 * JSON格式的键值对
 * @param <V>
 */
public abstract class KeyValue<V> {
    public abstract ValueType getType();

    public abstract String getKey();

    public abstract V getValue();

    public abstract void setKey(String tmpKey);
}
