package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.PixelmonBlock;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LetterboxBlock extends PixelmonBlock {
   private static final Map colorMap = new HashMap();
   private static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.15, 0.0, 0.15, 0.85, 0.4, 0.85);
   private static final AxisAlignedBB WALL_EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.15, 0.3, 0.75, 0.85);
   private static final AxisAlignedBB WALL_WEST_AABB = new AxisAlignedBB(0.7, 0.0, 0.15, 1.0, 0.75, 0.85);
   private static final AxisAlignedBB WALL_NORTH_AABB = new AxisAlignedBB(0.15, 0.0, 0.7, 0.85, 0.75, 1.0);
   private static final AxisAlignedBB WALL_SOUTH_AABB = new AxisAlignedBB(0.15, 0.0, 0.0, 0.85, 0.75, 0.3);
   public final EnumDyeColor color;
   public final boolean isWall;

   public LetterboxBlock(boolean isWall, EnumDyeColor color) {
      super(Material.field_151575_d);
      this.color = color;
      this.isWall = isWall;
      colorMap.put(this.color, this);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149711_c(1.0F);
      this.func_149663_c("letterbox_" + color.func_176610_l());
      this.func_149647_a(PixelmonCreativeTabs.decoration);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockProperties.FACING, EnumFacing.SOUTH));
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand == EnumHand.MAIN_HAND) {
         ItemStack heldItem = player.func_184586_b(hand);
         if (!heldItem.func_190926_b() && heldItem.func_77973_b() instanceof ItemDye) {
            EnumDyeColor dyeColor = EnumDyeColor.func_176766_a(heldItem.func_77952_i());
            Block block = Block.func_149684_b("pixelmon:letterbox_" + (this.isWall ? "wall_" : "standing_") + dyeColor.func_176610_l());
            if (block == null) {
               return true;
            }

            IBlockState newState = block.func_176203_a(this.func_176201_c(state));
            world.func_175656_a(pos, newState);
            if (!player.field_71075_bZ.field_75098_d) {
               heldItem.func_190918_g(1);
            }

            return true;
         }
      }

      return true;
   }

   public ItemStack func_185473_a(World worldIn, BlockPos pos, IBlockState state) {
      return new ItemStack(PixelmonBlocks.getItemFromBlock(this), 1, this.func_180651_a(state));
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return PixelmonBlocks.getItemFromBlock(this);
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING});
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, placer.func_174811_aO().func_176734_d());
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      if (this.isWall) {
         EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         if (facing == EnumFacing.WEST) {
            return WALL_WEST_AABB;
         } else if (facing == EnumFacing.EAST) {
            return WALL_EAST_AABB;
         } else if (facing == EnumFacing.NORTH) {
            return WALL_NORTH_AABB;
         } else {
            return facing == EnumFacing.SOUTH ? WALL_SOUTH_AABB : STANDING_AABB;
         }
      } else {
         return STANDING_AABB;
      }
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta & 3));
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
