package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;

public class MedicinePotion implements IMedicine {
   private IHealHP healHP;

   public MedicinePotion(IHealHP healHP) {
      this.healHP = healHP;
   }

   public boolean useMedicine(PokemonLink target, double multiplier) {
      int currentHealth = target.getHealth();
      int maxHealth = target.getMaxHealth();
      if (currentHealth < maxHealth) {
         target.setHealth(Math.min(maxHealth, currentHealth + (int)((double)this.healHP.getHealAmount(target) * multiplier)));
         target.update(EnumUpdateType.HP);
         target.sendMessage("pixelmon.effect.restorehealth", target.getNickname());
         return true;
      } else {
         return false;
      }
   }
}
