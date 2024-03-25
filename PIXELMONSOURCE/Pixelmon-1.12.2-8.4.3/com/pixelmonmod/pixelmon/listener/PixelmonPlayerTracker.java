package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.advancements.PixelmonAdvancements;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.trading.NPCTrades;
import com.pixelmonmod.pixelmon.battles.BattleQuery;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelectionList;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ServerConfigList;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ServerCosmeticsUpdatePacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.UpdateClientRules;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.UpdateClientPlayerData;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.customStarters.SelectPokemonController;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemQueryList;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQueryList;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PixelmonPlayerTracker {
   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (event.player instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         Pixelmon.network.sendTo(new ServerConfigList(), player);
         Pixelmon.network.sendTo(new ServerCosmeticsUpdatePacket(party.getServerCosmetics()), player);
         party.pokedex.checkForCharms();

         for(int i = 0; i < 6; ++i) {
            party.notifyListeners(new StoragePosition(-1, i), party.get(i), EnumUpdateType.CLIENT);
         }

         removePlayer(player);
         if (PixelmonConfig.alwaysHaveMegaRing) {
            try {
               if (!party.getMegaItemsUnlocked().canMega()) {
                  party.setMegaItem(EnumMegaItem.BraceletORAS, false);
                  party.unlockMega();
               }
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }

         if (PixelmonConfig.alwaysHaveDynamaxBand) {
            try {
               if (!party.getMegaItemsUnlocked().canDynamax()) {
                  party.setMegaItem(EnumMegaItem.DynamaxBand, false);
                  party.unlockDynamax();
               }
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }

         ((IPixelmonBankAccount)Pixelmon.moneyManager.getBankAccount(player).get()).updatePlayer();
         Pixelmon.network.sendTo(new UpdateClientPlayerData(party.trainerCardColor), player);
         party.pokedex.update();
         Pixelmon.network.sendTo(new UpdateClientRules(), player);
         PixelmonAdvancements.POKEDEX_TRIGGER.trigger(player);
      }
   }

   @SubscribeEvent
   public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
      EntityPlayer player = event.player;
      if (player instanceof EntityPlayerMP) {
         removePlayer((EntityPlayerMP)player);
      }

   }

   @SubscribeEvent
   public static void onPlayerDeath(LivingDeathEvent event) {
      Entity eventEntity = event.getEntity();
      if (event.getEntity() instanceof EntityPlayerMP) {
         removePlayer((EntityPlayerMP)eventEntity);
      }

   }

   public static void removePlayer(EntityPlayerMP player) {
      try {
         Pixelmon.storageManager.getParty(player).retrieveAll();
         SelectPokemonController.removePlayer(player);
         BattleQuery bq = BattleQuery.getQuery(player);
         if (bq != null) {
            bq.declineQuery(player);
         }

         EvolutionQuery eq = EvolutionQueryList.get(player);
         if (eq != null) {
            EvolutionQueryList.declineQuery(player, eq.pokemonUUID);
         }

         DropItemQueryList.removeQuery(player);
         BattleControllerBase bc = BattleRegistry.getBattle(player);
         if (bc != null) {
            if (bc.hasSpectator(player)) {
               bc.removeSpectator(player);
            } else {
               bc.endBattle(EnumBattleEndCause.FORCE);
            }
         }

         LearnMoveController.clearLearnMoves(player);
         TeamSelectionList.removeSelection(player);
         NPCTrades.clearTradePair(player.func_110124_au());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
