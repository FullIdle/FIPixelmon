package com.pixelmonmod.pixelmon.blocks.decorative;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemBlockBraille extends ItemMultiTexture {
   public ItemBlockBraille(Block block) {
      super(block, block, ((BlockBraille)block).alphabetInUse);
   }
}
