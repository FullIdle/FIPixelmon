package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class DisableCardEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String[] CODES = new String[]{"SELF_DISABLEPLAY", "OPP_DISABLEPLAY"};
   private boolean onMe;
   private CardType type;

   public DisableCardEffect() {
      super(CODES);
   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (!this.onMe) {
         PlayerServerState opp = server.getPlayer(server.getNextTurn());
         if (this.type == CardType.TRAINER) {
            opp.setAreTrainersDisabled(true);
         }
      }

   }

   public BaseAttackEffect parse(String... args) {
      String[] types = args[0].split("_");
      this.onMe = types[0].equalsIgnoreCase("SELF");
      this.type = CardType.getCardTypeFromString(args[1]);
      return super.parse(args);
   }
}
