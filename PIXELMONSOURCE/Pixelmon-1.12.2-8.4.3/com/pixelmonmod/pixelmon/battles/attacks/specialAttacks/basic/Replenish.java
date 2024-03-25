package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import java.util.ArrayList;
import java.util.Iterator;

public class Replenish extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      ArrayList targets = user.bc.getTeamPokemon(user);
      Iterator var4 = targets.iterator();

      while(var4.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var4.next();
         ItemHeld consumedItem = pw.getConsumedItem();
         if (RandomHelper.rand.nextBoolean() && consumedItem != NoItem.noItem && !pw.hasHeldItem() && consumedItem instanceof ItemBerry) {
            user.bc.sendToAll("pixelmon.effect.recycle", pw.getNickname(), consumedItem.getLocalizedName());
            pw.setHeldItem(consumedItem);
            pw.setConsumedItem((ItemHeld)null);
         }
      }

      return AttackResult.proceed;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.raiseWeight(25.0F);
   }
}
