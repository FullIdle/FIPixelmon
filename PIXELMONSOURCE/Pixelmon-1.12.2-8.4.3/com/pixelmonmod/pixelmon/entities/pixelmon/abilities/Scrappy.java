package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.List;

public class Scrappy extends AbilityBase {
   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      return user.attack == null || user.attack.getType() != EnumType.Normal && user.attack.getType() != EnumType.Fighting ? target.type : EnumType.ignoreType(target.type, EnumType.Ghost);
   }
}
