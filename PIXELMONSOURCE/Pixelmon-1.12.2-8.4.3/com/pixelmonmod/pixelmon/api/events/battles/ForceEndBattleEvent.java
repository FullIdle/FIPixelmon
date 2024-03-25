package com.pixelmonmod.pixelmon.api.events.battles;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleForceEndCause;
import java.util.ArrayList;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ForceEndBattleEvent extends Event {
   public final BattleControllerBase bc;
   public final EnumBattleForceEndCause cause;

   public ForceEndBattleEvent(BattleControllerBase bc, EnumBattleForceEndCause cause) {
      this.bc = bc;
      this.cause = cause;
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

   public boolean isCancelable() {
      return this.cause != EnumBattleForceEndCause.ENDBATTLE_FORCEFUL;
   }
}
