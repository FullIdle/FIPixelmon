package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class WaterSport extends Sport {
   public WaterSport() {
      this((PixelmonWrapper)null);
   }

   public WaterSport(PixelmonWrapper user) {
      super(user, StatusType.WaterSport, EnumType.Fire, "Water Sport");
   }

   protected Sport getNewInstance(PixelmonWrapper user) {
      return new WaterSport(user);
   }
}
