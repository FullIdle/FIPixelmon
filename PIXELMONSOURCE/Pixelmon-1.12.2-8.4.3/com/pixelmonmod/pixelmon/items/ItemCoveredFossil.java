package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumFossils;
import java.util.ArrayList;

public class ItemCoveredFossil extends PixelmonItem {
   public ItemFossil cleanedFossil;
   public static ArrayList fossilList = new ArrayList();
   public EnumFossils fossils;

   public ItemCoveredFossil(ItemFossil cleanedFossil, EnumFossils fossils) {
      super("covered_fossil_" + fossils.getIndex());
      this.cleanedFossil = cleanedFossil;
      this.fossils = fossils;
      this.canRepair = false;
      fossilList.add(this);
      this.func_77637_a(PixelmonCreativeTabs.natural);
   }

   public EnumFossils getFossils() {
      return this.fossils;
   }

   public int getGeneration() {
      return this.fossils.getGeneration();
   }
}
