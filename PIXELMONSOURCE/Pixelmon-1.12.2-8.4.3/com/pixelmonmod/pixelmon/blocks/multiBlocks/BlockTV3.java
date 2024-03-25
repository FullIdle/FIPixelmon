package com.pixelmonmod.pixelmon.blocks.multiBlocks;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTV3;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Optional;
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

public class BlockTV3 extends BlockGenericModelMultiblock {
   AxisAlignedBB AABB = new AxisAlignedBB(-0.5, 0.0, 0.3, 1.5, 1.1, 0.7);
   AxisAlignedBB AABB2 = new AxisAlignedBB(0.3, 0.0, -0.5, 0.7, 1.1, 1.5);

   public BlockTV3() {
      super(Material.field_151573_f, 3, 1.1, 1);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149663_c("TV3");
   }

   protected AxisAlignedBB getMultiBlockBoundingBox(IBlockAccess worldIn, BlockPos pos, EnumMultiPos multiPos, EnumFacing facing) {
      if (multiPos == EnumMultiPos.BASE) {
         return facing != EnumFacing.NORTH && facing != EnumFacing.SOUTH ? this.AABB2 : this.AABB;
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
      return Optional.of(new TileEntityTV3());
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return Item.func_150898_a(PixelmonBlocks.tv3Block);
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
