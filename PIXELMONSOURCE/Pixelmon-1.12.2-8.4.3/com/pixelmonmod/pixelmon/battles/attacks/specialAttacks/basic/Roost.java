package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.attacks.ValueType;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Roosting;

public class Roost extends Recover {
   public Roost() {
      super(new Value(50, ValueType.WholeNumber));
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      AttackResult result = super.applyEffectDuring(user, target);
      if (result == AttackResult.succeeded) {
         user.addStatus(new Roosting(user), user);
      }

      return result;
   }
}
