package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.List;

public class Libero extends AbilityBase {
   public void startMove(PixelmonWrapper user) {
      ArrayList newType = new ArrayList(2);
      Attack a = user.attack;
      EnumType changeType = a.getType();
      if (changeType == EnumType.Mystery) {
         changeType = EnumType.Normal;
      }

      newType.add(changeType);
      newType.add((Object)null);
      if (!user.type.equals(newType)) {
         user.bc.sendToAll("pixelmon.abilities.libero", user.getNickname(), changeType.getLocalizedName());
         user.setTempType((List)newType);
      }

   }
}
