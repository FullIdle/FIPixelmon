package com.pixelmonmod.pixelmon.api.events.raids;

import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.RaidPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class StartRaidEvent extends Event {
   private final EntityDen den;
   private final RaidData raid;
   private final BattleParticipant[] allyParticipants;
   private final EntityPixelmon raidPixelmon;
   private final RaidPixelmonParticipant raidParticipant;

   public StartRaidEvent(EntityDen den, RaidData raid, BattleParticipant[] allyParticipants, EntityPixelmon raidPixelmon, RaidPixelmonParticipant raidParticipant) {
      this.den = den;
      this.raid = raid;
      this.allyParticipants = allyParticipants;
      this.raidPixelmon = raidPixelmon;
      this.raidParticipant = raidParticipant;
   }

   public EntityDen getDen() {
      return this.den;
   }

   public RaidData getRaid() {
      return this.raid;
   }

   public BattleParticipant[] getAllyParticipants() {
      return this.allyParticipants;
   }

   public EntityPixelmon getRaidPixelmon() {
      return this.raidPixelmon;
   }

   public RaidPixelmonParticipant getRaidParticipant() {
      return this.raidParticipant;
   }
}
