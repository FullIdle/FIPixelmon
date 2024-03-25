package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.GenericRotatableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class TvBlock extends GenericRotatableBlock {
   public TvBlock() {
      super(Material.field_151573_f);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149663_c("tv2");
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }
}
