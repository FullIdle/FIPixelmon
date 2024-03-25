package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import java.util.Random;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMint extends BlockCrops {
   public static final PropertyInteger AGE = PropertyInteger.func_177719_a("age", 0, 3);
   private static final AxisAlignedBB[] AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.35, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.7, 1.0)};

   protected PropertyInteger func_185524_e() {
      return AGE;
   }

   public int func_185526_g() {
      return 3;
   }

   protected Item func_149866_i() {
      return PixelmonItems.mintSeeds;
   }

   protected Item func_149865_P() {
      return PixelmonItems.mint;
   }

   @Nonnull
   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(this.func_149866_i());
   }

   public void getDrops(NonNullList drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      Random rand = world instanceof World ? ((World)world).field_73012_v : RANDOM;
      if (this.func_185525_y(state)) {
         drops.add(new ItemStack(this.func_149865_P(), 1, rand.nextInt(EnumNature.values().length)));
      } else {
         drops.add(new ItemStack(this.func_149866_i(), 1));
      }

   }

   public boolean func_180671_f(World worldIn, BlockPos pos, IBlockState state) {
      IBlockState soil = worldIn.func_180495_p(pos.func_177977_b());
      return (worldIn.func_175699_k(pos) >= 8 || worldIn.func_175678_i(pos)) && soil.func_177230_c() == Blocks.field_150458_ak;
   }

   public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      super.func_180650_b(worldIn, pos, state, rand);
   }

   protected int func_185529_b(World worldIn) {
      return super.func_185529_b(worldIn) / 3;
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{AGE});
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return AABB[(Integer)state.func_177229_b(this.func_185524_e())];
   }
}
