package com.pixelmonmod.pixelmon.api.trading;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trading.SetClientTradePair;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;

public class NPCTrades {
   public static PokemonSpec UNTRADEABLE;
   private static final HashMap activeTrades = new HashMap();

   public static void updateClientTradeData(EntityPlayerMP player, TradePair tradePair) {
      boolean hasPokemon = false;
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
      Iterator var4 = party.getTeam().iterator();

      while(var4.hasNext()) {
         Pokemon pokemon = (Pokemon)var4.next();
         if (pokemon != null && tradePair.exchange.matches(pokemon) && !UNTRADEABLE.matches(pokemon)) {
            hasPokemon = true;
            party.retrieveAll();
         }
      }

      Pixelmon.network.sendTo(new SetClientTradePair(tradePair, hasPokemon), player);
   }

   public static void showTrade(EntityPlayerMP player, TradePair tradePair) {
      showTrade(player, tradePair, -1);
   }

   public static void showTrade(EntityPlayerMP player, TradePair tradePair, int trainerID) {
      updateClientTradeData(player, tradePair);
      activeTrades.put(player.func_110124_au(), tradePair);
      OpenScreen.open(player, EnumGuiScreen.NPCTraderGui, trainerID);
   }

   @Nullable
   public static TradePair getTradePair(UUID uuid) {
      return (TradePair)activeTrades.get(uuid);
   }

   public static void clearTradePair(UUID uuid) {
      activeTrades.remove(uuid);
   }
}
