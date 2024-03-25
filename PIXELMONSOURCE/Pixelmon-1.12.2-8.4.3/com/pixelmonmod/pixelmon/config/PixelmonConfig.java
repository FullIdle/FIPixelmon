package com.pixelmonmod.pixelmon.config;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleAIMode;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpace;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationOptions;
import info.pixelmon.repack.ninja.leaping.configurate.commented.CommentedConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMappingException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class PixelmonConfig {
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "awardPhotos"
   )
   public static boolean awardTokens = false;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "allowPokemonNicknames"
   )
   public static boolean allowNicknames = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "allowAnvilAutoreloading"
   )
   public static boolean allowAnvilAutoloading = false;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "allowVanillaMobs",
      requiresRestart = true
   )
   public static boolean allowNonPixelmonMobs = false;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "allowCaptureOutsideBattle",
      requiresRestart = true
   )
   public static boolean allowCapturingOutsideBattle = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean allowRandomPokemonToBeLegendary = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "growthScaleModifier",
      minValue = 0.0,
      maxValue = 2.0,
      useSlider = true
   )
   public static double growthModifier = 1.0;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean pokemonDropsEnabled = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean printErrors = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean allowPlanting = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 0.0
   )
   public static int maximumPlants = 32;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean cloningMachineEnabled = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 0.0
   )
   public static int lakeTrioMaxEnchants = 3;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "engagePlayerByPokeBall"
   )
   public static boolean pokeBallPlayerEngage = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 0.0,
      maxValue = 256.0,
      requiresRestart = true
   )
   public static int computerBoxes = 30;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "enableWildAggression"
   )
   public static boolean isAggressionAllowed = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean allowTMReuse = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean allowTRReuse = false;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean universalTMs = true;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean superUniversalTMs = false;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean writeEntitiesToWorld = false;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      requiresRestart = true
   )
   public static EnumEncounterMode shrineEncounterMode;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "spawnersOpOnly"
   )
   public static boolean opToUseSpawners;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "npcEditorOpOnly"
   )
   public static boolean opToUseNpcEditor;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "questEditorOpOnly"
   )
   public static boolean opToUseQuestEditor;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "tradersReusable"
   )
   public static boolean reuseTraders;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 1.0,
      maxValue = 90.0
   )
   public static int movesPerTutor;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "starterOnJoin"
   )
   public static boolean giveStarter;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "useSystemTimeForWorldTime"
   )
   public static boolean useSystemWorldTime;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "systemTimeSyncInterval",
      minValue = 2.0
   )
   public static int timeUpdateInterval;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 0.0,
      maxValue = 4.0,
      useSlider = true
   )
   public static EnumBattleAIMode battleAIWild;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 0.0,
      maxValue = 4.0,
      useSlider = true
   )
   public static EnumBattleAIMode battleAIBoss;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 0.0,
      maxValue = 4.0,
      useSlider = true
   )
   public static EnumBattleAIMode battleAITrainer;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "multiplePhotosOfSamePokemon"
   )
   public static boolean allowMultiplePhotosOfSamePokemon;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean bedsHealPokemon;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean allowPokemonEditors;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean allowChisels;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean dataSaveOnWorldSave;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean useDropGUI;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 0.10000000149011612,
      maxValue = 1000.0
   )
   public static float berryTreeGrowthMultiplier;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean useSystemTimeForBerries;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 1.0
   )
   public static int maxLevel;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 1.0
   )
   public static int despawnRadius;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean canPokemonBeHit;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      requiresRestart = true
   )
   public static boolean alwaysHaveMegaRing;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      requiresRestart = true
   )
   public static boolean alwaysHaveDynamaxBand;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean deleteUnwantedDrops;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static int chanceToGetSpecialBidoof;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean disabledNonPlayerPixelmonMovement;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean allowShinyCharmFromPokedex;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean allowOvalCharmFromPokedex;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static double expCharmMultiplier;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static double catchingCharmMultiplier;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static int markCharmRolls;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      type = String.class
   )
   public static List oreColors;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static int averageLureExpiryTicks;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean limitShopKeeperStackSize;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean relaxedBattleGimmickRules;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      type = Integer.class
   )
   public static List oldGenDimensions;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      type = Integer.class
   )
   public static List bothGenDimensions;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      minValue = 1.0
   )
   public static float dynamaxSize;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean allowIllegalShinies;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      useSlider = true,
      maxValue = 1.0,
      minValue = 0.0
   )
   public static float battleMusicVolume;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean useUpdateEntityWithOptionalForceFix;
   @Node(
      category = PixelmonConfig.Category.GENERAL
   )
   public static boolean verbose;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      nameOverride = "berryJuiceCrafting"
   )
   public static boolean berryJuiceCrafting;
   @Node(
      category = PixelmonConfig.Category.BATTLE,
      nameOverride = "allowPvPExperience"
   )
   public static boolean allowPVPExperience;
   @Node(
      category = PixelmonConfig.Category.BATTLE
   )
   public static boolean allowTrainerExperience;
   @Node(
      category = PixelmonConfig.Category.BATTLE
   )
   public static boolean returnHeldItems;
   @Node(
      category = PixelmonConfig.Category.BATTLE,
      minValue = 0.0,
      maxValue = 2.0,
      useSlider = true
   )
   public static EnumForceBattleResult forceEndBattleResult;
   @Node(
      category = PixelmonConfig.Category.BATTLE,
      minValue = 0.0
   )
   public static float expModifier;
   @Node(
      category = PixelmonConfig.Category.BATTLE,
      minValue = 0.0,
      maxValue = 1.0
   )
   public static float synchronizeChance;
   @Node(
      category = PixelmonConfig.Category.BATTLE
   )
   public static boolean allowHappyHour;
   @Node(
      category = PixelmonConfig.Category.BATTLE
   )
   public static boolean allowPayDay;
   @Node(
      category = PixelmonConfig.Category.BATTLE
   )
   public static double payDayMultiplier;
   @Node(
      category = PixelmonConfig.Category.BATTLE
   )
   public static boolean allowGMaxGoldRush;
   @Node(
      category = PixelmonConfig.Category.BATTLE
   )
   public static double gMaxGoldRushMultiplier;
   @Node(
      category = PixelmonConfig.Category.BATTLE
   )
   public static boolean allowAmuletCoin;
   @Node(
      category = PixelmonConfig.Category.BATTLE,
      minValue = 0.0
   )
   public static int pickupRate;
   @Node(
      category = PixelmonConfig.Category.BATTLE
   )
   public static boolean allowCatchCombo;
   @Node(
      category = PixelmonConfig.Category.BATTLE,
      type = Integer.class
   )
   public static List catchComboThresholds;
   @Node(
      category = PixelmonConfig.Category.BATTLE,
      type = Float.class
   )
   public static List catchComboExpBonuses;
   @Node(
      category = PixelmonConfig.Category.BATTLE,
      type = Float.class
   )
   public static List catchComboShinyModifiers;
   @Node(
      category = PixelmonConfig.Category.BATTLE,
      type = Integer.class
   )
   public static List catchComboPerfectIVs;
   @Node(
      category = PixelmonConfig.Category.HEALING
   )
   public static boolean usePassiveHealer;
   @Node(
      category = PixelmonConfig.Category.HEALING
   )
   public static float chanceToRevivePassively;
   @Node(
      category = PixelmonConfig.Category.HEALING
   )
   public static float chanceToHealStatusPassively;
   @Node(
      category = PixelmonConfig.Category.HEALING
   )
   public static int stepsToHealHealthPassively;
   @Node(
      category = PixelmonConfig.Category.HEALING
   )
   public static float passiveHealingMaxHealthPercentage;
   @Node(
      category = PixelmonConfig.Category.OUTSIDEEFFECTS
   )
   public static boolean useOutsideEffects;
   @Node(
      category = PixelmonConfig.Category.OUTSIDEEFFECTS
   )
   public static int stepsToApplyEffects;
   @Node(
      category = PixelmonConfig.Category.OUTSIDEEFFECTS
   )
   public static int poisonMaxDamage;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      nameOverride = "shinySpawnRate",
      minValue = 0.0
   )
   public static float shinyRate;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static float bossRate;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      type = Integer.class
   )
   public static List bossLevelIncreases;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      type = Integer.class
   )
   public static List bossWeights;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      type = Float.class
   )
   public static List bossCandyChances;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static boolean bossesAlwaysMegaIfPossible;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      minValue = 0.0
   )
   public static int bossSpawnTicks;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      minValue = 0.0,
      maxValue = 1.0
   )
   public static float bossSpawnChance;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      minValue = 0.0
   )
   public static int legendarySpawnTicks;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      minValue = 0.0,
      maxValue = 1.0
   )
   public static float legendarySpawnChance;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      minValue = 0.0
   )
   public static float spawnTicksPlayerMultiplier;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static boolean spawnLevelsByDistance;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      minValue = 5.0,
      maxValue = 100.0
   )
   public static int maxLevelByDistance;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      minValue = 0.0
   )
   public static int distancePerLevel;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static boolean spawnLevelsCloserToPlayerLevels;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static boolean spawnLevelsIncreaseInCaves;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int caveMaxMultiplier;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static double transformToDittoOnCatch;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int meltanSpawnChance;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static double meltanTransformChance;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      type = String.class
   )
   public static List canTransformToDittoOnCatch;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      nameOverride = "hiddenAbilitySpawnRate",
      minValue = 0.0
   )
   public static float hiddenAbilityRate;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      nameOverride = "gigantamaxFactorSpawnRate",
      minValue = 0.0
   )
   public static float gigantamaxFactorRate;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      nameOverride = "allowLegendarySpawn"
   )
   public static boolean allowLegendariesSpawn;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int legendaryDespawnTicks;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      nameOverride = "displayLegendaryGlobalMessage"
   )
   public static boolean doLegendaryEvent;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static boolean doLegendaryRaidEvent;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static boolean useRecentLevelMoves;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static boolean despawnOnFleeOrLoss;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int entitiesPerPlayer;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int spawnsPerPass;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static float spawnFrequency;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static float minimumDistanceBetweenSpawns;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int minimumDistanceFromCentre;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int maximumDistanceFromCentre;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static float horizontalTrackFactor;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static float verticalTrackFactor;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int horizontalSliceRadius;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int verticalSliceRadius;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int maximumSpawnedPokemon;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int maximumSpawnedFlyingPokemon;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int maximumSpawnedFlyingPokemonPerPlayer;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      minValue = 0.0,
      maxValue = 1.0
   )
   public static double bellSuccessChance;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      minValue = 0.0
   )
   public static int bellInclusionRange;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int lureFluteDuration;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static int lureFluteLevelModifier;
   @Node(
      category = PixelmonConfig.Category.SPAWNINGGENS
   )
   public static boolean Gen1;
   @Node(
      category = PixelmonConfig.Category.SPAWNINGGENS
   )
   public static boolean Gen2;
   @Node(
      category = PixelmonConfig.Category.SPAWNINGGENS
   )
   public static boolean Gen3;
   @Node(
      category = PixelmonConfig.Category.SPAWNINGGENS
   )
   public static boolean Gen4;
   @Node(
      category = PixelmonConfig.Category.SPAWNINGGENS
   )
   public static boolean Gen5;
   @Node(
      category = PixelmonConfig.Category.SPAWNINGGENS
   )
   public static boolean Gen6;
   @Node(
      category = PixelmonConfig.Category.SPAWNINGGENS
   )
   public static boolean Gen7;
   @Node(
      category = PixelmonConfig.Category.SPAWNINGGENS
   )
   public static boolean Gen8;
   @Node(
      category = PixelmonConfig.Category.POKELOOT
   )
   public static boolean spawnNormal;
   @Node(
      category = PixelmonConfig.Category.POKELOOT
   )
   public static boolean spawnHidden;
   @Node(
      category = PixelmonConfig.Category.POKELOOT
   )
   public static boolean spawnGrotto;
   @Node(
      category = PixelmonConfig.Category.POKELOOT,
      minValue = 0.0,
      maxValue = 3.0,
      useSlider = true
   )
   public static EnumPokelootRate spawnRate;
   @Node(
      category = PixelmonConfig.Category.POKELOOT,
      minValue = 0.0,
      maxValue = 3.0,
      useSlider = true
   )
   public static EnumPokelootModes spawnMode;
   @Node(
      category = PixelmonConfig.Category.POKELOOT,
      nameOverride = "timedLootReuseHours",
      minValue = 0.0
   )
   public static int lootTime;
   @Node(
      category = PixelmonConfig.Category.AFKHANDLER,
      nameOverride = "enableAFKHandler"
   )
   public static boolean afkHandlerOn;
   @Node(
      category = PixelmonConfig.Category.AFKHANDLER,
      nameOverride = "afkActivateSeconds",
      minValue = 0.0
   )
   public static int afkTimerActivateSeconds;
   @Node(
      category = PixelmonConfig.Category.AFKHANDLER,
      nameOverride = "afkHandlerTurnSeconds",
      minValue = 0.0
   )
   public static int afkTimerTurnSeconds;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      minValue = 0.0
   )
   public static double renderDistanceWeight;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      nameOverride = "namePlateRange",
      minValue = 0.0,
      maxValue = 3.0,
      useSlider = true
   )
   public static int nameplateRangeModifier;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      nameOverride = "renderWildLevels"
   )
   public static boolean renderWildLevels;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS
   )
   public static boolean showWildNames;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      nameOverride = "scalePokemonModels",
      minValue = 0.0
   )
   public static boolean scaleModelsUp;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      nameOverride = "useSmoothShadingOnPokeBalls"
   )
   public static boolean enableSmoothPokeballShading;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      nameOverride = "useSmoothShadingOnPokemon"
   )
   public static boolean enableSmoothPokemonShading;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      requiresRestart = true
   )
   public static boolean smoothAnimations;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      nameOverride = "showCurrentAttackTarget"
   )
   public static boolean showTarget;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS
   )
   public static boolean drawHealthBars;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS
   )
   public static boolean useBattleCamera;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS
   )
   public static boolean playerControlCamera;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS
   )
   public static boolean onlyShowAttackEffectsToBattlers;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS
   )
   public static boolean advancedBattleInformation;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      minValue = 0.0
   )
   public static int rangeToDisplayAttackAnimations;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS
   )
   public static boolean emissiveTextures;
   @Node(
      category = PixelmonConfig.Category.GRAPHICS,
      minValue = 1.0
   )
   public static int emissiveTexturesDistance;
   @Node(
      category = PixelmonConfig.Category.BREEDING
   )
   public static boolean allowBreeding;
   @Node(
      category = PixelmonConfig.Category.BREEDING
   )
   public static boolean allowDittoDittoBreeding;
   @Node(
      category = PixelmonConfig.Category.BREEDING
   )
   public static boolean allowRanchExpansion;
   @Node(
      category = PixelmonConfig.Category.BREEDING
   )
   public static boolean allowRandomBreedingEggsToBeLegendary;
   @Node(
      category = PixelmonConfig.Category.BREEDING,
      minValue = 20.0
   )
   public static int breedingTicks;
   @Node(
      category = PixelmonConfig.Category.BREEDING,
      minValue = 0.0
   )
   public static int maxCumulativePokemonInRanch;
   @Node(
      category = PixelmonConfig.Category.BREEDING,
      nameOverride = "numBreedingStages",
      minValue = 0.0
   )
   public static int numBreedingLevels;
   @Node(
      category = PixelmonConfig.Category.BREEDING
   )
   public static float ovalCharmMultiplier;
   @Node(
      category = PixelmonConfig.Category.BREEDING
   )
   public static boolean regionalFormsByDimension;
   @Node(
      category = PixelmonConfig.Category.BREEDING,
      type = Integer.class
   )
   public static List alolanEggDimensions;
   @Node(
      category = PixelmonConfig.Category.BREEDING,
      type = Integer.class
   )
   public static List galarianEggDimensions;
   @Node(
      category = PixelmonConfig.Category.BREEDING,
      minValue = 0.0
   )
   public static int stepsPerEggCycle;
   @Node(
      category = PixelmonConfig.Category.BREEDING
   )
   public static boolean useBreedingEnvironment;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES
   )
   public static boolean scaleGrassBattles;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES,
      nameOverride = "pokeGiftReusable"
   )
   public static boolean pokegiftMany;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES,
      nameOverride = "pokeGiftHaveEvents"
   )
   public static boolean doPokegiftEvents;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES,
      nameOverride = "eventPokeGiftLoad"
   )
   public static boolean isPokegiftEvent;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES,
      nameOverride = "eventHasLegendaries"
   )
   public static boolean pokegiftEventLegendaries;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES,
      nameOverride = "eventHasShinies"
   )
   public static boolean pokegiftEventShinies;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES,
      nameOverride = "eventMaxPokemon",
      minValue = 1.0,
      maxValue = 10.0,
      useSlider = true
   )
   public static int pokegiftEventMaxPokes;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES,
      nameOverride = "eventShinyRate",
      minValue = 0.0,
      maxValue = 100.0,
      useSlider = true
   )
   public static int pokegiftEventShinyRate;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES,
      nameOverride = "eventTime"
   )
   public static String customPokegiftEventTime;
   @Node(
      category = PixelmonConfig.Category.PIXELUTILITIES,
      nameOverride = "eventCoords",
      type = String.class
   )
   public static List pokegiftEventCoords;
   @Node(
      category = PixelmonConfig.Category.EXTERNALMOVES
   )
   public static boolean allowExternalMoves;
   @Node(
      category = PixelmonConfig.Category.EXTERNALMOVES
   )
   public static boolean allowDestructiveExternalMoves;
   @Node(
      category = PixelmonConfig.Category.EXTERNALMOVES
   )
   public static float forageChance;
   @Node(
      category = PixelmonConfig.Category.STARTERS
   )
   public static boolean useCustomStarters;
   @Node(
      category = PixelmonConfig.Category.STARTERS,
      nameOverride = "shiny"
   )
   public static boolean shinyStarter;
   @Node(
      category = PixelmonConfig.Category.STARTERS
   )
   public static boolean starterMarks;
   @Node(
      category = PixelmonConfig.Category.STARTERS,
      nameOverride = "level",
      minValue = 1.0,
      maxValue = 100.0,
      useSlider = true
   )
   public static int starterLevel;
   @Node(
      category = PixelmonConfig.Category.STARTERS,
      type = String.class
   )
   public static List starterList;
   @Node(
      category = PixelmonConfig.Category.RIDING
   )
   public static boolean allowRiding;
   @Node(
      category = PixelmonConfig.Category.RIDING,
      minValue = 0.0
   )
   public static float ridingSpeedMultiplier;
   @Node(
      category = PixelmonConfig.Category.RIDING
   )
   public static boolean enablePointToSteer;
   @Node(
      category = PixelmonConfig.Category.RIDING
   )
   public static boolean landMount;
   @Node(
      category = PixelmonConfig.Category.RIDING
   )
   public static boolean requireHM;
   @Node(
      category = PixelmonConfig.Category.RIDING
   )
   public static float flyingSpeedLimit;
   @Node(
      category = PixelmonConfig.Category.POKERUS,
      nameOverride = "pkrsEnabled"
   )
   public static boolean pokerusEnabled;
   @Node(
      category = PixelmonConfig.Category.POKERUS,
      nameOverride = "pkrsSpawnRate",
      minValue = 0.0
   )
   public static float pokerusRate;
   @Node(
      category = PixelmonConfig.Category.POKERUS,
      nameOverride = "pkrsInformPlayers"
   )
   public static boolean pokerusInformPlayers;
   @Node(
      category = PixelmonConfig.Category.POKERUS,
      nameOverride = "pkrsSpreadRate",
      minValue = -1.0
   )
   public static int pokerusSpreadRate;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      requiresRestart = true
   )
   public static boolean spawnStructures;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      requiresRestart = true
   )
   public static boolean spawnBirdShrines;
   @Node(
      category = PixelmonConfig.Category.GENERAL,
      requiresRestart = true
   )
   public static boolean spawnCelebiShrines;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      requiresRestart = true
   )
   public static boolean replaceMCVillagers;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      nameOverride = "spawnPokeMarts",
      requiresRestart = true
   )
   public static boolean spawnPokemarts;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static boolean spawnZygardeCells;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      requiresRestart = true
   )
   public static boolean spawnGyms;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      requiresRestart = true
   )
   public static boolean injectIntoLootTables;
   @Node(
      category = PixelmonConfig.Category.SPAWNING,
      type = String.class,
      requiresRestart = true
   )
   public static List lootIgnoreList;
   @Node(
      category = PixelmonConfig.Category.SPAWNING
   )
   public static double ilexShrineSpawnRate;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static boolean ultraSpace;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS,
      requiresRestart = true
   )
   public static int ultraSpaceDimId;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS,
      minValue = 0.0
   )
   public static float ultraSpaceShinyModifier;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS,
      minValue = 0.0
   )
   public static float ultraSpaceBossModifier;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS,
      minValue = 0.0
   )
   public static float ultraSpaceHiddenAbilityModifier;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS,
      minValue = -1.0
   )
   public static float ultraSpaceGigantamaxFactorModifier;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static boolean ultraSpaceColourblindMode;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static boolean spawnUltraSpaceExtraShrines;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static boolean spawnUltraSpaceNetherForts;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static boolean spawnUltraSpaceEndCities;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static boolean spawnUltraSpaceExtraOres;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static boolean spawnUltraSpaceExtraPlants;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static boolean spawnUltraSpaceExtraLoots;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static boolean drownedWorld;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS,
      requiresRestart = true
   )
   public static int drownedWorldDimId;
   @Node(
      category = PixelmonConfig.Category.DIMENSIONS
   )
   public static int drownedWorldRadius;
   @Node(
      category = PixelmonConfig.Category.RAIDS,
      type = Float.class
   )
   public static List raidShinyChances;
   @Node(
      category = PixelmonConfig.Category.RAIDS,
      type = Float.class
   )
   public static List raidHAChances;
   @Node(
      category = PixelmonConfig.Category.RAIDS,
      type = Float.class
   )
   public static List raidGigantamaxFactorChances;
   @Node(
      category = PixelmonConfig.Category.RAIDS
   )
   public static float denSpawnChancePerChunk;
   @Node(
      category = PixelmonConfig.Category.RAIDS
   )
   public static float denAdditionalSpawnChanceOnLiquid;
   @Node(
      category = PixelmonConfig.Category.RAIDS
   )
   public static int denRespawnTime;
   @Node(
      category = PixelmonConfig.Category.RAIDS
   )
   public static float denRespawnChance;
   @Node(
      category = PixelmonConfig.Category.RAIDS,
      type = Integer.class
   )
   public static List denStarWeights;
   @Node(
      category = PixelmonConfig.Category.RAIDS,
      type = Integer.class
   )
   public static List denStarDrops;
   @Node(
      category = PixelmonConfig.Category.RAIDS
   )
   public static boolean raidHaveLegendaries;
   @Node(
      category = PixelmonConfig.Category.RAIDS
   )
   public static boolean raidHaveUltraBeasts;
   @Node(
      category = PixelmonConfig.Category.RAIDS,
      type = Boolean.class
   )
   public static List raidMasterBallBlock;
   @Node(
      category = PixelmonConfig.Category.RAIDS,
      type = Boolean.class
   )
   public static List raidOnlyLeaderCanCatch;
   @Node(
      category = PixelmonConfig.Category.RAIDS,
      type = Float.class
   )
   public static List raidCatchHealthPercentage;
   @Node(
      category = PixelmonConfig.Category.RAIDS,
      type = String.class
   )
   public static List raidBlacklist;
   @Node(
      category = PixelmonConfig.Category.RAIDS
   )
   public static boolean raidDensBreakable;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static boolean useExternalJSONFilesDrops;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static boolean useExternalJSONFilesMoves;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static boolean useExternalJSONFilesNPCs;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static boolean useExternalJSONFilesRules;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static boolean useExternalJSONFilesSpawning;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static boolean useExternalJSONFilesStats;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static boolean useExternalJSONFilesStructures;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static boolean useExternalJSONFilesQuests;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static String spawnSetFolder;
   @Node(
      category = PixelmonConfig.Category.EXTERNALFILES
   )
   public static boolean useBetterSpawnerConfig;
   @Node(
      category = PixelmonConfig.Category.STORAGE
   )
   public static boolean useAsyncSaving;
   @Node(
      category = PixelmonConfig.Category.STORAGE
   )
   public static int asyncInterval;
   @Node(
      category = PixelmonConfig.Category.ELEVATOR,
      minValue = 1.0,
      maxValue = 255.0,
      useSlider = true
   )
   public static int elevatorSearchRange;
   @Node(
      category = PixelmonConfig.Category.QUESTS
   )
   public static int questMaxRange;
   @Node(
      category = PixelmonConfig.Category.QUESTS
   )
   public static boolean questRandomNPCs;
   public static boolean allowWildPokemonFleeing;
   private static HoconConfigurationLoader configurationLoader;
   private static CommentedConfigurationNode mainNode;
   private static CommentedConfigurationNode defaults;

   public static void init(File configFile) {
      configurationLoader = ((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setFile(configFile)).build();
      if (defaults == null) {
         defaults = configFromFields();
      }

      try {
         reload();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static void reload() throws IOException {
      mainNode = (CommentedConfigurationNode)configurationLoader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));
      load(mainNode);
   }

   public static void saveConfig() {
      try {
         CommentedConfigurationNode config = configFromFields();
         configurationLoader.save(config);
         mainNode = (CommentedConfigurationNode)configurationLoader.load();
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }

   public static CommentedConfigurationNode getConfig() {
      return mainNode;
   }

   public static void disableEventLoading() {
      isPokegiftEvent = false;
      saveConfig();
   }

   public static float getShinyRate(int dimension) {
      return dimension == UltraSpace.DIM_ID ? shinyRate * ultraSpaceShinyModifier : shinyRate;
   }

   public static float getBossRate(int dimension) {
      return dimension == UltraSpace.DIM_ID ? bossRate * ultraSpaceBossModifier : bossRate;
   }

   public static float getHiddenAbilityRate(int dimension) {
      return dimension == UltraSpace.DIM_ID ? hiddenAbilityRate * ultraSpaceHiddenAbilityModifier : hiddenAbilityRate;
   }

   public static float getGigantamaxFactorRate(int dimension) {
      if (dimension == UltraSpace.DIM_ID) {
         return ultraSpaceGigantamaxFactorModifier == -1.0F ? -1.0F : gigantamaxFactorRate * ultraSpaceGigantamaxFactorModifier;
      } else {
         return gigantamaxFactorRate;
      }
   }

   public static float getLegendaryRate() {
      return legendarySpawnChance;
   }

   public static float getRaidShinyRate(int stars) {
      int ordinal = stars - 1;
      return raidShinyChances.size() > ordinal ? (Float)raidShinyChances.get(ordinal) : shinyRate;
   }

   public static float getRaidHARate(int stars) {
      int ordinal = stars - 1;
      return raidHAChances.size() > ordinal ? (Float)raidHAChances.get(ordinal) : hiddenAbilityRate;
   }

   public static float getRaidGigantamaxFactorRate(int stars) {
      int ordinal = stars - 1;
      return raidGigantamaxFactorChances.size() > ordinal ? (Float)raidGigantamaxFactorChances.get(ordinal) : gigantamaxFactorRate;
   }

   public static int getRaidDropCount(int stars) {
      int ordinal = stars - 1;
      return denStarDrops.size() > ordinal ? (Integer)denStarDrops.get(ordinal) : 5;
   }

   public static boolean getRaidCanUseMaster(int stars) {
      int ordinal = stars - 1;
      if (raidMasterBallBlock.size() > ordinal) {
         return !(Boolean)raidMasterBallBlock.get(ordinal);
      } else {
         return true;
      }
   }

   public static boolean getRaidCanAllCatch(int stars) {
      int ordinal = stars - 1;
      if (raidOnlyLeaderCanCatch.size() > ordinal) {
         return !(Boolean)raidOnlyLeaderCanCatch.get(ordinal);
      } else {
         return true;
      }
   }

   public static float getRaidCatchHealthPercentage(int stars) {
      int ordinal = stars - 1;
      return raidCatchHealthPercentage.size() > ordinal ? (Float)raidCatchHealthPercentage.get(ordinal) : 0.0F;
   }

   public static double getGrowthModifier() {
      return growthModifier;
   }

   public static boolean isGenerationEnabled(int generation) {
      switch (generation) {
         case 1:
            return Gen1;
         case 2:
            return Gen2;
         case 3:
            return Gen3;
         case 4:
            return Gen4;
         case 5:
            return Gen5;
         case 6:
            return Gen6;
         case 7:
            return Gen7;
         case 8:
            return Gen8;
         default:
            return false;
      }
   }

   public static boolean allGenerationsDisabled() {
      return !Gen1 && !Gen2 && !Gen3 && !Gen4 && !Gen5 && !Gen6 && !Gen7 && !Gen8;
   }

   public static boolean allGenerationsEnabled() {
      return Gen1 && Gen2 && Gen3 && Gen4 && Gen5 && Gen6 && Gen7 && Gen8;
   }

   public static boolean usesGen8Features(int dimension) {
      return bothGenDimensions.contains(dimension) || !oldGenDimensions.contains(dimension);
   }

   public static boolean usesGen7Features(int dimension) {
      return bothGenDimensions.contains(dimension) || oldGenDimensions.contains(dimension);
   }

   public static boolean checkMigrations(CommentedConfigurationNode configNode) {
      boolean shouldSave = false;
      CommentedConfigurationNode val;
      if (configNode.hasMapChildren()) {
         for(Iterator var2 = configNode.getChildrenMap().values().iterator(); var2.hasNext(); shouldSave = checkMigrations(val) || shouldSave) {
            val = (CommentedConfigurationNode)var2.next();
         }
      }

      shouldSave = ConfigMigrationHandler.handleMigration(configNode) || shouldSave;
      return shouldSave;
   }

   public static int getRandomEnabledGeneration() {
      if (allGenerationsEnabled()) {
         return RandomHelper.getRandomNumberBetween(1, 8);
      } else if (allGenerationsDisabled()) {
         throw new IllegalStateException();
      } else {
         ArrayList generations = new ArrayList();
         if (Gen1) {
            generations.add(1);
         }

         if (Gen2) {
            generations.add(2);
         }

         if (Gen3) {
            generations.add(3);
         }

         if (Gen4) {
            generations.add(4);
         }

         if (Gen5) {
            generations.add(5);
         }

         if (Gen6) {
            generations.add(6);
         }

         if (Gen7) {
            generations.add(7);
         }

         if (Gen8) {
            generations.add(8);
         }

         return (Integer)generations.get(RandomHelper.getRandomNumberBetween(0, generations.size() - 1));
      }
   }

   public static int getShopMaxStackSize(ItemStack stack) {
      if (limitShopKeeperStackSize) {
         return stack == null ? 64 : stack.func_77976_d();
      } else {
         return 2304;
      }
   }

   private static void load(CommentedConfigurationNode config) {
      Iterator var1 = PixelmonConfig.Category.getTitles().iterator();

      String name;
      while(var1.hasNext()) {
         String category = (String)var1.next();
         CommentedConfigurationNode categoryNode = config;
         String[] var4 = category.split("\\.");
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            name = var4[var6];
            categoryNode = categoryNode.getNode(name);
         }

         categoryNode.setComment(I18n.func_74838_a("pixelmon.config." + category.toLowerCase() + ".comment"));
      }

      Field[] var14 = PixelmonConfig.class.getDeclaredFields();
      int var15 = var14.length;

      for(int var16 = 0; var16 < var15; ++var16) {
         Field field = var14[var16];
         if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.getAnnotation(Node.class) != null) {
            Node node = (Node)field.getAnnotation(Node.class);
            String category = node.category().getTitle();
            name = node.nameOverride().isEmpty() ? field.getName() : node.nameOverride();
            String comment = "pixelmon.config." + name + ".comment";
            CommentedConfigurationNode categoryNode = config;
            String[] var10 = category.split("\\.");
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               String cat = var10[var12];
               categoryNode = categoryNode.getNode(cat);
            }

            CommentedConfigurationNode configNode = categoryNode.getNode(name);
            configNode.setComment(I18n.func_74838_a(comment));
            set(field, configNode, node);
         }
      }

      Pixelmon.LOGGER.info("Checking for migrations..");
      if (checkMigrations(getConfig())) {
         saveConfig();
      }

      PixelmonServerConfig.load();
      saveConfig();
   }

   private static void set(Field field, CommentedConfigurationNode configNode, Node node) {
      try {
         Class type = field.getType();
         if (type == Boolean.TYPE) {
            boolean value = configNode.getBoolean(field.getBoolean((Object)null));
            field.setBoolean((Object)null, value);
         } else if (type == Integer.TYPE) {
            int value = configNode.getInt(field.getInt((Object)null));
            value = (double)value < node.minValue() ? (int)node.minValue() : ((double)value > node.maxValue() ? (int)node.maxValue() : value);
            field.setInt((Object)null, value);
         } else if (type == Float.TYPE) {
            float value = configNode.getFloat(field.getFloat((Object)null));
            value = (double)value < node.minValue() ? (float)node.minValue() : ((double)value > node.maxValue() ? (float)node.maxValue() : value);
            field.setFloat((Object)null, value);
         } else if (type == Double.TYPE) {
            double value = configNode.getDouble(field.getDouble((Object)null));
            value = value < node.minValue() ? node.minValue() : (value > node.maxValue() ? node.maxValue() : value);
            field.setDouble((Object)null, value);
         } else if (type == String.class) {
            String value = configNode.getString((String)field.get((Object)null));
            field.set((Object)null, value);
         } else if (type == List.class) {
            List value = configNode.getList(TypeToken.of(node.type()), (List)field.get((Object)null));
            field.set((Object)null, value);
         } else if (Enum.class.isAssignableFrom(type)) {
            field.set((Object)null, type.getEnumConstants()[configNode.getInt(((Enum)field.get((Object)null)).ordinal())]);
         } else {
            Pixelmon.LOGGER.error("Cannot read config value " + configNode.getKey());
         }
      } catch (ObjectMappingException | IllegalAccessException var6) {
         var6.printStackTrace();
      }

   }

   private static CommentedConfigurationNode configFromFields() {
      if (configurationLoader == null) {
         throw new NullPointerException("Configuration loader null, configFromFields() called to early!");
      } else {
         CommentedConfigurationNode emptyNode = configurationLoader.createEmptyNode(ConfigurationOptions.defaults());
         Iterator var1 = PixelmonConfig.Category.getTitles().iterator();

         String name;
         while(var1.hasNext()) {
            String category = (String)var1.next();
            CommentedConfigurationNode categoryNode = emptyNode;
            String[] var4 = category.split("\\.");
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               name = var4[var6];
               categoryNode = categoryNode.getNode(name);
            }

            categoryNode.setComment(I18n.func_74838_a("pixelmon.config." + category.toLowerCase() + ".comment"));
         }

         try {
            Field[] var15 = PixelmonConfig.class.getDeclaredFields();
            int var16 = var15.length;

            for(int var17 = 0; var17 < var16; ++var17) {
               Field field = var15[var17];
               if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.getAnnotation(Node.class) != null) {
                  Node node = (Node)field.getAnnotation(Node.class);
                  String category = node.category().getTitle();
                  name = node.nameOverride().isEmpty() ? field.getName() : node.nameOverride();
                  String comment = "pixelmon.config." + name + ".comment";
                  CommentedConfigurationNode categoryNode = emptyNode;
                  String[] var10 = category.split("\\.");
                  int var11 = var10.length;

                  for(int var12 = 0; var12 < var11; ++var12) {
                     String cat = var10[var12];
                     categoryNode = categoryNode.getNode(cat);
                  }

                  CommentedConfigurationNode configNode = categoryNode.getNode(name);
                  configNode.setComment(I18n.func_74838_a(comment));
                  if (Enum.class.isAssignableFrom(field.getType())) {
                     configNode.setValue(((Enum)field.get((Object)null)).ordinal());
                  } else {
                     configNode.setValue(field.get((Object)null));
                  }
               }
            }
         } catch (Throwable var14) {
            var14.printStackTrace();
         }

         return emptyNode;
      }
   }

   static {
      shrineEncounterMode = EnumEncounterMode.Once;
      opToUseSpawners = true;
      opToUseNpcEditor = true;
      opToUseQuestEditor = true;
      reuseTraders = false;
      movesPerTutor = 20;
      giveStarter = true;
      useSystemWorldTime = false;
      timeUpdateInterval = 30;
      battleAIWild = EnumBattleAIMode.Random;
      battleAIBoss = EnumBattleAIMode.Aggressive;
      battleAITrainer = EnumBattleAIMode.Tactical;
      allowMultiplePhotosOfSamePokemon = true;
      bedsHealPokemon = true;
      allowPokemonEditors = true;
      allowChisels = true;
      dataSaveOnWorldSave = true;
      useDropGUI = true;
      berryTreeGrowthMultiplier = 1.0F;
      useSystemTimeForBerries = false;
      maxLevel = 100;
      despawnRadius = 80;
      canPokemonBeHit = false;
      alwaysHaveMegaRing = false;
      alwaysHaveDynamaxBand = false;
      deleteUnwantedDrops = false;
      chanceToGetSpecialBidoof = 30;
      disabledNonPlayerPixelmonMovement = false;
      allowShinyCharmFromPokedex = true;
      allowOvalCharmFromPokedex = true;
      expCharmMultiplier = 1.5;
      catchingCharmMultiplier = 1.1;
      markCharmRolls = 3;
      oreColors = Lists.newArrayList(new String[]{"minecraft:iron_ore;E2C0AA;0", "minecraft:gold_ore;FCEE4B;0", "minecraft:redstone_ore;FF0000;0", "minecraft:emerald_ore;17DD62;1", "minecraft:diamond_ore;65F5E3;0", "minecraft:lapis_ore;1855BD;2", "minecraft:quartz_ore;EAE5DE;3", "pixelmon:amethyst_ore;9543A9;4", "pixelmon:sun_stone_ore;E57742;5", "pixelmon:silicon_ore;E2DFEE;0", "pixelmon:sapphire_ore;4A73E7;1", "pixelmon:ruby_ore;DD1C33;1", "pixelmon:fossil;443D32;6", "pixelmon:crystal_ore;AFEEEE;4", "pixelmon:bauxite_ore;D1B584;0", "pixelmon:dawn_dusk_ore;000000;7", "pixelmon:water_stone_ore;5D719F;8", "pixelmon:thunder_stone_ore;F0FF01;9", "pixelmon:leaf_stone_ore;7EA167;10", "pixelmon:fire_stone_ore;CC7132;11"});
      averageLureExpiryTicks = 17920;
      limitShopKeeperStackSize = true;
      relaxedBattleGimmickRules = true;
      oldGenDimensions = Lists.newArrayList();
      bothGenDimensions = Lists.newArrayList(new Integer[]{-1, 0, 1, 72, 73});
      dynamaxSize = 15.0F;
      allowIllegalShinies = true;
      battleMusicVolume = 1.0F;
      useUpdateEntityWithOptionalForceFix = false;
      verbose = false;
      berryJuiceCrafting = true;
      allowPVPExperience = true;
      allowTrainerExperience = true;
      returnHeldItems = true;
      forceEndBattleResult = EnumForceBattleResult.WINNER;
      expModifier = 1.0F;
      synchronizeChance = 1.0F;
      allowHappyHour = true;
      allowPayDay = true;
      payDayMultiplier = 5.0;
      allowGMaxGoldRush = true;
      gMaxGoldRushMultiplier = 100.0;
      allowAmuletCoin = true;
      pickupRate = 10;
      allowCatchCombo = true;
      catchComboThresholds = Lists.newArrayList(new Integer[]{0, 10, 20, 30, 40});
      catchComboExpBonuses = Lists.newArrayList(new Float[]{1.0F, 1.1F, 1.5F, 2.0F, 2.5F, 3.0F});
      catchComboShinyModifiers = Lists.newArrayList(new Float[]{1.0F, 1.0F, 1.4F, 1.8F, 2.2F, 2.2F});
      catchComboPerfectIVs = Lists.newArrayList(new Integer[]{0, 0, 2, 3, 4, 4});
      usePassiveHealer = true;
      chanceToRevivePassively = 0.001F;
      chanceToHealStatusPassively = 0.005F;
      stepsToHealHealthPassively = 75;
      passiveHealingMaxHealthPercentage = 0.25F;
      useOutsideEffects = true;
      stepsToApplyEffects = 15;
      poisonMaxDamage = 5;
      shinyRate = 4096.0F;
      bossRate = 256.0F;
      bossLevelIncreases = Lists.newArrayList(new Integer[]{5, 10, 20, 30, 40, 50, 25, 75});
      bossWeights = Lists.newArrayList(new Integer[]{16, 12, 8, 4, 2, 1});
      bossCandyChances = Lists.newArrayList(new Float[]{0.1F, 0.2F, 0.35F, 0.5F, 0.75F, 1.0F});
      bossesAlwaysMegaIfPossible = true;
      bossSpawnTicks = 10000;
      bossSpawnChance = 0.3F;
      legendarySpawnTicks = 25000;
      legendarySpawnChance = 0.3F;
      spawnTicksPlayerMultiplier = 0.01F;
      spawnLevelsByDistance = false;
      maxLevelByDistance = 60;
      distancePerLevel = 30;
      spawnLevelsCloserToPlayerLevels = true;
      spawnLevelsIncreaseInCaves = true;
      caveMaxMultiplier = 4;
      transformToDittoOnCatch = 0.001;
      meltanSpawnChance = 3072;
      meltanTransformChance = 0.25;
      canTransformToDittoOnCatch = Lists.newArrayList(new String[]{"Pidgey", "Rattata", "Gastly", "Zubat", "Mankey", "Yanma", "Hoothoot", "Sentret", "Zigzagoon", "Gulpin", "Whismur", "Taillow", "Remoraid", "Starly"});
      hiddenAbilityRate = 150.0F;
      gigantamaxFactorRate = 512.0F;
      allowLegendariesSpawn = true;
      legendaryDespawnTicks = 6000;
      doLegendaryEvent = true;
      doLegendaryRaidEvent = true;
      useRecentLevelMoves = false;
      despawnOnFleeOrLoss = false;
      entitiesPerPlayer = 45;
      spawnsPerPass = 2;
      spawnFrequency = 60.0F;
      minimumDistanceBetweenSpawns = 15.0F;
      minimumDistanceFromCentre = 18;
      maximumDistanceFromCentre = 64;
      horizontalTrackFactor = 80.0F;
      verticalTrackFactor = 0.0F;
      horizontalSliceRadius = 10;
      verticalSliceRadius = 25;
      maximumSpawnedPokemon = 3000;
      maximumSpawnedFlyingPokemon = 500;
      maximumSpawnedFlyingPokemonPerPlayer = 45;
      bellSuccessChance = 0.01;
      bellInclusionRange = 10;
      lureFluteDuration = 180;
      lureFluteLevelModifier = 10;
      Gen1 = true;
      Gen2 = true;
      Gen3 = true;
      Gen4 = true;
      Gen5 = true;
      Gen6 = true;
      Gen7 = true;
      Gen8 = true;
      spawnNormal = true;
      spawnHidden = true;
      spawnGrotto = true;
      spawnRate = EnumPokelootRate.NORMAL;
      spawnMode = EnumPokelootModes.FCFS;
      lootTime = 24;
      afkHandlerOn = false;
      afkTimerActivateSeconds = 90;
      afkTimerTurnSeconds = 15;
      renderDistanceWeight = 2.0;
      nameplateRangeModifier = 1;
      renderWildLevels = true;
      showWildNames = true;
      scaleModelsUp = true;
      enableSmoothPokeballShading = true;
      enableSmoothPokemonShading = true;
      smoothAnimations = false;
      showTarget = true;
      drawHealthBars = false;
      useBattleCamera = true;
      playerControlCamera = true;
      onlyShowAttackEffectsToBattlers = true;
      advancedBattleInformation = true;
      rangeToDisplayAttackAnimations = 40;
      emissiveTextures = true;
      emissiveTexturesDistance = 32;
      allowBreeding = true;
      allowDittoDittoBreeding = true;
      allowRanchExpansion = true;
      allowRandomBreedingEggsToBeLegendary = false;
      breedingTicks = 18000;
      maxCumulativePokemonInRanch = 0;
      numBreedingLevels = 5;
      ovalCharmMultiplier = 0.5F;
      regionalFormsByDimension = true;
      alolanEggDimensions = Lists.newArrayList(new Integer[]{72});
      galarianEggDimensions = Lists.newArrayList(new Integer[]{0});
      stepsPerEggCycle = 255;
      useBreedingEnvironment = true;
      scaleGrassBattles = false;
      pokegiftMany = false;
      doPokegiftEvents = true;
      isPokegiftEvent = false;
      pokegiftEventLegendaries = false;
      pokegiftEventShinies = false;
      pokegiftEventMaxPokes = 1;
      pokegiftEventShinyRate = 10;
      customPokegiftEventTime = "D/M";
      pokegiftEventCoords = Collections.singletonList("notConfigured");
      allowExternalMoves = true;
      allowDestructiveExternalMoves = true;
      forageChance = 0.3F;
      useCustomStarters = false;
      shinyStarter = false;
      starterMarks = true;
      starterLevel = 5;
      starterList = Arrays.asList("Bulbasaur", "Squirtle", "Charmander", "Chikorita", "Totodile", "Cyndaquil", "Treecko", "Mudkip", "Torchic", "Turtwig", "Piplup", "Chimchar", "Snivy", "Oshawott", "Tepig", "Chespin", "Froakie", "Fennekin", "Rowlet", "Popplio", "Litten", "Grookey", "Sobble", "Scorbunny");
      allowRiding = true;
      ridingSpeedMultiplier = 1.0F;
      enablePointToSteer = true;
      landMount = true;
      requireHM = false;
      flyingSpeedLimit = 4.0F;
      pokerusEnabled = true;
      pokerusRate = 12288.0F;
      pokerusInformPlayers = true;
      pokerusSpreadRate = 5;
      spawnStructures = true;
      spawnBirdShrines = true;
      spawnCelebiShrines = true;
      replaceMCVillagers = true;
      spawnPokemarts = true;
      spawnZygardeCells = true;
      spawnGyms = true;
      injectIntoLootTables = true;
      lootIgnoreList = Lists.newArrayList();
      ilexShrineSpawnRate = 0.005;
      ultraSpace = true;
      ultraSpaceDimId = 0;
      ultraSpaceShinyModifier = 0.5F;
      ultraSpaceBossModifier = 0.5F;
      ultraSpaceHiddenAbilityModifier = 0.5F;
      ultraSpaceGigantamaxFactorModifier = -1.0F;
      ultraSpaceColourblindMode = false;
      spawnUltraSpaceExtraShrines = true;
      spawnUltraSpaceNetherForts = true;
      spawnUltraSpaceEndCities = true;
      spawnUltraSpaceExtraOres = true;
      spawnUltraSpaceExtraPlants = true;
      spawnUltraSpaceExtraLoots = true;
      drownedWorld = true;
      drownedWorldDimId = 0;
      drownedWorldRadius = 30;
      raidShinyChances = new ArrayList(Arrays.asList(4096.0F, 4096.0F, 2048.0F, 1024.0F, 1024.0F));
      raidHAChances = new ArrayList(Arrays.asList(150.0F, 125.0F, 100.0F, 75.0F, 50.0F));
      raidGigantamaxFactorChances = new ArrayList(Arrays.asList(512.0F, 512.0F, 512.0F, 256.0F, 128.0F));
      denSpawnChancePerChunk = 0.03F;
      denAdditionalSpawnChanceOnLiquid = 0.5F;
      denRespawnTime = 100;
      denRespawnChance = 0.25F;
      denStarWeights = new ArrayList(Arrays.asList(10, 8, 6, 4, 2));
      denStarDrops = new ArrayList(Arrays.asList(2, 4, 6, 8, 10));
      raidHaveLegendaries = true;
      raidHaveUltraBeasts = true;
      raidMasterBallBlock = new ArrayList(Arrays.asList(false, false, false, false, false));
      raidOnlyLeaderCanCatch = new ArrayList(Arrays.asList(false, false, false, false, false));
      raidCatchHealthPercentage = new ArrayList(Arrays.asList(0.0F, 0.05F, 0.1F, 0.15F, 0.2F));
      raidBlacklist = new ArrayList(Arrays.asList("MissingNo"));
      raidDensBreakable = true;
      useExternalJSONFilesDrops = false;
      useExternalJSONFilesMoves = false;
      useExternalJSONFilesNPCs = false;
      useExternalJSONFilesRules = false;
      useExternalJSONFilesSpawning = false;
      useExternalJSONFilesStats = false;
      useExternalJSONFilesStructures = false;
      useExternalJSONFilesQuests = false;
      spawnSetFolder = "default";
      useBetterSpawnerConfig = false;
      useAsyncSaving = false;
      asyncInterval = 60;
      elevatorSearchRange = 10;
      questMaxRange = 500;
      questRandomNPCs = true;
      allowWildPokemonFleeing = false;
   }

   public static enum Category {
      GENERAL("General", true, false),
      BATTLE("Battle", true, false),
      SPAWNING("Spawning", true, false),
      SPAWNINGGENS("Spawning.Gens", true, false),
      POKELOOT("PokeLoot", true, false),
      AFKHANDLER("AFKHandler", true, false),
      GRAPHICS("Graphics", true, false),
      BREEDING("Breeding", true, false),
      PIXELUTILITIES("PixelUtilities", true, false),
      EXTERNALMOVES("ExternalMoves", true, false),
      POKERUS("Pokerus", true, false),
      DIMENSIONS("Dimensions", true, false),
      RAIDS("Raids", true, false),
      STORAGE("Storage", false, false),
      STARTERS("Starters", true, false),
      ELEVATOR("Elevator", true, false),
      HEALING("Healing", true, false),
      RIDING("Riding", true, false),
      OUTSIDEEFFECTS("OutsideEffects", true, false),
      EXTERNALFILES("ExternalFiles", true, true),
      QUESTS("Quests", true, true);

      private final String title;
      private final boolean showInGui;
      private final boolean requiresMcRestart;

      private Category(String title, boolean showInGui, boolean requiresMcRestart) {
         this.title = title;
         this.showInGui = showInGui;
         this.requiresMcRestart = requiresMcRestart;
      }

      public String getTitle() {
         return this.title;
      }

      public boolean shouldShowInGui() {
         return this.showInGui;
      }

      public boolean requiresMcRestart() {
         return this.requiresMcRestart;
      }

      public static List getTitles() {
         return (List)Arrays.stream(values()).map(Category::getTitle).collect(Collectors.toList());
      }
   }
}
