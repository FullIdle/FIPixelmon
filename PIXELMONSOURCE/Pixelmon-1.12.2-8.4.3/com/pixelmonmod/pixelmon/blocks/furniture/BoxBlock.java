package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBox;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BoxBlock extends GenericRotatableModelBlock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.20000000298023224, 0.0, 0.20000000298023224, 0.800000011920929, 0.6000000238418579, 0.800000011920929);

   public BoxBlock() {
      super(Material.field_151576_e);
      this.func_149711_c(0.5F);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149663_c("Box");
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         TileEntityBox tile = (TileEntityBox)BlockHelper.getTileEntity(TileEntityBox.class, world, pos);
         if (tile.isOpen()) {
            if (player.func_70093_af()) {
               tile.closeBox(world.func_73046_m());
            } else {
               player.func_71007_a(tile);
            }
         } else {
            tile.openBox();
         }

         return true;
      } else {
         return true;
      }
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityBox();
   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      TileEntityBox tile = (TileEntityBox)BlockHelper.getTileEntity(TileEntityBox.class, world, pos);
      if (tile != null) {
         InventoryHelper.func_180175_a(world, pos, tile);
      }

      super.func_180663_b(world, pos, state);
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
}
