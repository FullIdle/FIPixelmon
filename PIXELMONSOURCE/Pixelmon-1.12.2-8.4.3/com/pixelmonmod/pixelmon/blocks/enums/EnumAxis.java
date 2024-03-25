package com.pixelmonmod.pixelmon.blocks.enums;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

public enum EnumAxis implements IStringSerializable {
   X,
   Y,
   Z,
   NONE;

   public String func_176610_l() {
      return this.toString().toLowerCase();
   }

   public int toMeta() {
      return this.ordinal();
   }

   public static EnumAxis fromMeta(int i) {
      return values()[i];
   }

   public static EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
      // $FF: Couldn't be decompiled
   }
}
