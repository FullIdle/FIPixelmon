package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Iterator;

public abstract class GlobalStatusBase extends StatusBase {
   public GlobalStatusBase(StatusType type) {
      super(type);
   }

   public void applyRepeatedEffect(GlobalStatusController globalStatusController) {
   }

   public boolean isWeather() {
      return this instanceof Weather;
   }

   public boolean isTerrain() {
      return this instanceof Terrain;
   }

   public static boolean ignoreWeather(BattleControllerBase bc) {
      Iterator var1 = bc.getActiveUnfaintedPokemon().iterator();

      PixelmonWrapper pw;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         pw = (PixelmonWrapper)var1.next();
      } while(!pw.getBattleAbility().ignoreWeather());

      return true;
   }
}
