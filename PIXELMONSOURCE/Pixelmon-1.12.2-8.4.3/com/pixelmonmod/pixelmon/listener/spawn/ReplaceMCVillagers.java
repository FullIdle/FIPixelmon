package com.pixelmonmod.pixelmon.listener.spawn;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GeneralNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReplaceMCVillagers {
   @SubscribeEvent
   public static void onVillagerSpawn(EntityJoinWorldEvent event) {
      if (!event.getWorld().field_72995_K) {
         if (event.getEntity() instanceof EntityVillager) {
            event.setCanceled(true);
            int rand = RandomHelper.getRandomNumberBetween(1, 10);
            if (rand <= 2) {
               NPCTrainer trainer = new NPCTrainer(event.getWorld());
               BaseTrainer base = ServerNPCRegistry.trainers.getRandomBaseWithData();
               trainer.setBaseTrainer(base);
               trainer.init(base);
               trainer.func_70107_b(event.getEntity().field_70165_t, event.getEntity().field_70163_u, event.getEntity().field_70161_v);
               trainer.setEncounterMode(EnumEncounterMode.OncePerPlayer);
               trainer.npcLocation = SpawnLocationType.LandVillager;
               trainer.func_110163_bv();
               event.getWorld().func_72838_d(trainer);
            } else {
               NPCQuestGiver npc = new NPCQuestGiver(event.getWorld());
               GeneralNPCData data = ServerNPCRegistry.villagers.getRandom();
               npc.init(data);
               npc.setCustomSteveTexture(data.getRandomTexture());
               npc.func_70107_b(event.getEntity().field_70165_t, event.getEntity().field_70163_u, event.getEntity().field_70161_v);
               npc.setProfession(0);
               npc.initVilagerAI();
               npc.npcLocation = SpawnLocationType.LandVillager;
               npc.func_110163_bv();
               event.getWorld().func_72838_d(npc);
            }

            event.getEntity().func_70106_y();
         }

      }
   }
}
