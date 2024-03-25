package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.comm.PixelmonPotion;
import net.minecraft.item.EnumDyeColor;

public class PixelmonPotions {
   public static PixelmonPotion repel;

   static {
      repel = new PixelmonPotion("repel", false, EnumDyeColor.RED);
   }
}
