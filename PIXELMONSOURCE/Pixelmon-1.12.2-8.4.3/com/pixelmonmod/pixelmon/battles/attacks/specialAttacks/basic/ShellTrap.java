package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class ShellTrap extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasStatus(StatusType.ShellTrap) && ((ShellTrapStatus)user.getStatus(StatusType.ShellTrap)).gotHit) {
         return AttackResult.proceed;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void applyAfterEffect(PixelmonWrapper user) {
      user.removeStatus(StatusType.ShellTrap);
   }

   public void applyEarlyEffect(PixelmonWrapper user) {
      user.addStatus(new ShellTrapStatus(), user);
   }

   public static class ShellTrapStatus extends StatusBase {
      boolean gotHit = false;

      public ShellTrapStatus() {
         super(StatusType.ShellTrap);
      }

      public void onDamageReceived(PixelmonWrapper userWrapper, PixelmonWrapper pokemon, Attack attack, int damage, DamageTypeEnum damageType) {
         if (attack.getMove().getAttackCategory() == AttackCategory.PHYSICAL && !userWrapper.isAlly(pokemon)) {
            this.gotHit = true;
            BattleControllerBase bc = pokemon.bc;
            int order = bc.turnList.indexOf(pokemon);
            if (order > bc.turn) {
               bc.turnList.remove(order);
               bc.turnList.add(bc.turn + 1, pokemon);
            }
         }

      }
   }
}
