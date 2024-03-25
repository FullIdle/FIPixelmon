package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.state.DelayEffect;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class HardenEffect extends BaseAttackEffect {
   private static final String CODE = "HARDEN";
   private int amount;

   public HardenEffect() {
      super("HARDEN");
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      server.addDelayEffect(new DelayEffect(this, attack, card, server.getTurnCount() + 1));
   }

   public int modifyDamage(int damage, PokemonCardState pokemon, GameServerState server) {
      return server.getPlayer(server.getNextTurn()).getActiveCard() == pokemon && damage <= this.amount ? 0 : damage;
   }

   public BaseAttackEffect parse(String... args) {
      this.amount = Integer.parseInt(args[1]);
      return super.parse(args);
   }
}
