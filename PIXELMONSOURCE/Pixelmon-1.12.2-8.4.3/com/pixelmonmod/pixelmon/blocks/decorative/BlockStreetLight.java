package com.pixelmonmod.pixelmon.blocks.decorative;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStreetLight extends Block {
   private static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);
   private static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.2, 0.0, 0.0, 0.8, 1.0, 1.0);
   private static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0, 0.0, 0.2, 1.0, 1.0, 0.8);

   public BlockStreetLight() {
      super(Material.field_151594_q);
      this.field_149785_s = true;
      this.func_149715_a(1.0F);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149663_c("street_light");
      this.func_149647_a(PixelmonCreativeTabs.decoration);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockProperties.FACING_ALL, EnumFacing.UP));
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING_ALL});
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      if (this.canPlaceAt(worldIn, pos, facing)) {
         return this.func_176223_P().func_177226_a(BlockProperties.FACING_ALL, facing);
      } else {
         Iterator var9 = Plane.HORIZONTAL.iterator();

         EnumFacing enumfacing;
         do {
            if (!var9.hasNext()) {
               return this.func_176223_P();
            }

            enumfacing = (EnumFacing)var9.next();
         } while(!this.canPlaceAt(worldIn, pos, enumfacing));

         return this.func_176223_P().func_177226_a(BlockProperties.FACING_ALL, enumfacing);
      }
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      switch (((EnumFacing)state.func_177229_b(BlockProperties.FACING_ALL)).func_176740_k()) {
         case X:
            return X_AABB;
         case Z:
            return Z_AABB;
         default:
            return STANDING_AABB;
      }
   }

   private boolean canPlaceOn(World worldIn, BlockPos pos) {
      IBlockState state = worldIn.func_180495_p(pos);
      return state.func_177230_c().canPlaceTorchOnTop(state, worldIn, pos);
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      Iterator var3 = BlockProperties.FACING_ALL.func_177700_c().iterator();

      EnumFacing enumfacing;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         enumfacing = (EnumFacing)var3.next();
      } while(!this.canPlaceAt(worldIn, pos, enumfacing));

      return true;
   }

   private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
      BlockPos blockpos = pos.func_177972_a(facing.func_176734_d());
      IBlockState iblockstate = worldIn.func_180495_p(blockpos);
      Block block = iblockstate.func_177230_c();
      BlockFaceShape blockfaceshape = iblockstate.func_193401_d(worldIn, blockpos, facing);
      if (facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos)) {
         return true;
      } else if (facing == EnumFacing.UP) {
         return false;
      } else {
         return !func_193382_c(block) && (blockfaceshape == BlockFaceShape.SOLID || blockfaceshape == BlockFaceShape.MIDDLE_POLE);
      }
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
      if (this.checkForDrop(worldIn, pos, state)) {
         EnumFacing enumfacing = (EnumFacing)state.func_177229_b(BlockProperties.FACING_ALL);
         EnumFacing.Axis enumfacing$axis = enumfacing.func_176740_k();
         EnumFacing enumfacing1 = enumfacing.func_176734_d();
         BlockPos blockpos = pos.func_177972_a(enumfacing1);
         boolean flag = false;
         if (enumfacing$axis.func_176722_c() && worldIn.func_180495_p(blockpos).func_193401_d(worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID) {
            flag = true;
         } else if (enumfacing$axis.func_176720_b() && !this.canPlaceOn(worldIn, blockpos)) {
            flag = true;
         }

         if (flag) {
            this.func_176226_b(worldIn, pos, state, 0);
            worldIn.func_175698_g(pos);
         }
      }

   }

   protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
      if (state.func_177230_c() == this && this.canPlaceAt(worldIn, pos, (EnumFacing)state.func_177229_b(BlockProperties.FACING_ALL))) {
         return true;
      } else {
         if (worldIn.func_180495_p(pos).func_177230_c() == this) {
            this.func_176226_b(worldIn, pos, state, 0);
            worldIn.func_175698_g(pos);
         }

         return false;
      }
   }

   public IBlockState func_176203_a(int meta) {
      IBlockState iblockstate = this.func_176223_P();
      switch (meta) {
         case 0:
            iblockstate = iblockstate.func_177226_a(BlockProperties.FACING_ALL, EnumFacing.UP);
            break;
         case 1:
            iblockstate = iblockstate.func_177226_a(BlockProperties.FACING_ALL, EnumFacing.EAST);
            break;
         case 2:
            iblockstate = iblockstate.func_177226_a(BlockProperties.FACING_ALL, EnumFacing.WEST);
            break;
         case 3:
            iblockstate = iblockstate.func_177226_a(BlockProperties.FACING_ALL, EnumFacing.SOUTH);
            break;
         case 4:
            iblockstate = iblockstate.func_177226_a(BlockProperties.FACING_ALL, EnumFacing.NORTH);
            break;
         default:
            iblockstate = iblockstate.func_177226_a(BlockProperties.FACING_ALL, EnumFacing.DOWN);
      }

      return iblockstate;
   }

   public int func_176201_c(IBlockState state) {
      int i = 0;
      switch ((EnumFacing)state.func_177229_b(BlockProperties.FACING_ALL)) {
         case EAST:
            i |= 1;
            break;
         case WEST:
            i |= 2;
            break;
         case SOUTH:
            i |= 3;
            break;
         case NORTH:
            i |= 4;
            break;
         case DOWN:
            i |= 5;
      }

      return i;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return worldIn.func_180495_p(pos.func_177972_a(face)).func_177230_c() instanceof BlockFence ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
