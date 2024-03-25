package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemRaiseStat extends ItemHeld {
   private StatsType stat;
   private EnumType moveType;

   public ItemRaiseStat(EnumHeldItems heldItemType, String name, StatsType stat, EnumType moveType) {
      super(heldItemType, name);
      this.stat = stat;
      this.moveType = moveType;
   }

   public void postProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper holder, Attack attack, float damage) {
      if (attack.getType() == this.moveType && damage > 0.0F && holder.isAlive()) {
         holder.bc.sendToAll("pixelmon.abilities.activated", holder.getNickname(), this.getLocalizedName());
         holder.getBattleStats().modifyStat(1, (StatsType)this.stat);
         holder.consumeItem();
      }

   }
}
