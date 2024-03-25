package com.pixelmonmod.pixelmon.blocks.multiBlocks;

import com.pixelmonmod.pixelmon.blocks.IBlockHasOwner;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityFossilDisplay;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.items.ItemFossil;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockFossilDisplay extends BlockGenericModelMultiblock implements IBlockHasOwner {
   public BlockFossilDisplay() {
      super(Material.field_151575_d, 1, 2.0, 1);
      this.func_149663_c("FossilDisplay");
      this.func_149711_c(2.0F);
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      List drops = super.getDrops(world, pos, state, fortune);
      BlockPos base = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityFossilDisplay fossilDisplayEntity = (TileEntityFossilDisplay)BlockHelper.getTileEntity(TileEntityFossilDisplay.class, world, base);
      Item displayItem = fossilDisplayEntity.getItemInDisplay();
      if (displayItem != null) {
         drops.add(new ItemStack(displayItem));
      }

      drops.add(new ItemStack(PixelmonBlocks.fossilDisplayBlock, 1));
      return drops;
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityFossilDisplay());
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         ItemStack heldItem = player.func_184586_b(hand);
         pos = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityFossilDisplay fossilDisplayEntity = (TileEntityFossilDisplay)BlockHelper.getTileEntity(TileEntityFossilDisplay.class, world, pos);
         if (fossilDisplayEntity != null) {
            if (player.func_110124_au().equals(fossilDisplayEntity.getOwnerUUID())) {
               if (fossilDisplayEntity.isOpen()) {
                  Item item = fossilDisplayEntity.getItemInDisplay();
                  if (item != null) {
                     if (heldItem.func_190926_b() && !player.func_70093_af()) {
                        fossilDisplayEntity.setItemInDisplay((Item)null);
                        ((WorldServer)world).func_184164_w().func_180244_a(pos);
                        if (!player.field_71075_bZ.field_75098_d) {
                           DropItemHelper.giveItemStack((EntityPlayerMP)player, new ItemStack(item), false);
                        }
                     } else {
                        fossilDisplayEntity.closeGlass();
                     }
                  } else {
                     Item playerItem = heldItem.func_190926_b() ? null : heldItem.func_77973_b();
                     if (!heldItem.func_190926_b() && !player.func_70093_af()) {
                        if (playerItem instanceof ItemFossil) {
                           fossilDisplayEntity.setItemInDisplay(playerItem);
                           ((WorldServer)world).func_184164_w().func_180244_a(pos);
                           if (!player.field_71075_bZ.field_75098_d) {
                              heldItem.func_190918_g(1);
                           }

                           return true;
                        }

                        ChatHandler.sendChat(player, "pixelmon.blocks.fossildisplay.placecheck");
                        fossilDisplayEntity.closeGlass();
                     } else {
                        fossilDisplayEntity.closeGlass();
                        ((WorldServer)world).func_184164_w().func_180244_a(pos);
                     }
                  }
               } else {
                  fossilDisplayEntity.openGlass();
               }
            } else {
               ChatHandler.sendChat(player, "pixelmon.blocks.fossildisplay.ownership");
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public void setOwner(BlockPos pos, EntityPlayer playerIn) {
      UUID playerID = playerIn.func_110124_au();
      TileEntityFossilDisplay fossilDisplay = (TileEntityFossilDisplay)BlockHelper.getTileEntity(TileEntityFossilDisplay.class, playerIn.field_70170_p, pos);
      fossilDisplay.setOwner(playerID);
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
