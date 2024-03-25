package com.pixelmonmod.pixelmon.quests.listeners;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerListeners {
   @SubscribeEvent
   public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (event.player instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         if ((pps.getMegaItemsUnlocked().canMega() || pps.getMegaItemsUnlocked().canDynamax()) && pps.getMegaItem() == EnumMegaItem.Disabled) {
            pps.setMegaItem(EnumMegaItem.None, false);
         }

         QuestData data = pps.getQuestData(true);
         Iterator var5 = data.getProgress().iterator();

         while(var5.hasNext()) {
            QuestProgress progress = (QuestProgress)var5.next();
            progress.sendTo(player);
         }
      }

   }

   @SubscribeEvent
   public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) throws InvalidQuestArgsException {
      if (event.player instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         pps.getQuestData(true).receive("DIMENSION");
      }

   }

   @SubscribeEvent
   public void onDefeatTrainer(BeatTrainerEvent event) throws InvalidQuestArgsException {
      PlayerPartyStorage pps = Pixelmon.storageManager.getParty(event.player);
      pps.getQuestData(true).receive("TRAINER_DEFEAT", event.trainer);
   }

   @SubscribeEvent
   public void onLoseToTrainer(LostToTrainerEvent event) throws InvalidQuestArgsException {
      PlayerPartyStorage pps = Pixelmon.storageManager.getParty(event.player);
      pps.getQuestData(true).receive("TRAINER_DEFEATED_BY", event.trainer);
   }

   @SubscribeEvent
   public void onPlayerTrade(PixelmonTradeEvent event) throws InvalidQuestArgsException {
      if (event.player1 instanceof EntityPlayerMP) {
         EntityPlayerMP p1 = (EntityPlayerMP)event.player1;
         EntityPlayerMP p2 = (EntityPlayerMP)event.player2;
         PlayerPartyStorage pps1 = Pixelmon.storageManager.getParty(p1);
         PlayerPartyStorage pps2 = Pixelmon.storageManager.getParty(p2);
         pps1.getQuestData(false).receive("POKEMON_TRADE_GIVE", event.pokemon1);
         pps1.getQuestData(true).receive("POKEMON_TRADE_GET", event.pokemon2);
         pps2.getQuestData(false).receive("POKEMON_TRADE_GET", event.pokemon2);
         pps2.getQuestData(true).receive("POKEMON_TRADE_GIVE", event.pokemon1);
      }

   }
}
