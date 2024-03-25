package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.state.DelayEffect;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class DestinyBondEffect extends BaseAttackEffect {
   private static final String CODE = "DESTINYBOND";

   public DestinyBondEffect() {
      super("DESTINYBOND");
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      server.addDelayEffect(new DelayEffect(this, attack, card, server.getTurnCount() + 1));
   }

   public void applyDelayAfterDamage(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      PokemonCardState attacked = opp.getActiveCard();
      if (pokemon == attacked && attacked.getStatus().getDamage() >= attacked.getHP()) {
         PokemonCardState attacking = player.getActiveCard();
         attacking.getStatus().setDamage(attacking.getHP());
      }

   }
}
