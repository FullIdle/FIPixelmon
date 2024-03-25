package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class ConditionEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String[] CODES = new String[]{"SELF_CONDITION", "OPP_CONDITION"};
   private CardCondition cardCondition;
   private boolean onMe;
   private Integer modifier;

   public ConditionEffect() {
      super(CODES);
   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      if (!this.onMe) {
         opp.getActiveCard().addCondition(card, this.cardCondition, this.modifier, server);
      }

   }

   public void applyOnCorrectCoinSideAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      if (this.onMe) {
         me.getActiveCard().addCondition(card, this.cardCondition, this.modifier, server);
      }

   }

   public BaseAttackEffect parse(String... args) {
      String[] types = args[0].split("_");
      if (args.length > 2) {
         this.modifier = Integer.parseInt(args[2]);
      }

      this.onMe = types[0].equalsIgnoreCase("SELF");
      this.cardCondition = CardCondition.getFromDbString(args[1]);
      return super.parse(args);
   }
}
