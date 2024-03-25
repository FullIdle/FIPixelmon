package com.pixelmonmod.pixelmon.blocks.ranch;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.IBlockHasOwner;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientChangeOpenPC;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.EnumRanchClientPacketMode;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.RanchBlockClientPacket;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.items.EnumCheatItemType;
import com.pixelmonmod.pixelmon.items.ItemIsisHourglass;
import com.pixelmonmod.pixelmon.items.ItemRanchUpgrade;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRanchBlock extends MultiBlock implements IBlockHasOwner {
   private AxisAlignedBB FLOWER = new AxisAlignedBB(0.3, 0.0, 0.3, 0.7, 0.7, 0.7);
   private AxisAlignedBB EMPTY = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
   private AxisAlignedBB FULL_TOP = new AxisAlignedBB(0.0, -1.0, 0.0, 1.0, 1.0, 1.0);
   private AxisAlignedBB FULL_BOTTOM = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);

   public BlockRanchBlock() {
      super(Material.field_151576_e, 1, 2.0, 1);
      this.func_149711_c(2.5F);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149663_c("ranchblock");
      this.setHarvestLevel("pickaxe", 1);
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

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      BlockPos base = this.findBaseBlock(source, new BlockPos.MutableBlockPos(pos), state);
      TileEntityRanchBlock ranchblock = (TileEntityRanchBlock)BlockHelper.getTileEntity(TileEntityRanchBlock.class, source, base);
      if (ranchblock != null) {
         EnumMultiPos p = (EnumMultiPos)state.func_177229_b(MULTIPOS);
         if (ranchblock.percentAbove == 0) {
            return p == EnumMultiPos.TOP ? this.EMPTY : this.FLOWER;
         } else if (ranchblock.percentAbove == 100) {
            return p == EnumMultiPos.TOP ? this.FULL_TOP : this.FULL_BOTTOM;
         } else {
            return p == EnumMultiPos.TOP ? this.FULL_TOP : this.FULL_BOTTOM;
         }
      } else {
         return super.func_185496_a(state, source, pos);
      }
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (player instanceof EntityPlayerMP && hand == EnumHand.MAIN_HAND) {
         if (!PixelmonConfig.allowBreeding) {
            ChatHandler.sendChat(player, "pixelmon.general.disabledblock");
            return false;
         }

         BlockPos loc = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityRanchBlock ranch = (TileEntityRanchBlock)BlockHelper.getTileEntity(TileEntityRanchBlock.class, world, loc);
         if (ranch == null) {
            return true;
         }

         ItemStack heldItem = player.func_184586_b(hand);
         Item playerItem = null;
         if (!heldItem.func_190926_b()) {
            playerItem = heldItem.func_77973_b();
         }

         if (PixelmonConfig.allowRanchExpansion && !heldItem.func_190926_b() && heldItem.func_77973_b() instanceof ItemRanchUpgrade && ranch.getBounds().canExtend()) {
            EntityPlayerMP playerMP = (EntityPlayerMP)player;
            Pixelmon.network.sendTo(new ClientChangeOpenPC(Pixelmon.storageManager.getPCForPlayer(playerMP).uuid), playerMP);
            Pixelmon.network.sendTo(new RanchBlockClientPacket(ranch, EnumRanchClientPacketMode.UpgradeBlock), playerMP);
            OpenScreen.open(player, EnumGuiScreen.ExtendRanch, ranch.func_174877_v().func_177958_n(), ranch.func_174877_v().func_177956_o(), ranch.func_174877_v().func_177952_p());
         } else {
            if (!(playerItem instanceof ItemIsisHourglass) || ((ItemIsisHourglass)playerItem).type != EnumCheatItemType.Gold) {
               if (player.func_110124_au().equals(ranch.getOwnerUUID())) {
                  ((EntityPlayerMP)player).field_71135_a.func_147359_a(ranch.func_189518_D_());
                  ranch.onActivate((EntityPlayerMP)player);
                  return true;
               }

               ChatHandler.sendChat(player, "pixelmon.general.needowner");
               return false;
            }

            if (ranch.hasEgg()) {
               ChatHandler.sendChat(player, "pixelmon.ranch.hourglass.alreadyhasegg");
               return false;
            }

            if (ranch.canBreed() && ranch.applyHourglass()) {
               player.func_145747_a(new TextComponentTranslation("ranch.hourglass.upgradedall", new Object[0]));
               if (!player.field_71075_bZ.field_75098_d) {
                  player.field_71071_by.func_174925_a(playerItem, heldItem.func_77960_j(), 1, heldItem.func_77978_p());
               }
            }
         }
      }

      return true;
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityRanchBlock());
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return Item.func_150898_a(this);
   }

   public float func_180647_a(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
      BlockPos loc = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityRanchBlock ranchblock = (TileEntityRanchBlock)BlockHelper.getTileEntity(TileEntityRanchBlock.class, world, loc);
      return (ranchblock == null || !player.func_110124_au().equals(ranchblock.getOwnerUUID())) && !player.field_71075_bZ.field_75098_d ? -1.0F : super.func_180647_a(state, player, world, pos);
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      BlockPos loc = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));
      TileEntityRanchBlock ranchblock = (TileEntityRanchBlock)BlockHelper.getTileEntity(TileEntityRanchBlock.class, world, loc);
      if (ranchblock != null) {
         ranchblock.onDestroy();
      }

      return super.removedByPlayer(state, world, pos, player, willHarvest);
   }

   public void setOwner(BlockPos pos, EntityPlayer playerIn) {
      if (!playerIn.field_70170_p.field_72995_K) {
         TileEntityRanchBlock ranchblock = (TileEntityRanchBlock)BlockHelper.getTileEntity(TileEntityRanchBlock.class, playerIn.field_70170_p, pos);
         if (ranchblock != null) {
            ranchblock.setOwner((EntityPlayerMP)playerIn);
            ranchblock.setInitBounds();
         }
      }

   }

   public int func_149738_a(World world) {
      return 2;
   }

   public int func_180656_a(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      TileEntityRanchBlock ranchblock = (TileEntityRanchBlock)BlockHelper.getTileEntity(TileEntityRanchBlock.class, blockAccess, pos);
      return ranchblock != null && ranchblock.hasEgg() ? 15 : 0;
   }

   public int func_176211_b(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return 0;
   }

   public boolean func_149744_f(IBlockState state) {
      return true;
   }
}
