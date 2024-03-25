package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class Telekinesis extends StatusBase {
   private transient int effectTurns = 3;

   public Telekinesis() {
      super(StatusType.Telekinesis);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.hasStatus(StatusType.Telekinesis, StatusType.Ingrain) && !target.bc.globalStatusController.hasStatus(StatusType.Gravity) && target.getUsableHeldItem().getHeldItemType() != EnumHeldItems.ironBall) {
         if (target.addStatus(new Telekinesis(), user)) {
            target.bc.sendToAll("pixelmon.status.telekinesis", target.getNickname());
         }
      } else {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.effectTurns <= 0) {
         pw.bc.sendToAll("pixelmon.status.telekinesisend", pw.getNickname());
         pw.removeStatus((StatusBase)this);
      }

   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{power, -1};
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (user.attack.getType().equals(EnumType.Ground) && user.attack.getAttackCategory() != AttackCategory.STATUS && !pokemon.isGrounded()) {
         user.bc.sendToAll("pixelmon.battletext.noeffect", pokemon.getNickname());
         return true;
      } else {
         return false;
      }
   }
}
