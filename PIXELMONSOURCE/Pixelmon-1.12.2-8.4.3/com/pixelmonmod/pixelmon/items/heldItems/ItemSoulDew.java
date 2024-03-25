package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemSoulDew extends ItemHeld {
   public ItemSoulDew(EnumHeldItems heldItemType, String itemName) {
      super(heldItemType, itemName);
   }

   public double preProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      return attack.getType() != EnumType.Psychic && attack.getType() != EnumType.Dragon || attacker.getSpecies() != EnumSpecies.Latios && attacker.getSpecies() != EnumSpecies.Latias ? damage : damage * 1.2;
   }
}
