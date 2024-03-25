package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.enums.EnumPokeChestType;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;

public class BlockMasterChest extends BlockPokeChest {
   public BlockMasterChest() {
      super(TileEntityPokeChest.class);
      this.TYPE = EnumPokeChestType.MASTERBALL;
      this.func_149663_c("masterchest");
   }
}
