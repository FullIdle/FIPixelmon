package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.HiddenPower;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.List;

public class Protean extends AbilityBase {
   public void startMove(PixelmonWrapper user) {
      ArrayList newType = new ArrayList(2);
      EnumType changeType = user.attack.getType();
      if (user.attack.isAttack("Hidden Power")) {
         changeType = HiddenPower.getHiddenPowerType(user.getStats().ivs);
      }

      if (changeType == EnumType.Mystery) {
         changeType = EnumType.Normal;
      }

      newType.add(changeType);
      newType.add((Object)null);
      if (!newType.equals(user.type)) {
         user.bc.sendToAll("pixelmon.abilities.protean", user.getNickname(), changeType.getLocalizedName());
         user.setTempType((List)newType);
      }

   }
}
