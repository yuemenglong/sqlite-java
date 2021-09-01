package io.github.yuemenglong.sqlite.util;

import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public class Addr<T> {

    private final Object obj;
    private final Field field;

    public Addr(Object obj, String field) {
        try {
            this.obj = obj;
            this.field = obj.getClass().getDeclaredField(field);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void set(T value) {
        try {
            field.set(obj, value);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public T get() {
        try {
            return (T) field.get(obj);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
