package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class DreamEaterEffect extends BaseAttackEffect {
   private static final String CODE = "DREAMEATER";

   public DreamEaterEffect() {
      super("DREAMEATER");
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (!server.getPlayer(server.getNextTurn()).getActiveCard().getStatus().hasCondition(CardCondition.ASLEEP)) {
         attack.setDamage(0);
      }

   }
}
