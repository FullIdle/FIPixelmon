package com.pixelmonmod.pixelmon.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumMultiPos implements IStringSerializable {
   TOP,
   BOTTOM,
   BASE;

   public String func_176610_l() {
      return this.toString();
   }

   public int toMeta() {
      return this.ordinal();
   }

   public static EnumMultiPos fromMeta(int i) {
      return values()[i];
   }

   public String toString() {
      return super.toString().toLowerCase();
   }
}
