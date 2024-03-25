package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BattleStartedEvent extends Event {
   public final BattleControllerBase bc;
   public final BattleParticipant[] participant1;
   public final BattleParticipant[] participant2;

   public BattleStartedEvent(BattleControllerBase bc, BattleParticipant[] team1, BattleParticipant[] team2) {
      this.bc = bc;
      this.participant1 = team1;
      this.participant2 = team2;
   }
}
