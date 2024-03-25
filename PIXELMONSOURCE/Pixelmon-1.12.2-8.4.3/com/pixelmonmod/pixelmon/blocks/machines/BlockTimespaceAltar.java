package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.blocks.GenericDoubleRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTimespaceAltar;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
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

public class BlockTimespaceAltar extends GenericDoubleRotatableModelBlock {
   public BlockTimespaceAltar(Material par2Material) {
      super(par2Material);
      this.func_149675_a(true);
      this.func_149722_s();
      this.func_149752_b(6000000.0F);
      this.func_149663_c("timespace_altar");
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(Item.func_150898_a(PixelmonBlocks.timespaceAltar));
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
      return state.func_177229_b(BLOCKPOS) == EnumBlockPos.BOTTOM ? new TileEntityTimespaceAltar() : null;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return null;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      ItemStack heldItem = player.func_184586_b(hand);
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         if (state.func_177229_b(BLOCKPOS) == EnumBlockPos.TOP) {
            pos = pos.func_177977_b();
         }

         TileEntityTimespaceAltar tile = (TileEntityTimespaceAltar)BlockHelper.getTileEntity(TileEntityTimespaceAltar.class, world, pos);
         if (tile != null) {
            if (!heldItem.func_190926_b() && heldItem.func_77973_b() == PixelmonItems.redchain && tile.chainIn) {
               ChatHandler.sendChat(player, "pixelmon.blocks.timespace.alreadychain");
               return false;
            }

            tile.activate(player, this, state, heldItem);
         }

         return player.func_184812_l_();
      } else {
         return true;
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
