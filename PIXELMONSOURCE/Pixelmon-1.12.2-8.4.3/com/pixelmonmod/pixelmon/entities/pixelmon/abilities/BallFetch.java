package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import net.minecraft.item.ItemStack;

public class BallFetch extends AbilityBase {
   boolean hasFetched = false;

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (!this.hasFetched && pokemon.getHeldItem() == NoItem.noItem && pokemon.pokemon.getHeldItem().func_190926_b() && pokemon.getParticipant().lastFailedCapture != null) {
         pokemon.setHeldItem(new ItemStack(pokemon.getParticipant().lastFailedCapture.getItem()));
         this.hasFetched = true;
      }

   }

   public void applyEndOfBattleEffect(PixelmonWrapper pokemon) {
      this.hasFetched = false;
   }

   public boolean needNewInstance() {
      return true;
   }
}
