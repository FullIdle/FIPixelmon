package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Burn;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemFlameOrb extends ItemHeld {
   public ItemFlameOrb() {
      super(EnumHeldItems.flameOrb, "flame_orb");
   }

   public void applyRepeatedEffectAfterStatus(PixelmonWrapper pokemon) {
      if (Burn.burn(pokemon, pokemon, (Attack)null, false)) {
         pokemon.bc.sendToAll("pixelmon.helditems.flameorb", pokemon.getNickname());
      }

   }
}
