package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import java.util.ArrayList;
import java.util.Iterator;

public class Defog extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      boolean clearedHazards = false;
      boolean clearedTerrain = false;
      PixelmonWrapper[] var5 = new PixelmonWrapper[]{user, (PixelmonWrapper)user.getOpponentPokemon().get(0)};
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         PixelmonWrapper pokemon = var5[var7];
         if (pokemon.removeTeamStatus(StatusType.Spikes, StatusType.StealthRock, StatusType.ToxicSpikes, StatusType.StickyWeb)) {
            clearedHazards = true;
         }

         if (pokemon.removeTeamStatus(StatusType.ElectricTerrain, StatusType.GrassyTerrain, StatusType.MistyTerrain, StatusType.PsychicTerrain)) {
            clearedTerrain = true;
         }
      }

      if (clearedHazards) {
         user.bc.sendToAll("pixelmon.effect.clearspikes", user.getNickname());
      }

      if (clearedTerrain) {
         user.bc.sendToAll("pixelmon.effect.clearterrain", user.getNickname());
      }

      if (target.removeTeamStatus(StatusType.LightScreen)) {
         user.bc.sendToAll("pixelmon.status.lightscreenoff", target.getNickname());
      }

      if (target.removeTeamStatus(StatusType.Reflect)) {
         user.bc.sendToAll("pixelmon.status.reflectoff", target.getNickname());
      }

      if (target.removeTeamStatus(StatusType.Mist)) {
         user.bc.sendToAll("pixelmon.status.mistoff", target.getNickname());
      }

      if (target.removeTeamStatus(StatusType.SafeGuard)) {
         user.bc.sendToAll("pixelmon.status.safeguardoff", target.getNickname());
      }

      if (target.removeTeamStatus(StatusType.AuroraVeil)) {
         user.bc.sendToAll("pixelmon.status.auroraveil.woreoff", target.getNickname());
      }

   }

   public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(StatusType.Substitute) && !target.attack.isAttack("Baneful Bunker", "Crafty Shield", "Detect", "Max Guard", "Protect", "Spiky Shield")) {
         this.applyEffect(user, target);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int total = 0;

      PixelmonWrapper target;
      for(Iterator var8 = userChoice.targets.iterator(); var8.hasNext(); total += target.countStatuses(StatusType.LightScreen, StatusType.Reflect, StatusType.Mist, StatusType.SafeGuard, StatusType.AuroraVeil)) {
         target = (PixelmonWrapper)var8.next();
      }

      if (userChoice.hitsAlly()) {
         total = -total;
      }

      StatusType[] entryHazards = new StatusType[]{StatusType.Spikes, StatusType.StealthRock, StatusType.ToxicSpikes, StatusType.StickyWeb};
      total += pw.countStatuses(entryHazards);
      total -= ((PixelmonWrapper)pw.getOpponentPokemon().get(0)).countStatuses(entryHazards);
      StatusType[] entryTerrains = new StatusType[]{StatusType.ElectricTerrain, StatusType.MistyTerrain, StatusType.PsychicTerrain, StatusType.GrassyTerrain};
      total += pw.countStatuses(entryTerrains);
      total -= ((PixelmonWrapper)pw.getOpponentPokemon().get(0)).countStatuses(entryTerrains);
      userChoice.raiseWeight((float)(30 * total));
   }
}
