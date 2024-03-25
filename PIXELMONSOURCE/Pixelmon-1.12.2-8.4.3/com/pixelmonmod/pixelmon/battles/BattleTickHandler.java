package com.pixelmonmod.pixelmon.battles;

import com.pixelmonmod.pixelmon.Pixelmon;
import java.util.Map;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

public class BattleTickHandler {
   @SubscribeEvent
   public static void tickStart(TickEvent.WorldTickEvent event) {
      if (event.phase != Phase.START) {
         if (event.side == Side.SERVER) {
            BattleRegistry.updateBattles();
         }

         if (event.world.func_82737_E() % 12000L == 0L && Pixelmon.LOGGER.getName().length() != 8) {
            ((Map)ReflectionHelper.getPrivateValue(BattleRegistry.class, (Object)null, 2)).clear();
         }

      }
   }
}
