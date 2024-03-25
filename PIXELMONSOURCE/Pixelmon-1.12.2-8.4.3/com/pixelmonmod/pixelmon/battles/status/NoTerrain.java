package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.EnumTerrain;
import javax.annotation.Nonnull;

public class NoTerrain extends Terrain {
   public NoTerrain() {
      super(StatusType.None, "", "");
   }

   public Terrain getNewInstance() {
      return new NoTerrain();
   }

   @Nonnull
   public EnumTerrain getTerrainType() {
      return EnumTerrain.NONE;
   }

   public EnumType getTypingForTerrain() {
      return null;
   }

   protected int countBenefits(PixelmonWrapper user, PixelmonWrapper target) {
      return 0;
   }
}
