package com.pixelmonmod.pixelmon.util;

import com.google.common.base.Preconditions;
import java.util.UUID;

public class LootClaim {
   private final UUID playerID;
   private final long timeClaimed;

   public LootClaim(UUID playerID, long timeClaimed) {
      this.playerID = (UUID)Preconditions.checkNotNull(playerID);
      this.timeClaimed = timeClaimed;
   }

   public UUID getPlayerID() {
      return this.playerID;
   }

   public long getTimeClaimed() {
      return this.timeClaimed;
   }
}
