package com.pixelmonmod.pixelmon.api.spawning.calculators;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnLocationEvent;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.api.world.BlockCollection;
import com.pixelmonmod.pixelmon.api.world.MutableLocation;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICalculateSpawnLocations {
   int MIN_DIAMETER = 1;

   static ICalculateSpawnLocations getDefault() {
      return new DummyImpl();
   }

   default int getMaxSpawnLocationDiameter() {
      return 10;
   }

   default ArrayList calculateSpawnableLocations(BlockCollection collection) {
      ArrayList spawnableLocations = new ArrayList();
      World world = collection.world;
      int minX = collection.minX + 1;
      int minY = collection.minY + 1;
      int minZ = collection.minZ + 1;
      int maxX = collection.maxX - 1;
      int maxY = collection.maxY - 1;
      int maxZ = collection.maxZ - 1;

      for(int baseX = minX; baseX <= maxX; ++baseX) {
         for(int baseZ = minZ; baseZ <= maxZ; ++baseZ) {
            boolean canSeeSky = true;

            int baseY;
            for(baseY = 254; baseY >= maxY + 1; --baseY) {
               if (!BetterSpawnerConfig.doesBlockSeeSky(collection.getBlockState(baseX, baseY + 1, baseZ))) {
                  canSeeSky = false;
                  break;
               }
            }

            label216:
            for(baseY = maxY - 1; baseY >= minY; --baseY) {
               IBlockState state = collection.getBlockState(baseX, baseY + 1, baseZ);
               if (state == null) {
                  break;
               }

               if (canSeeSky && !BetterSpawnerConfig.doesBlockSeeSky(state)) {
                  canSeeSky = false;
               }

               List types = LocationType.getPotentialTypes(collection.getBlockState(baseX, baseY, baseZ));
               if (!types.isEmpty()) {
                  BlockPos base = new BlockPos(baseX, baseY + 1, baseZ);
                  int r = 0;

                  int diameter;
                  int y;
                  int x;
                  int y;
                  int z;
                  label212:
                  for(diameter = 0; diameter <= this.getMaxSpawnLocationDiameter(); ++r) {
                     y = base.func_177956_o() + r;
                     int[] var20 = new int[]{-1, 1};
                     x = var20.length;

                     for(y = 0; y < x; ++y) {
                        z = var20[y];
                        int[] var24 = new int[]{base.func_177958_n() + r * z, base.func_177958_n()};
                        int var25 = var24.length;

                        for(int var26 = 0; var26 < var25; ++var26) {
                           int x = var24[var26];
                           int[] var28 = new int[]{base.func_177952_p() + r * z, base.func_177952_p()};
                           int var29 = var28.length;

                           for(int var30 = 0; var30 < var29; ++var30) {
                              int z = var28[var30];
                              if (x > maxX || x < minX || y > maxY || y < minY || z > maxZ || z < minZ) {
                                 if (r <= 1) {
                                    continue label216;
                                 }
                                 break label212;
                              }

                              IBlockState rstate = collection.getBlockState(x, y, z);
                              if (rstate == null) {
                                 break label212;
                              }

                              if (diameter <= 1) {
                                 types.removeIf((typex) -> {
                                    return !typex.surroundingBlockCondition.test(rstate);
                                 });
                                 if (types.isEmpty()) {
                                    continue label216;
                                 }
                              } else {
                                 Iterator var33 = types.iterator();

                                 while(var33.hasNext()) {
                                    LocationType type = (LocationType)var33.next();
                                    if (!type.surroundingBlockCondition.test(rstate)) {
                                       break label212;
                                    }
                                 }
                              }

                              if (r == 0) {
                                 break;
                              }
                           }

                           if (r == 0) {
                              break;
                           }
                        }

                        ++diameter;
                     }
                  }

                  y = this.getMaxSpawnLocationDiameter();
                  Set uniqueBlocks = Sets.newHashSet();

                  for(x = baseX - y; x < baseX + y; ++x) {
                     if (x <= collection.maxX && x >= collection.minX) {
                        for(y = baseY - y; y <= baseY + y; ++y) {
                           if (y <= collection.maxY && y >= collection.minY) {
                              for(z = baseZ - y; z <= baseZ + y; ++z) {
                                 if (z <= collection.maxZ && z >= collection.minZ) {
                                    state = collection.getBlockState(x, y, z);
                                    if (state != null) {
                                       uniqueBlocks.add(state.func_177230_c());
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  boolean fCanSeeSky = canSeeSky;
                  Set finalTypes = Sets.newHashSet();
                  Iterator var38 = types.iterator();

                  while(true) {
                     LocationType type;
                     do {
                        do {
                           if (!var38.hasNext()) {
                              if (!finalTypes.isEmpty()) {
                                 MutableLocation loc = new MutableLocation(world, baseX, baseY + 1, baseZ);
                                 SpawnLocation spawnLocation = new SpawnLocation(collection.cause, loc, finalTypes, collection.getBlockState(baseX, baseY, baseZ).func_177230_c(), uniqueBlocks, collection.getBiome(baseX, baseZ), canSeeSky, diameter, collection.getLight(baseX, baseY + 1, baseZ));
                                 SpawnLocationEvent event = new SpawnLocationEvent(spawnLocation);
                                 if (!Pixelmon.EVENT_BUS.post(event)) {
                                    spawnableLocations.add(event.getSpawnLocation());
                                 }
                              }
                              continue label216;
                           }

                           type = (LocationType)var38.next();
                        } while(type.seesSky != null && fCanSeeSky != type.seesSky);
                     } while(type.neededNearbyBlockCondition != null && !type.neededNearbyBlockCondition.test(uniqueBlocks));

                     finalTypes.add(type);
                  }
               }
            }
         }
      }

      return spawnableLocations;
   }

   public static class DummyImpl implements ICalculateSpawnLocations {
   }
}
