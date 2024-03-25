package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.RockHead;
import java.util.ArrayList;

public class Recoil extends AttackModifierBase {
   public int percentRecoil;

   public Recoil(Value... values) {
      this.percentRecoil = values[0].value;
   }

   public void applyRecoil(PixelmonWrapper user, float damage) {
      if (!this.isImmune(user) && user.isAlive()) {
         user.bc.sendToAll("recoil.damage", user.getNickname());
         float recoilDamage = this.getRecoil(damage);
         user.doBattleDamage(user, recoilDamage, DamageTypeEnum.RECOIL);
      }

   }

   private boolean isImmune(PixelmonWrapper user) {
      AbilityBase userAbility = user.getBattleAbility();
      return userAbility instanceof RockHead || userAbility instanceof MagicGuard;
   }

   private float getRecoil(float damage) {
      float recoilDamage = damage * (float)this.percentRecoil / 100.0F;
      if (recoilDamage < 1.0F) {
         recoilDamage = 1.0F;
      }

      return recoilDamage;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (userChoice.tier >= 3 && !this.isImmune(pw)) {
         userChoice.weight -= pw.getHealthPercent(this.getRecoil((float)userChoice.result.damage));
      }

   }
}
