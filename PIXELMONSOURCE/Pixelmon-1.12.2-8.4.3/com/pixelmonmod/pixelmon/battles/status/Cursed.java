package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;

public class Cursed extends StatusBase {
   public Cursed() {
      super(StatusType.Cursed);
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (!(pw.getBattleAbility() instanceof MagicGuard)) {
         pw.bc.sendToAll("pixelmon.status.curseafflicted", pw.getNickname());
         pw.doBattleDamage(pw, (float)pw.getPercentMaxHealth(25.0F), DamageTypeEnum.STATUS);
      }
   }
}
