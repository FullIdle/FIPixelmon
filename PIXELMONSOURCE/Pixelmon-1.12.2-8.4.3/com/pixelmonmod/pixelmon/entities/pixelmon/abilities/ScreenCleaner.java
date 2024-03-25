package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class ScreenCleaner extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper pw) {
      pw.bc.getActivePokemon().forEach((p) -> {
         if (p.removeTeamStatus(StatusType.LightScreen)) {
            p.bc.sendToAll("pixelmon.status.lightscreenoff", p.getNickname());
         }

         if (p.removeTeamStatus(StatusType.Reflect)) {
            p.bc.sendToAll("pixelmon.status.reflectoff", p.getNickname());
         }

         if (p.removeTeamStatus(StatusType.AuroraVeil)) {
            p.bc.sendToAll("pixelmon.status.auroraveil.woreoff", p.getNickname());
         }

      });
   }
}
