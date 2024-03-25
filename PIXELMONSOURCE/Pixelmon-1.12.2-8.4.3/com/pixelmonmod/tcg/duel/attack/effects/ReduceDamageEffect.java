package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.state.DelayEffect;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class ReduceDamageEffect extends BaseAttackEffect {
   private static final String CODE = "DAMAGE_REDUCE";
   private int amount;
   private boolean benchingRemoves;

   public ReduceDamageEffect() {
      super("DAMAGE_REDUCE");
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      server.addDelayEffect(new DelayEffect(this, attack, card, server.getTurnCount() + 1));
   }

   public int modifyDamage(int damage, PokemonCardState pokemon, GameServerState server) {
      if (server.getPlayer(server.getNextTurn()).getActiveCard() == pokemon) {
         return this.amount == -1 ? Math.max(0, (int)(Math.floor((double)(damage / 2 / 10)) * 10.0)) : Math.max(0, damage - this.amount);
      } else {
         return damage;
      }
   }

   public BaseAttackEffect parse(String... args) {
      this.benchingRemoves = args.length > 2;
      this.amount = args[1].equalsIgnoreCase("HALF") ? -1 : Integer.parseInt(args[1]);
      return super.parse(args);
   }
}
