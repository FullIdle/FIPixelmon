package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class LeechSeedEffect extends BaseAttackEffect {
   private static final String CODE = "LEECHSEED";
   private Type type;
   private int healAmount;

   public LeechSeedEffect() {
      super("LEECHSEED");
   }

   public void applyAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server, int finalDamage) {
      if (this.type == LeechSeedEffect.Type.FullDamage) {
         card.getStatus().healDamage(finalDamage);
      } else if (this.type == LeechSeedEffect.Type.HalfDamage) {
         card.getStatus().healDamage((int)(Math.ceil((double)finalDamage / 20.0) * 10.0));
      } else if (finalDamage > 0) {
         card.getStatus().healDamage(this.healAmount);
      }

   }

   public BaseAttackEffect parse(String... args) {
      if (args[1].equalsIgnoreCase("DAMAGE")) {
         this.type = LeechSeedEffect.Type.FullDamage;
         this.healAmount = 0;
      } else if (args[1].equalsIgnoreCase("HALF_DAMAGE")) {
         this.type = LeechSeedEffect.Type.HalfDamage;
         this.healAmount = 0;
      } else {
         this.type = LeechSeedEffect.Type.Exact;
         this.healAmount = Integer.parseInt(args[1]);
      }

      return super.parse(args);
   }

   public static enum Type {
      Exact,
      FullDamage,
      HalfDamage;
   }
}
