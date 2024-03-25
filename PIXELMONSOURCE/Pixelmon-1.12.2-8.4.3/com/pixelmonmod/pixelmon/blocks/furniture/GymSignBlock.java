package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.IBlockHasOwner;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityGymSign;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.UUID;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class GymSignBlock extends GenericRotatableModelBlock implements IBlockHasOwner {
   public GymSignBlock() {
      super(Material.field_151573_f);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149663_c("GymSign");
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityGymSign();
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      super.func_180633_a(worldIn, pos, state, placer, stack);
      if (placer instanceof EntityPlayer && !worldIn.field_72995_K) {
         this.setOwner(pos, (EntityPlayer)placer);
      }

   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      ItemStack heldItem = player.func_184586_b(hand);
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         TileEntityGymSign gymSign = (TileEntityGymSign)BlockHelper.getTileEntity(TileEntityGymSign.class, world, pos);
         if (gymSign != null) {
            if (player.func_110124_au().equals(gymSign.getOwnerUUID())) {
               Item playerItem = null;
               if (!heldItem.func_190926_b()) {
                  playerItem = heldItem.func_77973_b();
               }

               boolean itemUsed = false;
               if (playerItem instanceof ItemDye) {
                  EnumDyeColor dyeColor = EnumDyeColor.func_176766_a(heldItem.func_77952_i());
                  String currentColour = gymSign.getColour();
                  String dyeName = dyeColor.func_176610_l();
                  if (!currentColour.equals(dyeName)) {
                     gymSign.setColour(dyeName);
                     ((WorldServer)world).func_184164_w().func_180244_a(pos);
                     if (!player.field_71075_bZ.field_75098_d) {
                        heldItem.func_190918_g(1);
                     }

                     itemUsed = true;
                  }
               }

               if (!itemUsed) {
                  ItemStack signStack = gymSign.getItemInSign();
                  ItemStack putting = null;
                  if (!heldItem.func_190926_b() && signStack != null && heldItem.func_77973_b() == signStack.func_77973_b()) {
                     putting = null;
                  }

                  if (!heldItem.func_190926_b()) {
                     putting = heldItem.func_77946_l();
                     putting.func_190920_e(1);
                     if (!player.field_71075_bZ.field_75098_d) {
                        heldItem.func_77979_a(1);
                     }
                  }

                  if (signStack != null && signStack != ItemStack.field_190927_a && !player.field_71075_bZ.field_75098_d) {
                     DropItemHelper.giveItemStack((EntityPlayerMP)player, signStack, false);
                  }

                  gymSign.setItemInSign(putting);
                  ((WorldServer)world).func_184164_w().func_180244_a(pos);
               }
            } else {
               ChatHandler.sendChat(player, "pixelmon.blocks.gymsign.ownership");
            }
         }
      }

      return true;
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      if (!player.field_71075_bZ.field_75098_d) {
         this.func_176206_d(world, pos, world.func_180495_p(pos));
      }

      return super.removedByPlayer(state, world, pos, player, willHarvest);
   }

   public void func_176206_d(World world, BlockPos pos, IBlockState state) {
      if (!world.field_72995_K) {
         TileEntityGymSign gymSign = (TileEntityGymSign)BlockHelper.getTileEntity(TileEntityGymSign.class, world, pos);
         if (gymSign != null && gymSign.getItemInSign() != null && gymSign.isDroppable()) {
            ItemStack stack = gymSign.getItemInSign();
            stack.func_190920_e(1);
            func_180635_a(world, pos, stack);
         }
      }

   }

   public void setOwner(BlockPos pos, EntityPlayer playerIn) {
      UUID playerID = playerIn.func_110124_au();
      TileEntityGymSign gymSign = (TileEntityGymSign)BlockHelper.getTileEntity(TileEntityGymSign.class, playerIn.func_130014_f_(), pos);
      gymSign.setOwner(playerID);
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
