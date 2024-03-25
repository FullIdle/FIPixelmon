package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import java.util.ArrayList;
import java.util.Iterator;

public class BrickBreak extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.removeTeamStatus(StatusType.LightScreen)) {
         user.bc.sendToAll("pixelmon.status.lightscreenoff", target.getNickname());
      }

      if (target.removeTeamStatus(StatusType.Reflect)) {
         user.bc.sendToAll("pixelmon.status.reflectoff", target.getNickname());
      }

      if (target.removeTeamStatus(StatusType.AuroraVeil)) {
         user.bc.sendToAll("pixelmon.status.auroraveil.woreoff", target.getNickname());
      }

      return AttackResult.proceed;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         if (target.hasStatus(StatusType.LightScreen, StatusType.Reflect, StatusType.AuroraVeil)) {
            userChoice.raiseWeight(50.0F);
         }
      }

   }
}
