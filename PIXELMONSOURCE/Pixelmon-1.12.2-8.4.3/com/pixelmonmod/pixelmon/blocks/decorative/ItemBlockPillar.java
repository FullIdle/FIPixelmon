package com.pixelmonmod.pixelmon.blocks.decorative;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemBlockPillar extends ItemMultiTexture {
   public ItemBlockPillar(Block block) {
      super(block, block, new String[]{"Pillar", "BrokenPillar"});
   }
}
