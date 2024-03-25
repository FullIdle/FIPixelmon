package com.pixelmonmod.pixelmon.blocks.decorative;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockIncense extends Block {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.45, 0.8);

   public BlockIncense(String name) {
      super(Material.field_151576_e);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149663_c(name);
      this.func_149711_c(2.0F);
      this.func_149647_a(PixelmonCreativeTabs.decoration);
      this.field_149784_t = 3;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return PixelmonBlocks.getItemFromBlock(this);
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(PixelmonBlocks.getItemFromBlock(this));
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
      if (this != PixelmonBlocks.incenseBurner && worldIn.func_72820_D() % 16L != 1L) {
         worldIn.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, (double)((float)pos.func_177958_n() + RandomHelper.useRandomForNumberBetween(rand, 0.3F, 0.7F)), (double)((float)pos.func_177956_o() + 0.6F), (double)((float)pos.func_177952_p() + RandomHelper.useRandomForNumberBetween(rand, 0.3F, 0.7F)), (double)RandomHelper.useRandomForNumberBetween(rand, -0.03F, 0.03F), (double)(rand.nextFloat() * 0.001F), (double)RandomHelper.useRandomForNumberBetween(rand, -0.03F, 0.03F), new int[0]);
      }
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return AABB;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
