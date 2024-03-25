package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.List;

public class Roosting extends StatusBase {
   public Roosting() {
      super(StatusType.Roosting);
   }

   public Roosting(PixelmonWrapper pw) {
      super(StatusType.Roosting);
      List newType = EnumType.ignoreType(pw.type, EnumType.Flying);
      if (newType.get(0) == EnumType.Mystery) {
         newType.clear();
      }

      if (newType.size() != pw.type.size()) {
         if (newType.size() == 0) {
            newType.add(EnumType.Normal);
         }

         pw.setTempType(newType);
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      List initialType = pw.getInitialType();
      if (initialType != null) {
         pw.setTempType(initialType);
      }

      pw.removeStatus((StatusBase)this);
   }
}
