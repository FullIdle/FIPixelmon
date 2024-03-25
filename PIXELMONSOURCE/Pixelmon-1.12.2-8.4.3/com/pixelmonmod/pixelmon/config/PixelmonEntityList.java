package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.EntityChairMount;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.EntityPokestop;
import com.pixelmonmod.pixelmon.entities.EntityWormhole;
import com.pixelmonmod.pixelmon.entities.bikes.EntityBike;
import com.pixelmonmod.pixelmon.entities.custom.EntityPixelmonPainting;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.npcs.NPCFisherman;
import com.pixelmonmod.pixelmon.entities.npcs.NPCNurseJoy;
import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
import com.pixelmonmod.pixelmon.entities.npcs.NPCRelearner;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrader;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityBreeding;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityEmptyPokeball;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityOccupiedPokeball;
import com.pixelmonmod.pixelmon.entities.projectiles.EntityHook;
import com.pixelmonmod.pixelmon.enums.EnumNPCTutorType;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.EntityId;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class PixelmonEntityList {
   static void registerEntities() {
      int entityID = 0;
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "npc_trainer"), NPCTrainer.class, "Trainer", entityID++, Pixelmon.instance, 100, 1, true);
      ++entityID;
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "npc_trader"), NPCTrader.class, "Trader", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "pixelmon"), EntityPixelmon.class, "Pixelmon", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "statue"), EntityStatue.class, "Statue", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "npc_chatting"), NPCChatting.class, "ChattingNPC", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "npc_questgiver"), NPCQuestGiver.class, "QuestGiverNPC", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "npc_relearner"), NPCRelearner.class, "Relearner", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "npc_tutor"), NPCTutor.class, "Tutor", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "empty_pokeball"), EntityEmptyPokeball.class, "Pokeball", entityID++, Pixelmon.instance, 80, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "hook"), EntityHook.class, "Hook", entityID++, Pixelmon.instance, 75, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "chair_mount"), EntityChairMount.class, "EntityChairMount", entityID++, Pixelmon.instance, 80, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "npc_nursejoy"), NPCNurseJoy.class, "NurseJoy", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "npc_shopkeeper"), NPCShopkeeper.class, "Shopkeeper", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "painting"), EntityPixelmonPainting.class, "PixelmonPainting", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "occupied_pokeball"), EntityOccupiedPokeball.class, "OccupiedPokeball", entityID++, Pixelmon.instance, 80, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "ultra_wormhole"), EntityWormhole.class, "Wormhole", entityID++, Pixelmon.instance, 128, 1, false);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "breeding"), EntityBreeding.class, "Breeding", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "bike"), EntityBike.class, "Bike", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "npc_oldfisherman"), NPCFisherman.class, "OldFisherman", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "pokestop"), EntityPokestop.class, "Pokestop", entityID++, Pixelmon.instance, 100, 1, true);
      EntityRegistry.registerModEntity(new ResourceLocation("pixelmon", "den"), EntityDen.class, "Den", entityID++, Pixelmon.instance, 100, 1, true);
      remap();
   }

   public static EntityLiving createEntityByName(String entityName, World world) {
      return createEntityByName(entityName, world, (String)null);
   }

   public static EntityLiving createEntityByName(String entityName, World world, String biomeID) {
      EntityLiving entity = null;

      try {
         EnumEntityListClassType type = null;
         if (EnumSpecies.hasPokemonAnyCase(entityName)) {
            type = EnumEntityListClassType.Pixelmon;
            entityName = EnumSpecies.getFromNameAnyCase(entityName).name;
         }

         if (type == EnumEntityListClassType.Pixelmon) {
            entity = (new PokemonSpec(entityName)).create(world);
         } else if (ServerNPCRegistry.trainers.has(entityName)) {
            BaseTrainer trainer = ServerNPCRegistry.trainers.get(entityName);
            entity = new NPCTrainer(world);
            ((NPCTrainer)entity).init(trainer);
         } else {
            try {
               EnumNPCType npcType = EnumNPCType.valueOf(entityName);
               if (npcType == EnumNPCType.Trader) {
                  entity = new NPCTrader(world);
               } else {
                  NPCTutor tutor;
                  if (npcType == EnumNPCType.Tutor) {
                     tutor = new NPCTutor(world);
                     entity = tutor;
                     tutor.setTutorType(EnumNPCTutorType.TUTOR);
                  } else if (npcType == EnumNPCType.TransferTutor) {
                     tutor = new NPCTutor(world);
                     entity = tutor;
                     tutor.setTutorType(EnumNPCTutorType.TRANSFER);
                  } else if (npcType == EnumNPCType.Shopkeeper) {
                     entity = new NPCShopkeeper(world);
                     ((NPCShopkeeper)entity).initWanderingAI();
                     ((NPCShopkeeper)entity).initRandom(biomeID);
                  } else if (npcType == EnumNPCType.Relearner) {
                     entity = new NPCRelearner(world);
                  } else if (npcType == EnumNPCType.OldFisherman) {
                     entity = new NPCFisherman(world);
                  } else {
                     entity = new NPCTrainer(world);
                  }
               }

               ((EntityNPC)entity).init(entityName);
            } catch (IllegalArgumentException var7) {
               entity = new NPCTrainer(world);
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      return (EntityLiving)entity;
   }

   /** @deprecated */
   @Deprecated
   public static Entity createEntityFromNBT(NBTTagCompound compound, World world) {
      EntityPixelmon pixelmon = null;

      try {
         pixelmon = new EntityPixelmon(world);
         pixelmon.setPokemon(Pixelmon.pokemonFactory.create(compound));
         pixelmon.getPokemonData().updateDimensionAndEntityID(world.field_73011_w.getDimension(), pixelmon.func_145782_y());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if (pixelmon != null) {
         pixelmon.func_70020_e(compound);
      } else if (PixelmonConfig.printErrors) {
         System.out.println("Skipping Entity with id " + compound.func_74779_i("id"));
      }

      return pixelmon;
   }

   private static void remap() {
      Map remap = (Map)ObfuscationReflectionHelper.getPrivateValue(EntityId.class, (Object)null, 0);
      remap.put("pixelmon.Trainer", "pixelmon:npc_trainer");
      remap.put("pixelmon.Hallwoeen", "pixelmon:halloween");
      remap.put("pixelmon.Trader", "pixelmon:npc_trader");
      remap.put("pixelmon.Pixelmon", "pixelmon:pixelmon");
      remap.put("pixelmon.Statue", "pixelmon:statue");
      remap.put("pixelmon.ChattingNPC", "pixelmon:npc_chatting");
      remap.put("pixelmon.Relearner", "pixelmon:npc_relearner");
      remap.put("pixelmon.Tutor", "pixelmon:npc_tutor");
      remap.put("pixelmon.Pokeball", "pixelmon:empty_pokeball");
      remap.put("pixelmon.Hook", "pixelmon:hook");
      remap.put("pixelmon.EntityChairMount", "pixelmon:chair_mount");
      remap.put("pixelmon.NurseJoy", "pixelmon:npc_nursejoy");
      remap.put("pixelmon.Shopkeeper", "pixelmon:npc_shopkeeper");
      remap.put("pixelmon.PixelmonPainting", "pixelmon:painting");
      remap.put("pixelmon.OccupiedPokeball", "pixelmon:occupied_pokeball");
      remap.put("pixelmon.OldFisherman", "pixelmon:oldfisherman");
   }
}
