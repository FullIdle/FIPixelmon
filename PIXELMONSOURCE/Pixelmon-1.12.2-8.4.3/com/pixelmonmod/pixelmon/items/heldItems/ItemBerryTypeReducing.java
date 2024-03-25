package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Ripen;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemBerryTypeReducing extends ItemBerry {
   public EnumType typeReduced;

   public ItemBerryTypeReducing(String itemName, EnumBerry berry, EnumType typeReduced) {
      super(EnumHeldItems.berryTypeReducing, berry, itemName);
      this.typeReduced = typeReduced;
   }

   public double preProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      if (canEatBerry(target) && attack.getType() == this.typeReduced && (attack.getTypeEffectiveness(attacker, target) >= 2.0 || this.typeReduced == EnumType.Normal)) {
         boolean ripened = target.getBattleAbility().isAbility(Ripen.class);
         damage /= (double)(ripened ? 4 : 2);
         target.bc.sendToAll("pixelmon.helditems.berrytypereducing", target.getNickname(), target.getHeldItem().getLocalizedName());
         if (ripened) {
            target.bc.sendToAll("pixelmon.abilities.ripen", target.getNickname(), target.getHeldItem().getLocalizedName());
         }

         super.eatBerry(target);
         target.consumeItem();
      }

      return damage;
   }

   public void eatBerry(PixelmonWrapper user) {
   }
}
