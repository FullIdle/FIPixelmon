package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sleep;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.EarlyBird;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Hydration;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryStatus;
import java.util.ArrayList;

public class Rest extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      Sleep sleep = new Sleep(2);
      if (!user.hasFullHealth() && !user.hasStatus(StatusType.Sleep, StatusType.HealBlock) && !Sleep.uproarActive(user) && !user.cannotHaveStatus(sleep, user, true)) {
         user.removePrimaryStatus(false);
         user.addStatus(sleep, user);
         user.healEntityBy(user.getMaxHealth());
         user.bc.sendToAll("pixelmon.effect.healthsleep", user.getNickname());
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.raiseWeight(100.0F - pw.getHealthPercent());
      if (pw.hasPrimaryStatus(false)) {
         userChoice.raiseWeight(30.0F);
      }

      AbilityBase ability = pw.getBattleAbility();
      ItemHeld heldItem = pw.getUsableHeldItem();
      if (!pw.getMoveset().hasAttack("Sleep Talk", "Snore") && !(ability instanceof EarlyBird) && (!(ability instanceof Hydration) || !pw.bc.globalStatusController.hasStatus(StatusType.Rainy))) {
         if (heldItem instanceof ItemBerryStatus) {
            ItemBerryStatus berry = (ItemBerryStatus)heldItem;
            if (berry.canHealStatus(StatusType.Sleep)) {
               return;
            }
         }

         if (MoveChoice.canKOFromFull(bestOpponentChoices, pw, 3)) {
            userChoice.lowerTier(1);
         }

         userChoice.weight /= 2.0F;
      }

   }
}
