package com.pixelmonmod.pixelmon;

import com.pixelmonmod.pixelmon.advancements.PixelmonAdvancements;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccountManager;
import com.pixelmonmod.pixelmon.api.lures.ILureExpirer;
import com.pixelmonmod.pixelmon.api.pokemon.IHatchEggs;
import com.pixelmonmod.pixelmon.api.pokemon.IPassiveEffects;
import com.pixelmonmod.pixelmon.api.pokemon.IPassiveHealer;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.storage.IStorageManager;
import com.pixelmonmod.pixelmon.api.storage.IStorageSaveScheduler;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.comm.PacketRegistry;
import com.pixelmonmod.pixelmon.commands.BattleCommand;
import com.pixelmonmod.pixelmon.commands.BattleLogCommand;
import com.pixelmonmod.pixelmon.commands.BlockSnapShot;
import com.pixelmonmod.pixelmon.commands.BreedCommand;
import com.pixelmonmod.pixelmon.commands.CatchingCharmCommand;
import com.pixelmonmod.pixelmon.commands.CheckSpawnsCommand;
import com.pixelmonmod.pixelmon.commands.DebugCommand;
import com.pixelmonmod.pixelmon.commands.DeepStorageCmd;
import com.pixelmonmod.pixelmon.commands.DoLegendarySpawnCommand;
import com.pixelmonmod.pixelmon.commands.DynamaxBandCommand;
import com.pixelmonmod.pixelmon.commands.EndBattleCommand;
import com.pixelmonmod.pixelmon.commands.ExpCharmCommand;
import com.pixelmonmod.pixelmon.commands.FillDexCommand;
import com.pixelmonmod.pixelmon.commands.FreezeCommand;
import com.pixelmonmod.pixelmon.commands.GetBiomeData;
import com.pixelmonmod.pixelmon.commands.GivePixelSpriteCommand;
import com.pixelmonmod.pixelmon.commands.HealCommand;
import com.pixelmonmod.pixelmon.commands.MarkCharmCommand;
import com.pixelmonmod.pixelmon.commands.MegaRingCommand;
import com.pixelmonmod.pixelmon.commands.OvalCharmCommand;
import com.pixelmonmod.pixelmon.commands.PixelTPCommand;
import com.pixelmonmod.pixelmon.commands.PokeGiveCommand;
import com.pixelmonmod.pixelmon.commands.RanchUnlockCommand;
import com.pixelmonmod.pixelmon.commands.ReloadCommand;
import com.pixelmonmod.pixelmon.commands.ReloadMoveAnimations;
import com.pixelmonmod.pixelmon.commands.SetPartyCommand;
import com.pixelmonmod.pixelmon.commands.SetRaidCommand;
import com.pixelmonmod.pixelmon.commands.ShinyCharmCommand;
import com.pixelmonmod.pixelmon.commands.SpawnCommand;
import com.pixelmonmod.pixelmon.commands.SpawningCommand;
import com.pixelmonmod.pixelmon.commands.SpectateCommand;
import com.pixelmonmod.pixelmon.commands.Stats;
import com.pixelmonmod.pixelmon.commands.StatsReset;
import com.pixelmonmod.pixelmon.commands.StrucCommand;
import com.pixelmonmod.pixelmon.commands.TeachCommand;
import com.pixelmonmod.pixelmon.commands.TierShow;
import com.pixelmonmod.pixelmon.commands.WarpPlateCommand;
import com.pixelmonmod.pixelmon.commands.econ.BankTransferCommand;
import com.pixelmonmod.pixelmon.commands.econ.GiveMoneyCommand;
import com.pixelmonmod.pixelmon.commands.quests.CompleteAllQuestsCommand;
import com.pixelmonmod.pixelmon.commands.quests.QuestDataCommand;
import com.pixelmonmod.pixelmon.commands.quests.ReloadQuestsCommand;
import com.pixelmonmod.pixelmon.commands.quests.ResetAllQuestsCommand;
import com.pixelmonmod.pixelmon.commands.quests.SetStageCommand;
import com.pixelmonmod.pixelmon.config.BlockRevealParser;
import com.pixelmonmod.pixelmon.config.FormLogRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.RemapHandler;
import com.pixelmonmod.pixelmon.listener.PixelmonLootTables;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import com.pixelmonmod.pixelmon.storage.ReforgedStorageManager;
import com.pixelmonmod.pixelmon.storage.adapters.ReforgedFileAdapter;
import com.pixelmonmod.pixelmon.storage.schedulers.ReforgedStorageAsyncScheduler;
import com.pixelmonmod.pixelmon.storage.schedulers.ReforgedStorageStandardScheduler;
import com.pixelmonmod.pixelmon.tickHandlers.SteadyLureExpirer;
import com.pixelmonmod.pixelmon.tickHandlers.WalkingEggHatcher;
import com.pixelmonmod.pixelmon.tickHandlers.WalkingPassiveEffects;
import com.pixelmonmod.pixelmon.tickHandlers.WalkingPassiveHealer;
import com.pixelmonmod.pixelmon.util.network.BetterNetworkWrapper;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.debug.DebugWorldRegistry;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.DrownedWorld;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpace;
import java.io.File;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
   modid = "pixelmon",
   name = "Pixelmon",
   version = "8.4.3",
   updateJSON = "https://reforged.gg/forge/update.json",
   guiFactory = "com.pixelmonmod.pixelmon.client.gui.factory.GuiFactoryPixelmon",
   dependencies = "required-client:forge@[14.23.5.2860,);"
)
public class Pixelmon {
   public static final String MODID = "pixelmon";
   public static final String NAME = "Pixelmon";
   protected static final String VERSION = "8.4.3";
   static final String UPDATE = "https://reforged.gg/forge/update.json";
   public static final EventBus EVENT_BUS = new EventBus();
   public static final Logger LOGGER = LogManager.getLogger("Pixelmon");
   @Instance("pixelmon")
   public static Pixelmon instance;
   @SidedProxy(
      clientSide = "com.pixelmonmod.pixelmon.client.ClientProxy",
      serverSide = "com.pixelmonmod.pixelmon.CommonProxy"
   )
   public static CommonProxy proxy;
   public static boolean freeze = false;
   public static File modDirectory;
   public static SimpleNetworkWrapper network;
   public static boolean canSendOutPokemon = true;
   public static boolean devEnvironment;
   public static PixelmonAdvancements ADVANCEMENTS;
   public static IPixelmonBankAccountManager moneyManager;
   public static IStorageManager storageManager;
   public static PokemonFactory pokemonFactory;
   public static IHatchEggs eggHatcher;
   public static IPassiveHealer passiveHealer;
   public static IPassiveEffects passiveEffects;
   public static ILureExpirer lureExpirer;
   private static PixelmonEventHandler eventHandler;

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      LOGGER.info("Loading Pixelmon version 8.4.3");
      network = new BetterNetworkWrapper("pixelmon");
      PacketRegistry.registerPackets();
      PacketRegistry.replaceNetworkCheck();
      modDirectory = new File(event.getModConfigurationDirectory().getParent());
      PixelmonConfig.init(new File("./config/pixelmon.hocon"));
      ADVANCEMENTS = new PixelmonAdvancements();
      PokemonSpec.registerDefaultExtraSpecs();
      eventHandler = new PixelmonEventHandler();
      MinecraftForge.EVENT_BUS.register(eventHandler);
      EVENT_BUS.register(eventHandler);
      PixelmonLootTables.register();
      proxy.registerKeyBindings();
      proxy.removeDungeonMobs();
      proxy.preInit();
      UltraSpace.register();
      UltraSpace.registerWorldFeatures();
      DrownedWorld.register();
      DrownedWorld.registerWorldFeatures();
      if (devEnvironment) {
         DebugWorldRegistry.init();
      }

