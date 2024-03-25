package com.pixelmonmod.pixelmon.blocks.multiBlocks;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityClothedTable;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockClothedTable extends BlockGenericModelMultiblock {
   private static AxisAlignedBB AABBEast;
   private static AxisAlignedBB AABBWest;
   private static AxisAlignedBB AABBSouth;
   private static AxisAlignedBB AABBNorth;

   public BlockClothedTable() {
      super(Material.field_151580_n, 2, 1.0, 2);
      this.func_149711_c(0.5F);
      this.func_149663_c("clothed_table");
      this.func_149672_a(SoundType.field_185848_a);
   }

   protected AxisAlignedBB getMultiBlockBoundingBox(IBlockAccess worldIn, BlockPos pos, EnumMultiPos multiPos, EnumFacing facing) {
      if (AABBEast == null) {
         AABBEast = new AxisAlignedBB(0.0, 0.0, 0.0, (double)this.width, 1.0, (double)this.length);
         AABBWest = new AxisAlignedBB((double)(-this.width + 1), 0.0, (double)(-this.length + 1), 1.0, 1.0, 1.0);
         AABBSouth = new AxisAlignedBB(1.0, 0.0, 0.0, (double)(1 - this.length), 1.0, (double)this.width);
         AABBNorth = new AxisAlignedBB(0.0, 0.0, 1.0, (double)this.length, 1.0, (double)(1 - this.width));
      }

      if (multiPos == EnumMultiPos.BASE) {
         if (facing == EnumFacing.EAST) {
            return AABBEast;
         } else if (facing == EnumFacing.WEST) {
            return AABBWest;
         } else {
            return facing == EnumFacing.SOUTH ? AABBSouth : AABBNorth;
         }
      } else {
         BlockPos base = this.findBaseBlock(worldIn, new BlockPos.MutableBlockPos(pos), worldIn.func_180495_p(pos));
         IBlockState baseBlock = worldIn.func_180495_p(base);
         if (baseBlock.func_185904_a() == Material.field_151579_a) {
            return null;
         } else {
            EnumFacing blockFacing;
            try {
               blockFacing = (EnumFacing)baseBlock.func_177229_b(BlockProperties.FACING);
            } catch (IllegalArgumentException var9) {
               return null;
            }

            return this.getMultiBlockBoundingBox(worldIn, base, EnumMultiPos.BASE, blockFacing).func_72317_d((double)(base.func_177958_n() - pos.func_177958_n()), (double)(base.func_177956_o() - pos.func_177956_o()), (double)(base.func_177952_p() - pos.func_177952_p()));
         }
      }
   }

   public Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityClothedTable());
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

   public Item getDroppedItem(World world, BlockPos pos) {
      return Item.func_150898_a(this);
   }

   public int func_149745_a(Random random) {
      return 1;
   }
}
