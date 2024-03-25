package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.GenericRotatableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class WorkplaceBlock extends GenericRotatableBlock {
   public WorkplaceBlock(String type) {
      super(Material.field_151575_d);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149663_c(type + "workplace");
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
