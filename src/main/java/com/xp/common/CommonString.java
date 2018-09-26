package com.xp.common;

import java.util.HashSet;
import java.util.Set;

public interface CommonString {
    public static final Set<String> SYSTEM = new HashSet<String>() {
        {
            add("java.lang.System");
            add("sun.management.MemoryImpl");
        }
    };

    public static final String NOCLASS = "doubidoubi";
}
