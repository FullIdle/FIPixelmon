package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityEvolutionRock;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.enums.EnumEvolutionRock;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEvolutionRock extends MultiBlock {
   private static AxisAlignedBB AABBEast;
   private static AxisAlignedBB AABBWest;
   private static AxisAlignedBB AABBSouth;
   private static AxisAlignedBB AABBNorth;
   public EnumEvolutionRock rockType;

   public BlockEvolutionRock(Material par2Material, EnumEvolutionRock rockType) {
      super(par2Material, 3, 2.0, 3);
      this.rockType = rockType;
      if (this.rockType == EnumEvolutionRock.MossyRock) {
         this.func_149663_c("mossyrock");
      } else {
         this.func_149663_c("icyrock");
      }

   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return this.rockType == EnumEvolutionRock.MossyRock ? new ItemStack(PixelmonBlocks.mossyRock) : new ItemStack(PixelmonBlocks.icyRock);
   }

   protected AxisAlignedBB getMultiBlockBoundingBox(IBlockAccess worldIn, BlockPos pos, EnumMultiPos multipos, EnumFacing facing) {
      if (AABBEast == null) {
         AABBEast = new AxisAlignedBB(0.0, 0.0, 0.0, (double)this.width, 2.0, (double)this.length);
         AABBWest = new AxisAlignedBB((double)(-this.width + 1), 0.0, (double)(-this.length + 1), 1.0, 2.0, 1.0);
         AABBSouth = new AxisAlignedBB(1.0, 0.0, 0.0, (double)(1 - this.length), 2.0, (double)this.width);
         AABBNorth = new AxisAlignedBB(0.0, 0.0, 1.0, (double)this.length, 2.0, (double)(1 - this.width));
      }

      if (multipos == EnumMultiPos.BASE) {
         if (facing == EnumFacing.EAST) {
            return AABBEast;
         } else if (facing == EnumFacing.WEST) {
            return AABBWest;
         } else {
            return facing == EnumFacing.SOUTH ? AABBSouth : AABBNorth;
         }
      } else {
         BlockPos base = this.findBaseBlock(worldIn, new BlockPos.MutableBlockPos(pos), worldIn.func_180495_p(pos));

         try {
            return this.getMultiBlockBoundingBox(worldIn, base, EnumMultiPos.BASE, (EnumFacing)worldIn.func_180495_p(base).func_177229_b(BlockProperties.FACING)).func_72317_d((double)(base.func_177958_n() - pos.func_177958_n()), (double)(base.func_177956_o() - pos.func_177956_o()), (double)(base.func_177952_p() - pos.func_177952_p()));
         } catch (IllegalArgumentException var7) {
            return new AxisAlignedBB(0.0, 0.0, 0.0, (double)this.width, this.height, (double)this.length);
         }
      }
   }

   public int func_149745_a(Random random) {
      return 0;
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
      return null;
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityEvolutionRock());
   }
}
