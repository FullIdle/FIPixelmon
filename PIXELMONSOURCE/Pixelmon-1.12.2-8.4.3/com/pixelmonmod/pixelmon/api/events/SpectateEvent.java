package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class SpectateEvent extends Event {
   public final EntityPlayerMP spectator;
   public final BattleControllerBase battleController;

   private SpectateEvent(EntityPlayerMP spectator, BattleControllerBase battleController) {
      this.spectator = spectator;
      this.battleController = battleController;
   }

   // $FF: synthetic method
   SpectateEvent(EntityPlayerMP x0, BattleControllerBase x1, Object x2) {
      this(x0, x1);
   }

   public static class StopSpectate extends SpectateEvent {
      public StopSpectate(EntityPlayerMP spectator, BattleControllerBase battleController) {
         super(spectator, battleController, null);
      }
   }

   @Cancelable
   public static class StartSpectate extends SpectateEvent {
      public final EntityPlayerMP target;

      public StartSpectate(EntityPlayerMP spectator, BattleControllerBase battleController, EntityPlayerMP target) {
         super(spectator, battleController, null);
         this.target = target;
      }
   }
}
