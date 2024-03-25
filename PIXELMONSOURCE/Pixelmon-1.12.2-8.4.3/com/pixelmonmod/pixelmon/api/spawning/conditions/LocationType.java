package com.pixelmonmod.pixelmon.api.spawning.conditions;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class LocationType {
   public static final ArrayList locationTypes = new ArrayList();
   public String name;
   public Predicate baseBlockCondition = (state) -> {
      return true;
   };
   public Predicate surroundingBlockCondition = (state) -> {
      return state.func_177230_c() == Blocks.field_150350_a;
   };
   public Predicate neededNearbyBlockCondition = (block) -> {
      return true;
   };
   public Consumer mutator = (spawnLocation) -> {
   };
   public Boolean seesSky = null;
   public static LocationType LAND = (new LocationType("Land")).setSeesSky(true).setBaseBlockCondition((state) -> {
      return BetterSpawnerConfig.getLandBlocks().contains(state.func_177230_c());
   }).setSurroundingBlockCondition((state) -> {
      return BetterSpawnerConfig.getAirBlocks().contains(state.func_177230_c());
   });
   public static LocationType UNDERGROUND;
   public static LocationType WATER;
   public static LocationType SURFACE_WATER;
   public static LocationType LAVA;
   public static LocationType SURFACE_LAVA;
   public static LocationType UNDERGROUND_LAVA;
   public static LocationType LIQUID;
   public static LocationType UNDERGROUND_LIQUID;
   public static LocationType SURFACE_LIQUID;
   public static LocationType SEAFLOOR;
   public static LocationType LAVA_FLOOR;
   public static LocationType LIQUID_FLOOR;
   public static LocationType UNDERGROUND_WATER;
   public static LocationType AIR;
   public static LocationType TREE_TOP;
   public static LocationType MANMADE;
   public static LocationType INDOORS;
   public static LocationType ROCK_SMASH;
   public static LocationType HEADBUTT;
   public static LocationType SWEET_SCENT;
   public static LocationType FORAGE;
   public static LocationType CURRY_NONE;
   public static LocationType CURRY_SWEET;
   public static LocationType CURRY_SOUR;
   public static LocationType CURRY_BITTER;
   public static LocationType CURRY_DRY;
   public static LocationType CURRY_SPICY;
   public static TriggerLocation OLD_ROD;
   public static TriggerLocation GOOD_ROD;
   public static TriggerLocation SUPER_ROD;
   public static TriggerLocation OAS_ROD;
   public static TriggerLocation OK_ROD_QUALITY;
   public static TriggerLocation SO_SO_ROD_QUALITY;
   public static TriggerLocation GOOD_ROD_QUALITY;
   public static TriggerLocation GREAT_ROD_QUALITY;
   public static TriggerLocation RARE_ROD_QUALITY;
   public static TriggerLocation PRO_ROD_QUALITY;
   public static TriggerLocation SUPREME_ROD_QUALITY;
   public static TriggerLocation OLD_ROD_LAVA;
   public static TriggerLocation GOOD_ROD_LAVA;
   public static TriggerLocation SUPER_ROD_LAVA;
   public static TriggerLocation GRASS;
   public static TriggerLocation CAVE_ROCK;
   public static TriggerLocation DOUBLE_GRASS;
   public static TriggerLocation SEAWEED;

   public LocationType(String name) {
      this.name = name;
   }

   public LocationType setBaseBlockCondition(Predicate baseBlockCondition) {
      this.baseBlockCondition = baseBlockCondition;
      return this;
   }

   public LocationType setSurroundingBlockCondition(Predicate surroundingBlockCondition) {
      this.surroundingBlockCondition = surroundingBlockCondition;
      return this;
   }

   public LocationType setNeededNearbyBlockCondition(Predicate neededNearbyBlockCondition) {
      this.neededNearbyBlockCondition = neededNearbyBlockCondition;
      return this;
   }

   public LocationType setSeesSky(Boolean seesSky) {
      this.seesSky = seesSky;
      return this;
   }

   public LocationType setLocationMutator(Consumer mutator) {
      this.mutator = mutator;
      return this;
   }

   public String toString() {
      return this.name;
   }

   public static LocationType of(String name) {
      Iterator var1 = locationTypes.iterator();

      LocationType type;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         type = (LocationType)var1.next();
      } while(!type.name.equalsIgnoreCase(name));

      return type;
   }

   public static List getPotentialTypes(IBlockState state) {
      if (state != null && !locationTypes.isEmpty()) {
         List types = Lists.newArrayList();

         for(int i = 0; i < locationTypes.size(); ++i) {
            if (((LocationType)locationTypes.get(i)).baseBlockCondition.test(state)) {
               types.add(locationTypes.get(i));
            }
         }

         return types;
      } else {
         return Collections.emptyList();
      }
   }

   public static void addPotentialTypes(ArrayList types, IBlockState state) {
      Iterator var2 = locationTypes.iterator();

      while(var2.hasNext()) {
         LocationType type = (LocationType)var2.next();
         if (state != null && type.baseBlockCondition.test(state)) {
            types.add(type);
         }
      }

   }

   static {
      UNDERGROUND = (new LocationType("Underground")).setSeesSky(false).setBaseBlockCondition(LAND.baseBlockCondition).setSurroundingBlockCondition(LAND.surroundingBlockCondition);
      WATER = (new LocationType("Water")).setSeesSky(true).setBaseBlockCondition((state) -> {
         return BetterSpawnerConfig.getWaterBlocks().contains(state.func_177230_c());
      }).setSurroundingBlockCondition((state) -> {
         return BetterSpawnerConfig.getWaterBlocks().contains(state.func_177230_c());
      });
      SURFACE_WATER = (new LocationType("Surface Water")).setSeesSky(true).setBaseBlockCondition(WATER.baseBlockCondition).setSurroundingBlockCondition((state) -> {
         return WATER.surroundingBlockCondition.test(state) || LAND.surroundingBlockCondition.test(state);
      }).setLocationMutator((spawnLocation) -> {
         int y = spawnLocation.location.pos.func_177956_o();
         BlockPos.MutableBlockPos highest = new BlockPos.MutableBlockPos(spawnLocation.location.pos);

         while(spawnLocation.location.world.func_180495_p(highest).func_185904_a().func_76224_d()) {
            ++y;
            highest.func_185336_p(y);
         }

         spawnLocation.location.pos.func_185336_p(highest.func_177956_o());
      });
      LAVA = (new LocationType("Lava")).setBaseBlockCondition((state) -> {
         return BetterSpawnerConfig.getLavaBlocks().contains(state.func_177230_c());
      }).setSurroundingBlockCondition((state) -> {
         return BetterSpawnerConfig.getLavaBlocks().contains(state.func_177230_c());
      });
      SURFACE_LAVA = (new LocationType("Surface Lava")).setSeesSky(true).setBaseBlockCondition(LAVA.baseBlockCondition).setSurroundingBlockCondition((state) -> {
         return LAVA.surroundingBlockCondition.test(state) || LAND.surroundingBlockCondition.test(state);
      }).setLocationMutator((spawnLocation) -> {
         int y = spawnLocation.location.pos.func_177956_o();
         BlockPos.MutableBlockPos highest = new BlockPos.MutableBlockPos(spawnLocation.location.pos);

         while(spawnLocation.location.world.func_180495_p(highest).func_185904_a().func_76224_d()) {
            ++y;
            highest.func_185336_p(y);
         }

         spawnLocation.location.pos.func_185336_p(highest.func_177956_o());
      });
      UNDERGROUND_LAVA = (new LocationType("Underground Lava")).setSeesSky(false).setBaseBlockCondition(LAVA.baseBlockCondition).setSurroundingBlockCondition(LAVA.surroundingBlockCondition);
      LIQUID = (new LocationType("Liquid")).setBaseBlockCondition(WATER.baseBlockCondition.or(LAVA.baseBlockCondition)).setSurroundingBlockCondition(WATER.baseBlockCondition.or(LAVA.baseBlockCondition));
      UNDERGROUND_LIQUID = (new LocationType("Underground Liquid")).setSeesSky(false).setBaseBlockCondition(LIQUID.baseBlockCondition).setSurroundingBlockCondition(LIQUID.surroundingBlockCondition);
      SURFACE_LIQUID = (new LocationType("Surface Liquid")).setSeesSky(true).setBaseBlockCondition(LIQUID.baseBlockCondition).setSurroundingBlockCondition((state) -> {
         return LIQUID.surroundingBlockCondition.test(state) || LAND.surroundingBlockCondition.test(state);
      }).setLocationMutator((spawnLocation) -> {
         int y = spawnLocation.location.pos.func_177956_o();
         BlockPos.MutableBlockPos highest = new BlockPos.MutableBlockPos(spawnLocation.location.pos);

         while(spawnLocation.location.world.func_180495_p(highest).func_185904_a().func_76224_d()) {
            ++y;
            highest.func_185336_p(y);
         }

         spawnLocation.location.pos.func_185336_p(highest.func_177956_o());
      });
      SEAFLOOR = (new LocationType("Seafloor")).setSeesSky(true).setBaseBlockCondition(LAND.baseBlockCondition).setSurroundingBlockCondition(WATER.surroundingBlockCondition);
      LAVA_FLOOR = (new LocationType("Lava Floor")).setSeesSky(true).setBaseBlockCondition(LAND.baseBlockCondition).setSurroundingBlockCondition(LAVA.surroundingBlockCondition);
      LIQUID_FLOOR = (new LocationType("Liquid Floor")).setSeesSky(true).setBaseBlockCondition(LAND.baseBlockCondition).setSurroundingBlockCondition(LIQUID.surroundingBlockCondition);
      UNDERGROUND_WATER = (new LocationType("Underground Water")).setSeesSky(false).setBaseBlockCondition(WATER.baseBlockCondition).setSurroundingBlockCondition(WATER.surroundingBlockCondition);
      AIR = (new LocationType("Air")).setSeesSky(true).setBaseBlockCondition((state) -> {
         return LAND.baseBlockCondition.test(state) || WATER.baseBlockCondition.test(state);
      }).setSurroundingBlockCondition((state) -> {
         return LAND.surroundingBlockCondition.test(state) || WATER.surroundingBlockCondition.test(state);
      }).setLocationMutator((spawnLocation) -> {
         BlockPos.MutableBlockPos highest = new BlockPos.MutableBlockPos(spawnLocation.location.pos);
         highest.func_185336_p(230);

         while(highest.func_177956_o() > 0) {
            IBlockState state = spawnLocation.location.world.func_180495_p(highest);
            if (state.func_185904_a().func_76224_d() || state.func_185904_a().func_76220_a()) {
               break;
            }

            highest.func_185336_p(highest.func_177956_o() - 1);
         }

         if (highest.func_177956_o() != 0) {
            if (highest.func_177956_o() < 230) {
               spawnLocation.location.pos.func_185336_p(highest.func_177956_o() + RandomHelper.getRandomNumberBetween(8, 20));
            }

         }
      });
      TREE_TOP = (new LocationType("Tree Top")).setBaseBlockCondition((state) -> {
         return BetterSpawnerConfig.getTreeTopBlocks().contains(state.func_177230_c());
      }).setSurroundingBlockCondition((state) -> {
         return BetterSpawnerConfig.getAirBlocks().contains(state.func_177230_c());
      }).setSeesSky(true);
      MANMADE = (new LocationType("Manmade")).setBaseBlockCondition((state) -> {
         return BetterSpawnerConfig.getStructureBlocks().contains(state.func_177230_c());
      }).setSurroundingBlockCondition((state) -> {
         return BetterSpawnerConfig.getAirBlocks().contains(state.func_177230_c());
      }).setSeesSky(true);
      INDOORS = (new LocationType("Indoors")).setBaseBlockCondition((state) -> {
         return BetterSpawnerConfig.getStructureBlocks().contains(state.func_177230_c());
      }).setSurroundingBlockCondition((state) -> {
         return BetterSpawnerConfig.getAirBlocks().contains(state.func_177230_c());
      }).setSeesSky(false);
      ROCK_SMASH = new TriggerLocation("Rock Smash");
      HEADBUTT = new TriggerLocation("Headbutt");
      SWEET_SCENT = new TriggerLocation("Sweet Scent");
      FORAGE = new TriggerLocation("Forage");
      CURRY_NONE = new TriggerLocation("Curry None");
      CURRY_SWEET = new TriggerLocation("Curry Sweet");
      CURRY_SOUR = new TriggerLocation("Curry Sour");
      CURRY_BITTER = new TriggerLocation("Curry Bitter");
      CURRY_DRY = new TriggerLocation("Curry Dry");
      CURRY_SPICY = new TriggerLocation("Curry Spicy");
      OLD_ROD = new TriggerLocation("Old Rod");
      GOOD_ROD = new TriggerLocation("Good Rod");
      SUPER_ROD = new TriggerLocation("Super Rod");
      OAS_ROD = new TriggerLocation("Oas Rod");
      OK_ROD_QUALITY = new TriggerLocation("OK Rod Quality");
      SO_SO_ROD_QUALITY = new TriggerLocation("So-So Rod Quality");
      GOOD_ROD_QUALITY = new TriggerLocation("Good Rod Quality");
      GREAT_ROD_QUALITY = new TriggerLocation("Great Rod Quality");
      RARE_ROD_QUALITY = new TriggerLocation("Rare Rod Quality");
      PRO_ROD_QUALITY = new TriggerLocation("Pro Rod Quality");
      SUPREME_ROD_QUALITY = new TriggerLocation("Supreme Rod Quality");
      OLD_ROD_LAVA = new TriggerLocation("Old Rod Lava");
      GOOD_ROD_LAVA = new TriggerLocation("Good Rod Lava");
      SUPER_ROD_LAVA = new TriggerLocation("Super Rod Lava");
      GRASS = new TriggerLocation("Grass");
      CAVE_ROCK = new TriggerLocation("Cave Rock");
      DOUBLE_GRASS = new TriggerLocation("Tall Grass");
      SEAWEED = new TriggerLocation("Seawed");
      locationTypes.add(LAND);
      locationTypes.add(UNDERGROUND);
      locationTypes.add(WATER);
      locationTypes.add(SURFACE_WATER);
      locationTypes.add(SEAFLOOR);
      locationTypes.add(UNDERGROUND_WATER);
      locationTypes.add(AIR);
      locationTypes.add(LAVA);
      locationTypes.add(SURFACE_LAVA);
      locationTypes.add(UNDERGROUND_LAVA);
      locationTypes.add(ROCK_SMASH);
      locationTypes.add(HEADBUTT);
      locationTypes.add(SWEET_SCENT);
      locationTypes.add(TREE_TOP);
      locationTypes.add(MANMADE);
      locationTypes.add(INDOORS);
      locationTypes.add(LIQUID);
      locationTypes.add(UNDERGROUND_LIQUID);
      locationTypes.add(SURFACE_LIQUID);
      locationTypes.add(LAVA_FLOOR);
      locationTypes.add(LIQUID_FLOOR);
      locationTypes.add(FORAGE);
      locationTypes.add(CURRY_NONE);
      locationTypes.add(CURRY_BITTER);
      locationTypes.add(CURRY_DRY);
      locationTypes.add(CURRY_SOUR);
      locationTypes.add(CURRY_SPICY);
      locationTypes.add(CURRY_SWEET);
      locationTypes.add(OLD_ROD);
      locationTypes.add(GOOD_ROD);
      locationTypes.add(SUPER_ROD);
      locationTypes.add(OAS_ROD);
      locationTypes.add(OLD_ROD_LAVA);
      locationTypes.add(GOOD_ROD_LAVA);
      locationTypes.add(SUPER_ROD_LAVA);
      locationTypes.add(OK_ROD_QUALITY);
      locationTypes.add(SO_SO_ROD_QUALITY);
      locationTypes.add(GOOD_ROD_QUALITY);
      locationTypes.add(GREAT_ROD_QUALITY);
      locationTypes.add(RARE_ROD_QUALITY);
      locationTypes.add(PRO_ROD_QUALITY);
      locationTypes.add(SUPREME_ROD_QUALITY);
      locationTypes.add(GRASS);
      locationTypes.add(CAVE_ROCK);
      locationTypes.add(DOUBLE_GRASS);
      locationTypes.add(SEAWEED);
   }
}
