package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.enums.EnumPokeChestType;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;

public class BlockUltraChest extends BlockPokeChest {
   public BlockUltraChest() {
      super(TileEntityPokeChest.class);
      this.TYPE = EnumPokeChestType.ULTRABALL;
      this.func_149663_c("ultrachest");
   }
}
