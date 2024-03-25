package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class SuperFangEffect extends BaseAttackEffect {
   private static final String CODE = "SUPERFANG";

   public SuperFangEffect() {
      super("SUPERFANG");
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PokemonCardState active = server.getPlayer(server.getNextTurn()).getActiveCard();
      int hpLeft = active.getHP() - active.getStatus().getDamage();
      int damage = (int)Math.ceil((double)hpLeft / 20.0) * 10;
      attack.setDamage(damage);
   }
}
