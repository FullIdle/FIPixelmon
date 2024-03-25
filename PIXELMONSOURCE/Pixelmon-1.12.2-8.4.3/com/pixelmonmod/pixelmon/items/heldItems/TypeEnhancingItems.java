package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumTypeEnhancingItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class TypeEnhancingItems extends ItemHeld {
   public EnumTypeEnhancingItems enhanceType;
   protected EnumType type;

   public TypeEnhancingItems(EnumTypeEnhancingItems EnhanceType, String itemName, EnumType type) {
      super(EnumHeldItems.typeEnhancer, itemName);
      this.enhanceType = EnhanceType;
      this.type = type;
   }

   public double preProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      return attack.getMove().getAttackType() == this.type ? damage * 1.2 : damage;
   }

   public EnumType getType() {
      return this.type;
   }
}
