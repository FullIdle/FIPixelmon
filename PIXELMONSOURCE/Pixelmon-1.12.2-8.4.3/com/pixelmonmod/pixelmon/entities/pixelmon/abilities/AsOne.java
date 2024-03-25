package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.forms.EnumCalyrex;

public class AsOne extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      newPokemon.bc.sendToAll("pixelmon.abilities.asone", newPokemon.getNickname());
   }

   public void tookDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper pokemon, Attack a) {
      if (pokemon.isFainted() && (!a.getMove().getMakesContact() || !(pokemon.getBattleAbility() instanceof Mummy))) {
         if (user.getFormEnum() == EnumCalyrex.ICERIDER) {
            this.sendActivatedMessage(user);
            user.getBattleStats().modifyStat(1, (StatsType)StatsType.Attack);
         }

         if (user.getFormEnum() == EnumCalyrex.SHADOWRIDER) {
            this.sendActivatedMessage(user);
            user.getBattleStats().modifyStat(1, (StatsType)StatsType.SpecialAttack);
         }
      }

   }
}
