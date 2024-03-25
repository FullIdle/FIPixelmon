package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.blocks.furniture.ArmchairBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityArmchair extends TileEntity implements ISpecialTexture {
   private static ResourceLocation[] textures = new ResourceLocation[EnumDyeColor.values().length];

   public ResourceLocation getTexture() {
      IBlockState state = this.func_145831_w().func_180495_p(this.func_174877_v());
      if (state.func_177230_c() instanceof ArmchairBlock) {
         ArmchairBlock block = (ArmchairBlock)state.func_177230_c();
         return textures[block.getColor().ordinal()];
      } else {
         return textures[0];
      }
   }

   static {
      EnumDyeColor[] var0 = EnumDyeColor.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumDyeColor color = var0[var2];
         textures[color.ordinal()] = new ResourceLocation("pixelmon:textures/blocks/armchair/armchair_" + color + ".png");
      }

   }
}
