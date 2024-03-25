package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityEndTable;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EndTableBlock extends GenericRotatableModelBlock {
   public EndTableBlock() {
      super(Material.field_151575_d);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149663_c("end_table");
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         TileEntityEndTable tile = (TileEntityEndTable)BlockHelper.getTileEntity(TileEntityEndTable.class, world, pos);
         if (tile != null) {
            if (tile.isOpen()) {
               if (player.func_70093_af()) {
                  tile.closeDrawer();
               } else {
                  player.func_71007_a(tile);
               }
            } else {
               if (player.func_70093_af()) {
                  return false;
               }

               tile.openDrawer();
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityEndTable();
   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      TileEntityEndTable tile = (TileEntityEndTable)BlockHelper.getTileEntity(TileEntityEndTable.class, world, pos);
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
      return face == EnumFacing.UP ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
