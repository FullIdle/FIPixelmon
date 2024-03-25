package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemMetalCoat extends ItemHeld {
   public ItemMetalCoat() {
      super(EnumHeldItems.metalCoat, "metal_coat");
   }

   public double preProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      return attack.getType() == EnumType.Steel ? damage * 1.2 : damage;
   }
}
