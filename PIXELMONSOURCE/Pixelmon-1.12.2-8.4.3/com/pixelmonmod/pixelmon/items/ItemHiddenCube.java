package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import net.minecraft.item.ItemBlock;

public class ItemHiddenCube extends ItemBlock {
   public ItemHiddenCube() {
      super(PixelmonBlocks.hiddenCube);
      this.func_77655_b("hidden_cube");
      this.func_77625_d(16);
      this.func_77637_a(PixelmonCreativeTabs.utilityBlocks);
   }
}
