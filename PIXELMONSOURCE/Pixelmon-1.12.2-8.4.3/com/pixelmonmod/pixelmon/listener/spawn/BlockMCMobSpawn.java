package com.pixelmonmod.pixelmon.listener.spawn;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class BlockMCMobSpawn {
   @SubscribeEvent
   public static void checkSpawn(LivingSpawnEvent.CheckSpawn event) {
      if (isDisabledEntity(event.getEntityLiving(), event.getWorld())) {
         event.setResult(Result.DENY);
      }

   }

   @SubscribeEvent
   public static void checkEntitySpawn(EntityJoinWorldEvent event) {
      if (isDisabledEntity(event.getEntity(), event.getWorld())) {
         event.setCanceled(true);
      }

      if (event.getEntity().getEntityData().func_74764_b("IsRaidTrainer")) {
         event.getEntity().func_70106_y();
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public static void blockEntitiesSpawn(PopulateChunkEvent.Populate event) {
      if (event.getType() == EventType.ANIMALS && !PixelmonConfig.allowNonPixelmonMobs) {
         event.setResult(Result.DENY);
      }

   }

   private static boolean isDisabledEntity(Entity entity, World world) {
      if (!PixelmonConfig.allowNonPixelmonMobs && !world.field_72995_K) {
         if (entity instanceof EntityGuardian || entity instanceof EntityCaveSpider || entity instanceof EntitySilverfish || entity instanceof EntityBlaze || entity instanceof EntityWitch) {
            return true;
         }

         if (entity instanceof EntityHorse) {
            EntityHorse horse = (EntityHorse)entity;
            if (horse.func_70662_br()) {
               return true;
            }
         }
      }

      return false;
   }
}
