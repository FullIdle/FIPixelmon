package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import java.util.List;

public class BenchDamageOppSelectEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String CODE = "BENCHDAMAGEOPPSELECT";
   private int count;
   private int amount;

   public BenchDamageOppSelectEffect() {
      super("BENCHDAMAGEOPPSELECT");
   }

   public CardSelectorState getOpponentSelectorState(GameServerState server) {
      if (this.isCorrectCoinSide(server)) {
         PlayerServerState opp = server.getPlayer(server.getNextTurn());
         return SelectorHelper.generateSelectorForBench(opp, "attack.effect.catpunch.byopp");
      } else {
         return null;
      }
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      if (!this.isCorrectCoinSide(server)) {
         return true;
      } else {
         PokemonCardState[] benchCards = server.getPlayer(server.getNextTurn()).getBenchCards();
         PokemonCardState[] var5 = benchCards;
         int var6 = benchCards.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            PokemonCardState card = var5[var7];
            if (card != null) {
               return parameters.size() == 1;
            }
         }

         return true;
      }
   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (!parameters.isEmpty()) {
         PokemonCardState bench = (PokemonCardState)parameters.get(0);
         bench.addDamage(card, this.amount, server);
      }

   }

   public BaseAttackEffect parse(String... args) {
      this.count = Integer.parseInt(args[1]);
      this.amount = Integer.parseInt(args[2]);
      return super.parse(args);
   }
}
