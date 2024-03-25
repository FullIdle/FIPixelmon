package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class Grudge extends StatusBase {
   public Grudge() {
      super(StatusType.Grudge);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.status.grudge", user.getNickname());
      user.addStatus(new Grudge(), user);
   }

   public void onAttackUsed(PixelmonWrapper user, Attack attack) {
      user.removeStatus((StatusBase)this);
   }

   public void onDamageReceived(PixelmonWrapper userWrapper, PixelmonWrapper pokemon, Attack a, int damage, DamageTypeEnum damageType) {
      if (pokemon.isFainted() && damageType.isDirect() && a != null && !a.isAttack("Struggle") && userWrapper.selectedAttack != null && userWrapper.selectedAttack.pp > 0) {
         userWrapper.bc.sendToAll("pixelmon.status.grudgeactivate", userWrapper.getNickname(), userWrapper.selectedAttack.getMove().getTranslatedName());
         userWrapper.selectedAttack.pp = 0;
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (pw.getParticipant().countAblePokemon() > 1 && MoveChoice.canOutspeedAnd2HKO(bestOpponentChoices, pw, userChoice.createList())) {
         userChoice.raiseWeight(25.0F);
      }

   }
}
