package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityOrb;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.enums.items.EnumOrbShard;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOrb extends GenericRotatableModelBlock {
   public final EnumOrbShard shardType;
   final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.7, 0.8);

   public BlockOrb(EnumOrbShard shardType) {
      super(Material.field_151592_s);
      this.func_149672_a(SoundType.field_185853_f);
      this.func_149663_c("block." + shardType.name().toLowerCase() + "_orb");
      this.shardType = shardType;
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING});
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return this.BOUNDS;
   }

   public boolean func_149730_j(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta));
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      worldIn.func_180501_a(pos, state.func_177226_a(BlockProperties.FACING, placer.func_174811_aO().func_176734_d()), 2);
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Items.field_190931_a;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      switch (this.shardType) {
         case RED:
            return new ItemStack(PixelmonItemsHeld.redOrb);
         case BLUE:
            return new ItemStack(PixelmonItemsHeld.blueOrb);
         case JADE:
            return ItemStack.field_190927_a;
         default:
            return ItemStack.field_190927_a;
      }
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      TileEntityOrb tile = (TileEntityOrb)world.func_175625_s(pos);
      if (tile == null) {
         return super.removedByPlayer(state, world, pos, player, willHarvest);
      } else if (player.func_184812_l_()) {
         return super.removedByPlayer(state, world, pos, player, willHarvest);
      } else {
         int pieces = tile.getPieces();
         ItemStack stack = null;
         if (this.shardType == EnumOrbShard.RED) {
            stack = pieces == 10 ? new ItemStack(PixelmonItemsHeld.redOrb, 1) : new ItemStack(PixelmonItems.redShard, pieces);
         } else if (this.shardType == EnumOrbShard.BLUE) {
            stack = pieces == 10 ? new ItemStack(PixelmonItemsHeld.blueOrb, 1) : new ItemStack(PixelmonItems.blueShard, pieces);
         }

         if (stack != null && !world.field_72995_K) {
            EntityItem drops = new EntityItem(world, (double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5, stack);
            world.func_72838_d(drops);
         }

         return super.removedByPlayer(state, world, pos, player, willHarvest);
      }
   }

   public TileEntity func_149915_a(World world, int var) {
      try {
         return new TileEntityOrb(1);
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }
}
