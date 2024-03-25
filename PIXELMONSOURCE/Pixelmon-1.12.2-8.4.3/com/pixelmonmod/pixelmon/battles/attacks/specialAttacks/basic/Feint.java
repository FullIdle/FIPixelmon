package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Feint extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      boolean showMessage = false;
      showMessage = target.removeStatus(StatusType.Protect);
      if (!user.bc.getTeamPokemon(user.getParticipant()).contains(target)) {
         showMessage = target.removeTeamStatus(StatusType.WideGuard) || showMessage;
         showMessage = target.removeTeamStatus(StatusType.QuickGuard) || showMessage;
         showMessage = target.removeTeamStatus(StatusType.CraftyShield) || showMessage;
      }

      if (showMessage) {
         target.bc.sendToAll("pixelmon.effect.feint", user.getNickname(), target.getNickname());
      }

      return AttackResult.proceed;
   }
}
