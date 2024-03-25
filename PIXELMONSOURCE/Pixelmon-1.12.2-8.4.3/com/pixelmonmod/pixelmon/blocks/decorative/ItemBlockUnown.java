package com.pixelmonmod.pixelmon.blocks.decorative;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemBlockUnown extends ItemMultiTexture {
   public ItemBlockUnown(Block block) {
      super(block, block, ((BlockUnown)block).alphabetInUse);
   }
}
