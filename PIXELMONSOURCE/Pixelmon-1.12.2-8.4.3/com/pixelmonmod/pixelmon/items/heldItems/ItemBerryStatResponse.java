package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Ripen;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemBerryStatResponse extends ItemBerry {
   private AttackCategory category;
   private StatsType stat;

   public ItemBerryStatResponse(EnumHeldItems type, EnumBerry berry, String name, AttackCategory category, StatsType stat) {
      super(type, berry, name);
      this.category = category;
      this.stat = stat;
   }

   public void postProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, float damage) {
      if (canEatBerry(target) && damage > 0.0F && attack.getAttackCategory() == this.category) {
         this.eatBerry(target);
         target.consumeItem();
      }

   }

   public void eatBerry(PixelmonWrapper user) {
      boolean ripened = user.getBattleAbility().isAbility(Ripen.class);
      user.bc.sendToAll("pixelmon.helditems.pinchberry", user.getNickname(), user.getHeldItem().getLocalizedName());
      if (ripened) {
         user.bc.sendToAll("pixelmon.abilities.ripen", user.getNickname(), this.getLocalizedName());
      }

      user.getBattleStats().modifyStat(ripened ? 2 : 1, this.stat);
      super.eatBerry(user);
   }
}
