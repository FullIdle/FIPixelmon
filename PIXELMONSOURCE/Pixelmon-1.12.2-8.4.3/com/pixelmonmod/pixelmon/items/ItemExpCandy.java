package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;

public class ItemExpCandy extends PixelmonItem {
   private ExperienceGainType gainType;
   private int amount;

   public ItemExpCandy(String name, ExperienceGainType gainType, int amount) {
      super(name);
      this.func_77637_a(PixelmonCreativeTabs.restoration);
      this.amount = amount;
      this.gainType = gainType;
   }

   public ExperienceGainType getGainType() {
      return this.gainType;
   }

   public int getAmount() {
      return this.amount;
   }
}
