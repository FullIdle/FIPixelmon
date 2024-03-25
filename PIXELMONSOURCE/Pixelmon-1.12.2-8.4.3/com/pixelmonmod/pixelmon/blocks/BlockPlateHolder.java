package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPlateHolder;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPlateHolder extends GenericModelBlock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.10000000149011612, 0.0, 0.10000000149011612, 0.8999999761581421, 1.0, 0.8999999761581421);

   public BlockPlateHolder() {
      super(Material.field_151576_e);
      this.func_149722_s();
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
      this.func_180632_j(this.field_176227_L.func_177621_b());
   }

   public TileEntity func_149915_a(World world, int var1) {
      return new TileEntityPlateHolder();
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_185496_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(Item.func_150898_a(PixelmonBlocks.plateHolder));
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public int func_149745_a(Random random) {
      return 0;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      ItemStack heldItem = player.func_184586_b(hand);
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         TileEntityPlateHolder tile = (TileEntityPlateHolder)BlockHelper.getTileEntity(TileEntityPlateHolder.class, world, pos);
         if (tile != null) {
            tile.activate(player, this, state, heldItem);
         }

         return player.func_184812_l_();
      } else {
         return true;
      }
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
