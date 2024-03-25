package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SheerForce;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemLifeOrb extends ItemHeld {
   public ItemLifeOrb() {
      super(EnumHeldItems.lifeorb, "life_orb");
   }

   public double preProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      return damage > 0.0 ? damage * 1.3 : damage;
   }

   public void dealtDamage(PixelmonWrapper attacker, PixelmonWrapper defender, Attack attack, DamageTypeEnum damageType) {
      AbilityBase attackerAbility = attacker.getBattleAbility();
      if (attack != null && damageType == DamageTypeEnum.ATTACK && !attacker.isFainted()) {
         boolean doesRecoil = !(attackerAbility instanceof MagicGuard) && (defender.isFainted() || !attacker.inParentalBond) && (!(attackerAbility instanceof SheerForce) || !attack.getMove().hasSecondaryEffect());
         if (doesRecoil) {
            int recoil = attacker.getPercentMaxHealth(10.0F);
            if (attacker.isDynamax()) {
               recoil = attacker.getPercentMaxHealth(10.0F, true);
            }

            attacker.bc.sendToAll("pixelmon.helditem.lifeowner", attacker.getNickname());
            attacker.doBattleDamage(attacker, (float)recoil, DamageTypeEnum.ITEM);
         }
      }

   }
}
