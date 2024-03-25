package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

/** @deprecated */
@Deprecated
public class PlayerBattleEndedEvent extends Event {
   public final EntityPlayerMP player;
   public final BattleControllerBase battleController;
   public final BattleResults result;

   public PlayerBattleEndedEvent(EntityPlayerMP player, BattleControllerBase battleControllerBase, BattleResults result) {
      this.player = player;
      this.battleController = battleControllerBase;
      this.result = result;
   }
}
