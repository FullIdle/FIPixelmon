package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class IgnoreStatsEffect extends BaseAttackEffect {
   private static final String CODE = "IGNORESTATS";
   private PokemonCardState applyOn;
   private Energy weakness;
   private Energy resistance;

   public IgnoreStatsEffect() {
      super("IGNORESTATS");
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      this.applyOn = server.getPlayer(server.getNextTurn()).getActiveCard();
      this.weakness = this.applyOn.getWeakness();
      this.resistance = this.applyOn.getResistance();
      this.applyOn.setWeakness((Energy)null);
      this.applyOn.setResistance((Energy)null);
   }

   public void applyAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server, int finalDamage) {
      this.applyOn.setWeakness(this.weakness);
      this.applyOn.setResistance(this.resistance);
   }
}
