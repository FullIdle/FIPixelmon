package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sleep;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;

public class EarlyBird extends AbilityBase {
   public void onStatusAdded(StatusBase status, PixelmonWrapper user, PixelmonWrapper opponent) {
      if (status instanceof Sleep) {
         Sleep sleepStatus = (Sleep)status;
         sleepStatus.effectTurns /= 2;
      }

   }
}
