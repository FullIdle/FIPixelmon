package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Gluttony;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.QuickDraw;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Ripen;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;

public class ItemBerryCustap extends ItemBerry {
   public ItemBerryCustap() {
      super(EnumHeldItems.berryCustap, EnumBerry.Custap, "custap_berry");
   }

   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      if (!triggered.isTrue() || !pokemon.getBattleAbility().isAbility(QuickDraw.class)) {
         float lowHealth = pokemon.getBattleAbility() instanceof Gluttony ? 50.0F : 25.0F;
         if (pokemon.getHealthPercent() <= lowHealth && canEatBerry(pokemon)) {
            boolean ripened = pokemon.getBattleAbility().isAbility(Ripen.class);
            priority += ripened ? 0.4F : 0.2F;
            pokemon.consumeItem();
            pokemon.bc.sendToAll("pixelmon.helditems.custapberry", pokemon.getNickname());
            if (ripened) {
               pokemon.bc.sendToAll("pixelmon.abilities.ripen", pokemon.getNickname(), this.getLocalizedName());
            }

            super.eatBerry(pokemon);
         }
      }

      return priority;
   }

   public void eatBerry(PixelmonWrapper user) {
   }
}
