package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class Battery extends AbilityBase {
   public int[] modifyPowerAndAccuracyTeammate(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{a.getAttackCategory() == AttackCategory.SPECIAL ? (int)((float)power * 1.3F) : power, accuracy};
   }
}
