package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

public class ItemPokeballDisc extends PixelmonItem {
   public EnumPokeballs pokeball;

   public ItemPokeballDisc(EnumPokeballs type) {
      super(type.getFilenamePrefix() + "_disc");
      this.pokeball = type;
      this.field_77777_bU = 64;
      this.func_77656_e(0);
      this.func_77637_a(PixelmonCreativeTabs.pokeball);
      this.canRepair = false;
   }
}
