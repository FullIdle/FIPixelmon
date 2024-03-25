package com.pixelmonmod.pixelmon;

import io.netty.buffer.ByteBuf;
import java.util.UUID;

public class PixelmonMethods {
   public static void toBytesUUID(ByteBuf buf, UUID uuid) {
      buf.writeLong(uuid.getMostSignificantBits());
      buf.writeLong(uuid.getLeastSignificantBits());
   }

   public static UUID fromBytesUUID(ByteBuf buf) {
      return new UUID(buf.readLong(), buf.readLong());
   }
}
