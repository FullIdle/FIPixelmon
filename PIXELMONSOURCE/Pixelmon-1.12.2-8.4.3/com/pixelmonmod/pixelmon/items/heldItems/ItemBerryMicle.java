package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Gluttony;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Ripen;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemBerryMicle extends ItemBerry {
   public ItemBerryMicle() {
      super(EnumHeldItems.berryMicle, EnumBerry.Micle, "micle_berry");
   }

   public int[] modifyPowerAndAccuracyUser(int[] modifiedMoveStats, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      int lowHealth = user.getBattleAbility() instanceof Gluttony ? 50 : 25;
      if (modifiedMoveStats[1] > 0 && user.getHealthPercent() <= (float)lowHealth && canEatBerry(user)) {
         boolean ripened = user.getBattleAbility().isAbility(Ripen.class);
         user.bc.sendToAll("pixelmon.helditems.micleberry", user.getNickname());
         if (ripened) {
            user.bc.sendToAll("pixelmon.abilities.ripen", user.getNickname(), this.getLocalizedName());
         }

         modifiedMoveStats[1] = (int)((double)modifiedMoveStats[1] * (ripened ? 1.4 : 1.2));
         this.eatBerry(user);
         user.consumeItem();
      }

      return modifiedMoveStats;
   }
}
