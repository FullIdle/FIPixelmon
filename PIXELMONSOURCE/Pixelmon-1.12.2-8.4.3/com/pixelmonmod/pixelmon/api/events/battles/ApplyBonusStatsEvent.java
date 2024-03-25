package com.pixelmonmod.pixelmon.api.events.battles;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BonusStats;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ApplyBonusStatsEvent extends Event {
   private final BattleControllerBase bc;
   private final Pokemon pokemon;

   public ApplyBonusStatsEvent(BattleControllerBase bc, Pokemon pokemon) {
      this.bc = bc;
      this.pokemon = pokemon;
   }

   public BattleControllerBase getBattleController() {
      return this.bc;
   }

   public Pokemon getPokemon() {
      return this.pokemon;
   }

   public static class Post extends ApplyBonusStatsEvent {
      private final BonusStats bonusStats;

      public Post(BattleControllerBase bc, Pokemon pokemon, BonusStats bonusStats) {
         super(bc, pokemon);
         this.bonusStats = bonusStats;
      }

      public BonusStats getBonusStats() {
         return this.bonusStats;
      }
   }

   @Cancelable
   public static class Pre extends ApplyBonusStatsEvent {
      private BonusStats bonusStats;

      public Pre(BattleControllerBase bc, Pokemon pokemon, BonusStats bonusStats) {
         super(bc, pokemon);
         this.bonusStats = bonusStats;
      }

      public BonusStats getBonusStats() {
         return this.bonusStats;
      }

      public void setBonusStats(BonusStats bonusStats) {
         this.bonusStats = bonusStats;
      }
   }
}
