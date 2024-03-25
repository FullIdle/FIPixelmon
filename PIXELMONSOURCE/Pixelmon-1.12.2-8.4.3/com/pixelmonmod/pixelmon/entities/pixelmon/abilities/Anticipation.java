package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;

public class Anticipation extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      ArrayList opponents = newPokemon.getOpponentPokemon();
      Iterator var3 = opponents.iterator();

      while(var3.hasNext()) {
         PixelmonWrapper opponent = (PixelmonWrapper)var3.next();
         Attack[] var5 = opponent.getMoveset().attacks;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Attack a = var5[var7];
            if (a != null) {
               float effectiveness = EnumType.getTotalEffectiveness(newPokemon.type, a.getType(), newPokemon.bc.rules.hasClause("inverse"));
               if (effectiveness >= 2.0F || a.isAttack("Fissure", "Guillotine", "Horn Drill", "Sheer Cold")) {
                  newPokemon.bc.sendToAll("pixelmon.abilities.anticipation", newPokemon.getNickname());
                  return;
               }
            }
         }
      }

   }
}
