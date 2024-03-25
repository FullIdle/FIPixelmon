package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemMentalHerb extends ItemHeld {
   public ItemMentalHerb() {
      super(EnumHeldItems.mentalHerb, "mental_herb");
   }

   public void onStatusAdded(PixelmonWrapper user, PixelmonWrapper opponent, StatusBase status) {
      if (this.healStatus(user)) {
         user.consumeItem();
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.onStatusAdded(newPokemon, newPokemon, (StatusBase)null);
   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
      if (!this.healStatus(userWrapper)) {
         userWrapper.bc.sendToAll("pixelmon.general.noeffect");
      }

      return super.useFromBag(userWrapper, targetWrapper);
   }

   public boolean healStatus(PixelmonWrapper pokemon) {
      boolean used = pokemon.removeStatuses(false, StatusType.Infatuated, StatusType.Disable, StatusType.Encore, StatusType.Taunt);
      if (used) {
         pokemon.bc.sendToAll("pixelmon.helditems.mentalherb", pokemon.getNickname());
      }

      return used;
   }
}
