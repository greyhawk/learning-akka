package com.akkademy.messages;

import java.io.Serializable;

/**
 * KeyNotFoundException
 * @author ging wu
 * @date 2018/12/5
 */
public class KeyNotFoundException extends Exception implements Serializable {

    private final String key;

    public KeyNotFoundException(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
