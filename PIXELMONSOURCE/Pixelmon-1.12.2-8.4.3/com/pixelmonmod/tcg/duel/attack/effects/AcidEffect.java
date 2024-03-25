package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.DelayEffect;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class AcidEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String CODE = "ACID";

   public AcidEffect() {
      super("ACID");
   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      server.addDelayEffect(new DelayEffect(this, attack, card, server.getTurnCount() + 1));
   }

   public void modifyTurn(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      player.getAvailableActions().setCanRetreatActive(false);
   }

   public BaseAttackEffect parse(String... args) {
      this.setRequiredCoinSide(CoinSide.Head);
      return super.parse(args);
   }
}
