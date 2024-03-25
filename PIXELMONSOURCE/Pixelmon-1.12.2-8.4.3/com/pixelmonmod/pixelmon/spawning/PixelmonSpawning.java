package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnerCoordinator;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.checkspawns.FishingCheckSpawns;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.checkspawns.GenericTriggerCheckSpawns;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.checkspawns.LegendaryCheckSpawns;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.checkspawns.PlayerTrackingCheckSpawns;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.checkspawns.RayTraceCheckSpawns;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.selection.FlatDistinctAlgorithm;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.spawners.TriggerSpawner;
import com.pixelmonmod.pixelmon.api.spawning.calculators.CurryCheckSpawns;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.api.spawning.util.SetLoader;
import com.pixelmonmod.pixelmon.blocks.BlockFossil;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class PixelmonSpawning {
   public static SpawnerCoordinator coordinator = null;
   public static ArrayList standard = new ArrayList();
   public static ArrayList legendaries = new ArrayList();
   public static ArrayList fishing = new ArrayList();
   public static ArrayList megas = new ArrayList();
   public static ArrayList npcs = new ArrayList();
   public static ArrayList rocksmash = new ArrayList();
   public static ArrayList headbutt = new ArrayList();
   public static ArrayList sweetscent = new ArrayList();
   public static ArrayList curry = new ArrayList();
   public static ArrayList grass = new ArrayList();
   public static ArrayList tallgrass = new ArrayList();
   public static ArrayList seaweed = new ArrayList();
   public static ArrayList caveRock = new ArrayList();
   public static ArrayList forage = new ArrayList();
   public static LegendarySpawner legendarySpawner = null;
   public static LegendarySpawner megaBossSpawner = null;
   public static TriggerSpawner fishingSpawner = null;
   public static TriggerSpawner rocksmashSpawner = null;
   public static TriggerSpawner headbuttSpawner = null;
   public static TriggerSpawner sweetscentSpawner = null;
   public static TriggerSpawner currySpawner = null;
   public static TriggerSpawner grassSpawner = null;
   public static TriggerSpawner tallGrassSpawner = null;
   public static TriggerSpawner seaweedSpawner = null;
   public static TriggerSpawner caveRockSpawner = null;
   public static TriggerSpawner forageSpawner = null;
   public static PlayerTrackingSpawner.PlayerTrackingSpawnerBuilder trackingSpawnerPreset = (PlayerTrackingSpawner.PlayerTrackingSpawnerBuilder)(new PlayerTrackingSpawner.PlayerTrackingSpawnerBuilder()).setCheckSpawns(new PlayerTrackingCheckSpawns());
   public static LegendarySpawner.LegendarySpawnerBuilder legendarySpawnerPreset = (LegendarySpawner.LegendarySpawnerBuilder)(new LegendarySpawner.LegendarySpawnerBuilder()).setSelectionAlgorithm(new FlatDistinctAlgorithm()).setCheckSpawns(new LegendaryCheckSpawns("pixelmon.checkspawns.legendary"));
   public static LegendarySpawner.LegendarySpawnerBuilder megaBossSpawnerPreset = (LegendarySpawner.LegendarySpawnerBuilder)(new LegendarySpawner.LegendarySpawnerBuilder()).setFiresChooseEvent(false).setSelectionAlgorithm(new FlatDistinctAlgorithm()).setCheckSpawns(new LegendaryCheckSpawns("pixelmon.checkspawns.megaboss"));
   public static AbstractSpawner.SpawnerBuilder rockSmashPreset;
   public static AbstractSpawner.SpawnerBuilder headbuttPreset;
   public static AbstractSpawner.SpawnerBuilder sweetScentPreset;
   public static AbstractSpawner.SpawnerBuilder curryPreset;
   public static AbstractSpawner.SpawnerBuilder pixelmonGrassPreset;
   public static AbstractSpawner.SpawnerBuilder pixelmonDoubleGrassPreset;
   public static AbstractSpawner.SpawnerBuilder seaweedPreset;
   public static AbstractSpawner.SpawnerBuilder caverockPreset;
   public static AbstractSpawner.SpawnerBuilder fishingPreset;
   public static AbstractSpawner.SpawnerBuilder foragePreset;

   public static void startTrackingSpawner() {
      if (coordinator != null && coordinator.getActive()) {
         coordinator.deactivate();
      }

      if (coordinator == null || !coordinator.getActive()) {
         coordinator = (new TrackingSpawnerCoordinator()).activate();
      }

   }

   public static void loadAndInitialize() {
      Pixelmon.LOGGER.info("Registering spawn sets.");
      SetLoader.clearAll();
      standard.clear();
      legendaries.clear();
      megas.clear();
      npcs.clear();
      fishing.clear();
      rocksmash.clear();
      sweetscent.clear();
      grass.clear();
      tallgrass.clear();
      seaweed.clear();
      caveRock.clear();
      forage.clear();
      if (PixelmonConfig.useExternalJSONFilesSpawning) {
         if ("default".equals(PixelmonConfig.spawnSetFolder)) {
            File spawnSetDir = new File("pixelmon/spawning/default");
            if (!spawnSetDir.isDirectory()) {
               Pixelmon.LOGGER.info("Creating spawning directory");
               spawnSetDir.mkdirs();
            }

            SetLoader.checkForMissingSpawnSets();
         }

         standard.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/standard"));
         legendaries.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/legendaries"));
         megas.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/megas"));
         npcs.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/npcs"));
         fishing.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/fishing"));
         rocksmash.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/rocksmash"));
         headbutt.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/headbutt"));
         sweetscent.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/sweetscent"));
         grass.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/grass"));
         caveRock.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/caverock"));
         forage.addAll(SetLoader.importSetsFrom("pixelmon/spawning/" + PixelmonConfig.spawnSetFolder + "/forage"));
      } else {
         standard.addAll(SetLoader.retrieveSpawnSetsFromAssets("standard"));
         legendaries.addAll(SetLoader.retrieveSpawnSetsFromAssets("legendaries"));
         megas.addAll(SetLoader.retrieveSpawnSetsFromAssets("megas"));
         npcs.addAll(SetLoader.retrieveSpawnSetsFromAssets("npcs"));
         fishing.addAll(SetLoader.retrieveSpawnSetsFromAssets("fishing"));
         rocksmash.addAll(SetLoader.retrieveSpawnSetsFromAssets("rocksmash"));
         headbutt.addAll(SetLoader.retrieveSpawnSetsFromAssets("headbutt"));
         sweetscent.addAll(SetLoader.retrieveSpawnSetsFromAssets("sweetscent"));
         grass.addAll(SetLoader.retrieveSpawnSetsFromAssets("grass"));
         caveRock.addAll(SetLoader.retrieveSpawnSetsFromAssets("caverock"));
         forage.addAll(SetLoader.retrieveSpawnSetsFromAssets("forage"));
      }

      BiConsumer applyTweak = (builder, tweakx) -> {
         if (!CollectionHelper.containsA(builder.tweaks, tweakx.getClass())) {
            builder.addTweak(tweakx);
         }

      };
      BiConsumer applyCondition = (builder, conditionx) -> {
         if (!CollectionHelper.containsA(builder.conditions, conditionx.getClass())) {
            builder.addCondition(conditionx);
         }

      };
      List presets = Arrays.asList(trackingSpawnerPreset, legendarySpawnerPreset, megaBossSpawnerPreset, rockSmashPreset, headbuttPreset, fishingPreset, sweetScentPreset, seaweedPreset, pixelmonGrassPreset, pixelmonDoubleGrassPreset, caverockPreset, foragePreset);
      if (PixelmonConfig.spawnLevelsCloserToPlayerLevels) {
         PlayerBasedLevels playerBasedLevels = new PlayerBasedLevels();
         presets.forEach((preset) -> {
            applyTweak.accept(preset, playerBasedLevels);
            applyCondition.accept(preset, playerBasedLevels);
         });
      } else {
         presets.forEach((preset) -> {
            CollectionHelper.removeAny(preset.tweaks, PlayerBasedLevels.class);
         });
      }

      if (PixelmonConfig.spawnLevelsByDistance) {
         SpawnDistanceLevelTweak tweak = new SpawnDistanceLevelTweak();
         LevelByDistanceEvolutionCondition condition = new LevelByDistanceEvolutionCondition();
         presets.forEach((preset) -> {
            applyTweak.accept(preset, tweak);
            applyCondition.accept(preset, condition);
         });
      } else {
         presets.forEach((preset) -> {
            CollectionHelper.removeAny(preset.tweaks, SpawnDistanceLevelTweak.class);
            CollectionHelper.removeAny(preset.conditions, LevelByDistanceEvolutionCondition.class);
         });
      }

      if (PixelmonConfig.lureFluteDuration > 0) {
         FluteLevelTweak fluteLevelTweak = new FluteLevelTweak();
         presets.forEach((preset) -> {
            applyTweak.accept(preset, fluteLevelTweak);
         });
      } else {
         presets.forEach((preset) -> {
            CollectionHelper.removeAny(preset.tweaks, FluteLevelTweak.class);
         });
      }

      if (PixelmonConfig.spawnLevelsIncreaseInCaves) {
         presets.forEach((preset) -> {
            applyTweak.accept(preset, new CaveLevelTweak());
         });
      }

      trackingSpawnerPreset.setSpawnSets(standard).addSpawnSets((Collection)npcs).setupCache();
      if (PixelmonConfig.allowLegendariesSpawn) {
         legendarySpawner = (LegendarySpawner)legendarySpawnerPreset.setSpawnFrequency(1200.0F / (float)PixelmonConfig.legendarySpawnTicks).setSpawnSets(legendaries).setupCache().apply(new LegendarySpawner("legendary"));
      }

      megaBossSpawner = (LegendarySpawner)megaBossSpawnerPreset.setSpawnFrequency(1200.0F / (float)PixelmonConfig.bossSpawnTicks).setSpawnSets(megas).setupCache().apply(new LegendarySpawner("megaboss"));
      fishingSpawner = (TriggerSpawner)fishingPreset.setSpawnSets((List)fishing).apply(new TriggerSpawner("fishing"));
      rocksmashSpawner = (TriggerSpawner)rockSmashPreset.setSpawnSets((List)rocksmash).apply(new TriggerSpawner("rocksmash"));
      headbuttSpawner = (TriggerSpawner)headbuttPreset.setSpawnSets((List)headbutt).apply(new TriggerSpawner("headbutt"));
      sweetscentSpawner = (TriggerSpawner)sweetScentPreset.setSpawnSets((List)sweetscent).apply(new TriggerSpawner("sweetscent"));
      currySpawner = (TriggerSpawner)curryPreset.setSpawnSets((List)curry).apply(new TriggerSpawner("curry"));
      grassSpawner = (TriggerSpawner)pixelmonGrassPreset.setSpawnSets((List)grass).apply(new TriggerSpawner("grass"));
      tallGrassSpawner = (TriggerSpawner)pixelmonDoubleGrassPreset.setSpawnSets((List)tallgrass).apply(new TriggerSpawner("tallgrass"));
      seaweedSpawner = (TriggerSpawner)seaweedPreset.setSpawnSets((List)seaweed).apply(new TriggerSpawner("seaweed"));
      caveRockSpawner = (TriggerSpawner)caverockPreset.setSpawnSets((List)caveRock).apply(new TriggerSpawner("caverock"));
      forageSpawner = (TriggerSpawner)foragePreset.setSpawnSets((List)forage).apply(new TriggerSpawner("forage"));
   }

   public static void addRegularSpawners(SpawnerCoordinator coordinator) {
      if (PixelmonConfig.allowLegendariesSpawn) {
         coordinator.spawners.add(legendarySpawner);
      }

      coordinator.spawners.add(megaBossSpawner);
      coordinator.spawners.add(fishingSpawner);
      coordinator.spawners.add(caveRockSpawner);
      coordinator.spawners.add(grassSpawner);
      coordinator.spawners.add(headbuttSpawner);
      coordinator.spawners.add(rocksmashSpawner);
      coordinator.spawners.add(seaweedSpawner);
      coordinator.spawners.add(tallGrassSpawner);
      coordinator.spawners.add(sweetscentSpawner);
      coordinator.spawners.add(currySpawner);
      coordinator.spawners.add(forageSpawner);
   }

   public static void totalReload() {
      coordinator.deactivate();
      BetterSpawnerConfig.load();
      loadAndInitialize();
      coordinator.activate();
   }

   static {
      rockSmashPreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new RayTraceCheckSpawns(LocationType.ROCK_SMASH, (state) -> {
         if (state.func_185904_a() == Material.field_151576_e && state.func_177230_c() != Blocks.field_150357_h) {
            return !(state.func_177230_c() instanceof MultiBlock) && !(state.func_177230_c() instanceof BlockFossil);
         } else {
            return false;
         }
      }, "pixelmon.checkspawns.rocksmash"));
      headbuttPreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new RayTraceCheckSpawns(LocationType.HEADBUTT, (state) -> {
         return state.func_177230_c() instanceof BlockLog;
      }, "pixelmon.checkspawns.headbutt"));
      sweetScentPreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new GenericTriggerCheckSpawns(LocationType.SWEET_SCENT, "pixelmon.checkspawns.sweetscent"));
      curryPreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new CurryCheckSpawns());
      pixelmonGrassPreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new GenericTriggerCheckSpawns(LocationType.GRASS, "pixelmon.checkspawns.grass"));
      pixelmonDoubleGrassPreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new GenericTriggerCheckSpawns(LocationType.DOUBLE_GRASS, "pixelmon.checkspawns.tallgrass"));
      seaweedPreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new GenericTriggerCheckSpawns(LocationType.SEAWEED, "pixelmon.checkspawns.seaweed"));
      caverockPreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new GenericTriggerCheckSpawns(LocationType.CAVE_ROCK, "pixelmon.checkspawns.caverock"));
      fishingPreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new FishingCheckSpawns());
      foragePreset = (new AbstractSpawner.SpawnerBuilder()).setCheckSpawns(new GenericTriggerCheckSpawns(LocationType.FORAGE, "pixelmon.checkspawns.forage"));
   }
}
