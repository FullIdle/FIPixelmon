package com.pixelmonmod.pixelmon.util.helpers;

import com.pixelmonmod.pixelmon.util.IEncodeable;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ArrayHelper {
   public static boolean contains(Object[] array, Object value) {
      if (array == null) {
         return false;
      } else {
         for(int i = 0; i < array.length; ++i) {
            if (array[i] == value || array[i] != null && value != null && array[i].equals(value)) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean arrayHasNull(Object[] array) {
      if (array != null && array.length != 0) {
         Object[] var1 = array;
         int var2 = array.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Object object = var1[var3];
            if (object == null) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public static void validateArrayNonNull(Object[] array) {
      if (arrayHasNull(array)) {
         throw new IllegalArgumentException("Array cannot have null elements.");
      }
   }

   public static void encodeArray(ByteBuf buffer, IEncodeable[] array) {
      if (array == null) {
         buffer.writeInt(0);
      } else {
         buffer.writeInt(array.length);
         IEncodeable[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            IEncodeable encodeable = var2[var4];
            encodeable.encodeInto(buffer);
         }

      }
   }

   public static void encodeList(ByteBuf buffer, List list) {
      if (list == null) {
         buffer.writeInt(0);
      } else {
         buffer.writeInt(list.size());
         Iterator var2 = list.iterator();

         while(var2.hasNext()) {
            IEncodeable encodeable = (IEncodeable)var2.next();
            encodeable.encodeInto(buffer);
         }

      }
   }

   public static void encodeStringList(ByteBuf buffer, List list) {
      if (list == null) {
         buffer.writeInt(0);
      } else {
         encodeStringArray(buffer, (String[])list.toArray(new String[list.size()]));
      }
   }

   public static void encodeStringArray(ByteBuf buffer, String[] array) {
      if (array == null) {
         buffer.writeInt(0);
      } else {
         buffer.writeInt(array.length);
         String[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String string = var2[var4];
            if (string == null) {
               string = "";
            }

            ByteBufUtils.writeUTF8String(buffer, string);
         }

      }
   }

   public static List decodeStringList(ByteBuf buffer) {
      List list = new ArrayList();
      int listLength = buffer.readInt();

      for(int i = 0; i < listLength; ++i) {
         list.add(ByteBufUtils.readUTF8String(buffer));
      }

      return list;
   }

   public static String[] decodeStringArray(ByteBuf buffer) {
      String[] array = new String[buffer.readInt()];

      for(int i = 0; i < array.length; ++i) {
         array[i] = ByteBufUtils.readUTF8String(buffer);
      }

      return array;
   }

   public static boolean[][] deepCopy(boolean[][] array) {
      boolean[][] copy = new boolean[array.length][];

      int i;
      for(i = 0; i < array.length; ++i) {
         copy[i] = new boolean[array[i].length];
      }

      for(i = 0; i < array.length; ++i) {
         System.arraycopy(array[i], 0, copy[i], 0, array[i].length);
      }

      return copy;
   }

   public static Object[][] deepCopy(Object[][] array) {
      Object[][] copy = (Object[][])(new Object[array.length][]);

      int i;
      for(i = 0; i < array.length; ++i) {
         copy[i] = (Object[])(new Object[array[i].length]);
      }

      for(i = 0; i < array.length; ++i) {
         System.arraycopy(array[i], 0, copy[i], 0, array[i].length);
      }

      return copy;
   }

   public static Object[][][] deepCopy(Object[][][] array) {
      Object[][][] copy = (Object[][][])(new Object[array.length][][]);

      for(int i = 0; i < array.length; ++i) {
         copy[i] = deepCopy(array[i]);
      }

      return copy;
   }
}
