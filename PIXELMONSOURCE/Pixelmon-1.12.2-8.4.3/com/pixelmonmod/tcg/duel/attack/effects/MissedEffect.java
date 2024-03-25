package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class MissedEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String[] CODES = new String[]{"SELF_MISSED", "SELF_MISS"};

   public MissedEffect() {
      super(CODES);
   }

   public MissedEffect(CoinSide coinSide) {
      this();
      this.setRequiredCoinSide(coinSide);
   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      attack.setMissed(true);
   }
}
