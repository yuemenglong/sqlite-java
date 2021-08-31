package io.github.yuemenglong.sqlite.common;

public interface INext<T> {
    T getNext();

    void setNext(T v);
}
