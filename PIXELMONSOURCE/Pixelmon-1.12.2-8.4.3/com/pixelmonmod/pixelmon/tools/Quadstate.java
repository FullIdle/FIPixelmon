package com.pixelmonmod.pixelmon.tools;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public enum Quadstate {
   NONE,
   NO,
   YES,
   BOTH;

   public boolean isYes() {
      return this == YES || this == BOTH;
   }

   public boolean isNo() {
      return this == NO || this == BOTH;
   }

   public void writeToNBT(NBTTagCompound tag, String key) {
      tag.func_74774_a(key, (byte)this.ordinal());
   }

   public static Quadstate readFromNBT(NBTTagCompound tag, String key) {
      return values()[tag.func_74764_b(key) ? tag.func_74771_c(key) : 0];
   }

   public void writeToBuffer(ByteBuf buf) {
      buf.writeByte(this.ordinal());
   }

   public static Quadstate readFromBuffer(ByteBuf buf) {
      return values()[buf.readByte()];
   }

   public static Quadstate valueOf(boolean value) {
      return value ? YES : NO;
   }
}
