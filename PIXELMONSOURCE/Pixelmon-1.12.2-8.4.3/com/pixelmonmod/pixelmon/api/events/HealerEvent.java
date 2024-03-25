package com.pixelmonmod.pixelmon.api.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class HealerEvent extends Event {
   public final EntityPlayer player;

   protected HealerEvent(EntityPlayer player) {
      this.player = player;
   }

   public static class Post extends HealerEvent {
      public final String npcName;

      public Post(EntityPlayer player, String npcName) {
         super(player);
         this.npcName = npcName;
      }
   }

   @Cancelable
   public static class Pre extends HealerEvent {
      public final BlockPos pos;
      public final boolean fromNpc;

      public Pre(EntityPlayer player, BlockPos pos, boolean fromNpc) {
         super(player);
         this.pos = pos;
         this.fromNpc = fromNpc;
      }
   }
}
