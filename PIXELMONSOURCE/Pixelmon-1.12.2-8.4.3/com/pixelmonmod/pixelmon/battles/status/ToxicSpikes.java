package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class ToxicSpikes extends EntryHazard {
   public ToxicSpikes() {
      super(StatusType.ToxicSpikes, 2);
   }

   public boolean isUnharmed(PixelmonWrapper pw) {
      return super.isUnharmed(pw) || pw.hasType(EnumType.Steel) || this.isAirborne(pw);
   }

   public void applyEffectOnSwitch(PixelmonWrapper pw) {
      if (!this.isUnharmed(pw)) {
         this.doEffect(pw);
      }

   }

   protected void doEffect(PixelmonWrapper pw) {
      if (pw.hasType(EnumType.Poison)) {
         pw.bc.sendToAll("pixelmon.status.toxicspikesabsorbed", pw.getNickname());
         pw.removeTeamStatus((StatusBase)this);
      } else {
         Poison poison = this.numLayers == 1 ? new Poison() : new PoisonBadly();
         if (pw.addStatus((StatusBase)poison, pw)) {
            pw.bc.sendToAll(this.getAffectedMessage(), pw.getNickname());
         }

      }
   }

   public EntryHazard getNewInstance() {
      return new ToxicSpikes();
   }

   protected String getFirstLayerMessage() {
      return "pixelmon.effect.toxicspikes";
   }

   protected String getMultiLayerMessage() {
      return "pixelmon.effect.moretoxicspikes";
   }

   protected String getAffectedMessage() {
      return this.numLayers == 1 ? "pixelmon.status.toxicspikespoisoned" : "pixelmon.status.toxicspikesbadlypoisoned";
   }

   public int getAIWeight() {
      return 15;
   }
}