      try {
         AttackBase.loadAllAttacks();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      pokemonFactory = new ReforgedPokemonFactory();
      ReforgedStorageManager storageManager = new ReforgedStorageManager((IStorageSaveScheduler)(PixelmonConfig.useAsyncSaving ? new ReforgedStorageAsyncScheduler() : new ReforgedStorageStandardScheduler()), new ReforgedFileAdapter());
      Pixelmon.storageManager = storageManager;
      storageManager.setSaveAdapter(storageManager.getSaveAdapter());
      storageManager.setSaveScheduler(storageManager.getSaveScheduler());
      MinecraftForge.EVENT_BUS.register(storageManager);
      MinecraftForge.EVENT_BUS.register(storageManager.getSaveScheduler());
      if (Pixelmon.storageManager instanceof IPixelmonBankAccountManager && moneyManager == null) {
         moneyManager = storageManager;
      }

      passiveHealer = new WalkingPassiveHealer();
      eggHatcher = new WalkingEggHatcher();
      passiveEffects = new WalkingPassiveEffects();
      lureExpirer = new SteadyLureExpirer();
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.registerInteractions();
      NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
      proxy.init();
      proxy.registerRenderers();
      proxy.registerTickHandlers();
      FormLogRegistry.init();
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      BlockRevealParser.parse();
      proxy.registerCommands();
      proxy.postInit();
   }

