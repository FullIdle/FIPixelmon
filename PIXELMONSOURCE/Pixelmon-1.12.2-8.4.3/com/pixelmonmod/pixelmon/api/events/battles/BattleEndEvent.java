package com.pixelmonmod.pixelmon.api.events.battles;

import com.google.common.collect.ImmutableMap;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BattleEndEvent extends Event {
   public final BattleControllerBase bc;
   public final EnumBattleEndCause cause;
   public final boolean abnormal;
   public final ImmutableMap results;

   public BattleEndEvent(BattleControllerBase bc, EnumBattleEndCause cause, boolean abnormal, HashMap results) {
      this.bc = bc;
      this.cause = cause;
      this.abnormal = abnormal;
      this.results = ImmutableMap.copyOf(results);
   }

   public ArrayList getPlayers() {
      ArrayList players = new ArrayList();
      this.bc.participants.forEach((p) -> {
         if (p instanceof PlayerParticipant) {
            players.add(((PlayerParticipant)p).player);
         }

      });
      return players;
   }
}
