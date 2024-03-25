package com.pixelmonmod.pixelmon.battles.controller;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class DimensionLocation {
   @SubscribeEvent
   public void dimensionChanged(PlayerEvent.PlayerChangedDimensionEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         EntityPlayer player = event.player;
         BattleControllerBase var3 = BattleRegistry.getBattle(player);
      }

   }
}
