package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemHeavyDutyBoots extends HeldItem {
   public ItemHeavyDutyBoots() {
      super(EnumHeldItems.other, "heavy_duty_boots");
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (newPokemon.hasStatus(StatusType.ToxicSpikes)) {
         if (!newPokemon.isAirborne() && newPokemon.getSpecies().getBaseStats().getTypeList().contains(EnumType.Poison)) {
            newPokemon.bc.sendToAll("pixelmon.status.toxicspikesabsorbed", newPokemon.getNickname());
            newPokemon.removeTeamStatus(StatusType.ToxicSpikes);
         }

      }
   }
}
