package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class DynamaxEvent extends Event {
   public final boolean gigantamax;

   public DynamaxEvent(boolean gigantamax) {
      this.gigantamax = gigantamax;
   }

   public static class PostEvolve extends DynamaxEvent {
      public final EntityPlayerMP player;
      public final EntityPixelmon preEvo;
      public final EntityPixelmon postEvo;

      public PostEvolve(EntityPlayerMP player, EntityPixelmon preEvo, boolean gigantamax, EntityPixelmon postEvo) {
         super(gigantamax);
         this.player = player;
         this.preEvo = preEvo;
         this.postEvo = postEvo;
      }
   }

   @Cancelable
   public static class BattleEvolve extends DynamaxEvent {
      public final PixelmonWrapper pw;

      public BattleEvolve(PixelmonWrapper pw, boolean gigantamax) {
         super(gigantamax);
         this.pw = pw;
      }
   }
}
