package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class OwnTempo extends PreventStatus {
   public OwnTempo() {
      super("pixelmon.abilities.owntempo", "pixelmon.abilities.owntempocure", StatusType.Confusion);
   }
}
