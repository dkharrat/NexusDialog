package com.github.dkharrat.nexusdialog.util;

public class ObjectUtil {

    public static boolean objectsEqual(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }
}
