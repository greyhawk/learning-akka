package com.akkademy.messages;

import java.io.Serializable;

/**
 * GetRequest
 * @author ging wu
 * @date 2018/12/5
 */
public class GetRequest implements Serializable {

    private final String key;

    public GetRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
