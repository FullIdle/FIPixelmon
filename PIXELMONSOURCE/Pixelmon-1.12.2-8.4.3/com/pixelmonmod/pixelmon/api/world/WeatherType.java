package com.pixelmonmod.pixelmon.api.world;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;

public enum WeatherType {
   CLEAR,
   RAIN,
   STORM;

   public static WeatherType get(World world) {
      if (world.func_72911_I()) {
         return STORM;
      } else {
         return world.func_72896_J() ? RAIN : CLEAR;
      }
   }

   public boolean isCurrent(World world) {
      if (!world.func_72911_I() || this != RAIN && this != STORM) {
         if (world.func_72896_J() && this == RAIN) {
            return true;
         } else {
            return !world.func_72896_J() && this == CLEAR;
         }
      } else {
         return true;
      }
   }

   public String getUnlocalizedName() {
      return this.name().charAt(0) + this.name().toLowerCase().substring(1);
   }

   public String getLocalizedName() {
      return I18n.func_135052_a("weather." + this.name().toLowerCase(), new Object[0]);
   }
}
