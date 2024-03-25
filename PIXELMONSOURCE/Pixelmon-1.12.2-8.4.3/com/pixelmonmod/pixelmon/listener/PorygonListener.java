package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PorygonListener {
   @SubscribeEvent
   public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
      Item item = event.crafting.func_77973_b();
      EntityPlayer player = event.player;
      if (!player.field_70170_p.field_72995_K && (item == Item.func_150898_a(PixelmonBlocks.pc) || item == Item.func_150898_a(PixelmonBlocks.tradeMachine) || item == Item.func_150898_a(PixelmonBlocks.healer)) && RandomHelper.getRandomChance(30)) {
         DropItemHelper.giveItemStack((EntityPlayerMP)player, new ItemStack(PixelmonItems.porygonPieces, 1, RandomHelper.getRandomNumberBetween(1, 4)), false);
      }

   }
}
