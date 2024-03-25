package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.enums.EnumUsed;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityIlexShrine;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
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

public class BlockIlexShrine extends GenericRotatableModelBlock {
   public static final PropertyEnum BLOCKPOS = PropertyEnum.func_177709_a("blockpos", EnumBlockPos.class);
   public static final PropertyEnum USED = PropertyEnum.func_177709_a("used", EnumUsed.class);

   public BlockIlexShrine(Material materialIn) {
      super(materialIn);
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING, BLOCKPOS, USED});
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta & 3)).func_177226_a(BLOCKPOS, (meta & 7) > 3 ? EnumBlockPos.TOP : EnumBlockPos.BOTTOM).func_177226_a(USED, meta > 7 ? EnumUsed.YES : EnumUsed.NO);
   }

   public int func_176201_c(IBlockState state) {
      int meta = 0;
      meta |= ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
      return meta + (state.func_177229_b(BLOCKPOS) == EnumBlockPos.TOP ? 4 : 0) + (state.func_177229_b(USED) == EnumUsed.YES ? 8 : 0);
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState iblockstate = this.func_176223_P();
      if (facing.func_176740_k().func_176722_c()) {
         iblockstate = iblockstate.func_177226_a(BlockProperties.FACING, facing);
      }

      return iblockstate.func_177226_a(BLOCKPOS, EnumBlockPos.BOTTOM).func_177226_a(USED, EnumUsed.NO);
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(Item.func_150898_a(PixelmonBlocks.shrineIlex));
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public int func_149745_a(Random random) {
      return 0;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return state.func_177229_b(BLOCKPOS) == EnumBlockPos.BOTTOM ? new TileEntityIlexShrine() : null;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return null;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      ItemStack heldItem = player.func_184586_b(hand);
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         if (state.func_177229_b(BLOCKPOS) == EnumBlockPos.TOP) {
            pos = pos.func_177977_b();
            state = world.func_180495_p(pos);
         }

         TileEntityIlexShrine tile = (TileEntityIlexShrine)BlockHelper.getTileEntity(TileEntityIlexShrine.class, world, pos);
         if (tile != null) {
            tile.activate(player, this, state, heldItem);
         }

         return player.func_184812_l_();
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
         worldIn.func_175713_t(pos);
         worldIn.func_175713_t(pos.func_177977_b());
      } else {
         worldIn.func_175698_g(pos.func_177984_a());
         worldIn.func_175713_t(pos);
         worldIn.func_175713_t(pos.func_177984_a());
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
