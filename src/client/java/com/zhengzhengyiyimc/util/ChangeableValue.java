package com.zhengzhengyiyimc.util;

import java.util.ArrayList;
import java.util.List;

public class ChangeableValue<T> {
    private T _v;
    private List<Lisener<T>> liseners = new ArrayList<>();
    public static interface Lisener<T> {
        public void callback(T a, T b);
    }
    public ChangeableValue(T v) {
        this._v = v;
    }
    public T get() {
        return this._v;
    }
    public void set(T v) {
        liseners.forEach(lisener -> {
            lisener.callback(v, this._v);
        });
        this._v = v;
    }
}
