package com.pixelmonmod.pixelmon.blocks.multiBlocks;

import com.pixelmonmod.pixelmon.blocks.IBlockHasOwner;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTree;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockTree extends BlockGenericModelMultiblock implements IBlockHasOwner {
   public BlockTree() {
      super(Material.field_151575_d, 1, 2.0, 1);
      this.func_149711_c(0.5F);
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return PixelmonItems.treeItem;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(PixelmonItems.treeItem);
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityTree());
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand == EnumHand.MAIN_HAND) {
         pos = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityTree treeEntity = (TileEntityTree)BlockHelper.getTileEntity(TileEntityTree.class, world, pos);
         if (treeEntity != null && player.func_110124_au().equals(treeEntity.getOwnerUUID())) {
            if (treeEntity.getTreeType() < 4) {
               treeEntity.setTreeType(treeEntity.getTreeType() + 1);
            } else {
               treeEntity.setTreeType(1);
            }

            ((WorldServer)world).func_184164_w().func_180244_a(pos);
            ChatHandler.sendChat(player, "pixelmon.blocks.tree.change" + treeEntity.getTreeType());
         }
      }

      return true;
   }

   public void setOwner(BlockPos pos, EntityPlayer playerIn) {
      UUID playerID = playerIn.func_110124_au();
      TileEntityTree tree = (TileEntityTree)BlockHelper.getTileEntity(TileEntityTree.class, playerIn.field_70170_p, pos);
      tree.setOwner(playerID);
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
