package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCloningMachine;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCloningMachine extends MultiBlock {
   public BlockCloningMachine() {
      super(Material.field_151573_f, 5, 3.0, 1);
      this.func_149711_c(1.0F);
      this.func_149663_c("cloningMachine");
   }

   public ItemStack func_185473_a(World worldIn, BlockPos pos, IBlockState state) {
      return ItemStack.field_190927_a;
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

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         ItemStack heldItem = player.func_184586_b(hand);
         BlockPos loc = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityCloningMachine te = (TileEntityCloningMachine)BlockHelper.getTileEntity(TileEntityCloningMachine.class, world, loc);
         if (te != null) {
            te.activate(player, heldItem);
         }
      }

      return true;
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      BlockPos loc = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityCloningMachine te = (TileEntityCloningMachine)BlockHelper.getTileEntity(TileEntityCloningMachine.class, world, loc);
      return te != null && !te.isBroken ? Item.func_150898_a(PixelmonBlocks.cloningMachine) : null;
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityCloningMachine());
   }

   public float func_180647_a(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
      BlockPos loc = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityCloningMachine ranchblock = (TileEntityCloningMachine)BlockHelper.getTileEntity(TileEntityCloningMachine.class, world, loc);
      return ranchblock != null && ranchblock.owner != null && !player.func_110124_au().equals(ranchblock.owner) && !player.field_71075_bZ.field_75098_d ? -1.0F : super.func_180647_a(state, player, world, pos);
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      BlockPos loc = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityCloningMachine block = (TileEntityCloningMachine)BlockHelper.getTileEntity(TileEntityCloningMachine.class, world, loc);
      if (block != null) {
         block.onDestroy();
      }

      return super.removedByPlayer(state, world, pos, player, willHarvest);
   }
}
