package com.pixelmonmod.pixelmon.blocks.decorative;

import com.pixelmonmod.pixelmon.blocks.PixelmonBlock;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

public class BlockBridge extends PixelmonBlock {
   public BlockBridge() {
      super(Material.field_151575_d);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149711_c(0.5F);
      this.func_149663_c("BridgeBlock");
      this.func_149647_a(PixelmonCreativeTabs.decoration);
   }

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }
}
