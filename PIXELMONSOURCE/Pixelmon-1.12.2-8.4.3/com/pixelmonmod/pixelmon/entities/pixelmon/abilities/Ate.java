package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public abstract class Ate extends AbilityBase {
   protected EnumType changeType;

   public Ate(EnumType changeType) {
      this.changeType = changeType;
   }

   public EnumType modifyType(PixelmonWrapper pw, Attack a) {
      return !a.isAttack("Hidden Power", "Judgment", "Multi-Attack", "Natural Gift", "Revelation Dance", "Struggle", "Weather Ball") && a.getType() == EnumType.Normal ? this.changeType : null;
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      int newPower = power;
      if (!a.isAttack("Hidden Power", "Judgment", "Multi-Attack", "Natural Gift", "Revelation Dance", "Struggle", "Weather Ball") && a.getActualType() == EnumType.Normal) {
         newPower = (int)((double)power * 1.2);
      }

      return new int[]{newPower, accuracy};
   }
}
