package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Ripen;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemBerryRecoil extends ItemBerry {
   private AttackCategory attackCategory;

   public ItemBerryRecoil(EnumHeldItems berryType, EnumBerry berry, String itemName, AttackCategory attackCategory) {
      super(berryType, berry, itemName);
      this.attackCategory = attackCategory;
   }

   public void postProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, float damage) {
      if (canEatBerry(target) && damage > 0.0F && attack.getAttackCategory() == this.attackCategory && attacker.isAlive() && !(attacker.getBattleAbility() instanceof MagicGuard)) {
         boolean ripened = target.getBattleAbility().isAbility(Ripen.class);
         attacker.bc.sendToAll("pixelmon.helditems.recoilberry", target.getNickname(), target.getHeldItem().getLocalizedName(), attacker.getNickname());
         if (ripened) {
            target.bc.sendToAll("pixelmon.abilities.ripen", target.getNickname(), this.getLocalizedName());
         }

         attacker.doBattleDamage(target, (float)attacker.getPercentMaxHealth(ripened ? 25.0F : 12.5F), DamageTypeEnum.ITEM);
         this.eatBerry(target);
         target.consumeItem();
      }

   }
}
