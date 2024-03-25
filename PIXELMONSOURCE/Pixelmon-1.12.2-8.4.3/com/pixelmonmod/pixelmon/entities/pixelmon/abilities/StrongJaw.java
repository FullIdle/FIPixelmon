package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Set;

public class StrongJaw extends AbilityBase {
   private Set bitingMoves = Sets.newHashSet(new String[]{"Bite", "Crunch", "Fire Fang", "Hyper Fang", "Ice Fang", "Poison Fang", "Psychic Fangs", "Thunder Fang", "Fishious Rend", "Jaw Lock"});

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (this.bitingMoves.contains(a.getMove().getAttackName())) {
         power = (int)((double)power * 1.5);
      }

      return new int[]{power, accuracy};
   }
}
