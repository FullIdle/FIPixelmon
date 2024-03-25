package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Ripen;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemBerryEnigma extends ItemBerry {
   public ItemBerryEnigma() {
      super(EnumHeldItems.berryEnigma, EnumBerry.Enigma, "enigma_berry");
   }

   public void tookDamage(PixelmonWrapper attacker, PixelmonWrapper defender, float damage, DamageTypeEnum damageType) {
      Attack attack = attacker.attack;
      if (damage > 0.0F && damageType == DamageTypeEnum.ATTACK && attack.getTypeEffectiveness(attacker, defender) >= 2.0 && canEatBerry(defender)) {
         this.eatBerry(defender);
      }

   }

   public void eatBerry(PixelmonWrapper pokemon) {
      if (canEatBerry(pokemon) && !pokemon.hasFullHealth()) {
         boolean ripened = pokemon.getBattleAbility().isAbility(Ripen.class);
         pokemon.bc.sendToAll("pixelmon.helditems.consumerestorehp", pokemon.getNickname(), this.getLocalizedName());
         if (ripened) {
            pokemon.bc.sendToAll("pixelmon.abilities.ripen", pokemon.getNickname(), this.getLocalizedName());
         }

         pokemon.healByPercent(ripened ? 50.0F : 25.0F);
         super.eatBerry(pokemon);
         pokemon.consumeItem();
      }

   }
}
