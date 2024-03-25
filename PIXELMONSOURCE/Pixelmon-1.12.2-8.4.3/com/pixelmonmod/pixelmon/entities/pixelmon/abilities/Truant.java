package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Truant extends AbilityBase {
   public boolean canMove = true;

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (user.hasStatus(StatusType.Sleep)) {
         this.canMove = true;
         return true;
      } else if (this.canMove) {
         this.canMove = false;
         return true;
      } else {
         this.canMove = true;
         user.bc.sendToAll("pixelmon.abilities.truant", user.getNickname());
         return false;
      }
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.canMove = true;
   }

   public boolean needNewInstance() {
      return true;
   }
}
