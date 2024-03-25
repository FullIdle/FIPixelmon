package com.pixelmonmod.tcg.block;

import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.item.ItemBattleRule;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.battles.TCGGuiClientPacket;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleSpectator;
import java.util.Optional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBattleSpectator extends MultiBlock {
   public BlockBattleSpectator() {
      super(Material.field_151573_f, 1, 2.0, 1);
      this.func_149663_c(getName());
      this.func_149711_c(1.5F);
      this.func_149752_b(10.0F);
      this.setRegistryName("tcg", getName());
      this.func_149647_a(TCG.tabTCG);
   }

   public static String getName() {
      return "battle_spectator";
   }

   public Item getDroppedItem(World world, BlockPos blockPos) {
      return null;
   }

   protected Optional getTileEntity(World world, IBlockState iBlockState) {
      return Optional.of(new TileEntityBattleSpectator());
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K && hand == EnumHand.MAIN_HAND) {
         return false;
      } else if (player == null) {
         return false;
      } else if (!(player.field_71070_bA instanceof ContainerPlayer)) {
         return false;
      } else if (player.func_184614_ca() != ItemStack.field_190927_a && player.func_184614_ca().func_77973_b() instanceof ItemBattleRule) {
         return false;
      } else {
         pos = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityBattleSpectator spectator = (TileEntityBattleSpectator)world.func_175625_s(pos);
         if (spectator != null && spectator.getControllerPosition() != null) {
            TileEntityBattleController bc = (TileEntityBattleController)world.func_175625_s(spectator.getControllerPosition());
            if (bc != null) {
               GameServerState server = bc.getGameServer();
               if (server.isGameInProgress()) {
                  EntityPlayerMP p = (EntityPlayerMP)player;
                  PacketHandler.net.sendTo(new TCGGuiClientPacket(spectator.getControllerPosition(), spectator.getPlayerIndex(), true), p);
                  server.getSpectators().put(p, spectator.getPlayerIndex());
                  return false;
               }

               player.func_146105_b(new TextComponentString(TextFormatting.RED + "The match is not started!"), false);
            }
         }

         return false;
      }
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
      return false;
   }

   public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
      return false;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return null;
   }
}
