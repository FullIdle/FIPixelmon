package com.pixelmonmod.pixelmon.worldGeneration.structure.worldGen;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.PixelmonStructureGenerationEvent;
import com.pixelmonmod.pixelmon.config.PixelmonItemsBadges;
import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GymNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.enums.EnumOldGenMode;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureRegistry;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.GymInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.WorldGymData;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.WorldGymInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.towns.NPCPlacementInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.StructureScattered;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenGym extends MapGenScatteredFeature implements IWorldGenerator {
   public static StructureBoundingBox lastTownBB;
   static final int distance = 30;
   public static final List usedTownsList = new ArrayList();
   public static final List gymsGenerated = new ArrayList();
   public static boolean locked = false;

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (lastTownBB != null) {
         if (!usedTownsList.contains(lastTownBB)) {
            if (!locked) {
               int minX = chunkX * 16;
               int maxX = minX + 16;
               int minZ = chunkZ * 16;
               int maxZ = minZ + 16;
               if (this.isClose(minX, maxX, lastTownBB.field_78897_a, lastTownBB.field_78893_d) && this.isClose(minZ, maxZ, lastTownBB.field_78896_c, lastTownBB.field_78892_f)) {
                  WorldGymData data = (WorldGymData)world.getPerWorldStorage().func_75742_a(WorldGymData.class, "gyminfo");
                  if (data == null) {
                     data = new WorldGymData();
                     world.getPerWorldStorage().func_75745_a("gyminfo", data);
                  }

                  GymInfo gyminfo = StructureRegistry.getNextGym(data, random);
                  if (gyminfo == null || gyminfo.type == null || this.getBadge(data, gyminfo.type) == null) {
                     if (gyminfo != null && gyminfo.type == null) {
                        Pixelmon.LOGGER.warn("Gym type info not found. External JSON files are probably not updated correctly.");
                     }

                     return;
                  }

                  for(int i = 0; i < 10; ++i) {
                     int xPos = random.nextInt(16) + chunkX * 16;
                     int yPos = 64;
                     int zPos = random.nextInt(16) + chunkZ * 16;
                     BlockPos pos = new BlockPos(xPos, yPos, zPos);
                     StructureScattered s = gyminfo.createStructure(random, pos, true, false, (EnumFacing)null, false);
                     if (this.canSpawnStructureAtCoords(world, s, gyminfo, pos)) {
                        locked = true;
                        GymPlacementInfo info = new GymPlacementInfo(s, gyminfo, pos, lastTownBB);
                        ItemStack badge = this.getBadge(data, info.gyminfo.type);
                        info.struc.signItem = badge;
                        boolean generated = info.struc.generate(world, random);
                        PixelmonStructureGenerationEvent generationEvent = new PixelmonStructureGenerationEvent.Post(world, info.struc, info.gyminfo, info.gymPos, generated);
                        Pixelmon.EVENT_BUS.post(generationEvent);
                        if (generated) {
                           gymsGenerated.add(pos);
                           info.gyminfo.level = data.getGymLevel();
                           spawnNPCs(world, info.struc, info.gyminfo, badge);
                           data.addGym(info.gyminfo.name, badge, info.gyminfo.level);
                           usedTownsList.add(info.lastTownBB);
                           Pixelmon.LOGGER.info("Gym spawned at " + info.struc.func_74874_b().field_78897_a + " " + info.struc.func_74874_b().field_78895_b + " " + info.struc.func_74874_b().field_78896_c);
                        }
                        break;
                     }
                  }

                  locked = false;
               }

            }
         }
      }
   }

   private ItemStack getBadge(WorldGymData data, EnumType[] types) {
      List clearedList = PixelmonItemsBadges.getBadgeList(types);
      Iterator var4 = data.getGymList().iterator();

      while(var4.hasNext()) {
         WorldGymInfo info = (WorldGymInfo)var4.next();
         if (clearedList.contains(info.badge.func_77973_b())) {
            clearedList.remove(info.badge.func_77973_b());
         }
      }

      if (clearedList.size() == 0) {
         clearedList = PixelmonItemsBadges.getBadgeList(types);
      }

      return new ItemStack((Item)RandomHelper.getRandomElementFromList(clearedList));
   }

   public static void spawnNPCs(World world, StructureScattered s, GymInfo gymInfo) {
      spawnNPCs(world, s, gymInfo, new ItemStack(PixelmonItemsBadges.getRandomBadge(gymInfo.type)));
   }

   public static void spawnNPCs(World world, StructureScattered s, GymInfo gymInfo, ItemStack badge) {
      Iterator var4 = gymInfo.getNPCS().iterator();

      while(true) {
         while(var4.hasNext()) {
            NPCPlacementInfo npcInfo = (NPCPlacementInfo)var4.next();
            GymNPCData data;
            if (npcInfo.npcType != EnumNPCType.ChattingNPC && npcInfo.npcType != EnumNPCType.QuestGiver) {
               if (npcInfo.npcType == EnumNPCType.Trainer) {
                  NPCTrainer trainer = new NPCTrainer(world);
                  data = ServerNPCRegistry.getGymMember(npcInfo.npcName);
                  if (data == null) {
                     Pixelmon.LOGGER.warn("Can't find Gym NPC " + npcInfo.npcName);
                  } else {
                     trainer.init(data, gymInfo, npcInfo.tier);
                     trainer.setAIMode(EnumTrainerAI.StillAndEngage);
                     trainer.initAI();
                     trainer.setEncounterMode(EnumEncounterMode.OncePerPlayer);
                     trainer.setMegaItem(trainer.isGymLeader ? EnumMegaItemsUnlocked.Both : EnumMegaItemsUnlocked.None);
                     trainer.setOldGenMode(EnumOldGenMode.World);
                     trainer.setStartRotationYaw(getRotation(npcInfo.rotation, s.getFacing()));
                     if (npcInfo.drops != null) {
                        List dropsList = new ArrayList();
                        dropsList.add(RandomHelper.getRandomElementFromList(npcInfo.drops));
                        if (npcInfo.tier == 0 && badge != null) {
                           dropsList.add(badge);
                        }

                        trainer.updateDrops((ItemStack[])dropsList.toArray(new ItemStack[dropsList.size()]));
                     }

                     trainer.func_110163_bv();
                     spawnVillager(world, s, npcInfo.x, npcInfo.y, npcInfo.z, trainer);
                  }
               }
            } else {
               NPCQuestGiver npc = new NPCQuestGiver(world);
               data = ServerNPCRegistry.getGymMember(npcInfo.npcName);
               if (data == null) {
                  Pixelmon.LOGGER.warn("Can't find Gym NPC " + npcInfo.npcName);
               } else {
                  npc.init(data);
                  npc.setCustomSteveTexture((String)data.textures.get(data.getRandomTextureIndex()));
                  npc.initDefaultAI();
                  npc.func_110163_bv();
                  spawnVillager(world, s, npcInfo.x, npcInfo.y, npcInfo.z, npc);
               }
            }
         }

         return;
      }
   }

   private static float getRotation(int rotation, EnumFacing facing) {
      switch (facing) {
         case EAST:
            return (float)rotation;
         case WEST:
            return (float)(rotation + 180);
         case NORTH:
            return (float)(rotation - 90);
         case SOUTH:
            return (float)(rotation + 90);
         default:
            return 0.0F;
      }
   }

   protected static void spawnVillager(World worldIn, StructureScattered s, int x, int y, int z, EntityLiving entity) {
      int xoff = s.getX(x, z);
      int yoff = s.getY(y);
      int zoff = s.getZ(x, z);
      if (s.func_74874_b().func_175898_b(new BlockPos(xoff, yoff, zoff))) {
         entity.func_70012_b((double)xoff + 0.5, (double)yoff, (double)zoff + 0.5, 0.0F, 0.0F);
         worldIn.func_72838_d(entity);
      }
   }

   private boolean isClose(int min1, int max1, int min2, int max2) {
      return Math.abs(min1 - min2) < 30 || Math.abs(min1 - max2) < 30 || Math.abs(max1 - min2) < 30 || Math.abs(max1 - max2) < 30;
   }

   protected boolean canSpawnStructureAtCoords(World world, StructureScattered s, GymInfo gyminfo, BlockPos pos) {
      Iterator var5 = gymsGenerated.iterator();

      BlockPos p;
      do {
         if (!var5.hasNext()) {
            if (usedTownsList.contains(lastTownBB)) {
               return false;
            }

            PixelmonStructureGenerationEvent generationEvent = new PixelmonStructureGenerationEvent.Pre(world, s, gyminfo, pos);
            Pixelmon.EVENT_BUS.post(generationEvent);
            if (!generationEvent.isCanceled()) {
               return true;
            }

            return false;
         }

         p = (BlockPos)var5.next();
      } while(!(p.func_177951_i(pos) < 90000.0));

      return false;
   }

   private class GymPlacementInfo {
      public StructureScattered struc;
      public GymInfo gyminfo;
      public BlockPos gymPos;
      public StructureBoundingBox lastTownBB;

      public GymPlacementInfo(StructureScattered s, GymInfo gyminfo, BlockPos pos, StructureBoundingBox lastTownBB) {
         this.struc = s;
         this.gyminfo = gyminfo;
         this.gymPos = pos;
         this.lastTownBB = lastTownBB;
      }
   }
}
