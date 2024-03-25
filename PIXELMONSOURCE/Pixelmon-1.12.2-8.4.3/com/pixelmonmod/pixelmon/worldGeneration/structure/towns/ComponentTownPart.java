package com.pixelmonmod.pixelmon.worldGeneration.structure.towns;

import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.enums.EnumShopKeeperType;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureRegistry;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.IVillageStructure;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.StructureScattered;
import com.pixelmonmod.pixelmon.worldGeneration.structure.worldGen.WorldGenGym;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class ComponentTownPart extends StructureVillagePieces.House1 implements IVillageStructure {
   StructureInfo info;

   public ComponentTownPart() {
      this.info = null;
   }

   public ComponentTownPart(StructureInfo info, StructureVillagePieces.Start villagePiece, int par2, Random random, StructureBoundingBox structureBoundingBox, EnumFacing facing) {
      this.info = info;
      this.func_186164_a(facing);
      this.field_74887_e = structureBoundingBox;
   }

   public static ComponentTownPart buildComponent(StructureVillagePieces.Start villagePiece, List pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5, StructureInfo info) {
      facing = facing.func_176746_e();
      StructureScattered structureGenerated = info.createStructure(random, new BlockPos(p1, p2, p3), true, true, facing, false);
      StructureBoundingBox structureBoundingBox = structureGenerated.func_74874_b();
      return func_74895_a(structureBoundingBox) && StructureComponent.func_74883_a(pieces, structureBoundingBox) == null ? new ComponentTownPart(info, villagePiece, p5, random, structureBoundingBox, facing) : null;
   }

   public boolean func_74875_a(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
      if (this.field_143015_k < 0) {
         this.field_143015_k = this.func_74889_b(worldIn, structureBoundingBoxIn);
         if (this.field_143015_k < 0 || this.field_74887_e == null || this.info == null) {
            return true;
         }

         this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78895_b - this.info.getDepth(), 0);
      }

      if (this.info != null && this.field_74887_e != null) {
         this.info.createStructure(randomIn, new BlockPos(this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c), true, true, this.func_186165_e(), false).func_74875_a(worldIn, randomIn, structureBoundingBoxIn);
         spawnVillagers(this, this.info, worldIn, structureBoundingBoxIn);
         WorldGenGym.lastTownBB = structureBoundingBoxIn;
         return true;
      } else {
         return true;
      }
   }

   private boolean closeTo(StructureBoundingBox structureBoundingBoxIn) {
      Vec3i center = getCenter(structureBoundingBoxIn);
      if (getCenter(WorldGenGym.lastTownBB).func_177951_i(center) < 6400.0) {
         return true;
      } else {
         synchronized(WorldGenGym.usedTownsList) {
            for(int i = 0; i < WorldGenGym.usedTownsList.size(); ++i) {
               if (getCenter((StructureBoundingBox)WorldGenGym.usedTownsList.get(i)).func_177951_i(center) < 6400.0) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public static void spawnVillagers(IVillageStructure structure, StructureInfo info, World worldIn, StructureBoundingBox structureBoundingBoxIn) {
      Iterator var4 = info.getNPCS().iterator();

      while(var4.hasNext()) {
         NPCPlacementInfo npcInfo = (NPCPlacementInfo)var4.next();
         if (npcInfo.npcType == EnumNPCType.Shopkeeper) {
            ShopkeeperData shopkeeper = null;
            if (npcInfo.extraData.equalsIgnoreCase("Main")) {
               shopkeeper = ServerNPCRegistry.shopkeepers.getRandom(EnumShopKeeperType.PokemartMain);
            } else if (npcInfo.extraData.equalsIgnoreCase("Secondary")) {
               shopkeeper = ServerNPCRegistry.shopkeepers.getRandom(EnumShopKeeperType.PokemartSecond);
            }

            if (shopkeeper != null) {
               NPCShopkeeper entity = new NPCShopkeeper(worldIn);
               entity.init(shopkeeper);
               entity.initDefaultAI();
               entity.npcLocation = SpawnLocationType.LandVillager;
               entity.func_110163_bv();
               spawnVillager(structure, worldIn, structureBoundingBoxIn, npcInfo.x, npcInfo.y, npcInfo.z, 1, entity);
            }
         } else if (npcInfo.npcType != null) {
            EntityNPC npc = npcInfo.npcType.constructNew(worldIn);
            if (npc != null) {
               npc.setAIMode(EnumTrainerAI.StandStill);
               npc.initAI();
               npc.func_110163_bv();
               spawnVillager(structure, worldIn, structureBoundingBoxIn, npcInfo.x, npcInfo.y, npcInfo.z, 1, npc);
            }
         }
      }

   }

   protected static void spawnVillager(IVillageStructure structure, World worldIn, StructureBoundingBox bbox, int x, int y, int z, int num, EntityLiving entity) {
      int villagersSpawned = 0;
      if (villagersSpawned < num) {
         for(int i1 = villagersSpawned; i1 < num; ++i1) {
            int xoff = structure.getNPCXWithOffset(x + i1, z);
            int yoff = structure.getNPCYWithOffset(y);
            int zoff = structure.getNPCZWithOffset(x + i1, z);
            if (!bbox.func_175898_b(new BlockPos(xoff, yoff, zoff))) {
               break;
            }

            ++villagersSpawned;
            entity.func_70012_b((double)xoff + 0.5, (double)yoff, (double)zoff + 0.5, 0.0F, 0.0F);
            worldIn.func_72838_d(entity);
         }
      }

   }

   protected void func_143011_b(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
      if (tagCompound.func_74764_b("strucID")) {
         this.info = StructureRegistry.getByID(tagCompound.func_74779_i("strucID"));
      }

      super.func_143011_b(tagCompound, p_143011_2_);
   }

   protected void func_143012_a(NBTTagCompound tagCompound) {
      if (this.info != null) {
         tagCompound.func_74778_a("strucID", this.info.id);
      }

      super.func_143012_a(tagCompound);
   }

   public int getNPCXWithOffset(int x, int z) {
      return super.func_74865_a(x, z);
   }

   public int getNPCYWithOffset(int y) {
      return super.func_74862_a(y);
   }

   public int getNPCZWithOffset(int x, int z) {
      return super.func_74873_b(x, z);
   }

   public static BlockPos getCenter(StructureBoundingBox boundingBox) {
      return new BlockPos(boundingBox.field_78897_a + (boundingBox.field_78893_d - boundingBox.field_78897_a + 1) / 2, boundingBox.field_78895_b + (boundingBox.field_78894_e - boundingBox.field_78895_b + 1) / 2, boundingBox.field_78896_c + (boundingBox.field_78892_f - boundingBox.field_78896_c + 1) / 2);
   }
}
