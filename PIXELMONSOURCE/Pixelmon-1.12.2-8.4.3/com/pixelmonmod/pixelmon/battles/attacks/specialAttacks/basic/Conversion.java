package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.List;

public class Conversion extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      List types = this.getPossibleTypes(user);
      EnumType newType = (EnumType)types.get(0);
      if (user.isSingleType(newType)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         user.bc.sendToAll("pixelmon.effect.changetype", user.getNickname(), newType.getLocalizedName());
         user.setTempType(newType);
         return AttackResult.proceed;
      }
   }

   private List getPossibleTypes(PixelmonWrapper user) {
      List typeList = new ArrayList();
      typeList.add(user.getMoveset().get(0).getType());
      return typeList;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      pw.getBattleAI().weightTypeChange(pw, userChoice, this.getPossibleTypes(pw), bestUserChoices, bestOpponentChoices);
   }
}
