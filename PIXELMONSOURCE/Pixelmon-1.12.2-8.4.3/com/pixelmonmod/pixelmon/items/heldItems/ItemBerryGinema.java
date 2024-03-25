package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemBerryGinema extends ItemBerry {
   public ItemBerryGinema() {
      super(EnumHeldItems.ginemaBerry, EnumBerry.Ginema, "ginema_berry");
   }

   public void onStatModified(PixelmonWrapper affected) {
      if (canEatBerry(affected)) {
         this.eatBerry(affected);
      }

   }

   public void eatBerry(PixelmonWrapper affected) {
      if (ItemWhiteHerb.healStats(affected)) {
         super.eatBerry(affected);
         affected.consumeItem();
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.onStatModified(newPokemon);
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      this.onStatModified(pw);
   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
      if (!ItemWhiteHerb.healStats(userWrapper)) {
         userWrapper.bc.sendToAll("pixelmon.general.noeffect");
      }

      return super.useFromBag(userWrapper, targetWrapper);
   }
}
