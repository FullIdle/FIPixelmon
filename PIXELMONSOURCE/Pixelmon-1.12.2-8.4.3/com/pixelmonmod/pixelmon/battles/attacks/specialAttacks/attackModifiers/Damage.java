package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.attacks.ValueType;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Damage extends AttackModifierBase {
   public ValueType type;
   public int amount;

   public Damage(Value... values) {
      this.type = values[0].type;
      this.amount = values[0].value;
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      double dmg;
      if (this.type == ValueType.Percent) {
         dmg = (double)target.getPercentMaxHealth((float)this.amount / 100.0F);
      } else {
         dmg = (double)this.amount;
      }

      target.doBattleDamage(user, (float)((int)dmg), DamageTypeEnum.ATTACKFIXED);
      user.attack.playAnimation(user, target);
      return AttackResult.succeeded;
   }
}
