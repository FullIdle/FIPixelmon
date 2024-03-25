package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Focus extends StatusBase {
   public Focus() {
      super(StatusType.Focus);
   }

   public void onDamageReceived(PixelmonWrapper userWrapper, PixelmonWrapper pokemon, Attack a, int damage, DamageTypeEnum damageType) {
      if (damageType == DamageTypeEnum.ATTACK || damageType == DamageTypeEnum.ATTACKFIXED) {
         pokemon.removeStatus((StatusBase)this);
      }

   }
}
