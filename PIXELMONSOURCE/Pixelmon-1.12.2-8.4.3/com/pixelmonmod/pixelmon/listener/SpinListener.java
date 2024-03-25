package com.pixelmonmod.pixelmon.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SpinListener {
   private static final Map workmap = new HashMap();

   public static void registerListener(EntityPlayer player, Consumer consumer) {
      if (workmap.isEmpty()) {
         MinecraftForge.EVENT_BUS.register(SpinListener.class);
      }

      workmap.put(player.func_110124_au(), new SpinData(player, consumer));
   }

   @SubscribeEvent
   public static void onPlayerSpin(TickEvent.PlayerTickEvent event) {
      EntityPlayer player = event.player;
      if (workmap.containsKey(player.func_110124_au())) {
         SpinData data = (SpinData)workmap.get(player.func_110124_au());
         boolean completed = false;
         if (player.func_174811_aO() != data.lastFacing) {
            if (data.lastFacing == data.startFacing) {
               data.forward = data.lastFacing.func_176746_e() == player.func_174811_aO();
               data.lastFacing = player.func_174811_aO();
            } else if (data.lastFacing.func_176746_e() == player.func_174811_aO() && data.forward) {
               if (data.startFacing == player.func_174811_aO()) {
                  completed = true;
               }

               data.lastFacing = player.func_174811_aO();
            } else if (data.lastFacing.func_176746_e().func_176734_d() == player.func_174811_aO() && !data.forward) {
               if (data.startFacing == player.func_174811_aO()) {
                  completed = true;
               }

               data.lastFacing = player.func_174811_aO();
            } else {
               data.reset(player);
            }
         }

         if (completed) {
            data.consumer.accept(player);
            workmap.remove(player.func_110124_au());
            if (workmap.isEmpty()) {
               MinecraftForge.EVENT_BUS.unregister(SpinListener.class);
            }
         }
      }

   }

   @SubscribeEvent
   public static void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
      workmap.remove(event.player.func_110124_au());
      if (workmap.isEmpty()) {
         MinecraftForge.EVENT_BUS.unregister(SpinListener.class);
      }

   }

   private static class SpinData {
      EnumFacing startFacing = null;
      EnumFacing lastFacing = null;
      boolean forward = false;
      Consumer consumer;

      public SpinData(EntityPlayer player, Consumer consumer) {
         this.reset(player);
         this.consumer = consumer;
      }

      public void reset(EntityPlayer player) {
         this.startFacing = this.lastFacing = player.func_174811_aO();
      }

      public String toString() {
         return "SpinData{startFacing=" + this.startFacing + ", lastFacing=" + this.lastFacing + ", forward=" + this.forward + '}';
      }
   }
}
