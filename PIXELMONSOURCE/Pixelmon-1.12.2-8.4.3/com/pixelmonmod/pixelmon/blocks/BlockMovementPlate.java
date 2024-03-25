package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.EntityPokestop;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMovementPlate extends GenericRotatableBlock {
   private static final int MAX_LOOP_LENGTH = 100;
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);

   public BlockMovementPlate() {
      super(Material.field_151573_f);
      this.func_149711_c(2.5F);
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   public Boolean isLoop(World world, BlockPos posOriginal, int maxLength) {
      BlockPos posCurrent = new BlockPos(posOriginal);

      for(int iteration = 1; iteration < maxLength; ++iteration) {
         switch ((EnumFacing)world.func_180495_p(posCurrent).func_177229_b(BlockProperties.FACING)) {
            case NORTH:
               posCurrent = posCurrent.func_177978_c();
               break;
            case SOUTH:
               posCurrent = posCurrent.func_177968_d();
               break;
            case EAST:
               posCurrent = posCurrent.func_177974_f();
               break;
            case WEST:
               posCurrent = posCurrent.func_177976_e();
               break;
            default:
               return false;
         }

         if (!(world.func_180495_p(posCurrent).func_177230_c() instanceof BlockMovementPlate)) {
            return false;
         }

         if (posCurrent.equals(posOriginal)) {
            return true;
         }
      }

      return false;
   }

   public void func_180634_a(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
      EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
      double speedModifier = 1.0;
      if (this.targetSticky(worldIn, pos)) {
         speedModifier = 0.4;
      }

      if (!(entity instanceof EntityDen) && !(entity instanceof EntityPokestop)) {
         double distanceFromCenterX = entity.field_70165_t - (double)pos.func_177958_n() - 0.5;
         double distanceFromCenterZ = entity.field_70161_v - (double)pos.func_177952_p() - 0.5;
         double pushToMiddle = 0.2;
         if (facing != EnumFacing.NORTH && facing != EnumFacing.SOUTH) {
            if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
               entity.field_70159_w = (double)facing.func_82601_c() * speedModifier;
               if (distanceFromCenterZ > 0.1) {
                  if (entity.field_70179_y > -0.2) {
                     entity.field_70179_y = -1.0 * pushToMiddle;
                  }
               } else if (distanceFromCenterZ < -0.1) {
                  if (entity.field_70179_y < 0.2) {
                     entity.field_70179_y = pushToMiddle;
                  }
               } else {
                  entity.field_70179_y = 0.0;
               }
            }
         } else {
            entity.field_70179_y = (double)facing.func_82599_e() * speedModifier;
            if (distanceFromCenterX > 0.1) {
               if (entity.field_70159_w > -0.2) {
                  entity.field_70159_w = -1.0 * pushToMiddle;
               }
            } else if (distanceFromCenterX < -0.1) {
               if (entity.field_70159_w < 0.2) {
                  entity.field_70159_w = pushToMiddle;
               }
            } else {
               entity.field_70159_w = 0.0;
            }
         }

         entity.field_70181_x = (double)facing.func_96559_d() * speedModifier;
      }
   }

   public boolean targetSticky(World world, BlockPos pos) {
      BlockPos targetPos = null;
      switch ((EnumFacing)world.func_180495_p(pos).func_177229_b(BlockProperties.FACING)) {
         case NORTH:
            targetPos = pos.func_177978_c();
            break;
         case SOUTH:
            targetPos = pos.func_177968_d();
            break;
         case EAST:
            targetPos = pos.func_177974_f();
            break;
         case WEST:
            targetPos = pos.func_177976_e();
      }

      if (targetPos != null) {
         return world.func_180495_p(targetPos).func_177230_c() instanceof BlockStickPlate;
      } else {
         return false;
      }
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      worldIn.func_180501_a(pos, state.func_177226_a(BlockProperties.FACING, placer.func_174811_aO()), 2);
      if (this.isLoop(worldIn, pos, 100)) {
         worldIn.func_175698_g(pos);
         if (!worldIn.field_72995_K) {
            EntityPlayer player = (EntityPlayer)placer;
            ChatHandler.sendChat(player, "pixelmon.blocks.movepad.loopbuilderror");
         }
      }

   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return AABB;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
