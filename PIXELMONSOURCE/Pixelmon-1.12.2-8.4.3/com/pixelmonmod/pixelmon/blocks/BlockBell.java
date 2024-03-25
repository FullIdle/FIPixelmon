package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBell;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import java.util.Random;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBell extends GenericModelBlock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   private Type bell;

   public BlockBell(Type bell) {
      super(Material.field_151574_g);
      this.func_149711_c(5.0F);
      this.func_149752_b(20.0F);
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
      this.func_180632_j(this.field_176227_L.func_177621_b());
      this.bell = bell;
      switch (bell) {
         case Clear:
            this.func_149663_c("clear_bell");
            break;
         case Tidal:
            this.func_149663_c("tidal_bell");
      }

   }

   public Type getBell() {
      return this.bell;
   }

   public TileEntity func_149915_a(World world, int var1) {
      return new TileEntityBell(this.getBell());
   }

   public boolean func_176198_a(World worldIn, BlockPos pos, EnumFacing side) {
      return side == EnumFacing.DOWN && worldIn.func_180495_p(pos.func_177973_b(side.func_176730_m())).func_185917_h();
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      if (placer instanceof EntityPlayerMP) {
         TileEntity te = worldIn.func_175625_s(pos);
         if (te != null && te instanceof TileEntityBell) {
            TileEntityBell bell = (TileEntityBell)te;
            bell.owner = placer.getPersistentID();
            bell.func_70296_d();
         }
      }

   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_185496_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      switch (this.bell) {
         case Clear:
            return new ItemStack(Item.func_150898_a(PixelmonBlocks.clearBell));
         case Tidal:
            return new ItemStack(Item.func_150898_a(PixelmonBlocks.tidalBell));
         default:
            return ItemStack.field_190927_a;
      }
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      switch (this.bell) {
         case Clear:
            return Item.func_150898_a(PixelmonBlocks.clearBell);
         case Tidal:
            return Item.func_150898_a(PixelmonBlocks.tidalBell);
         default:
            return null;
      }
   }

   public int func_149745_a(Random random) {
      return 1;
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

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public static enum Type {
      Tidal,
      Clear;
   }
}
