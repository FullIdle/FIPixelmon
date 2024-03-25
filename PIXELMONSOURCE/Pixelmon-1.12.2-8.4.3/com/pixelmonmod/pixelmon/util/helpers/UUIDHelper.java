package com.pixelmonmod.pixelmon.util.helpers;

import io.netty.buffer.ByteBuf;
import java.util.UUID;

public class UUIDHelper {
   public static final UUID ZEROED_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

   public static void writeUUID(UUID uuid, ByteBuf buf) {
      buf.writeLong(uuid.getMostSignificantBits());
      buf.writeLong(uuid.getLeastSignificantBits());
   }

   public static UUID readUUID(ByteBuf buf) {
      return new UUID(buf.readLong(), buf.readLong());
   }

   public static boolean isUUID(String string) {
      String[] components = string.split("-");
      return components.length == 5;
   }

   public static Object questUUID(String s) {
      if (!s.equalsIgnoreCase("-") && !s.equalsIgnoreCase("any")) {
         return isUUID(s) ? UUID.fromString(s) : "?" + s + "?";
      } else {
         return ZEROED_UUID;
      }
   }
}
