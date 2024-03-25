package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers;

import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.ArrayList;
import java.util.List;

public class OnlyUser extends AttackModifierBase {
   List users = new ArrayList();

   public OnlyUser(Value... values) {
      Value[] var2 = values;
      int var3 = values.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Value value = var2[var4];
         if (value != null) {
            String name = value.stringValue;
            if (name != null && EnumSpecies.hasPokemon(name)) {
               this.users.add(EnumSpecies.getFromNameAnyCase(name));
            }
         }
      }

   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!this.users.contains(user.getSpecies()) && (!user.hasStatus(StatusType.Transformed) || !this.users.contains(user.entity.transformed.getSpecies()))) {
         user.bc.sendToAll("pixelmon.battletext.cantuse", user.getNickname());
         return AttackResult.failed;
      } else {
         return AttackResult.proceed;
      }
   }
}
