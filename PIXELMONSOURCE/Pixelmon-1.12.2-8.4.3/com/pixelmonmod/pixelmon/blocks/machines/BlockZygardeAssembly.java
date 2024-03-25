package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericDoubleRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityZygardeAssembly;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

public class BlockZygardeAssembly extends GenericDoubleRotatableModelBlock {
   public BlockZygardeAssembly() {
      super(Material.field_151573_f);
      this.func_149711_c(2.5F);
      this.func_149663_c("reassembly_unit");
      this.func_180632_j(this.field_176227_L.func_177621_b());
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(Item.func_150898_a(PixelmonBlocks.assemblyUnit));
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return state.func_177229_b(BLOCKPOS) == EnumBlockPos.BOTTOM ? new TileEntityZygardeAssembly() : null;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      ItemStack heldItem = player.func_184586_b(hand);
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         if (state.func_177229_b(BLOCKPOS) == EnumBlockPos.TOP) {
            pos = pos.func_177977_b();
         }

         TileEntityZygardeAssembly tile = (TileEntityZygardeAssembly)BlockHelper.getTileEntity(TileEntityZygardeAssembly.class, world, pos);
         if (tile != null && player instanceof EntityPlayerMP) {
            tile.activate((EntityPlayerMP)player, state, heldItem);
         }

         return true;
      } else {
         return true;
      }
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return worldIn.func_180495_p(pos.func_177984_a()).func_177230_c().func_176200_f(worldIn, pos.func_177984_a()) && super.func_176196_c(worldIn, pos);
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      super.func_180633_a(worldIn, pos, state, placer, stack);
      worldIn.func_180501_a(pos.func_177984_a(), state.func_177226_a(BlockProperties.FACING, placer.func_174811_aO().func_176734_d()).func_177226_a(BLOCKPOS, EnumBlockPos.TOP), 2);
   }

   public void func_176206_d(World worldIn, BlockPos pos, IBlockState state) {
      super.func_176206_d(worldIn, pos, state);
      if (state.func_177229_b(BLOCKPOS) == EnumBlockPos.TOP) {
         worldIn.func_175698_g(pos.func_177977_b());
      } else {
         worldIn.func_175698_g(pos.func_177984_a());
      }

   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return field_185505_j.func_186670_a(pos);
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return field_185505_j;
   }

   public AxisAlignedBB func_185496_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return field_185505_j;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public boolean func_149686_d(IBlockState state) {
      return true;
   }
}
