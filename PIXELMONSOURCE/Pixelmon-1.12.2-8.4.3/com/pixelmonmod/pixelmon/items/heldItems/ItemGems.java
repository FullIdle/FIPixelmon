package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Pledge;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumGems;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.Iterator;

public class ItemGems extends ItemHeld {
   public EnumGems gemType;
   EnumType type;

   public ItemGems(EnumGems GemType, String itemName, EnumType type) {
      super(EnumHeldItems.gems, itemName);
      this.gemType = GemType;
      this.type = type;
   }

   public double preProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      if (damage > 0.0 && attack.getType() == this.type && !attack.isAttack("Struggle")) {
         Iterator var6 = attack.getMove().effects.iterator();

         EffectBase effect;
         do {
            if (!var6.hasNext()) {
               attacker.bc.sendToAll("pixelmon.helditems.gem", attacker.getNickname(), attacker.getHeldItem().getLocalizedName());
               attacker.consumeItem();
               return damage * 1.3;
            }

            effect = (EffectBase)var6.next();
         } while(!(effect instanceof Pledge));

         return damage;
      } else {
         return damage;
      }
   }
}
