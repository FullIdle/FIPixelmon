package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;

public class Conversion2 extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.lastAttack == null) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         ArrayList ableTypes = this.getPossibleTypes(user, target);
         if (!ableTypes.isEmpty()) {
            EnumType chosenType = (EnumType)RandomHelper.getRandomElementFromList(ableTypes);
            user.setTempType(chosenType);
            user.bc.sendToAll("pixelmon.effect.changetype", user.getNickname(), chosenType.getLocalizedName());
            return AttackResult.succeeded;
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }
      }
   }

   private ArrayList getPossibleTypes(PixelmonWrapper user, PixelmonWrapper target) {
      ArrayList ableTypes = new ArrayList();
      EnumType[] var4 = EnumType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EnumType type = var4[var6];
         if (EnumType.getEffectiveness(target.lastAttack.getType(), type) < 1.0F) {
            ableTypes.add(type);
         }
      }

      if (user.type.size() == 1) {
         ableTypes.remove(user.type.get(0));
      }

      return ableTypes;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         pw.getBattleAI().weightTypeChange(pw, userChoice, this.getPossibleTypes(pw, target), bestUserChoices, bestOpponentChoices);
      }

   }
}
