package io.github.yuemenglong.sqlite.util;

public class Assert {
    public static void assertTrue(boolean v) {
        if (!v) {
            throw new RuntimeException("Assert Fail");
        }
    }
}
