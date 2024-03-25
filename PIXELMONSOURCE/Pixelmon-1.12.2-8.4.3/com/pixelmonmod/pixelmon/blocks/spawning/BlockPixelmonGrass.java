package com.pixelmonmod.pixelmon.blocks.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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

public class BlockPixelmonGrass extends Block implements IGrowable, IShearable, IPlantable {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.8, 0.9);

   public BlockPixelmonGrass() {
      super(Material.field_151585_k);
      this.func_149675_a(true);
      this.func_149672_a(SoundType.field_185850_c);
      this.func_149711_c(3.0F);
   }

   public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
      return EnumPlantType.Plains;
   }

   protected boolean canPlaceBlockOn(Block ground) {
      Material groundMaterial = ground.func_149688_o(ground.func_176194_O().func_177621_b());
      return groundMaterial == Material.field_151578_c || groundMaterial == Material.field_151595_p || groundMaterial == Material.field_151577_b;
   }

   public void func_180634_a(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
      if (entityIn instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)entityIn;
         if (BattleRegistry.getBattle(player) != null) {
            return;
         }

         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
         if (storage.getTicksTillEncounter() <= 1 && player.field_70163_u == (double)pos.func_177956_o()) {
            BlockSpawningHandler.getInstance().performBattleStartCheck(worldIn, pos, player, (EntityPixelmon)null, EnumBattleStartTypes.PUGRASSSINGLE, state);
         }

         storage.updateTicksTillEncounter();
      }

   }

   public boolean func_176200_f(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public int func_149679_a(int fortune, Random random) {
      return 1 + random.nextInt(fortune * 2 + 1);
   }

   public int func_180651_a(IBlockState state) {
      return state.func_177230_c().func_176201_c(state);
   }

   public boolean func_176473_a(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
      return true;
   }

   public boolean func_180670_a(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      return true;
   }

   public void func_176474_b(World worldIn, Random rand, BlockPos pos, IBlockState state) {
   }

   public int func_176201_c(IBlockState state) {
      return 0;
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P();
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[0]);
   }

   public List onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
      List ret = new ArrayList();
      ret.add(new ItemStack(PixelmonBlocks.pixelmonGrassBlock, 1, 0));
      return ret;
   }

   public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
      return true;
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return super.func_176196_c(worldIn, pos) && worldIn.func_180495_p(pos.func_177977_b()).func_177230_c().canSustainPlant(worldIn.func_180495_p(pos.func_177977_b()), worldIn, pos.func_177977_b(), EnumFacing.UP, this);
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
         worldIn.func_180501_a(pos, Blocks.field_150350_a.func_176223_P(), 3);
      }

   }

   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
      BlockPos down = pos.func_177977_b();
      Block soil = worldIn.func_180495_p(down).func_177230_c();
      return state.func_177230_c() != this ? this.canPlaceBlockOn(soil) : soil.canSustainPlant(state, worldIn, down, EnumFacing.UP, this);
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return null;
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

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT;
   }

   public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
      IBlockState state = world.func_180495_p(pos);
      return state.func_177230_c() != this ? this.func_176223_P() : state;
   }
}
