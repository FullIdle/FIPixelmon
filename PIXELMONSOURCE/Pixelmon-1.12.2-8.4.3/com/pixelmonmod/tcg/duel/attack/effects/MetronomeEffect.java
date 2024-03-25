package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.attack.PendingAttack;
import com.pixelmonmod.tcg.duel.state.CoinFlipState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class MetronomeEffect extends BaseAttackEffect {
   private static final String CODE = "METRONOME";

   public MetronomeEffect() {
      super("METRONOME");
   }

   public boolean chooseOppAttack() {
      return true;
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      return !parameters.isEmpty();
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PokemonAttackStatus attackData = (PokemonAttackStatus)parameters.get(0);
      server.setEffectsParameters((List)null);
      server.setCoinFlip((CoinFlipState)null);
      server.setRevealedCoinFlipResults(0);
      server.setPendingAttack(new PendingAttack(server.getPlayer(server.getCurrentTurn()), card, new PokemonAttackStatus(attackData.getData())));
      server.setCurrentEffectIndex(-2);
   }
}
