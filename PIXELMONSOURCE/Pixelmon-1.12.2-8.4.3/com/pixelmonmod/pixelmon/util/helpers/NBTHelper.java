package com.pixelmonmod.pixelmon.util.helpers;

import java.util.function.BiFunction;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper {
   public static Object getOrDefault(NBTTagCompound nbt, String key, Object defaultValue, BiFunction function) {
      return nbt.func_74764_b(key) ? function.apply(nbt, key) : defaultValue;
   }
}
