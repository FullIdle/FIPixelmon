package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SheerForce;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemShellBell extends ItemHeld {
   public ItemShellBell() {
      super(EnumHeldItems.shellBell, "shell_bell");
   }

   public void postProcessDamagingAttackUser(PixelmonWrapper holder, PixelmonWrapper defender, Attack attack, float damage) {
      if (damage > 0.0F && !holder.hasFullHealth() && holder.getHealth() != 0 && !holder.hasStatus(StatusType.HealBlock) && holder != defender) {
         if (holder.getBattleAbility() instanceof SheerForce && attack.getMove().hasSecondaryEffect()) {
            return;
         }

         int healAmount = Math.max(1, (int)(damage / 8.0F));
         holder.bc.sendToAll("pixelmon.helditems.shellbell", holder.getNickname());
         holder.healEntityBy(healAmount);
      }

   }
}
