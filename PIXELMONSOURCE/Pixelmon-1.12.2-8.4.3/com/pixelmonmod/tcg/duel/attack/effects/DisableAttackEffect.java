package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class DisableAttackEffect extends BaseAttackEffect {
   private static final String[] CODES = new String[]{"OPP_DISABLE", "OPP_DISABLED", "SELF_DISABLE", "SELF_DISABLED"};
   private int turnCount;
   private boolean onMe;

   public DisableAttackEffect() {
      super(CODES);
   }

   public void applyAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server, int finalDamage) {
      if (this.onMe) {
         attack.disable(this.turnCount);
      }

   }

   public boolean chooseOppAttack() {
      return !this.onMe;
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      if (this.onMe) {
         return true;
      } else {
         return !parameters.isEmpty();
      }
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (!this.onMe) {
         PokemonAttackStatus chosenAttack = (PokemonAttackStatus)parameters.get(0);
         chosenAttack.disable(this.turnCount);
      }

   }

   public BaseAttackEffect parse(String... args) {
      String[] token = args[0].split("_");
      this.onMe = token[0].equalsIgnoreCase("SELF");
      int count = args[1].equalsIgnoreCase("FOREVER") ? 1000000 : Integer.parseInt(args[1]);
      this.turnCount = count + (this.onMe ? 1 : 0);
      return super.parse(args);
   }
}
