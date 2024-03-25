package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.attack.enums.HealType;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class HealEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String[] CODES = new String[]{"SELF_HEAL", "OPP_HEAL"};
   private boolean onMe;
   private HealType type;

   public HealEffect() {
      super(CODES);
   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PokemonCardState active;
      if (this.onMe) {
         active = server.getPlayer(server.getCurrentTurn()).getActiveCard();
      } else {
         active = server.getPlayer(server.getNextTurn()).getActiveCard();
      }

      if (this.type == HealType.All || this.type == HealType.Damage) {
         active.getStatus().setDamage(0);
      }

      if (this.type == HealType.All || this.type == HealType.Conditions) {
         active.getStatus().getConditions().clear();
      }

   }

   public BaseAttackEffect parse(String... args) {
      String[] types = args[0].split("_");
      this.onMe = types[0].equalsIgnoreCase("SELF");
      this.type = HealType.getFromDbString(args[1]);
      return super.parse(args);
   }
}
