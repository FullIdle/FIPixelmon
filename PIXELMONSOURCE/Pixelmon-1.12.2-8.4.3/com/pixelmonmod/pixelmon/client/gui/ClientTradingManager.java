package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientTradingManager {
   public static EntityPlayer tradePartner;
   public static Pokemon tradeTarget;
   public static PixelmonStatsData tradeTargetStats;
   public static PixelmonStatsData selectedStats;
   public static boolean player1Ready = false;
   public static boolean player2Ready = false;
   public static boolean targetPartyIsMoreThanOnePokemon = false;

   public static void findTradePartner(UUID uuid) {
      tradePartner = null;
      if (uuid != null) {
         List playerList = Minecraft.func_71410_x().field_71441_e.field_73010_i;
         playerList.stream().filter((p) -> {
            return p.func_110124_au().equals(uuid);
         }).forEach((p) -> {
            tradePartner = p;
         });
      }

   }

   public static void reset() {
      tradePartner = null;
      tradeTarget = null;
      tradeTargetStats = null;
      selectedStats = null;
      player1Ready = false;
      player2Ready = false;
      targetPartyIsMoreThanOnePokemon = false;
   }
}
