package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.attack.enums.HealType;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class ImmuneEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String[] CODES = new String[]{"SELF_IMMUNE", "OPP_IMMUNE"};
   private boolean onMe;
   private HealType immuneType;

   public ImmuneEffect() {
      super(CODES);
   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      if (this.onMe) {
         if (this.immuneType == HealType.All || this.immuneType == HealType.Damage) {
            me.getActiveCard().getStatus().setDamageImmune(true);
         }

         if (this.immuneType == HealType.All || this.immuneType == HealType.Conditions) {
            me.getActiveCard().getStatus().setConditionImmune(true);
         }
      } else {
         if (this.immuneType == HealType.All || this.immuneType == HealType.Damage) {
            opp.getActiveCard().getStatus().setDamageImmune(true);
         }

         if (this.immuneType == HealType.All || this.immuneType == HealType.Conditions) {
            opp.getActiveCard().getStatus().setConditionImmune(true);
         }
      }

   }

   public BaseAttackEffect parse(String... args) {
      String[] types = args[0].split("_");
      this.immuneType = HealType.getFromDbString(args[1]);
      this.onMe = types[0].equalsIgnoreCase("SELF");
      return super.parse(args);
   }
}
