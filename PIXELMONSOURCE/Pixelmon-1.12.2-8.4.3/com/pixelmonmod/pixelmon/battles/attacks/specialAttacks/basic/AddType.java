package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AddType extends SpecialAttackBase {
   public EnumType type;

   public AddType(EnumType type) {
      this.type = type;
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasType(this.type)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         List newType = this.getNewType(target);
         if (!user.bc.simulateMode) {
            target.addedType = this.type;
         }

         target.setTempType(newType);
         user.bc.sendToAll("pixelmon.effect.addtype", this.type.getLocalizedName(), target.getNickname());
         return AttackResult.succeeded;
      }
   }

   private List getNewType(PixelmonWrapper target) {
      List newType = new ArrayList();
      newType.addAll(target.type);
      newType.add(this.type);
      if (target.addedType != null && target.addedType != this.type) {
         newType.remove(target.addedType);
      }

      return newType;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         pw.getBattleAI().weightSingleTypeChange(pw, userChoice, this.getNewType(target), target, bestUserChoices, bestOpponentChoices);
      }

   }
}
