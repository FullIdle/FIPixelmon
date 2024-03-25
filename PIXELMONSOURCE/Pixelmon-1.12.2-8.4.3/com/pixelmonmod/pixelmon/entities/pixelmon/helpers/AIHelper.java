package com.pixelmonmod.pixelmon.entities.pixelmon.helpers;

import com.pixelmonmod.pixelmon.AI.AIExecuteAction;
import com.pixelmonmod.pixelmon.AI.AIFlying;
import com.pixelmonmod.pixelmon.AI.AIFlyingPersistent;
import com.pixelmonmod.pixelmon.AI.AIIsInBattle;
import com.pixelmonmod.pixelmon.AI.AIMoveTowardsBlock;
import com.pixelmonmod.pixelmon.AI.AIMoveTowardsTarget;
import com.pixelmonmod.pixelmon.AI.AISwimming;
import com.pixelmonmod.pixelmon.AI.AITargetNearest;
import com.pixelmonmod.pixelmon.AI.AITeleportAway;
import com.pixelmonmod.pixelmon.AI.AITempt;
import com.pixelmonmod.pixelmon.AI.AIWander;
import com.pixelmonmod.pixelmon.blocks.enums.EnumSpawnerAggression;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity4Interactions;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumAggression;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;

public class AIHelper {
   int i = 0;

   public void populateTasks(Entity4Interactions entity, EntityAITasks tasks) {
      EntityPixelmon pixelmon = (EntityPixelmon)entity;
      if (!tasks.field_75782_a.isEmpty()) {
         tasks.field_75782_a.clear();
      }

      this.initBaseAI(pixelmon, tasks);
      if (pixelmon.getSpawnLocation() != SpawnLocationType.Land || entity.getBaseStats().canFly() || entity.getBaseStats().IsRideable() && entity.getBaseStats().getTypeList().contains(EnumType.Water)) {
         if (entity.getBaseStats().canFly() && entity.getFlyingParameters() != null) {
            if (pixelmon.getSpawnLocation() == SpawnLocationType.AirPersistent) {
               this.initFlyingPersistentAI(pixelmon, tasks);
            } else {
               this.initFlyingAI(pixelmon, tasks);
            }
         } else if (pixelmon.getSpawnLocation() == SpawnLocationType.Water) {
            this.initSwimmingAI(pixelmon, tasks);
         } else {
            this.initGroundAI(pixelmon, tasks);
         }
      } else {
         this.initGroundAI(pixelmon, tasks);
      }

   }

   public PathNavigate createNavigator(Entity4Interactions pixelmon) {
      if (pixelmon.getSpawnLocation() != SpawnLocationType.Land || pixelmon.getBaseStats().canFly() || pixelmon.getBaseStats().IsRideable() && pixelmon.getBaseStats().getTypeList().contains(EnumType.Water)) {
         if (pixelmon.getBaseStats().canFly() && pixelmon.getFlyingParameters() != null) {
            return pixelmon.getSpawnLocation() == SpawnLocationType.AirPersistent ? new PathNavigateFlying(pixelmon, pixelmon.field_70170_p) : new PathNavigateFlying(pixelmon, pixelmon.field_70170_p);
         } else {
            return pixelmon.getSpawnLocation() == SpawnLocationType.Water ? new PathNavigateGround(pixelmon, pixelmon.field_70170_p) : new PathNavigateGround(pixelmon, pixelmon.field_70170_p);
         }
      } else {
         return new PathNavigateGround(pixelmon, pixelmon.field_70170_p);
      }
   }

