package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.util.Table;

public class Strsafe {

    private static Table<String, String> x1a;

    public static void init() {
        if (x1a != null) return;
        x1a = new Table<>();
        x1a.init(1024);
    }

    public int insert(String data) {
        return x1a.insert(data, data);
    }

    public String find(String data) {
        return x1a.find(data);
    }
}
