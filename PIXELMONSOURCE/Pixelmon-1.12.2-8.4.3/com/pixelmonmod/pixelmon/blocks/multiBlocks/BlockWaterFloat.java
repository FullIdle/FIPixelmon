package com.pixelmonmod.pixelmon.blocks.multiBlocks;

import com.pixelmonmod.pixelmon.blocks.enums.ColorEnum;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityWaterFloat;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWaterFloat extends BlockGenericSittableModelMultiblock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.1875, 0.9);
   private ColorEnum color;

   public BlockWaterFloat(ColorEnum color) {
      super(Material.field_151580_n, 2, 0.1875, 1);
      this.color = color;
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185854_g);
      this.func_149663_c(color.toString().toLowerCase() + "_water_float");
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.SOLID;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(this.getFloatItem());
   }

   public Item getFloatItem() {
      return Item.func_150898_a(this);
   }

   public int func_149745_a(Random random) {
      return 1;
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return this.getFloatItem();
   }

   public Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityWaterFloat());
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

   public ColorEnum getColor() {
      return this.color;
   }

   float getSittingHeight() {
      return 0.16F;
   }
}
