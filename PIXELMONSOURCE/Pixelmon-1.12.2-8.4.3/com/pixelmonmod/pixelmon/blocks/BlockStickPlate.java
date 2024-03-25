package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStickPlate extends GenericRotatableBlock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);

   public BlockStickPlate() {
      super(Material.field_151573_f);
      this.func_149711_c(2.5F);
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   public void func_180634_a(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
      double distanceFromMiddleX = Math.abs((double)pos.func_177958_n() - (entity.field_70165_t - 0.5));
      double distanceFromMiddleZ = Math.abs((double)pos.func_177952_p() - (entity.field_70161_v - 0.5));
      if (distanceFromMiddleX < 0.2) {
         entity.field_70159_w = 0.0;
      }

      if (distanceFromMiddleZ < 0.2) {
         entity.field_70179_y = 0.0;
      }

   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      worldIn.func_180501_a(pos, state.func_177226_a(BlockProperties.FACING, placer.func_174811_aO()), 2);
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess access, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return AABB;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
