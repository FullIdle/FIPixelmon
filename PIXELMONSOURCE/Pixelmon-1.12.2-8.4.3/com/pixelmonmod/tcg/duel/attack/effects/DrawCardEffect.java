package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class DrawCardEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String[] CODES = new String[]{"SELF_DRAW", "OPP_DRAW"};
   private boolean onMe;
   private int count;

   public DrawCardEffect() {
      super(CODES);
   }

   public void applyOnCorrectCoinSideAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      if (this.onMe) {
         me.drawCards(this.count, server);
      } else {
         opp.drawCards(this.count, server);
      }

   }

   public BaseAttackEffect parse(String... args) {
      String[] types = args[0].split("_");
      this.onMe = types[0].equalsIgnoreCase("SELF");
      this.count = Integer.parseInt(args[1]);
      return super.parse(args);
   }
}
