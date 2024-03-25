package com.pixelmonmod.pixelmon.blocks.decorative;

import com.pixelmonmod.pixelmon.blocks.spawning.BlockSpawningHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;

public class BlockSeaweed extends Block implements IShearable, IPlantable {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.8, 0.9);

   public BlockSeaweed() {
      super(Material.field_151586_h);
      this.func_149675_a(true);
      this.func_149672_a(SoundType.field_185850_c);
      this.func_149711_c(3.0F);
   }

   public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
      return EnumPlantType.Water;
   }

   public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
      IBlockState state = world.func_180495_p(pos);
      return state.func_177230_c() != this ? this.func_176223_P() : state;
   }

   public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
      return true;
   }

   public List onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
      List ret = new ArrayList();
      return ret;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT;
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return null;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
      super.func_189540_a(state, worldIn, pos, blockIn, fromPos);
      this.checkAndDropBlock(worldIn, pos, state);
   }

   public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      this.checkAndDropBlock(worldIn, pos, state);
   }

   protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
      if (!this.canBlockStay(worldIn, pos, state)) {
         this.func_176226_b(worldIn, pos, state, 0);
         worldIn.func_180501_a(pos, Blocks.field_150355_j.func_176223_P(), 3);
      }

   }

   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
      BlockPos down = pos.func_177977_b();
      Block soil = worldIn.func_180495_p(down).func_177230_c();
      if (!this.func_176196_c(worldIn, pos)) {
         return false;
      } else {
         return state.func_177230_c() != this ? this.canPlaceBlockOn(soil) : soil.canSustainPlant(state, worldIn, down, EnumFacing.UP, this);
      }
   }

   protected boolean canPlaceBlockOn(Block ground) {
      return ground == Blocks.field_150349_c || ground == Blocks.field_150346_d || ground == Blocks.field_150351_n || ground == Blocks.field_150354_m;
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return worldIn.func_180495_p(pos) == Blocks.field_150355_j.func_176223_P();
   }

   public void func_180634_a(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
      if (entityIn instanceof EntityPlayerMP) {
         BlockSpawningHandler.getInstance().performBattleStartCheck(worldIn, pos, entityIn, (EntityPixelmon)null, EnumBattleStartTypes.SEAWEED, state);
      }

   }

   public boolean func_176209_a(IBlockState state, boolean hitIfLiquid) {
      return super.func_176209_a(state, hitIfLiquid);
   }

   public boolean func_176200_f(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return super.func_180642_a(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      super.func_180633_a(worldIn, pos, state, placer, stack);
   }
}
