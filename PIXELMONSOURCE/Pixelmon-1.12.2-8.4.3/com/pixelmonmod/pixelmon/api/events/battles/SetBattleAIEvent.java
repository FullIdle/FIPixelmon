package com.pixelmonmod.pixelmon.api.events.battles;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SetBattleAIEvent extends Event {
   public final BattleControllerBase bc;
   public final BattleParticipant participant;
   private BattleAIBase ai;

   public SetBattleAIEvent(BattleControllerBase bc, BattleParticipant participant, BattleAIBase ai) {
      this.bc = bc;
      this.participant = participant;
      this.ai = ai;
   }

   public BattleAIBase getAI() {
      return this.ai;
   }

   public void setAI(BattleAIBase ai) {
      if (ai != null) {
         this.ai = ai;
      }

   }
}
