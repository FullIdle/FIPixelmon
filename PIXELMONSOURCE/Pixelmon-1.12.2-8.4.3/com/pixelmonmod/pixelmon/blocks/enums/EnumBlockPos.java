package com.pixelmonmod.pixelmon.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumBlockPos implements IStringSerializable {
   TOP,
   BOTTOM;

   public String func_176610_l() {
      return this.toString();
   }

   public int toMeta() {
      return this.ordinal();
   }

   public static EnumBlockPos fromMeta(int meta) {
      return values()[meta];
   }

   public String toString() {
      return super.toString().toLowerCase();
   }
}
