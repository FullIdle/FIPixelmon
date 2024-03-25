package com.pixelmonmod.tcg;

import com.pixelmonmod.tcg.api.loader.TCGLoader;
import com.pixelmonmod.tcg.api.loader.json.JSONLoader;
import com.pixelmonmod.tcg.api.registries.AbilityCardRegistry;
import com.pixelmonmod.tcg.api.registries.AttackCardRegistry;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.api.registries.CardSetRegistry;
import com.pixelmonmod.tcg.api.registries.RequirementRegistry;
import com.pixelmonmod.tcg.api.registries.ThemeDeckRegistry;
import com.pixelmonmod.tcg.block.BlockBattleController;
import com.pixelmonmod.tcg.block.BlockPrinter;
import com.pixelmonmod.tcg.commands.TCGCommand;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.listener.ClientEventHandler;
import com.pixelmonmod.tcg.listener.RegistryEvents;
import com.pixelmonmod.tcg.listener.ServerEventHandler;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.proxy.TCGCommonProxy;
import java.io.File;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
   name = "Pixelmon TCG",
   version = "1.12.2-8.4.3-universal",
   acceptableRemoteVersions = "1.12.2-8.4.3-universal",
   modid = "tcg",
   dependencies = "after:pixelmon"
)
public class TCG {
   public static final String MODVERSION = "1.12.2-8.4.3-universal";
   public static final String MODID = "tcg";
   public static final String MODNAME = "Pixelmon TCG";
   public static final EventBus EVENT_BUS = new EventBus();
   @Instance("tcg")
   public static TCG instance;
   public static TCGLoader loader;
   public static CreativeTabs tabTCG;
   public static CreativeTabs tabCards;
   public static CreativeTabs tabDecks;
   public static CreativeTabs tabPacks;
   public static CreativeTabs tabTCGCosmetic;
   @ObjectHolder("tcg:card")
   public static Item itemCard;
   @ObjectHolder("tcg:pack")
   public static Item itemPack;
   @ObjectHolder("tcg:deck")
   public static Item itemDeck;
   @ObjectHolder("tcg:card_printer")
   public static Item itemPrinter;
   public static Block blockPrinter;
   @ObjectHolder("tcg:rulebook")
   public static Item itemRulebook;
   @ObjectHolder("tcg:battle_rule")
   public static Item itemBattleRule;
   @ObjectHolder("tcg:small_battle_controller")
   public static Item itemSmallBattleController;
   public static Block blockSmallBattleController;
   @ObjectHolder("tcg:medium_battle_controller")
   public static Item itemMediumBattleController;
   public static Block blockMediumBattleController;
   @ObjectHolder("tcg:large_battle_controller")
   public static Item itemLargeBattleController;
   public static Block blockLargeBattleController;
   @ObjectHolder("tcg:binder")
   public static Item itemBinder;
   @ObjectHolder("tcg:card_back")
   public static Item itemCardBack;
   @ObjectHolder("tcg:coin")
   public static Item itemCoin;
   @ObjectHolder("tcg:compendium")
   public static Item itemCompendium;
   @ObjectHolder("tcg:eye")
   public static Item itemEye;
   @ObjectHolder("tcg:pendant")
   public static Item itemPendant;
   @ObjectHolder("tcg:scales")
   public static Item itemScales;
   @ObjectHolder("tcg:necklace")
   public static Item itemNecklace;
   @ObjectHolder("tcg:key")
   public static Item itemKey;
   @ObjectHolder("tcg:rod")
   public static Item itemRod;
   @ObjectHolder("tcg:ring")
   public static Item itemRing;
   @ObjectHolder("tcg:shadowwand")
   public static Item itemShadowWand;
   public static ClientEventHandler clientEventHandler;
   public static ServerEventHandler serverEventHandler;
   public static RegistryEvents registryEvents;
   public TCGConfig config = TCGConfig.getInstance();
   public static Logger logger = LogManager.getLogger("Pixelmon TCG");
   public static File rootDirectory;
   @SidedProxy(
      clientSide = "com.pixelmonmod.tcg.proxy.TCGClientProxy",
      serverSide = "com.pixelmonmod.tcg.proxy.TCGCommonProxy"
   )
   public static TCGCommonProxy proxy;

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) throws Exception {
      rootDirectory = new File(event.getModConfigurationDirectory().getParent());
      RequirementRegistry.init();
      PacketHandler.initPackets();
      this.config.loadConfig(event);
      this.addMetadata(event);
      loader = new JSONLoader();
      itemPrinter = ((Item)(new ItemBlock(new BlockPrinter())).setRegistryName("tcg:card_printer")).func_77637_a(tabTCG);
      itemSmallBattleController = ((Item)(new ItemBlock(new BlockBattleController("small_battle_controller", 1.0F))).setRegistryName("tcg:small_battle_controller")).func_77637_a(tabTCG);
      itemMediumBattleController = ((Item)(new ItemBlock(new BlockBattleController("medium_battle_controller", 2.0F))).setRegistryName("tcg:medium_battle_controller")).func_77637_a(tabTCG);
      itemLargeBattleController = ((Item)(new ItemBlock(new BlockBattleController("large_battle_controller", 4.0F))).setRegistryName("tcg:large_battle_controller")).func_77637_a(tabTCG);
      registryEvents = new RegistryEvents();
      MinecraftForge.EVENT_BUS.register(registryEvents);
      tabTCG = new CreativeTabs("tabTCG1") {
         @SideOnly(Side.CLIENT)
         public ItemStack func_78016_d() {
            return TCG.itemCard.func_190903_i();
         }
      };
      tabCards = new CreativeTabs("tabTCG2") {
         @SideOnly(Side.CLIENT)
         public ItemStack func_78016_d() {
            return TCG.itemCompendium.func_190903_i();
         }
      };
      tabDecks = new CreativeTabs("tabTCG3") {
         @SideOnly(Side.CLIENT)
         public ItemStack func_78016_d() {
            return TCG.itemDeck.func_190903_i();
         }
      };
      tabPacks = new CreativeTabs("tabTCG4") {
         @SideOnly(Side.CLIENT)
         public ItemStack func_78016_d() {
            return TCG.itemPack.func_190903_i();
         }
      };
      tabTCGCosmetic = new CreativeTabs("tabTCG5") {
         @SideOnly(Side.CLIENT)
         public ItemStack func_78016_d() {
            return TCG.itemCardBack.func_190903_i();
         }
      };
      proxy.preInit(event);
   }

   @EventHandler
   public void init(FMLInitializationEvent event) throws Exception {
      logger.info("Initializing Pixelmon TCG v1.12.2-8.4.3-universal");
      AttackCardRegistry.load();
      AbilityCardRegistry.load();
      CardRegistry.load();
      ThemeDeckRegistry.load();
      CardSetRegistry.load();
      FMLCommonHandler.instance().reloadSearchTrees();
      proxy.init(event);
      NetworkRegistry.INSTANCE.registerGuiHandler(this, new TCGCommonProxy());
   }

   @EventHandler
   public void serverLoad(FMLServerStartingEvent event) {
      event.registerServerCommand(new TCGCommand());
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      proxy.postInit(event);
   }

   private void addMetadata(FMLPreInitializationEvent event) {
      ModMetadata m = event.getModMetadata();
      m.autogenerated = false;
      m.modId = "tcg";
      m.version = "1.12.2-8.4.3-universal";
      m.name = "Pixelmon TCG";
      m.url = "www.pixelmonmod.com";
      m.description = "Trade cards and do battle!";
      m.credits = "Waterdude, Hy, Isi, SpreadSheetGuy, Ribchop, Envyful, and the Pixelmon Team";
   }
}
