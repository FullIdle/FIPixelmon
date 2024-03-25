package com.pixelmonmod.pixelmon.api.storage;

import io.netty.buffer.ByteBuf;

public class StoragePosition {
   public int box;
   public int order;

   public StoragePosition(int box, int order) {
      this.box = box;
      this.order = order;
   }

   public StoragePosition set(int box, int order) {
      this.box = box;
      this.order = order;
      return this;
   }

   public boolean equals(Object other) {
      if (!(other instanceof StoragePosition)) {
         return false;
      } else {
         return ((StoragePosition)other).order == this.order && ((StoragePosition)other).box == this.box;
      }
   }

   public int hashCode() {
      return this.box << 16 ^ this.order;
   }

   public void encode(ByteBuf buffer) {
      buffer.writeShort(this.box);
      buffer.writeByte(this.order);
   }

   public static StoragePosition decode(ByteBuf buffer) {
      StoragePosition pos = new StoragePosition(0, 0);
      pos.box = buffer.readShort();
      pos.order = buffer.readUnsignedByte();
      return pos;
   }

   public String toString() {
      return this.getClass().getSimpleName() + "{box=" + this.box + ", order=" + this.order + '}';
   }
}
