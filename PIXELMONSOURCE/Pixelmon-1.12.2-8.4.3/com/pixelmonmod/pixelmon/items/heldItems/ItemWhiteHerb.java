package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemWhiteHerb extends ItemHeld {
   public ItemWhiteHerb() {
      super(EnumHeldItems.whiteHerb, "white_herb");
   }

   public void onStatModified(PixelmonWrapper affected) {
      if (healStats(affected)) {
         affected.consumeItem();
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.onStatModified(newPokemon);
   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
      if (!healStats(userWrapper)) {
         userWrapper.bc.sendToAll("pixelmon.general.noeffect");
      }

      return super.useFromBag(userWrapper, targetWrapper);
   }

   public static boolean healStats(PixelmonWrapper affected) {
      boolean activated = false;
      int[] stages = affected.getBattleStats().getStages();

      for(int i = 0; i < 7; ++i) {
         if (stages[i] < 0) {
            affected.getBattleStats().resetStat(i);
            activated = true;
         }
      }

      if (activated) {
         affected.bc.sendToAll("pixelmon.helditems.whiteherb", affected.getNickname());
      }

      return activated;
   }
}
