package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.List;

public class TarShot extends StatusBase {
   public TarShot() {
      super(StatusType.TarShot);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      target.addStatus(this, user);
   }

   public double modifyTypeEffectiveness(List effectiveTypes, EnumType moveType, double baseEffectiveness) {
      return moveType == EnumType.Fire ? baseEffectiveness * 2.0 : baseEffectiveness;
   }
}
