package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.RandomHelper;

public enum EnumNPCTutorType {
   TUTOR,
   TRANSFER;

   private static final EnumNPCTutorType[] VALUES = values();

   public static EnumNPCTutorType fromOrdinal(int ordinal) {
      return ordinal >= 0 && ordinal < VALUES.length ? VALUES[ordinal] : null;
   }

   public static EnumNPCTutorType random() {
      return (EnumNPCTutorType)RandomHelper.getRandomElementFromArray(VALUES);
   }
}