   private void initBaseAI(EntityPixelmon pixelmon, EntityAITasks tasks) {
      tasks.func_75776_a(this.i++, new AIIsInBattle(pixelmon));
      tasks.func_75776_a(this.i++, new AIMoveTowardsTarget(pixelmon, 15.0F));
      tasks.func_75776_a(this.i++, new AIExecuteAction(pixelmon));
      if (pixelmon.getSpawnLocation() != SpawnLocationType.Water && (!pixelmon.getBaseStats().IsRideable() || !pixelmon.getBaseStats().getTypeList().contains(EnumType.Water))) {
         if ((PixelmonConfig.isAggressionAllowed || pixelmon.spawner != null && pixelmon.spawner.aggression == EnumSpawnerAggression.Aggressive) && pixelmon.aggression == EnumAggression.aggressive) {
            tasks.func_75776_a(this.i++, new AITargetNearest(pixelmon, 10.0F, true));
         } else if (pixelmon.aggression == EnumAggression.timid) {
            tasks.func_75776_a(this.i++, new EntityAIAvoidEntity(pixelmon, EntityPlayer.class, 16.0F, 0.23000000417232513, 0.4000000059604645));
         }
      }

      if (pixelmon.func_70902_q() != null) {
         tasks.func_75776_a(this.i++, new AIMoveTowardsBlock(pixelmon, 25.0F));
      }

   }

   private void initFlyingPersistentAI(EntityPixelmon pixelmon, EntityAITasks tasks) {
      tasks.func_75776_a(this.i++, new EntityAIFollowOwner(pixelmon, 1.0, 10.0F, 4.0F));
      tasks.func_75776_a(this.i++, new AITempt(pixelmon, false, new Item[]{PixelmonItems.rareCandy, PixelmonItems.mint}));
      tasks.func_75776_a(this.i++, new AIFlyingPersistent(pixelmon));
   }

   private void initSwimmingAI(EntityPixelmon pixelmon, EntityAITasks tasks) {
      tasks.func_75776_a(this.i++, new EntityAIFollowOwner(pixelmon, 1.0, 10.0F, 4.0F));
      tasks.func_75776_a(this.i++, new AITempt(pixelmon, false, new Item[]{PixelmonItems.rareCandy, PixelmonItems.mint}));
      tasks.func_75776_a(this.i++, new AISwimming(pixelmon));
   }

   private void initFlyingAI(EntityPixelmon pixelmon, EntityAITasks tasks) {
      tasks.func_75776_a(this.i++, new EntityAISwimming(pixelmon));
      tasks.func_75776_a(this.i++, new EntityAIFollowOwner(pixelmon, 1.0, 10.0F, 4.0F));
      tasks.func_75776_a(this.i++, new AITempt(pixelmon, false, new Item[]{PixelmonItems.rareCandy, PixelmonItems.mint}));
      tasks.func_75776_a(this.i++, new EntityAIWatchClosest(pixelmon, EntityPixelmon.class, 8.0F));
      tasks.func_75776_a(this.i++, new AIFlying(pixelmon));
   }

   private void initGroundAI(EntityPixelmon entity, EntityAITasks tasks) {
      if (entity.getBaseStats().getTypeList().contains(EnumType.Fire)) {
         if (entity.func_70661_as() instanceof PathNavigateGround) {
            ((PathNavigateGround)entity.func_70661_as()).func_179693_d(true);
         }

         if (entity.func_70661_as() instanceof PathNavigateFlying) {
            ((PathNavigateFlying)entity.func_70661_as()).func_192877_c(true);
         }
      }

      if (entity.getSpecies() == EnumSpecies.Abra) {
         tasks.func_75776_a(this.i++, new AITeleportAway(entity));
      }

      tasks.func_75776_a(this.i++, new EntityAISwimming(entity));
      tasks.func_75776_a(this.i++, new EntityAIFollowOwner(entity, 1.0, 10.0F, 4.0F));
      tasks.func_75776_a(this.i++, new AITempt(entity, false, new Item[]{PixelmonItems.rareCandy, PixelmonItems.mint}));
      tasks.func_75776_a(this.i++, new AIWander(entity));
      tasks.func_75776_a(this.i++, new EntityAIWatchClosest(entity, EntityPixelmon.class, 8.0F));
      tasks.func_75776_a(this.i++, new EntityAILookIdle(entity));
   }
}
