package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Arrays;

public class Reckless extends AbilityBase {
   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      String[] recoilMoves = new String[]{"Brave Bird", "Double-Edge", "Flare Blitz", "Head Charge", "Head Smash", "Hi Jump Kick", "Jump Kick", "Submission", "Take Down", "Volt Tackle", "Wild Charge", "Wood Hammer"};
      if (Arrays.asList(recoilMoves).contains(a.getMove().getAttackName())) {
         power = (int)((double)power * 1.2);
      }

      return new int[]{power, accuracy};
   }
}
