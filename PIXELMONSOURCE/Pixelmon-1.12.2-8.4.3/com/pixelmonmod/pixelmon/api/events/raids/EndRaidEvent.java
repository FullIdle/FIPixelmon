package com.pixelmonmod.pixelmon.api.events.raids;

import com.pixelmonmod.pixelmon.battles.controller.participants.RaidPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.battles.raids.RaidGovernor;
import java.util.ArrayList;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EndRaidEvent extends Event {
   private final RaidData raid;
   private final RaidGovernor governor;
   private final ArrayList allyParticipants;
   private final RaidPixelmonParticipant raidParticipant;
   private final boolean raidersWon;

   public EndRaidEvent(RaidData raid, RaidGovernor governor, ArrayList allyParticipants, RaidPixelmonParticipant raidParticipant, boolean raidersWon) {
      this.raid = raid;
      this.governor = governor;
      this.allyParticipants = allyParticipants;
      this.raidParticipant = raidParticipant;
      this.raidersWon = raidersWon;
   }

   public RaidData getRaid() {
      return this.raid;
   }

   public RaidGovernor getGovernor() {
      return this.governor;
   }

   public ArrayList getAllyParticipants() {
      return this.allyParticipants;
   }

   public RaidPixelmonParticipant getRaidParticipant() {
      return this.raidParticipant;
   }

   public boolean didRaidersWin() {
      return this.raidersWon;
   }
}
