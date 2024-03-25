package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Arrays;

public class IronFist extends AbilityBase {
   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      String[] punchMoves = new String[]{"Bullet Punch", "Comet Punch", "Dizzy Punch", "Drain Punch", "DynamicPunch", "Fire Punch", "Focus Punch", "Hammer Arm", "Ice Punch", "Mach Punch", "Mega Punch", "Meteor Mash", "Shadow Punch", "Sky Uppercut", "Thunder Punch", "Double Iron Bash", "Plasma Fists", "Ice Hammer", "Power-Up Punch"};
      if (Arrays.asList(punchMoves).contains(a.getMove().getAttackName())) {
         power = (int)((double)power * 1.2);
      }

      return new int[]{power, accuracy};
   }
}
