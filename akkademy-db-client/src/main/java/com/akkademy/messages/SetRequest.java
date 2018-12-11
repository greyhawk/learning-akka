package com.akkademy.messages;

import java.io.Serializable;

/**
 * SetRequest 键值消息
 * @author ging wu
 * @date 2018/12/3
 */
public class SetRequest implements Serializable {

    private final String key;
    private final Object value;

    public SetRequest(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}