package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class BideStatus extends StatusBase {
   public transient float damageTaken;
   public transient PixelmonWrapper lastAttacker;

   public BideStatus() {
      super(StatusType.Bide);
   }

   public void onDamageReceived(PixelmonWrapper userWrapper, PixelmonWrapper pokemon, Attack a, int damage, DamageTypeEnum damageType) {
      if (!userWrapper.bc.simulateMode && damageType == DamageTypeEnum.ATTACK || damageType == DamageTypeEnum.ATTACKFIXED) {
         this.damageTaken += (float)damage;
         this.lastAttacker = userWrapper;
      }

   }
}
