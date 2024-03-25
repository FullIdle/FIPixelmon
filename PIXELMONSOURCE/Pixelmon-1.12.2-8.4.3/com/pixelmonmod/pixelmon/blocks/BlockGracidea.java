package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityGracidea;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockGracidea extends GenericRotatableModelBlock implements IPlantable, IGrowable {
   public BlockGracidea() {
      super(Material.field_151585_k);
   }

   public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
      return EnumPlantType.Plains;
   }

   public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
      IBlockState state = world.func_180495_p(pos);
      return state.func_177230_c() != this ? this.func_176223_P() : state;
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      return Collections.singletonList(new ItemStack(PixelmonItems.gracidea, 1));
   }

   public TileEntity func_149915_a(World world, int var) {
      try {
         return new TileEntityGracidea();
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.3, 0.0, 0.3, 0.7, 0.2, 0.7);
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(PixelmonItems.gracidea, 1);
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return PixelmonItems.gracidea;
   }

   public int func_149745_a(Random random) {
      return 1;
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

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos from) {
      this.checkAndDropBlock(worldIn, pos, state);
   }

   protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
      if (!this.canBlockStay(worldIn, pos, state)) {
         this.func_176226_b(worldIn, pos, state, 0);
         worldIn.func_180501_a(pos, Blocks.field_150350_a.func_176223_P(), 3);
      }

   }

   protected boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
      return world.func_180495_p(pos.func_177977_b()).func_177230_c() == Blocks.field_150349_c;
   }

   public boolean func_176473_a(World world, BlockPos pos, IBlockState state, boolean isClient) {
      TileEntityGracidea tile = (TileEntityGracidea)BlockHelper.getTileEntity(TileEntityGracidea.class, world, pos);
      return tile != null ? tile.canGrow() : false;
   }

   public boolean func_180670_a(World world, Random rand, BlockPos pos, IBlockState state) {
      TileEntityGracidea tile = (TileEntityGracidea)BlockHelper.getTileEntity(TileEntityGracidea.class, world, pos);
      return tile != null ? tile.canGrow() : false;
   }

   public void func_176474_b(World world, Random rand, BlockPos pos, IBlockState state) {
      TileEntityGracidea tile = (TileEntityGracidea)BlockHelper.getTileEntity(TileEntityGracidea.class, world, pos);
      if (tile != null) {
         tile.doGrow(RandomHelper.getRandomNumberBetween(1, 2));
      }

   }
}
