package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.GenericRotatableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RelicBlock extends GenericRotatableBlock {
   public static final AxisAlignedBB AABB = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.5, 0.8);

   public RelicBlock(String name) {
      super(Material.field_151576_e);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149663_c(name);
      this.func_149711_c(1.0F);
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return AABB;
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return worldIn.func_180495_p(pos.func_177977_b()).isSideSolid(worldIn, pos, EnumFacing.UP);
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
