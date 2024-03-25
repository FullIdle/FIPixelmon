package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers.AttackModifierBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class GrassyGlide extends AttackModifierBase {
   public int priority = 0;

   public GrassyGlide(Value... values) {
      this.priority = values[0].value;
   }

   public int modifyPriority(int priority, AttackBase attack, PixelmonWrapper pw) {
      return pw.bc.globalStatusController.hasStatus(StatusType.GrassyTerrain) && !pw.isAirborne() ? priority + this.priority : super.modifyPriority(priority, attack, pw);
   }
}
