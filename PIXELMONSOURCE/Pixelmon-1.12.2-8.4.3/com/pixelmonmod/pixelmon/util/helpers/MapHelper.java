package com.pixelmonmod.pixelmon.util.helpers;

import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MapHelper {
   public static void writeStringStringMapToByteBuf(ByteBuf buf, HashMap map) {
      buf.writeInt(map.size());
      Iterator var2 = map.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry entry = (Map.Entry)var2.next();
         ByteBufUtils.writeUTF8String(buf, (String)entry.getKey());
         ByteBufUtils.writeUTF8String(buf, (String)entry.getValue());
      }

   }

   public static HashMap readStringStringMapFromByteBuf(ByteBuf buf) {
      HashMap map = new HashMap();
      int size = buf.readInt();

      for(int i = 0; i < size; ++i) {
         map.put(ByteBufUtils.readUTF8String(buf), ByteBufUtils.readUTF8String(buf));
      }

      return map;
   }

   public static void writeStringLongMapToByteBuf(ByteBuf buf, HashMap map) {
      buf.writeInt(map.size());
      Iterator var2 = map.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry entry = (Map.Entry)var2.next();
         ByteBufUtils.writeUTF8String(buf, (String)entry.getKey());
         buf.writeLong((Long)entry.getValue());
      }

   }

   public static HashMap readStringLongMapFromByteBuf(ByteBuf buf) {
      HashMap map = new HashMap();
      int size = buf.readInt();

      for(int i = 0; i < size; ++i) {
         map.put(ByteBufUtils.readUTF8String(buf), buf.readLong());
      }

      return map;
   }
}
