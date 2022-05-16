package com.test.global.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WebState {
    private Map<String, Object> state;

    public WebState() {
        state = Collections.synchronizedMap(new HashMap<>());
    }

    public void put(String k, Object v) {
        state.put(k, v);
    }

    public Object get(String k) {
        return state.get(k);
    }

    public Object get(String k, Object _default) {
        Object v = get(k);
        return (v == null) ? _default : v;
    }

    protected void clear() {
        state.clear();
    }

    public String toString() {
        StringBuilder mapAsString = new StringBuilder("{");
        for (String key : this.state.keySet()) {
            mapAsString.append(key + " = " + this.state.get(key) + ", ");
        }
        mapAsString.delete(mapAsString.length() - 2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }
}
