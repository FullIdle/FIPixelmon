package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryRestoreHP;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import java.util.ArrayList;

public class Recycle extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      ItemHeld consumedItem = user.getConsumedItem();
      if (consumedItem != NoItem.noItem && !user.hasHeldItem()) {
         user.bc.sendToAll("pixelmon.effect.recycle", user.getNickname(), consumedItem.getLocalizedName());
         user.setHeldItem(consumedItem);
         user.setConsumedItem((ItemHeld)null);
         if (user.getHeldItem().isBerry()) {
            ItemBerryRestoreHP berry = (ItemBerryRestoreHP)user.getUsableHeldItem();
            berry.tookDamage(target, user, 0.0F, DamageTypeEnum.SELF);
         }

         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.raiseWeight(25.0F);
   }
}
