package com.fipixelmonmod.fipixelmon.helper;

import java.util.Objects;

public class ObjectHelper {
    //直接用有些地方的警告看着烦人所以写了这个奇怪的东西
    public static boolean equals(Object a, Object b){
        return Objects.equals(a, b);
    }
}
