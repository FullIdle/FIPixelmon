package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class MudSport extends Sport {
   public MudSport() {
      this((PixelmonWrapper)null);
   }

   public MudSport(PixelmonWrapper user) {
      super(user, StatusType.MudSport, EnumType.Electric, "Mud Sport");
   }

   protected Sport getNewInstance(PixelmonWrapper user) {
      return new MudSport(user);
   }
}
