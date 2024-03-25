package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

/** @deprecated */
@Deprecated
public class PlayerBattleEndedAbnormalEvent extends Event {
   public final EntityPlayerMP player;
   public final BattleControllerBase battleController;

   public PlayerBattleEndedAbnormalEvent(EntityPlayerMP player, BattleControllerBase battleControllerBase) {
      this.player = player;
      this.battleController = battleControllerBase;
   }
}
