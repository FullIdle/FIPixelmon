package com.pixelmonmod.pixelmon.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumDawnDuskOre implements IStringSerializable {
   dawn,
   dusk,
   none;

   public static EnumDawnDuskOre getValue(int index) {
      return index < values().length && index >= 0 ? values()[index] : none;
   }

   public String func_176610_l() {
      return this.toString();
   }
}
