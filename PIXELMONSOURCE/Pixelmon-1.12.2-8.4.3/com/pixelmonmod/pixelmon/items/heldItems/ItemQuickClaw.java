package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.QuickDraw;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;

public class ItemQuickClaw extends ItemHeld {
   public ItemQuickClaw() {
      super(EnumHeldItems.quickClaw, "quick_claw");
   }

   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      if ((!triggered.isTrue() || !pokemon.getBattleAbility().isAbility(QuickDraw.class)) && RandomHelper.getRandomChance(20)) {
         pokemon.bc.sendToAll("pixelmon.helditems.quickclaw", pokemon.getNickname());
         priority += 0.2F;
      }

      return priority;
   }
}
