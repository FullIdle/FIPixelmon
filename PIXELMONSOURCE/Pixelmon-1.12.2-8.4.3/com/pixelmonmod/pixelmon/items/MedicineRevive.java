package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class MedicineRevive implements IMedicine {
   private IHealHP healHP;

   public MedicineRevive(IHealHP healHP) {
      this.healHP = healHP;
   }

   public boolean useMedicine(PokemonLink target, double multiplier) {
      return this.healPokemon(target);
   }

   protected boolean healPokemon(PokemonLink pxm) {
      if (pxm.getHealth() <= 0) {
         pxm.setHealth(this.healHP.getHealAmount(pxm));
         if (pxm.getSpecies() == EnumSpecies.Shedinja) {
            pxm.setHealth(1);
         }

         pxm.update(EnumUpdateType.HP);
         pxm.sendMessage("pixelmon.effect.revived", pxm.getNickname());
         return true;
      } else {
         return false;
      }
   }
}
