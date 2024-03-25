package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.List;

public class SmackedDown extends StatusBase {
   public SmackedDown() {
      super(StatusType.SmackedDown);
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      return user.attack != null && user.attack.getType() == EnumType.Ground ? EnumType.ignoreType(target.type, EnumType.Flying) : target.type;
   }
}
