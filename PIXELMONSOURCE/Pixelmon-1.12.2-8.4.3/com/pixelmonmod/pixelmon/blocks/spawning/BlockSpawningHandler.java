package com.pixelmonmod.pixelmon.blocks.spawning;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.PixelmonBlockStartingBattleEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonBlockTriggeredBattleEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.api.spawning.util.SpatialData;
import com.pixelmonmod.pixelmon.api.world.MutableLocation;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Level;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumBerryFlavor;
import com.pixelmonmod.pixelmon.enums.EnumCurryRating;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class BlockSpawningHandler {
   private static final BlockSpawningHandler instance = new BlockSpawningHandler();
   private static final Random RNG = new Random();

   public static BlockSpawningHandler getInstance() {
      return instance;
   }

   public void performBattleStartCheck(World world, BlockPos pos, Entity entity, EntityPixelmon entityPixelmon, EnumBattleStartTypes startType, IBlockState state, Object... extra) {
      EntityPlayerMP playerMP = (EntityPlayerMP)entity;
      if (BattleRegistry.getBattle(playerMP) == null) {
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(playerMP);
         if (!party.guiOpened) {
            if (!Pixelmon.EVENT_BUS.post(new PixelmonBlockTriggeredBattleEvent(this, world, pos, playerMP, entityPixelmon, startType))) {
               if (startType == EnumBattleStartTypes.HEADBUTT) {
                  if (RNG.nextInt(100) <= 25) {
                     playerMP.func_145747_a(new TextComponentTranslation("pixelmon.moveskill.headbutt.nothing", new Object[0]));
                     return;
                  }

                  this.grabSpawns(world, pos, playerMP, startType, entityPixelmon, state, extra);
               } else if (startType == EnumBattleStartTypes.ROCKSMASH) {
                  if (RNG.nextInt(100) <= 40) {
                     return;
                  }

                  this.grabSpawns(world, pos, playerMP, startType, entityPixelmon, state, extra);
               } else if (startType == EnumBattleStartTypes.CURRY) {
                  if (RNG.nextInt(100) <= 7) {
                     return;
                  }

                  this.grabSpawns(world, pos, playerMP, startType, entityPixelmon, state, extra);
               } else if (startType == EnumBattleStartTypes.FORAGE) {
                  if (!RandomHelper.getRandomChance(PixelmonConfig.forageChance)) {
                     playerMP.func_145747_a(new TextComponentTranslation("pixelmon.moveskill.forage.nothing", new Object[]{entityPixelmon.getNickname()}));
                     return;
                  }

                  this.grabSpawns(world, pos, playerMP, startType, entityPixelmon, state, extra);
               } else {
                  this.grabSpawns(world, pos, playerMP, startType, entityPixelmon, state, extra);
               }

            }
         }
      }
   }

   private void grabSpawns(World worldIn, BlockPos pos, EntityPlayerMP player, EnumBattleStartTypes startType, EntityPixelmon fightingPokemon, IBlockState state, Object... extra) {
      EntityPixelmon pixelmon = null;
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
      Pokemon firstAble = party.findOne((pokemon) -> {
         return !pokemon.isEgg() && pokemon.getHealth() > 0;
      });
      if (firstAble != null) {
         SpatialData data;
         SpawnLocation spawnLocation;
         Entity entity;
         int lvl;
         int maxLvl;
         EntityPixelmon startingPixelmon;
         int minLvl;
         Level level;
         if (startType == EnumBattleStartTypes.PUGRASSSINGLE) {
            if (PixelmonSpawning.grassSpawner != null && PixelmonSpawning.coordinator != null) {
               data = PixelmonSpawning.grassSpawner.calculateSpatialData(worldIn, pos.func_177984_a(), 10, true, (block) -> {
                  return true;
               });
               spawnLocation = new SpawnLocation(player, new MutableLocation(worldIn, pos), Sets.newHashSet(new LocationType[]{LocationType.GRASS}), worldIn.func_180495_p(pos).func_177230_c(), data.uniqueSurroundingBlocks, worldIn.func_180494_b(pos), worldIn.func_175678_i(pos), 6, worldIn.func_175699_k(pos.func_177984_a()));
               entity = PixelmonSpawning.grassSpawner.trigger(spawnLocation);
               if (entity instanceof EntityPixelmon) {
                  pixelmon = (EntityPixelmon)entity;
               }
            }

            if (PixelmonConfig.scaleGrassBattles) {
               maxLvl = Math.min(party.getHighestLevel(), PixelmonServerConfig.maxLevel);
               minLvl = party.getLowestLevel();
               if (maxLvl - minLvl > 5) {
                  minLvl = maxLvl - minLvl;
               }

               level = pixelmon.getLvl();
               if (minLvl < maxLvl) {
                  lvl = RNG.nextInt(maxLvl - minLvl) + minLvl;
                  level.setLevel(lvl);
               } else {
                  level.setLevel(maxLvl);
               }

               pixelmon.getPokemonData().getMoveset().clear();
               pixelmon.getPokemonData().getMoveset().addAll(pixelmon.getBaseStats().loadMoveset(level.getLevel()));
            }

            startingPixelmon = firstAble.getOrSpawnPixelmon(player);
            if (pixelmon != null) {
               this.setupBattle(worldIn, pos, player, startType, startingPixelmon, pixelmon);
            }
         }

         if (startType == EnumBattleStartTypes.PUGRASSDOUBLE) {
            if (PixelmonSpawning.tallGrassSpawner != null && PixelmonSpawning.coordinator != null) {
               data = PixelmonSpawning.tallGrassSpawner.calculateSpatialData(worldIn, pos.func_177984_a(), 10, true, (block) -> {
                  return true;
               });
               spawnLocation = new SpawnLocation(player, new MutableLocation(worldIn, pos), Sets.newHashSet(new LocationType[]{LocationType.DOUBLE_GRASS}), worldIn.func_180495_p(pos).func_177230_c(), data.uniqueSurroundingBlocks, worldIn.func_180494_b(pos), worldIn.func_175678_i(pos), 6, worldIn.func_175699_k(pos.func_177984_a()));
               entity = PixelmonSpawning.tallGrassSpawner.trigger(spawnLocation);
               if (entity instanceof EntityPixelmon) {
                  pixelmon = (EntityPixelmon)entity;
               }
            } else if (PixelmonConfig.scaleGrassBattles) {
               maxLvl = Math.min(party.getHighestLevel(), PixelmonServerConfig.maxLevel);
               minLvl = party.getLowestLevel();
               if (maxLvl - minLvl > 5) {
                  minLvl = maxLvl - minLvl;
               }

               level = pixelmon.getLvl();
               if (minLvl < maxLvl) {
                  lvl = RNG.nextInt(maxLvl - minLvl) + minLvl;
                  level.setLevel(lvl);
               } else {
                  level.setLevel(maxLvl);
               }

               pixelmon.getPokemonData().getMoveset().clear();
               pixelmon.getPokemonData().getMoveset().addAll(pixelmon.getBaseStats().loadMoveset(level.getLevel()));
            }

            startingPixelmon = firstAble.getOrSpawnPixelmon(player);
            if (pixelmon != null) {
               this.setupBattle(worldIn, pos, player, startType, startingPixelmon, pixelmon);
            }
         } else if (startType == EnumBattleStartTypes.CAVEROCK) {
            if (PixelmonSpawning.caveRockSpawner != null && PixelmonSpawning.coordinator != null) {
               data = PixelmonSpawning.caveRockSpawner.calculateSpatialData(worldIn, pos.func_177984_a(), 10, true, (block) -> {
                  return true;
               });
               spawnLocation = new SpawnLocation(player, new MutableLocation(worldIn, pos.func_177984_a()), Sets.newHashSet(new LocationType[]{LocationType.CAVE_ROCK}), worldIn.func_180495_p(pos).func_177230_c(), data.uniqueSurroundingBlocks, worldIn.func_180494_b(pos), worldIn.func_175678_i(pos), 6, worldIn.func_175699_k(pos.func_177984_a()));
               entity = PixelmonSpawning.caveRockSpawner.trigger(spawnLocation);
               if (entity instanceof EntityPixelmon) {
                  pixelmon = (EntityPixelmon)entity;
               }
            }

            if (PixelmonConfig.scaleGrassBattles) {
               maxLvl = Math.min(party.getHighestLevel(), PixelmonServerConfig.maxLevel);
               minLvl = party.getLowestLevel();
               if (maxLvl - minLvl > 5) {
                  minLvl = maxLvl - minLvl;
               }

               level = pixelmon.getLvl();
               if (minLvl < maxLvl) {
                  lvl = RNG.nextInt(maxLvl - minLvl) + minLvl;
                  level.setLevel(lvl);
               } else {
                  level.setLevel(maxLvl);
               }

               pixelmon.getPokemonData().getMoveset().clear();
               pixelmon.getPokemonData().getMoveset().addAll(pixelmon.getBaseStats().loadMoveset(level.getLevel()));
            }

            startingPixelmon = firstAble.getOrSpawnPixelmon(player);
            if (pixelmon != null) {
               this.setupBattle(worldIn, pos.func_177984_a(), player, startType, startingPixelmon, pixelmon);
            }
         } else if (startType == EnumBattleStartTypes.HEADBUTT) {
            if (PixelmonSpawning.headbuttSpawner != null && PixelmonSpawning.coordinator != null) {
               data = PixelmonSpawning.headbuttSpawner.calculateSpatialData(worldIn, pos.func_177984_a(), 10, true, (block) -> {
                  return true;
               });
               data.baseBlock = worldIn.func_180495_p(pos).func_177230_c();
               spawnLocation = new SpawnLocation(player, new MutableLocation(worldIn, pos), Sets.newHashSet(new LocationType[]{LocationType.HEADBUTT}), worldIn.func_180495_p(pos).func_177230_c(), data.uniqueSurroundingBlocks, worldIn.func_180494_b(pos), worldIn.func_175678_i(pos), 6, worldIn.func_175699_k(pos.func_177984_a()));
               entity = PixelmonSpawning.headbuttSpawner.trigger(spawnLocation);
               if (entity instanceof EntityPixelmon) {
                  pixelmon = (EntityPixelmon)entity;
               }
            }

            if (pixelmon != null) {
               this.setupBattle(worldIn, pos, player, startType, fightingPokemon, pixelmon);
               player.func_145747_a(new TextComponentTranslation("pixelmon.moveskill.headbutt.battle", new Object[]{pixelmon.getLocalizedName()}));
            } else {
               player.func_145747_a(new TextComponentTranslation("pixelmon.moveskill.headbutt.nothing", new Object[0]));
            }
         } else if (startType == EnumBattleStartTypes.ROCKSMASH) {
            if (PixelmonSpawning.rocksmashSpawner != null && PixelmonSpawning.coordinator != null) {
               data = PixelmonSpawning.rocksmashSpawner.calculateSpatialData(worldIn, pos.func_177984_a(), 10, true, (block) -> {
                  return true;
               });
               data.baseBlock = state.func_177230_c();
               spawnLocation = new SpawnLocation(player, new MutableLocation(worldIn, pos), Sets.newHashSet(new LocationType[]{LocationType.ROCK_SMASH}), data.baseBlock, data.uniqueSurroundingBlocks, worldIn.func_180494_b(pos), worldIn.func_175678_i(pos), 10, worldIn.func_175699_k(pos.func_177984_a()));
               entity = PixelmonSpawning.rocksmashSpawner.trigger(spawnLocation);
               if (entity instanceof EntityPixelmon) {
                  pixelmon = (EntityPixelmon)entity;
               }
            }

            if (pixelmon != null) {
               this.setupBattle(worldIn, pos, player, startType, fightingPokemon, pixelmon);
            }
         } else if (startType == EnumBattleStartTypes.SWEETSCENT) {
            if (PixelmonSpawning.sweetscentSpawner != null && PixelmonSpawning.coordinator != null) {
               data = PixelmonSpawning.sweetscentSpawner.calculateSpatialData(worldIn, pos.func_177984_a(), 10, true, (block) -> {
                  return true;
               });
               spawnLocation = new SpawnLocation(player, new MutableLocation(worldIn, pos), Sets.newHashSet(new LocationType[]{LocationType.SWEET_SCENT}), worldIn.func_180495_p(pos).func_177230_c(), data.uniqueSurroundingBlocks, worldIn.func_180494_b(pos), worldIn.func_175678_i(pos), 6, worldIn.func_175699_k(pos.func_177984_a()));
               entity = PixelmonSpawning.sweetscentSpawner.trigger(spawnLocation);
               if (entity instanceof EntityPixelmon) {
                  pixelmon = (EntityPixelmon)entity;
               }

               if (pixelmon != null) {
                  this.setupBattle(worldIn, pos, player, startType, fightingPokemon, pixelmon);
               }
            }
         } else {
            Entity entity;
            SpawnLocation spawnLocation;
            if (startType == EnumBattleStartTypes.CURRY) {
               if (PixelmonSpawning.currySpawner != null && PixelmonSpawning.coordinator != null) {
                  data = PixelmonSpawning.currySpawner.calculateSpatialData(worldIn, pos.func_177984_a(), 10, true, (block) -> {
                     return true;
                  });
                  LocationType type = LocationType.CURRY_NONE;
                  if (extra != null && extra[0] instanceof EnumBerryFlavor) {
                     switch ((EnumBerryFlavor)extra[0]) {
                        case SPICY:
                           type = LocationType.CURRY_SPICY;
                           break;
                        case DRY:
                           type = LocationType.CURRY_DRY;
                           break;
                        case SWEET:
                           type = LocationType.CURRY_SWEET;
                           break;
                        case BITTER:
                           type = LocationType.CURRY_BITTER;
                           break;
                        case SOUR:
                           type = LocationType.CURRY_SOUR;
                     }
                  }

                  spawnLocation = new SpawnLocation(player, new MutableLocation(worldIn, pos), Sets.newHashSet(new LocationType[]{type}), worldIn.func_180495_p(pos).func_177230_c(), data.uniqueSurroundingBlocks, worldIn.func_180494_b(pos), worldIn.func_175678_i(pos), 6, worldIn.func_175699_k(pos.func_177984_a()));
                  entity = PixelmonSpawning.currySpawner.trigger(spawnLocation);
                  if (entity instanceof EntityPixelmon) {
                     pixelmon = (EntityPixelmon)entity;
                  }

                  if (pixelmon != null) {
                     if (extra != null && extra.length >= 2) {
                        EnumCurryRating rating = (EnumCurryRating)extra[1];
                        IVStore ivs = pixelmon.getPokemonData().getIVs();
                        int[] var17;
                        int var18;
                        int var19;
                        int i;
                        label194:
                        switch (rating) {
                           case KOFFING:
                              StatsType[] var29 = StatsType.values();
                              var18 = var29.length;
                              var19 = 0;

                              while(true) {
                                 if (var19 >= var18) {
                                    break label194;
                                 }

                                 StatsType t = var29[var19];
                                 ivs.setStat(t, RandomHelper.getRandomNumberBetween(0, 15));
                                 ++var19;
                              }
                           case MILCERY:
                              var17 = RandomHelper.getRandomDistinctNumbersBetween(0, 5, 1);
                              var18 = var17.length;
                              var19 = 0;

                              while(true) {
                                 if (var19 >= var18) {
                                    break label194;
                                 }

                                 i = var17[var19];
                                 ivs.setStat(StatsType.values()[i], 31);
                                 ++var19;
                              }
                           case COPPERAJAH:
                              var17 = RandomHelper.getRandomDistinctNumbersBetween(0, 5, 2);
                              var18 = var17.length;
                              var19 = 0;

                              while(true) {
                                 if (var19 >= var18) {
                                    break label194;
                                 }

                                 i = var17[var19];
                                 ivs.setStat(StatsType.values()[i], 31);
                                 ++var19;
                              }
                           case CHARIZARD:
                              var17 = RandomHelper.getRandomDistinctNumbersBetween(0, 5, 3);
                              var18 = var17.length;

                              for(var19 = 0; var19 < var18; ++var19) {
                                 i = var17[var19];
                                 ivs.setStat(StatsType.values()[i], 31);
                              }
                        }
                     }

                     pixelmon.getPokemonData().addRibbon(EnumRibbonType.CURRY);
                  }
               }
            } else if (startType == EnumBattleStartTypes.FORAGE && PixelmonSpawning.forageSpawner != null && PixelmonSpawning.coordinator != null) {
               BlockPos up = pos.func_177984_a();
               SpatialData data = PixelmonSpawning.forageSpawner.calculateSpatialData(worldIn, up, 10, true, (block) -> {
                  return true;
               });
               data.baseBlock = worldIn.func_180495_p(pos).func_177230_c();
               spawnLocation = new SpawnLocation(player, new MutableLocation(worldIn, pos), Sets.newHashSet(new LocationType[]{LocationType.FORAGE}), worldIn.func_180495_p(pos).func_177230_c(), data.uniqueSurroundingBlocks, worldIn.func_180494_b(pos), worldIn.func_175678_i(up), 6, worldIn.func_175699_k(up));
               entity = PixelmonSpawning.forageSpawner.trigger(spawnLocation);
               if (entity instanceof EntityPixelmon) {
                  pixelmon = (EntityPixelmon)entity;
               }

               if (pixelmon != null) {
                  this.setupBattle(worldIn, pos, player, startType, fightingPokemon, pixelmon);
               }
            }
         }

      }
   }

   private void setupBattle(World worldIn, BlockPos pos, EntityPlayerMP player, EnumBattleStartTypes startType, EntityPixelmon fightingPokemon, EntityPixelmon pixelmon) {
      PlayerParticipant playerParticipant = new PlayerParticipant(player, new EntityPixelmon[]{fightingPokemon});
      WildPixelmonParticipant wildPixelmonParticipant = new WildPixelmonParticipant(true, new EntityPixelmon[]{pixelmon});
      if (pixelmon != null && !Pixelmon.EVENT_BUS.post(new PixelmonBlockStartingBattleEvent(worldIn, pos, player, startType, fightingPokemon, pixelmon, (EntityPixelmon)null))) {
         wildPixelmonParticipant.startedBattle = true;
         BattleRegistry.startBattle(playerParticipant, wildPixelmonParticipant);
      }
   }
}
