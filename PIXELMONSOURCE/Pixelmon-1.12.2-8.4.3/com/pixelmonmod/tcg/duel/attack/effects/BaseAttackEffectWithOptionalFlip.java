package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public abstract class BaseAttackEffectWithOptionalFlip extends BaseAttackEffect {
   protected CoinSide needCoinSide;

   public BaseAttackEffectWithOptionalFlip(String... name) {
      super(name);
   }

   public List flipCoin(List currentResult, PokemonCardState pokemon, GameServerState server) {
      if (this.needCoinSide != null && currentResult.size() < 1) {
         currentResult.add(CoinSide.getRandom());
         return currentResult;
      } else {
         return currentResult;
      }
   }

   public final void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (this.isCorrectCoinSide(server)) {
         this.applyOnCorrectCoinSideBeforeDamage(parameters, attack, card, server);
      }

   }

   public void applyAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server, int finalDamage) {
      if (this.isCorrectCoinSide(server)) {
         this.applyOnCorrectCoinSideAfterDamage(parameters, attack, card, server);
      }

   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
   }

   public void applyOnCorrectCoinSideAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
   }

   protected boolean isCorrectCoinSide(GameServerState server) {
      return this.needCoinSide == null || server.getCoinFlip() != null && this.needCoinSide == server.getCoinFlip().getResults().get(0);
   }

   public BaseAttackEffect setRequiredCoinSide(CoinSide coinSide) {
      this.needCoinSide = coinSide;
      return super.setRequiredCoinSide(coinSide);
   }
}
