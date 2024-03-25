package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.EvolutionStage;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;

public class MegaEvolution extends BattleScreen {
   private static EntityPixelmon evolvingPokemon;
   int ticks = 0;
   int fadeCount = 0;
   int stage = 0;

   public MegaEvolution(GuiBattle parent) {
      super(parent, BattleMode.MegaEvolution);
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
   }

   public void click(int width, int height, int mouseX, int mouseY) {
   }

   public static void selectEntity() {
      evolvingPokemon = ClientProxy.battleManager.getEntity(ClientProxy.battleManager.ultraBurst != null ? ClientProxy.battleManager.ultraBurst : ClientProxy.battleManager.megaEvolution);
      ClientProxy.battleManager.ultraBurst = null;
   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.bm.battleEnded || evolvingPokemon == null || evolvingPokemon.evoStage == null || evolvingPokemon.evoStage == EvolutionStage.End) {
         this.parent.selectScreenImmediate(BattleMode.ChooseAttack);
         this.bm.mode = BattleMode.Waiting;
      }

   }
}
