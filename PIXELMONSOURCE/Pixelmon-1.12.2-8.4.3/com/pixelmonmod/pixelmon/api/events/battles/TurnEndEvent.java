package com.pixelmonmod.pixelmon.api.events.battles;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public class TurnEndEvent extends Event {
   public final BattleControllerBase bcb;

   public TurnEndEvent(BattleControllerBase bcb) {
      this.bcb = bcb;
   }
}
