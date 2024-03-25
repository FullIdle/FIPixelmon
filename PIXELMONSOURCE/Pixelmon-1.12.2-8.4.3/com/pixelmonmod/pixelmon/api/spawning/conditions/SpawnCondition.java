package com.pixelmonmod.pixelmon.api.spawning.conditions;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.conditions.data.SpawnTime;
import com.pixelmonmod.pixelmon.api.world.WeatherType;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class SpawnCondition {
   public static Class targetedSpawnCondition = SpawnCondition.class;
   public ArrayList times = null;
   public ArrayList weathers = null;
   private ArrayList realWorldSpawnTimes = null;
   public transient Set cachedBaseBlocks = Sets.newHashSet();
   public transient Set cachedNeededNearbyBlocks = Sets.newHashSet();
   public transient Set biomes = Sets.newHashSet();
   private ArrayList stringBiomes = null;
   private ArrayList baseBlocks = null;
   private ArrayList neededNearbyBlocks = null;
   public ArrayList variant = null;
   private ArrayList partyHeadSpecies = null;
   public String tag = null;
   public Boolean seesSky = null;
   public Biome.TempCategory temperature = null;
   public ArrayList dimensions = null;
   public ArrayList worlds = null;
   public Integer minX = null;
   public Integer maxX = null;
   public Integer minY = null;
   public Integer maxY = null;
   public Integer minZ = null;
   public Integer maxZ = null;
   public Integer moonPhase = null;
   public Integer minLightLevel = null;
   public Integer maxLightLevel = null;
   static ArrayList currentTimes = new ArrayList();

   public void onExport() {
      this.stringBiomes = new ArrayList();
      Iterator var1 = this.biomes.iterator();

      while(var1.hasNext()) {
         Biome biome = (Biome)var1.next();
         this.stringBiomes.add(biome.getRegistryName().toString());
      }

      var1 = this.cachedBaseBlocks.iterator();

      Block block;
      while(var1.hasNext()) {
         block = (Block)var1.next();
         this.baseBlocks.add(block.getRegistryName().toString());
      }

      var1 = this.cachedNeededNearbyBlocks.iterator();

      while(var1.hasNext()) {
         block = (Block)var1.next();
         this.neededNearbyBlocks.add(block.getRegistryName().toString());
      }

   }

   public static void cacheRegistry(Class clazz, ArrayList from, Set to, HashMap categories) {
      if (from != null) {
         IForgeRegistry registry = RegistryManager.ACTIVE.getRegistry(clazz);
         Iterator var5 = from.iterator();

         while(true) {
            label34:
            while(var5.hasNext()) {
               String id = (String)var5.next();
               if (categories.containsKey(id)) {
                  to.addAll((Collection)categories.get(id));
               } else {
                  Iterator var7 = registry.getEntries().iterator();

                  Map.Entry entry;
                  do {
                     if (!var7.hasNext()) {
                        continue label34;
                     }

                     entry = (Map.Entry)var7.next();
                  } while(!((ResourceLocation)entry.getKey()).toString().equalsIgnoreCase(id) && !((ResourceLocation)entry.getKey()).func_110623_a().equalsIgnoreCase(id));

                  to.add(entry.getValue());
               }
            }

            return;
         }
      }
   }

   public void onImport() {
      if (this.stringBiomes == null) {
         this.stringBiomes = new ArrayList();
      }

      cacheRegistry(Biome.class, this.stringBiomes, this.biomes, BetterSpawnerConfig.INSTANCE.cachedBiomeCategories);
      cacheRegistry(Block.class, this.baseBlocks, this.cachedBaseBlocks, BetterSpawnerConfig.INSTANCE.cachedBlockCategories);
      cacheRegistry(Block.class, this.neededNearbyBlocks, this.cachedNeededNearbyBlocks, BetterSpawnerConfig.INSTANCE.cachedBlockCategories);
   }

   public boolean fits(SpawnInfo spawnInfo, SpawnLocation spawnLocation) {
      if (!this.doesHeadOfPartyMatch(spawnLocation)) {
         return false;
      } else if (!this.doesWorldTimeFit(spawnLocation.location.world)) {
         return false;
      } else if (!this.doesRealTimeFit()) {
         return false;
      } else if (this.tag != null && (spawnInfo.tags == null || !spawnInfo.tags.contains(this.tag))) {
         return false;
      } else if (this.seesSky != null && this.seesSky != spawnLocation.seesSky) {
         return false;
      } else if (this.temperature != null && this.temperature != spawnLocation.biome.func_150561_m()) {
         return false;
      } else if (this.dimensions != null && !this.dimensions.isEmpty() && !this.dimensions.contains(spawnLocation.location.world.field_73011_w.getDimension())) {
         return false;
      } else if (this.worlds != null && !this.worlds.isEmpty() && !this.worlds.contains(spawnLocation.location.world.func_72912_H().func_76065_j())) {
         return false;
      } else if (this.minX != null && spawnLocation.location.pos.func_177958_n() < this.minX) {
         return false;
      } else if (this.maxX != null && spawnLocation.location.pos.func_177958_n() > this.maxX) {
         return false;
      } else if (this.minY != null && spawnLocation.location.pos.func_177956_o() < this.minY) {
         return false;
      } else if (this.maxY != null && spawnLocation.location.pos.func_177956_o() > this.maxY) {
         return false;
      } else if (this.minZ != null && spawnLocation.location.pos.func_177952_p() < this.minZ) {
         return false;
      } else if (this.maxZ != null && spawnLocation.location.pos.func_177952_p() > this.maxZ) {
         return false;
      } else if (this.weathers != null && !this.weathers.isEmpty() && !this.weathers.contains(WeatherType.get(spawnLocation.location.world))) {
         return false;
      } else if ((spawnLocation.location.world.func_175624_G() != WorldType.field_77138_c || !Pixelmon.devEnvironment) && this.biomes != null && !this.biomes.isEmpty() && !this.biomes.contains(spawnLocation.biome)) {
         return false;
      } else if (this.moonPhase != null && spawnLocation.location.world.field_73011_w.func_76559_b(spawnLocation.location.world.func_72820_D()) != this.moonPhase) {
         return false;
      } else if (this.minLightLevel != null && spawnLocation.light < this.minLightLevel) {
         return false;
      } else if (this.maxLightLevel != null && spawnLocation.light > this.maxLightLevel) {
         return false;
      } else if (this.cachedBaseBlocks != null && !this.cachedBaseBlocks.isEmpty() && !this.cachedBaseBlocks.contains(spawnLocation.baseBlock)) {
         return false;
      } else if (this.cachedNeededNearbyBlocks != null && !this.cachedNeededNearbyBlocks.isEmpty() && !CollectionHelper.containsAll(spawnLocation.uniqueSurroundingBlocks, this.cachedNeededNearbyBlocks)) {
         return false;
      } else if (spawnInfo.condition.variant != null && !spawnInfo.condition.variant.isEmpty()) {
         IBlockState state = spawnLocation.location.world.func_180495_p(spawnLocation.location.pos);
         int meta = spawnLocation.baseBlock.func_176201_c(state);
         Collection iProperties = spawnLocation.baseBlock.func_176194_O().func_177623_d();

         for(int i = 0; i < iProperties.size(); ++i) {
            IProperty property = (IProperty)iProperties.toArray()[i];
            if (property.func_177701_a().equalsIgnoreCase("variant")) {
               if (meta >= property.func_177700_c().size()) {
                  meta -= 4;
               }

               String name = property.func_177700_c().toArray()[meta].toString();
               Iterator var9 = spawnInfo.condition.variant.iterator();

               while(var9.hasNext()) {
                  String variant = (String)var9.next();
                  if (name.equalsIgnoreCase(variant)) {
                     return true;
                  }
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean doesHeadOfPartyMatch(SpawnLocation spawnLocation) {
      if (this.partyHeadSpecies != null && !this.partyHeadSpecies.isEmpty()) {
         if (!(spawnLocation.cause instanceof EntityPlayerMP)) {
            return false;
         } else {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)spawnLocation.cause);
            Pokemon headOfParty = party.get(0);
            return headOfParty == null ? false : this.partyHeadSpecies.contains(headOfParty.getSpecies());
         }
      } else {
         return true;
      }
   }

   private boolean doesWorldTimeFit(World world) {
      if (this.times != null && !this.times.isEmpty()) {
         ArrayList times = WorldTime.getCurrent(world);

         for(int i = 0; i < times.size(); ++i) {
            if (this.times.contains(times.get(i))) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean doesRealTimeFit() {
      if (this.realWorldSpawnTimes != null && !this.realWorldSpawnTimes.isEmpty()) {
         for(int i = 0; i < this.realWorldSpawnTimes.size(); ++i) {
            if (((SpawnTime)this.realWorldSpawnTimes.get(i)).matches()) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
