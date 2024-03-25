package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;

public class MedicineStatus implements IMedicine {
   private StatusType[] statuses;

   public MedicineStatus(StatusType... statuses) {
      this.statuses = statuses;
   }

   public boolean useMedicine(PokemonLink target, double multiplier) {
      if (target.removeStatuses(this.statuses)) {
         target.update(EnumUpdateType.Status);
         return true;
      } else {
         return false;
      }
   }
}
