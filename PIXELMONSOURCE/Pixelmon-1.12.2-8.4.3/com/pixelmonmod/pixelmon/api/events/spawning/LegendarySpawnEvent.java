package com.pixelmonmod.pixelmon.api.events.spawning;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.spawning.LegendarySpawner;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LegendarySpawnEvent extends Event {
   public final AbstractSpawner spawner;

   protected LegendarySpawnEvent(AbstractSpawner spawner) {
      this.spawner = spawner;
   }

   @Cancelable
   public static class DoSpawn extends LegendarySpawnEvent {
      public final SpawnAction action;

      public DoSpawn(AbstractSpawner spawner, SpawnAction action) {
         super(spawner);
         this.action = action;
      }

      public EnumSpecies getLegendary() {
         return ((EntityPixelmon)this.action.getOrCreateEntity()).getSpecies();
      }
   }

   @Cancelable
   public static class ChoosePlayer extends LegendarySpawnEvent {
      public EntityPlayerMP player;
      public final ArrayList clusters;

      public ChoosePlayer(LegendarySpawner spawner, EntityPlayerMP player, ArrayList clusters) {
         super(spawner);
         this.player = player;
         this.clusters = clusters;
      }
   }
}
