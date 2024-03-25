package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.comm.packetHandlers.statueEditor.EnumStatuePacketMode;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class StatueEvent extends Event {
   public final EntityPlayerMP player;

   public StatueEvent(EntityPlayerMP player) {
      this.player = player;
   }

   @Cancelable
   public static class DestroyStatue extends StatueEvent {
      public final EntityStatue statue;

      public DestroyStatue(EntityPlayerMP player, EntityStatue statue) {
         super(player);
         this.statue = statue;
      }
   }

   @Cancelable
   public static class ModifyStatue extends StatueEvent {
      private EntityStatue statue;
      public final EnumStatuePacketMode changeType;
      private Object value;

      public ModifyStatue(EntityPlayerMP player, EntityStatue statue, EnumStatuePacketMode changeType, Object value) {
         super(player);
         this.statue = statue;
         this.changeType = changeType;
         this.value = value;
      }

      public Object getValue() {
         return this.value;
      }

      public void setValue(Object value) {
         if (value != null && value.getClass() == this.value.getClass()) {
            this.value = value;
         }

      }

      public EntityStatue getStatue() {
         return this.statue;
      }

      public void setStatue(EntityStatue statue) {
         if (statue != null) {
            this.statue = statue;
         }

      }
   }

   @Cancelable
   public static class CreateStatue extends StatueEvent {
      public final WorldServer world;
      public final BlockPos location;
      private EntityStatue statue;

      public CreateStatue(EntityPlayerMP player, WorldServer world, BlockPos location, EntityStatue statue) {
         super(player);
         this.world = world;
         this.location = location;
         this.statue = statue;
      }

      public EntityStatue getStatue() {
         return this.statue;
      }

      public void setStatue(EntityStatue statue) {
         if (statue != null) {
            this.statue = statue;
         }

      }
   }
}
