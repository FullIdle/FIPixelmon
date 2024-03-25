package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Iterator;

public class SheerForce extends AbilityBase {
   public boolean powerModified = false;

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      this.powerModified = false;
      Iterator var6 = a.getMove().effects.iterator();

      while(var6.hasNext()) {
         EffectBase effect = (EffectBase)var6.next();
         if (effect.isChance()) {
            effect.changeChance(0);
            if (!this.powerModified) {
               power = (int)((double)power * 1.3);
               this.powerModified = true;
            }
         }
      }

      return new int[]{power, accuracy};
   }
}
