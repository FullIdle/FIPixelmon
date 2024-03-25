package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class HealingWish extends StatusBase {
   public HealingWish() {
      super(StatusType.HealingWish);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.getParticipant().hasMorePokemonReserve()) {
         user.addStatus(new HealingWish(), user);
         user.doBattleDamage(user, (float)user.getHealth(), DamageTypeEnum.SELF);
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public boolean isWholeTeamStatus() {
      return false;
   }

   public void applyEffectOnSwitch(PixelmonWrapper pw) {
      boolean didHeal = false;
      if (pw.getMaxHealth() > pw.getHealth()) {
         pw.healEntityBy(pw.getHealthDeficit());
         didHeal = true;
      }

      for(int i = 0; i < pw.getStatusSize(); ++i) {
         StatusType currentStatus = pw.getStatus(i).type;
         if (currentStatus.isPrimaryStatus() || currentStatus == StatusType.HealingWish) {
            if (currentStatus != StatusType.HealingWish) {
               didHeal = true;
            }

            pw.removeStatus(i);
            --i;
         }
      }

      if (!didHeal) {
         pw.addStatus(new HealingWish(), pw);
         pw.bc.sendToAll("pixelmon.effect.healfailed", pw.getNickname());
      } else {
         pw.bc.sendToAll("pixelmon.effect.healingwish");
      }

   }

   public void applySwitchOutEffect(PixelmonWrapper outgoing, PixelmonWrapper incoming) {
      outgoing.removeStatus(StatusType.HealingWish);
   }
}
