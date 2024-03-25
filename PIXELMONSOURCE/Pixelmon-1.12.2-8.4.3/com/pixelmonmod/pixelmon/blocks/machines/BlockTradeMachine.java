package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.blocks.IBlockHasOwner;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTradeMachine;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTradeMachine extends MultiBlock implements IBlockHasOwner {
   public BlockTradeMachine() {
      super(Material.field_151576_e, 3, 2.0, 1);
      this.func_149711_c(3.5F);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149663_c("trade_machine");
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return PixelmonBlocks.getItemFromBlock(this);
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

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K || hand == EnumHand.OFF_HAND) {
         ItemStack heldItem = player.func_184586_b(hand);
         pos = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityTradeMachine tileEntityTradeMachine;
         if (!heldItem.func_190926_b() && heldItem.func_77973_b() instanceof ItemDye) {
            tileEntityTradeMachine = (TileEntityTradeMachine)BlockHelper.getTileEntity(TileEntityTradeMachine.class, world, pos);
            if (tileEntityTradeMachine != null) {
               if (player.func_110124_au().equals(tileEntityTradeMachine.getOwnerUUID())) {
                  EnumDyeColor dyeColor = EnumDyeColor.func_176766_a(heldItem.func_77952_i());
                  tileEntityTradeMachine.setColour(dyeColor.func_176610_l());
                  if (!player.field_71075_bZ.field_75098_d) {
                     heldItem.func_190918_g(1);
                  }
               } else {
                  ChatHandler.sendChat(player, "pixelmon.blocks.trader.ownership");
               }
            }

            return true;
         }

         try {
            if (!(player.field_71070_bA instanceof ContainerPlayer)) {
               return false;
            }

            tileEntityTradeMachine = (TileEntityTradeMachine)BlockHelper.getTileEntity(TileEntityTradeMachine.class, world, pos);
            if (tileEntityTradeMachine != null) {
               if (tileEntityTradeMachine.playerCount < 2) {
                  tileEntityTradeMachine.registerPlayer((EntityPlayerMP)player);
               } else {
                  ChatHandler.sendChat(player, "pixelmon.blocks.tradefull");
               }
            }
         } catch (Exception var13) {
         }
      }

      return true;
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityTradeMachine());
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return Item.func_150898_a(this);
   }

   public void setOwner(BlockPos pos, EntityPlayer playerIn) {
      UUID playerID = playerIn.func_110124_au();
      TileEntityTradeMachine tileEntityTradeMachine = (TileEntityTradeMachine)BlockHelper.getTileEntity(TileEntityTradeMachine.class, playerIn.func_130014_f_(), pos);
      tileEntityTradeMachine.setOwner(playerID);
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
