package com.pixelmonmod.pixelmon.api.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;

public abstract class SpawnAction {
   public SpawnInfo spawnInfo;
   public SpawnLocation spawnLocation;
   protected Entity entity;

   public SpawnAction(SpawnInfo spawnInfo, SpawnLocation spawnLocation) {
      this.spawnInfo = spawnInfo;
      this.spawnLocation = spawnLocation;
   }

   protected abstract Entity createEntity();

   public Entity getOrCreateEntity() {
      if (this.entity == null) {
         this.entity = this.createEntity();
      }

      return this.entity;
   }

   public void setEntity(Entity t) {
      if (t != null) {
         this.entity = t;
      }

   }

   @Nullable
   public Entity doSpawn(AbstractSpawner spawner) {
      if (!this.spawnLocation.location.world.func_175667_e(this.spawnLocation.location.pos)) {
         return null;
      } else {
         this.getOrCreateEntity();
         if (this.entity != null && !this.entity.field_70128_L) {
            if (CollectionHelper.some(this.spawnInfo.tags, (tag) -> {
               return !BetterSpawnerConfig.checkInterval(tag);
            })) {
               return null;
            } else if (this.spawnLocation.location.world.func_175723_af() != null && !this.spawnLocation.location.world.func_175723_af().func_177746_a(this.spawnLocation.location.pos)) {
               return null;
            } else {
               spawner.tweaks.forEach((tweak) -> {
                  tweak.doTweak(spawner, this);
               });
               SpawnEvent spawnEvent = new SpawnEvent(spawner, this);
               if (Pixelmon.EVENT_BUS.post(spawnEvent)) {
                  return null;
               } else {
                  this.entity.func_70107_b((double)this.spawnLocation.location.pos.func_177958_n() + 0.5, (double)this.spawnLocation.location.pos.func_177956_o(), (double)this.spawnLocation.location.pos.func_177952_p() + 0.5);
                  this.spawnLocation.location.world.func_72838_d(this.entity);
                  spawner.spawnedTracker.addEntity(this.entity);
                  spawner.lastSpawnTime = System.currentTimeMillis();
                  this.spawnInfo.tags.forEach(BetterSpawnerConfig::consumeInterval);
                  return this.entity;
               }
            }
         } else {
            return null;
         }
      }
   }

   public void applyLocationMutations() {
      Iterator var1 = this.spawnLocation.types.iterator();

      while(var1.hasNext()) {
         LocationType type = (LocationType)var1.next();
         if (this.spawnInfo.locationTypes.contains(type)) {
            type.mutator.accept(this.spawnLocation);
         }
      }

   }
}
