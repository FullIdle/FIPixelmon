package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.enums.EnumPokeChestType;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;

public class BlockBeastChest extends BlockPokeChest {
   public BlockBeastChest() {
      super(TileEntityPokeChest.class);
      this.TYPE = EnumPokeChestType.BEASTBALL;
      this.func_149663_c("beastchest");
   }
}
