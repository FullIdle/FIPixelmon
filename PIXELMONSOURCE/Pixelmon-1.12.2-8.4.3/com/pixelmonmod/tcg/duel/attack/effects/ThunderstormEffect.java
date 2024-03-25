package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class ThunderstormEffect extends BaseAttackEffect {
   private static final String CODE = "THUNDERSTORM";
   private int benchDamage;
   private int selfDamage;

   public ThunderstormEffect() {
      super("THUNDERSTORM");
   }

   public List flipCoin(List currentResult, PokemonCardState pokemon, GameServerState server) {
      PokemonCardState[] var4 = server.getPlayer(server.getNextTurn()).getBenchCards();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         PokemonCardState benchPokemon = var4[var6];
         if (benchPokemon != null) {
            currentResult.add(CoinSide.getRandom());
         }
      }

      return currentResult;
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PlayerServerState oppPlayer = server.getPlayer(server.getNextTurn());
      int counter = 0;
      int tails = 0;
      PokemonCardState[] var8 = oppPlayer.getBenchCards();
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         PokemonCardState pokemon = var8[var10];
         if (pokemon != null) {
            if (server.getCoinFlip().getResults().get(counter) == CoinSide.Head) {
               pokemon.addDamage(card, this.benchDamage, server);
            } else {
               ++tails;
            }

            ++counter;
         }
      }

      card.addDamage(card, tails * this.selfDamage, server);
   }

   public BaseAttackEffect parse(String... args) {
      this.benchDamage = Integer.parseInt(args[1]);
      this.selfDamage = Integer.parseInt(args[2]);
      return super.parse(args);
   }
}
