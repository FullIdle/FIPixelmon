package com.fipixelmonmod.fipixelmon.helper;

import akka.util.Unsafe;
import lombok.val;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 不知道对不对，先写了
 */
public class EnumHelper {
    public static sun.misc.Unsafe unsafe = Unsafe.instance;

    /**
     * 需要注意缓存的清理
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> void removeEnumSafely(Class<T> enumClass, T enumToRemove) {
        try {
            val offset = unsafe.staticFieldOffset(enumClass.getDeclaredField("$VALUES"));

            val oldValues = (T[]) unsafe.getObject(enumClass,offset);
            val newValues = new ArrayList<>(Arrays.asList(oldValues));
            if (!newValues.remove(enumToRemove)) return;

            T[] newArray = (T[]) Array.newInstance(enumClass, newValues.size());
            newArray = newValues.toArray(newArray);
            unsafe.putObject(enumClass, offset, newArray);

            System.gc();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to modify enum", ex);
        }
    }
}
