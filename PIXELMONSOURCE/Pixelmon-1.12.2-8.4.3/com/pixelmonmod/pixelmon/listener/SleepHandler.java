package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SleepHandler {
   @SubscribeEvent
   public static void playerWakeUp(PlayerWakeUpEvent event) {
      if (PixelmonConfig.bedsHealPokemon && !event.wakeImmediately() && !event.updateWorld() && event.shouldSetSpawn() && event.getEntityPlayer() instanceof EntityPlayerMP) {
         EntityPlayer player = event.getEntityPlayer();
         PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
         party.getTeam().forEach((pokemon) -> {
            pokemon.heal();
         });
      }

   }
}
