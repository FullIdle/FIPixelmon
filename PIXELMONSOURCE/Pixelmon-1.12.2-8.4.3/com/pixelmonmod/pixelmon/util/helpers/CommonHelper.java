package com.pixelmonmod.pixelmon.util.helpers;

import com.google.common.primitives.Primitives;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.registry.RegistryNamespaced;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.filter.RegexFilter;

public class CommonHelper {
   private static Field NBTMapField;
   private static Field NBTListField;
   private static Field ItemRegistryMapField;
   public static final int[] bitFilters;

   public static void ensureIndex(ArrayList a, int i) {
      while(a.size() <= i) {
         a.add((Object)null);
      }

   }

   public static Object set(ArrayList a, Object value, int index) {
      if (a.size() <= index) {
         a.add(index, value);
         return null;
      } else {
         return a.set(index, value);
      }
   }

   public static void insert(ArrayList a, Object value) {
      int nullIndex = a.indexOf((Object)null);
      if (nullIndex == -1) {
         a.add(value);
      } else {
         a.set(nullIndex, value);
      }

   }

   public static Map getMap(NBTTagCompound nbt) {
      try {
         return (Map)NBTMapField.get(nbt);
      } catch (Exception var2) {
         System.err.println(nbt);
         var2.printStackTrace();
         return null;
      }
   }

   public static ArrayList getList(NBTTagList nbt) {
      try {
         return (ArrayList)NBTListField.get(nbt);
      } catch (Exception var2) {
         System.err.println(nbt);
         System.err.println(NBTListField);
         var2.printStackTrace();
         return null;
      }
   }

   public static int indexOfAbsoluteMax(Object array) throws ClassCastException {
      int index = -1;
      int maxes = 0;
      Double max = null;

      int i;
      Number n;
      for(i = 0; i < Array.getLength(array); ++i) {
         n = (Number)Array.get(array, i);
         if (max == null || n.doubleValue() > max) {
            max = n.doubleValue();
         }
      }

      for(i = 0; i < Array.getLength(array); ++i) {
         n = (Number)Array.get(array, i);
         if (n.doubleValue() == max) {
            ++maxes;
            index = i;
         }

         if (maxes > 1) {
            return -1;
         }
      }

      return index;
   }

   public static Object[] wrapperArray(Object aprimitive) {
      if (!aprimitive.getClass().isArray()) {
         throw new IllegalArgumentException("The variable 'primitiveArray' must ACTUALLY BE AN ARRAY!");
      } else {
         Class cl = aprimitive.getClass().getComponentType();
         if (!cl.isPrimitive()) {
            return (Object[])((Object[])aprimitive);
         } else {
            cl = Primitives.wrap(cl);
            Object awrapper = Array.newInstance(cl, Array.getLength(aprimitive));

            for(int i = 0; i < Array.getLength(aprimitive); ++i) {
               Array.set(awrapper, i, Array.get(aprimitive, i));
            }

            return (Object[])((Object[])awrapper);
         }
      }
   }

   public static Map addEntries(Map map, Object[] keys, Object[] vals) {
      for(int i = 0; i < keys.length; ++i) {
         map.put(keys[i], vals[i]);
      }

      return map;
   }

   public static Map addEntries(Map map, Object[] kvs) {
      for(int i = 0; i < kvs.length / 2; ++i) {
         map.put(kvs[i * 2], kvs[i * 2 + 1]);
      }

      return map;
   }

   public static Object getIgnoreCase(Map map, String key) {
      if (key == null) {
         return map.get((Object)null);
      } else {
         Iterator var2 = map.entrySet().iterator();

         Map.Entry entry;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            entry = (Map.Entry)var2.next();
         } while(!key.equalsIgnoreCase((String)entry.getKey()));

         return entry.getValue();
      }
   }

   public static String textInQuotes(String str) {
      return RegexPatterns.REMOVEQUOTES.matcher(str).replaceAll("$1").trim();
   }

   public static byte[] decodeInteger(int encoded, int size) {
      if (size >= 1 && size <= 8) {
         byte[] decoded = new byte[32 / size];

         for(int i = 0; i < 32 / size; ++i) {
            decoded[i] = (byte)(encoded & bitFilters[size - 1]);
            encoded >>= size;
         }

         return decoded;
      } else {
         throw new IllegalArgumentException("Invalid value for size; must be between 1 and 8 inclusive");
      }
   }

   public static int encodeInteger(byte[] toEncode, int size) {
      if (size >= 1 && size <= 8) {
         int encoded = 0;

         for(int i = 0; i < 32 / size && i < toEncode.length; ++i) {
            encoded += (toEncode[i] & bitFilters[size - 1]) << size * i;
         }

         return encoded;
      } else {
         throw new IllegalArgumentException("Invalid value for size; must be between 1 and 8 inclusive");
      }
   }

   public static boolean shutUpLogger(Class clazz, String regex) {
      try {
         Field[] var2 = clazz.getDeclaredFields();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(Logger.class)) {
               field.setAccessible(true);
               Logger logger = (Logger)field.get((Object)null);
               logger.addFilter(RegexFilter.createFilter(regex, (String[])null, false, Result.DENY, Result.NEUTRAL));
               return true;
            }
         }
      } catch (Throwable var7) {
      }

      return false;
   }

   static {
      Field[] Fields = NBTTagCompound.class.getDeclaredFields();
      Field[] var1 = Fields;
      int var2 = Fields.length;

      int var3;
      Field f;
      for(var3 = 0; var3 < var2; ++var3) {
         f = var1[var3];
         if (Map.class.isAssignableFrom(f.getType())) {
            NBTMapField = f;
            NBTMapField.setAccessible(true);
            break;
         }
      }

      Fields = NBTTagList.class.getDeclaredFields();
      var1 = Fields;
      var2 = Fields.length;

      for(var3 = 0; var3 < var2; ++var3) {
         f = var1[var3];
         if (List.class.isAssignableFrom(f.getType())) {
            NBTListField = f;
            NBTListField.setAccessible(true);
            break;
         }
      }

      Fields = RegistryNamespaced.class.getDeclaredFields();
      var1 = Fields;
      var2 = Fields.length;

      for(var3 = 0; var3 < var2; ++var3) {
         f = var1[var3];
         if (Map.class.isAssignableFrom(f.getType())) {
            ItemRegistryMapField = f;
            ItemRegistryMapField.setAccessible(true);
            break;
         }
      }

      bitFilters = new int[]{1, 3, 7, 15, 31, 63, 127, 255};
   }
}
