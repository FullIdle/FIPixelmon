package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.List;

public class ColorChange extends AbilityBase {
   public void tookDamageTargetAfterMove(PixelmonWrapper user, PixelmonWrapper target, Attack a, float damage) {
      if (target.isAlive()) {
         ArrayList newType = new ArrayList(2);
         EnumType changeType = a.getType();
         if (changeType == EnumType.Mystery) {
            changeType = EnumType.Normal;
         }

         newType.add(changeType);
         newType.add((Object)null);
         if (!target.type.equals(newType)) {
            target.bc.sendToAll("pixelmon.abilities.colourchange", target.getNickname(), a.getType());
            target.setTempType((List)newType);
         }
      }

   }
}
