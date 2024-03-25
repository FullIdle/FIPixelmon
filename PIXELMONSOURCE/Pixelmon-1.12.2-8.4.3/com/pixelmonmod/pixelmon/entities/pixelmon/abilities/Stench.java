package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Flinch;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class Stench extends AbilityBase {
   public void tookDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      EnumHeldItems heldItem = user.getUsableHeldItem().getHeldItemType();
      if (heldItem != EnumHeldItems.kingsRock && heldItem != EnumHeldItems.razorFang && RandomHelper.getRandomChance(10)) {
         Flinch.flinch(user, target);
      }

   }
}
