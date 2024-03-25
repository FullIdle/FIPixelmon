package com.pixelmonmod.pixelmon.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class PixelmonBlock extends Block {
   private final boolean growable;

   public PixelmonBlock(Material material) {
      this(material, false);
   }

   public PixelmonBlock(Material material, boolean growable) {
      super(material);
      this.growable = growable;
      this.func_149711_c(0.5F);
   }

   public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
      if (this.growable) {
         EnumPlantType plantType = plantable.getPlantType(world, pos.func_177972_a(direction));
         if (plantable instanceof BlockBush) {
            return true;
         }

         switch (plantType) {
            case Crop:
            case Plains:
               return true;
         }
      }

      return super.canSustainPlant(state, world, pos, direction, plantable);
   }

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT;
   }

   public boolean func_149686_d(IBlockState state) {
      return this.field_149764_J == Material.field_151592_s ? false : super.func_149686_d(state);
   }

   public boolean func_149662_c(IBlockState state) {
      return this.field_149764_J != Material.field_151592_s;
   }

   public Block func_149672_a(SoundType sound) {
      return super.func_149672_a(sound);
   }
}
