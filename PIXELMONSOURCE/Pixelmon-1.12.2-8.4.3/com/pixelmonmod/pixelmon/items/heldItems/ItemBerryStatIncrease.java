package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Gluttony;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Ripen;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumBerryStatIncrease;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemBerryStatIncrease extends ItemBerry {
   public EnumBerryStatIncrease berryType;
   private StatsType stat;

   public ItemBerryStatIncrease(EnumBerryStatIncrease berryType, EnumBerry berry, String itemName, StatsType stat) {
      super(EnumHeldItems.berryStatIncrease, berry, itemName);
      this.berryType = berryType;
      this.stat = stat;
   }

   public void tookDamage(PixelmonWrapper attacker, PixelmonWrapper defender, float damage, DamageTypeEnum damageType) {
      int lowHealth = defender.getBattleAbility() instanceof Gluttony ? 50 : 25;
      if (defender.isAlive() && defender.getHealthPercent() <= (float)lowHealth) {
         this.eatBerry(defender);
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (newPokemon.getHealthPercent() <= 25.0F) {
         this.eatBerry(newPokemon);
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      this.applySwitchInEffect(pw);
   }

   public void eatBerry(PixelmonWrapper pokemon) {
      if (canEatBerry(pokemon)) {
         boolean ripened = pokemon.getBattleAbility().isAbility(Ripen.class);
         pokemon.bc.sendToAll("pixelmon.helditems.pinchberry", pokemon.getNickname(), pokemon.getHeldItem().getLocalizedName());
         if (this.stat != null) {
            pokemon.getBattleStats().modifyStat(ripened ? 2 : 1, this.stat);
         } else if (this.berryType == EnumBerryStatIncrease.starfBerry) {
            pokemon.getBattleStats().raiseRandomStat(ripened ? 4 : 2);
         } else if (this.berryType == EnumBerryStatIncrease.lansatBerry) {
            pokemon.getBattleStats().increaseCritStage(ripened ? 4 : 2, false);
         }

         if (ripened) {
            pokemon.bc.sendToAll("pixelmon.abilities.ripen", pokemon.getNickname(), this.getLocalizedName());
         }

         super.eatBerry(pokemon);
         pokemon.consumeItem();
      }

   }
}
