package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.PoisonBadly;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemToxicOrb extends ItemHeld {
   public ItemToxicOrb() {
      super(EnumHeldItems.toxicOrb, "toxic_orb");
   }

   public void applyRepeatedEffectAfterStatus(PixelmonWrapper pokemon) {
      if (PoisonBadly.poisonBadly(pokemon, pokemon, (Attack)null, false)) {
         pokemon.bc.sendToAll("pixelmon.helditems.toxicorb", pokemon.getNickname());
      }

   }
}
