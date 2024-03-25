package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;

public class Spikes extends EntryHazard {
   public Spikes() {
      super(StatusType.Spikes, 3);
   }

   public boolean isUnharmed(PixelmonWrapper pw) {
      return super.isUnharmed(pw) || this.isAirborne(pw) || pw.getBattleAbility() instanceof MagicGuard;
   }

   public int getDamage(PixelmonWrapper pw) {
      return pw.getPercentMaxHealth(100.0F / (float)(8 - (this.numLayers - 1) * 2));
   }

   protected String getFirstLayerMessage() {
      return "pixelmon.effect.spikes";
   }

   protected String getMultiLayerMessage() {
      return "pixelmon.effect.morespikes";
   }

   protected String getAffectedMessage() {
      return "pixelmon.status.hurtbyspikes";
   }

   public int getAIWeight() {
      return 20;
   }

   public EntryHazard getNewInstance() {
      return new Spikes();
   }
}