   @EventHandler
   public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
      RemapHandler.attemptHackyFixForRipOffPixelmonMappingIssues(event.getServer());
      if (event.getSide() != Side.SERVER && event.getSide() == Side.CLIENT) {
         storageManager.clearAll();
      }

   }

   public static boolean isServer() {
      return FMLCommonHandler.instance().getMinecraftServerInstance() != null;
   }

   @EventHandler
   public void onServerStart(FMLServerStartingEvent event) {
      event.registerServerCommand(new BattleLogCommand());
      event.registerServerCommand(new GiveMoneyCommand());
      event.registerServerCommand(new BankTransferCommand());
      event.registerServerCommand(new BattleCommand());
      event.registerServerCommand(new BlockSnapShot());
      event.registerServerCommand(new BreedCommand());
      event.registerServerCommand(new CheckSpawnsCommand());
      event.registerServerCommand(new DeepStorageCmd());
      event.registerServerCommand(new DoLegendarySpawnCommand());
      event.registerServerCommand(new EndBattleCommand());
      event.registerServerCommand(new FreezeCommand());
      event.registerServerCommand(new HealCommand());
      event.registerServerCommand(new MegaRingCommand());
      event.registerServerCommand(new DynamaxBandCommand());
      event.registerServerCommand(new ShinyCharmCommand());
      event.registerServerCommand(new OvalCharmCommand());
      event.registerServerCommand(new ExpCharmCommand());
      event.registerServerCommand(new CatchingCharmCommand());
      event.registerServerCommand(new MarkCharmCommand());
      event.registerServerCommand(new PokeGiveCommand());
      event.registerServerCommand(new SetPartyCommand());
      event.registerServerCommand(new SpawnCommand());
      event.registerServerCommand(new SpawningCommand());
      event.registerServerCommand(new SpectateCommand());
      event.registerServerCommand(new Stats());
      event.registerServerCommand(new StatsReset());
      event.registerServerCommand(new StrucCommand());
      event.registerServerCommand(new TeachCommand());
      event.registerServerCommand(new TierShow());
      event.registerServerCommand(new RanchUnlockCommand());
      event.registerServerCommand(new WarpPlateCommand());
      event.registerServerCommand(new GivePixelSpriteCommand());
      event.registerServerCommand(new ReloadCommand());
      event.registerServerCommand(new ReloadMoveAnimations());
      event.registerServerCommand(new SetStageCommand());
      event.registerServerCommand(new ResetAllQuestsCommand());
      event.registerServerCommand(new CompleteAllQuestsCommand());
      event.registerServerCommand(new QuestDataCommand());
      event.registerServerCommand(new ReloadQuestsCommand());
      event.registerServerCommand(new SetRaidCommand());
      if (event.getSide().isClient()) {
         event.registerServerCommand(new FillDexCommand());
      }

      if (devEnvironment && event.getSide().isClient()) {
         event.registerServerCommand(new DebugCommand());
         event.registerServerCommand(new GetBiomeData());
         event.registerServerCommand(new PixelTPCommand());
      }

      if (PixelmonSpawning.coordinator == null) {
         PixelmonSpawning.startTrackingSpawner();
      }

      PixelSounds.linkPixelmonSounds();
   }

   @EventHandler
   public void onServerStopping(FMLServerStoppingEvent event) {
      storageManager.getSaveScheduler().onServerStopping(event);
   }

   public static String getVersion() {
      return "8.4.3";
   }

   static {
      devEnvironment = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
   }
}
