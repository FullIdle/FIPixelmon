package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.Iterator;

public class Synchronoise extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      Iterator var3 = user.type.iterator();

      EnumType type;
      do {
         if (!var3.hasNext()) {
            user.bc.sendToAll("pixelmon.battletext.noeffect", target.getNickname());
            return AttackResult.failed;
         }

         type = (EnumType)var3.next();
      } while(type == null || !target.hasType(type));

      return AttackResult.proceed;
   }
}
