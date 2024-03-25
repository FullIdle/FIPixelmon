package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.List;

public class BurnUp extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasType(EnumType.Fire)) {
         List types = new ArrayList(user.type);
         types.remove(EnumType.Fire);
         if (types.isEmpty()) {
            types.add(EnumType.Mystery);
         }

         user.setTempType((List)types);
         if (user.hasStatus(StatusType.Freeze)) {
            user.removeStatus(StatusType.Freeze);
         }

         user.bc.sendToAll("pixelmon.effect.burnup", user.getNickname());
         return AttackResult.proceed;
      } else {
         return AttackResult.failed;
      }
   }
}
