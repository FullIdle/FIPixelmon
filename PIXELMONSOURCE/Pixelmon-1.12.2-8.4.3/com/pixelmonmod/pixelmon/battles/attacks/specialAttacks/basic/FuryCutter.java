package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.FuryCutterStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class FuryCutter extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      FuryCutterStatus furyCutter = (FuryCutterStatus)user.getStatus(StatusType.FuryCutter);
      if (furyCutter == null) {
         user.addStatus(new FuryCutterStatus(user.bc.battleTurn), user);
      } else {
         user.attack.getMove().setBasePower(furyCutter.power);
         if (!user.bc.simulateMode) {
            if (furyCutter.power < 160) {
               furyCutter.power *= 2;
            }

            furyCutter.turnInc = user.bc.battleTurn;
         }
      }

      return AttackResult.proceed;
   }

   public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.removeStatus(StatusType.FuryCutter);
   }
}
