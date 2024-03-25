package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Limber extends PreventStatus {
   public Limber() {
      super("pixelmon.abilities.limber", "pixelmon.abilities.limbercure", StatusType.Paralysis);
   }
}
