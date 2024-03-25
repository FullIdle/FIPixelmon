package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class DestinyBond extends StatusBase {
   public DestinyBond() {
      super(StatusType.DestinyBond);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.lastAttack != null && user.lastAttack.getMove() != null && user.lastAttack.getMove().isAttack("Destiny Bond") && user.lastAttack.moveResult != null && user.lastAttack.moveResult.result != null && user.lastAttack.moveResult.result.isSuccess()) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         user.addStatus(new DestinyBond(), user);
         user.bc.sendToAll("pixelmon.effect.applydestinybond", user.getNickname());
         user.attack.moveResult.result = AttackResult.succeeded;
      }
   }

   public void onAttackUsed(PixelmonWrapper user, Attack attack) {
      user.removeStatus((StatusBase)this);
   }

   public void onDamageReceived(PixelmonWrapper user, PixelmonWrapper pokemon, Attack a, int damage, DamageTypeEnum damagetype) {
      if (pokemon.isFainted() && (damagetype == DamageTypeEnum.ATTACK || damagetype == DamageTypeEnum.ATTACKFIXED) && !user.isAlly(pokemon)) {
         if (user.isDynamax > 0) {
            user.bc.sendToAll("pixelmon.effect.blockedbydynamax");
            return;
         }

         user.bc.sendToAll("pixelmon.effect.destinybond", pokemon.getNickname());
         user.doBattleDamage(pokemon, (float)user.getHealth(), DamageTypeEnum.STATUS);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (MoveChoice.canOutspeedAnd2HKO(bestOpponentChoices, pw, userChoice.createList())) {
         userChoice.raiseWeight(75.0F);
      }

   }
}
