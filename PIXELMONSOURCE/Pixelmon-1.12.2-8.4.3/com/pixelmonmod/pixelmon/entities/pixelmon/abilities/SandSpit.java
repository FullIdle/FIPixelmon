package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sandstorm;

public class SandSpit extends AbilityBase {
   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (!(target.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Sandstorm) && target.bc.globalStatusController.canWeatherChange(new Sandstorm())) {
         Sandstorm sandstorm = new Sandstorm();
         sandstorm.setStartTurns(target);
         target.bc.globalStatusController.addGlobalStatus(sandstorm);
         target.bc.sendToAll("pixelmon.abilities.sandspit", target.getNickname());
      }

   }
}
