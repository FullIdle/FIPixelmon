package com.pixelmonmod.pixelmon.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumUsed implements IStringSerializable {
   YES,
   NO;

   public String func_176610_l() {
      return this.toString();
   }

   public int getMeta() {
      return this.ordinal();
   }

   public static EnumUsed fromMeta(int i) {
      return values()[i];
   }

   public String toString() {
      return super.toString().toLowerCase();
   }
}
