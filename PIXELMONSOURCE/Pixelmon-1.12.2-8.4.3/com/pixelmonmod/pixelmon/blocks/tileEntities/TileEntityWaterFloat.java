package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.blocks.enums.ColorEnum;
import com.pixelmonmod.pixelmon.blocks.multiBlocks.BlockWaterFloat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityWaterFloat extends TileEntity implements ISpecialTexture {
   private static ResourceLocation[] textures = new ResourceLocation[ColorEnum.values().length];

   public ResourceLocation getTexture() {
      IBlockState state = this.func_145831_w().func_180495_p(this.func_174877_v());
      if (state.func_177230_c() instanceof BlockWaterFloat) {
         BlockWaterFloat block = (BlockWaterFloat)state.func_177230_c();
         return textures[block.getColor().ordinal()];
      } else {
         return textures[0];
      }
   }

   static {
      ColorEnum[] var0 = ColorEnum.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         ColorEnum color = var0[var2];
         textures[color.ordinal()] = new ResourceLocation("pixelmon:textures/blocks/WaterFloat/" + color + "WaterFloatModel.png");
      }

   }
}
