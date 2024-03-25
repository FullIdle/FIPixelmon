package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.RandomHelper;
import java.util.Arrays;

public enum EnumPokerusType {
   UNINFECTED(-1),
   A(86400),
   B(172800),
   C(259200),
   D(345600);

   public final int duration;
   private static EnumPokerusType[] infectedValues = (EnumPokerusType[])Arrays.copyOfRange(values(), 1, values().length);

   private EnumPokerusType(int durationInSeconds) {
      this.duration = durationInSeconds;
   }

   public static EnumPokerusType getRandomType() {
      return (EnumPokerusType)RandomHelper.getRandomElementFromArray(infectedValues);
   }
}
