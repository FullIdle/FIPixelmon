package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class SwordsDanceEffect extends BaseAttackEffect {
   private static final String CODE = "SWORDSDANCE";

   public SwordsDanceEffect() {
      super("SWORDSDANCE");
   }

   public void applyAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server, int finalDamage) {
      card.getAttacksStatus()[1].setDamageBonus(30, 2);
   }
}
