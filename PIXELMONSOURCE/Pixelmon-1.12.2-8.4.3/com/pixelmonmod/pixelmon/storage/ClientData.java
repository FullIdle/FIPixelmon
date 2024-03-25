package com.pixelmonmod.pixelmon.storage;

import com.pixelmonmod.pixelmon.enums.EnumTrainerCardColor;

public class ClientData {
   public static int playerMoney;
   public static int openMegaItemGui = -1;
   public static EnumTrainerCardColor color;

   static {
      color = EnumTrainerCardColor.WHITE;
   }
}
