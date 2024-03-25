package com.pixelmonmod.pixelmon.blocks.multiBlocks;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTV;
import java.util.Optional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTV extends BlockGenericModelMultiblock {
   AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 1.1, 0.9);

   public BlockTV() {
      super(Material.field_151573_f, 1, 1.1, 1);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149663_c("TV");
   }

   protected AxisAlignedBB getMultiBlockBoundingBox(IBlockAccess worldIn, BlockPos pos, EnumMultiPos multiPos, EnumFacing facing) {
      if (multiPos == EnumMultiPos.BASE) {
         return this.AABB;
      } else {
         BlockPos base = this.findBaseBlock(worldIn, new BlockPos.MutableBlockPos(pos), worldIn.func_180495_p(pos));

         try {
            return this.getMultiBlockBoundingBox(worldIn, base, EnumMultiPos.BASE, (EnumFacing)worldIn.func_180495_p(base).func_177229_b(BlockProperties.FACING)).func_72317_d((double)(base.func_177958_n() - pos.func_177958_n()), (double)(base.func_177956_o() - pos.func_177956_o()), (double)(base.func_177952_p() - pos.func_177952_p()));
         } catch (IllegalArgumentException var7) {
            return new AxisAlignedBB(0.0, 0.0, 0.0, (double)this.width, this.height, (double)this.length);
         }
      }
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityTV());
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
