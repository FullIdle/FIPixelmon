package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Multitype;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;

public class Soak extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.isSingleType(EnumType.Water) && !(target.getBattleAbility() instanceof Multitype)) {
         user.bc.sendToAll("pixelmon.effect.changetype", target.getNickname(), EnumType.Water.getLocalizedName());
         target.setTempType(EnumType.Water);
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         pw.getBattleAI().weightTypeChange(pw, userChoice, EnumType.Water.makeTypeList(), target, bestUserChoices, bestOpponentChoices);
      }

   }
}
