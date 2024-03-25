package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AuraBreak;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.Iterator;

public class AuraStatus extends GlobalStatusBase {
   private EnumType boostType;

   public AuraStatus(EnumType boostType, StatusType auraStatus) {
      super(auraStatus);
      this.boostType = boostType;
   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getType() == this.boostType) {
         float multiplier = 1.333F;
         Iterator var7 = user.bc.getActiveUnfaintedPokemon().iterator();

         while(var7.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var7.next();
            if (pw.getBattleAbility() instanceof AuraBreak) {
               multiplier = 0.666F;
               break;
            }
         }

         power = (int)((float)power * multiplier);
      }

      return new int[]{power, accuracy};
   }
}
