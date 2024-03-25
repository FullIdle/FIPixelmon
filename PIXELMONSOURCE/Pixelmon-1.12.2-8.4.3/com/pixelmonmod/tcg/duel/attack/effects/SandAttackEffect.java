package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class SandAttackEffect extends BaseAttackEffect {
   private static final String CODE = "SANDATTACK";

   public SandAttackEffect() {
      super("SANDATTACK");
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PokemonCardState oppActive = server.getPlayer(server.getNextTurn()).getActiveCard();
      PokemonAttackStatus[] var6 = oppActive.getAttacksStatus();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         PokemonAttackStatus attackStatus = var6[var8];
         attackStatus.setTemporaryEffect(new MissedEffect(CoinSide.Tail), "attack.sandattack.effect.text");
      }

   }
}
