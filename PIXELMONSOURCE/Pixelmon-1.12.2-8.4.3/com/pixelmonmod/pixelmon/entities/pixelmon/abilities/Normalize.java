package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Normalize extends Ate {
   public Normalize() {
      super(EnumType.Normal);
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (!a.isAttack("Hidden Power", "Judgment", "Multi-Attack", "Natural Gift", "Revelation Dance", "Struggle", "Techno Blast", "Weather Ball")) {
         a.overrideType(this.changeType);
         power = (int)((double)power * 1.2);
      }

      return new int[]{power, accuracy};
   }
}
