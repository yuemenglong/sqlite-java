package io.github.yuemenglong.sqlite.lemon;

public class Set {
    private static int size = 0;

    public static void setSize(int n) {
        size = n + 1;
    }

    public static byte[] setNew() {
        byte[] s;
        int i;
        s = new byte[size];
        for (i = 0; i < size; i++) {
            s[i] = 0;
        }
        return s;
    }

    public static void setFree(byte[] s) {
        s = null;
    }

    public static int setAdd(byte[] s, int e) {
        int rv;
        rv = s[e];
        s[e] = 1;
        return 1 - rv;
    }

    public static int setUnion(byte[] s1, byte[] s2) {
        int i, progress;
        progress = 0;
        for (i = 0; i < size; i++) {
            if (s2[i] == 0) continue;
            if (s1[i] == 0) {
                progress = 1;
                s1[i] = 1;
            }
        }
        return progress;
    }
}
