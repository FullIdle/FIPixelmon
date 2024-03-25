package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Priority extends AttackModifierBase {
   public int priority = 0;

   public Priority(Value... values) {
      this.priority = values[0].value;
   }

   public int modifyPriority(int priority, AttackBase attack, PixelmonWrapper pw) {
      return this.priority;
   }
}
