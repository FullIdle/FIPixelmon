package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemStickyBarb extends ItemHeld {
   public ItemStickyBarb() {
      super(EnumHeldItems.stickyBarb, "sticky_barb");
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.isAlive() && !(pokemon.getBattleAbility() instanceof MagicGuard)) {
         pokemon.bc.sendToAll("pixelmon.helditems.stickybarb", pokemon.getNickname());
         int damage = pokemon.getPercentMaxHealth(12.5F);
         pokemon.doBattleDamage(pokemon, (float)damage, DamageTypeEnum.ITEM);
      }

   }

   public void applyEffectOnContact(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.hasHeldItem()) {
         user.setHeldItem(target.getHeldItem());
         target.removeHeldItem();
      }

   }
}
