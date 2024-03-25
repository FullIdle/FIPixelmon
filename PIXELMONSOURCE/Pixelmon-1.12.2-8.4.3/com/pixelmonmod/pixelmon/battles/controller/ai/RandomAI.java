package com.pixelmonmod.pixelmon.battles.controller.ai;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.UUID;

public class RandomAI extends BattleAIBase {
   public RandomAI(BattleParticipant participant) {
      super(participant);
   }

   public MoveChoice getNextMove(PixelmonWrapper pw) {
      return this.getRandomAttackChoice(pw);
   }

   public UUID getNextSwitch(PixelmonWrapper pw) {
      return (UUID)RandomHelper.getRandomElementFromList(this.getPossibleSwitchIDs());
   }
}
