package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class BeakBlastStatus extends StatusBase {
   public BeakBlastStatus() {
      super(StatusType.BeakBlast);
   }

   public void onDamageReceived(PixelmonWrapper user, PixelmonWrapper pokemon, Attack attack, int damage, DamageTypeEnum damageType) {
      if (attack.getMove().getMakesContact() && !user.hasStatus(StatusType.Burn)) {
         user.addStatus(new Burn(), user);
         user.bc.sendToAll("pixelmon.status.burn.added", user.getNickname());
         pokemon.removeStatus(StatusType.BeakBlast);
      }

   }
}
