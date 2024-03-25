package com.pixelmonmod.pixelmon.quests.listeners;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class BlockListeners {
   @SubscribeEvent
   public void onBlockUse(PlayerInteractEvent.RightClickBlock event) throws InvalidQuestArgsException {
      if (event.getSide() == Side.SERVER && event.getHand() == EnumHand.MAIN_HAND && !(event.getEntityPlayer() instanceof FakePlayer)) {
         EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         IBlockState state = player.func_71121_q().func_180495_p(event.getPos());
         pps.getQuestData(false).receive("BLOCK_USE", new ItemStack(state.func_177230_c()), state.func_177230_c());
      }

   }

   @SubscribeEvent
   public void onBlockBreak(BlockEvent.BreakEvent event) throws InvalidQuestArgsException {
      if (event.getPlayer() instanceof EntityPlayerMP && !(event.getPlayer() instanceof FakePlayer)) {
         EntityPlayerMP player = (EntityPlayerMP)event.getPlayer();
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         IBlockState state = event.getState();
         ItemStack stack = new ItemStack(state.func_177230_c());
         if (stack.func_190926_b()) {
            if (state.func_177230_c() instanceof BlockRedstoneOre) {
               stack = new ItemStack(Blocks.field_150450_ax);
            } else {
               stack = new ItemStack(state.func_177230_c().func_176223_P().func_177230_c());
            }
         }

         pps.getQuestData(false).receive("BLOCK_BREAK", stack, state.func_177230_c());
      }

   }

   @SubscribeEvent
   public void onBlockPlace(BlockEvent.EntityPlaceEvent event) throws InvalidQuestArgsException {
      if (event.getEntity() instanceof EntityPlayerMP && !(event.getEntity() instanceof FakePlayer)) {
         EntityPlayerMP player = (EntityPlayerMP)event.getEntity();
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         IBlockState state = event.getState();
         ItemStack stack = new ItemStack(state.func_177230_c());
         if (stack.func_190926_b()) {
            if (state.func_177230_c() instanceof BlockRedstoneOre) {
               stack = new ItemStack(Blocks.field_150450_ax);
            } else {
               stack = new ItemStack(state.func_177230_c().func_176223_P().func_177230_c());
            }
         }

         pps.getQuestData(false).receive("BLOCK_PLACE", stack, state.func_177230_c());
      }

   }
}
