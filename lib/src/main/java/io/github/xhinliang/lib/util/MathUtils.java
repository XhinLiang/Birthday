package io.github.xhinliang.lib.util;

/**
 * Created by xhinliang on 16-2-23.
 * Math
 */
public class MathUtils {
    public static int compare(int a, int b) {
        if (a == b)
            return 0;
        if (a < b)
            return -1;
        return 1;
    }
}
