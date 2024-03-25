package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.blocks.BlockScroll;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.EnumBike;
import com.pixelmonmod.pixelmon.enums.EnumCurryKey;
import com.pixelmonmod.pixelmon.enums.EnumDecreaseEV;
import com.pixelmonmod.pixelmon.enums.EnumEvolutionStone;
import com.pixelmonmod.pixelmon.enums.EnumIncreaseEV;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumShrine;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleItems;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumBottleCap;
import com.pixelmonmod.pixelmon.enums.items.EnumOrbShard;
import com.pixelmonmod.pixelmon.enums.items.EnumRodType;
import com.pixelmonmod.pixelmon.items.DecreaseEV;
import com.pixelmonmod.pixelmon.items.EnumCheatItemType;
import com.pixelmonmod.pixelmon.items.GenericItem;
import com.pixelmonmod.pixelmon.items.HealFixed;
import com.pixelmonmod.pixelmon.items.HealFraction;
import com.pixelmonmod.pixelmon.items.IMedicine;
import com.pixelmonmod.pixelmon.items.IncenseBurner;
import com.pixelmonmod.pixelmon.items.IncreaseEV;
import com.pixelmonmod.pixelmon.items.ItemAbilityCapsule;
import com.pixelmonmod.pixelmon.items.ItemAbilityPatch;
import com.pixelmonmod.pixelmon.items.ItemAzureFlute;
import com.pixelmonmod.pixelmon.items.ItemBattleItem;
import com.pixelmonmod.pixelmon.items.ItemBike;
import com.pixelmonmod.pixelmon.items.ItemBottleCap;
import com.pixelmonmod.pixelmon.items.ItemCamera;
import com.pixelmonmod.pixelmon.items.ItemCurryDex;
import com.pixelmonmod.pixelmon.items.ItemCurryDish;
import com.pixelmonmod.pixelmon.items.ItemCurryKey;
import com.pixelmonmod.pixelmon.items.ItemDynamaxCandy;
import com.pixelmonmod.pixelmon.items.ItemElixir;
import com.pixelmonmod.pixelmon.items.ItemEther;
import com.pixelmonmod.pixelmon.items.ItemEvolutionStone;
import com.pixelmonmod.pixelmon.items.ItemExpAll;
import com.pixelmonmod.pixelmon.items.ItemExpCandy;
import com.pixelmonmod.pixelmon.items.ItemFishingLog;
import com.pixelmonmod.pixelmon.items.ItemFishingRod;
import com.pixelmonmod.pixelmon.items.ItemFlute;
import com.pixelmonmod.pixelmon.items.ItemGift;
import com.pixelmonmod.pixelmon.items.ItemGracidea;
import com.pixelmonmod.pixelmon.items.ItemHiddenDoor;
import com.pixelmonmod.pixelmon.items.ItemHirokusLens;
import com.pixelmonmod.pixelmon.items.ItemIsisHourglass;
import com.pixelmonmod.pixelmon.items.ItemItemFinder;
import com.pixelmonmod.pixelmon.items.ItemJuiceShoppe;
import com.pixelmonmod.pixelmon.items.ItemKeyItem;
import com.pixelmonmod.pixelmon.items.ItemMaxSoup;
import com.pixelmonmod.pixelmon.items.ItemMint;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import com.pixelmonmod.pixelmon.items.ItemPPUp;
import com.pixelmonmod.pixelmon.items.ItemPixelmonPainting;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import com.pixelmonmod.pixelmon.items.ItemPokemonEditor;
import com.pixelmonmod.pixelmon.items.ItemPotion;
import com.pixelmonmod.pixelmon.items.ItemQuestEditor;
import com.pixelmonmod.pixelmon.items.ItemRanchUpgrade;
import com.pixelmonmod.pixelmon.items.ItemRepel;
import com.pixelmonmod.pixelmon.items.ItemRevive;
import com.pixelmonmod.pixelmon.items.ItemRuby;
import com.pixelmonmod.pixelmon.items.ItemSacredAsh;
import com.pixelmonmod.pixelmon.items.ItemScroll;
import com.pixelmonmod.pixelmon.items.ItemShard;
import com.pixelmonmod.pixelmon.items.ItemShrineOrb;
import com.pixelmonmod.pixelmon.items.ItemSpawnDen;
import com.pixelmonmod.pixelmon.items.ItemSpawnGrotto;
import com.pixelmonmod.pixelmon.items.ItemStatueMaker;
import com.pixelmonmod.pixelmon.items.ItemStatusAilmentHealer;
import com.pixelmonmod.pixelmon.items.ItemUIElement;
import com.pixelmonmod.pixelmon.items.ItemWailmerPail;
import com.pixelmonmod.pixelmon.items.ItemZygardeCube;
import com.pixelmonmod.pixelmon.items.MedicinePotion;
import com.pixelmonmod.pixelmon.items.MedicineRevive;
import com.pixelmonmod.pixelmon.items.MedicineStatus;
import com.pixelmonmod.pixelmon.items.MilcerySweet;
import com.pixelmonmod.pixelmon.items.PixelmonItem;
import com.pixelmonmod.pixelmon.items.PixelmonItemBlock;
import com.pixelmonmod.pixelmon.items.PorygonPiece;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import com.pixelmonmod.pixelmon.items.heldItems.ItemOriginForm;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;

public class PixelmonItems {
   /** @deprecated */
   @Deprecated
   public static Map allItemMap = null;
   /** @deprecated */
   @Deprecated
   public static ArrayList allItemList = null;
   public static Item itemFinder;
   public static Item rareCandy;
   public static Item xsCandy;
   public static Item sCandy;
   public static Item mCandy;
   public static Item lCandy;
   public static Item xlCandy;
   public static Item potion;
   public static Item superPotion;
   public static Item hyperPotion;
   public static Item maxPotion;
   public static Item freshWater;
   public static Item sodaPop;
   public static Item lemonade;
   public static Item moomooMilk;
   public static Item ether;
   public static Item maxEther;
   public static Item elixir;
   public static Item maxElixir;
   public static Item ppUp;
   public static Item ppMax;
   public static Item fullRestore;
   public static Item revive;
   public static Item maxRevive;
   public static Item antidote;
   public static Item parlyzHeal;
   public static Item awakening;
   public static Item burnHeal;
   public static Item iceHeal;
   public static Item fullHeal;
   public static Item rageCandyBar;
   public static Item lavaCookie;
   public static Item oldGateau;
   public static Item casteliacone;
   public static Item lumioseGalette;
   public static Item shalourSable;
   public static Item bigMalasada;
   public static Item HpUp;
   public static Item Protein;
   public static Item Iron;
   public static Item Calcium;
   public static Item Zinc;
   public static Item Carbos;
   public static Item healthWing;
   public static Item muscleWing;
   public static Item resistWing;
   public static Item geniusWing;
   public static Item cleverWing;
   public static Item swiftWing;
   public static Item silverWing;
   public static Item rainbowWing;
   public static Item healthFeather;
   public static Item muscleFeather;
   public static Item resistFeather;
   public static Item geniusFeather;
   public static Item cleverFeather;
   public static Item swiftFeather;
   public static Item pomegBerry;
   public static Item kelpsyBerry;
   public static Item qualotBerry;
   public static Item hondewBerry;
   public static Item grepaBerry;
   public static Item tamatoBerry;
   public static Item razzBerry;
   public static Item blukBerry;
   public static Item nanabBerry;
   public static Item wepearBerry;
   public static Item pinapBerry;
   public static Item cornnBerry;
   public static Item magostBerry;
   public static Item rabutaBerry;
   public static Item nomelBerry;
   public static Item spelonBerry;
   public static Item pamtreBerry;
   public static Item watmelBerry;
   public static Item durinBerry;
   public static Item belueBerry;
   public static Item healPowder;
   public static Item energyPowder;
   public static Item energyRoot;
   public static Item revivalHerb;
   public static Item xAttack;
   public static Item xDefence;
   public static Item xSpecialAttack;
   public static Item xSpecialDefence;
   public static Item xSpeed;
   public static Item xAccuracy;
   public static Item direHit;
   public static Item guardSpec;
   public static Item maxMushroom;
   public static Item fluffyTail;
   public static Item fireStone;
   public static Item waterStone;
   public static Item moonStone;
   public static Item thunderStone;
   public static Item leafStone;
   public static Item sunStone;
   public static Item dawnStone;
   public static Item duskStone;
   public static Item shinyStone;
   public static Item iceStone;
   public static Item thunderStoneShard;
   public static Item leafStoneShard;
   public static Item waterStoneShard;
   public static Item fireStoneShard;
   public static Item sunStoneShard;
   public static Item moonStoneShard;
   public static Item dawnStoneShard;
   public static Item duskStoneShard;
   public static Item shinyStoneShard;
   public static Item iceStoneShard;
   public static Item wailmerPail;
   public static Item oldRod;
   public static Item goodRod;
   public static Item superRod;
   public static Item oasFishingRod;
   public static Item fishingLog;
   public static Item curryDex;
   public static Item curryBread;
   public static Item curryPasta;
   public static Item curryFriedFood;
   public static Item curryInstantNoodles;
   public static Item curryPrecookedBurger;
   public static Item currySausages;
   public static Item curryBeanTin;
   public static Item curryMixedMushrooms;
   public static Item curryPotatoPack;
   public static Item currySaladMix;
   public static Item currySpiceMix;
   public static Item curryBobsTin;
   public static Item curryBachsTin;
   public static Item curryBrittleBones;
   public static Item curryPungentRoot;
   public static Item curryFreshCream;
   public static Item curryPackagedCurry;
   public static Item curryCoconutMilk;
   public static Item currySmokePokeTail;
   public static Item curryLargeLeek;
   public static Item curryFancyApple;
   public static Item curryBoiledEgg;
   public static Item curryFruitBunch;
   public static Item curryMoomooCheese;
   public static Item curryGigantamix;
   public static Item dishCurry;
   public static Item dishSausageCurry;
   public static Item dishJuicyCurry;
   public static Item dishRichCurry;
   public static Item dishBeanMedleyCurry;
   public static Item dishToastCurry;
   public static Item dishPastaCurry;
   public static Item dishMushroomMedleyCurry;
   public static Item dishSmokeTailCurry;
   public static Item dishLeekCurry;
   public static Item dishAppleCurry;
   public static Item dishBoneCurry;
   public static Item dishPotatoCurry;
   public static Item dishHerbMedleyCurry;
   public static Item dishSaladCurry;
   public static Item dishFriedFoodCurry;
   public static Item dishBoiledEggCurry;
   public static Item dishTropicalCurry;
   public static Item dishCheeseCoveredCurry;
   public static Item dishSeasonedCurry;
   public static Item dishWhippedCreamCurry;
   public static Item dishDecorativeCurry;
   public static Item dishCoconutCurry;
   public static Item dishInstantNoodleCurry;
   public static Item dishBurgerSteakCurry;
   public static Item dishGigantamixCurry;
   public static Item chisel;
   public static Item ranchUpgrade;
   public static Item tradeMonitor;
   public static Item tradeHolderRight;
   public static Item LtradeHolderLeft;
   public static Item tradePanel;
   public static Item orb;
   public static Item unoOrb;
   public static Item dosOrb;
   public static Item tresOrb;
   public static Item trainerEditor;
   public static Item pokemonEditor;
   public static Item grottoSpawner;
   public static Item denSpawner;
   public static Item cloningMachineGreenItem;
   public static Item cloningMachineOrangeItem;
   public static Item cloningMachineCordItem;
   public static Item hourglassGold;
   public static Item hourglassSilver;
   public static Item hirokusLensGold;
   public static Item hirokusLensSilver;
   public static Item hiddenIronDoorItem;
   public static Item hiddenWoodenDoorItem;
   public static Item templeSlab;
   public static Item templeBrickSlab;
   public static Item gift;
   public static Item itemPixelmonSprite;
   public static Item pixelmonPaintingItem;
   public static Item cameraItem;
   public static Item filmItem;
   public static ArrayList potionElixirList = new ArrayList();
   public static ArrayList evostoneList = new ArrayList();
   public static Item treeItem;
   public static Item amethyst;
   public static Item ruby;
   public static Item crystal;
   public static Item sapphire;
   public static Item siliconItem;
   public static Item aluminiumIngot;
   public static Item aluminiumPlate;
   public static Item abilityCapsule;
   public static Item abilityPatch;
   public static Item expAll;
   public static Item gracidea;
   public static Item reveal_glass;
   public static Item redchain;
   public static Item repel;
   public static Item superRepel;
   public static Item maxRepel;
   public static Item porygonPieces;
   public static Item redShard;
   public static Item blueShard;
   public static Item jadeShard;
   public static Item azureFlute;
   public static Item sacredAsh;
   public static Item goldBottleCap;
   public static Item silverBottleCap;
   public static Item colress_machine;
   public static Item machBike;
   public static Item machBikeFrame;
   public static Item acroBike;
   public static Item acroBikeFrame;
   public static Item stuntBike;
   public static Item stuntBikeFrame;
   public static Item handles;
   public static Item wheel;
   public static Item incenseBurner;
   public static Item zygardeCube;
   public static Item gaeBolg;
   public static Item questEditor;
   public static Item tartApple;
   public static Item sweetApple;
   public static Item mintSeeds;
   public static Item mint;
   public static Item chippedPot;
   public static Item crackedPot;
   public static Item sweetBerry;
   public static Item sweetClover;
   public static Item sweetFlower;
   public static Item sweetHeart;
   public static Item sweetRibbon;
   public static Item sweetStar;
   public static Item sweetStrawberry;
   public static Item dynamaxCandy;
   public static Item maxSoup;
   public static Item galaricaTwig;
   public static Item galaricaWreath;
   public static Item eyeOfLugia;
   public static Item scrollOfWaters;
   public static Item scrollOfDarkness;
   public static Item keyStone;
   public static Item wishingStar;
   public static Item ovalCharm;
   public static Item shinyCharm;
   public static Item expCharm;
   public static Item catchingCharm;
   public static Item markCharm;
   public static Item uiElement;
   public static Item armoriteOre;
   public static Item dyniteOre;
   public static Item wishingPiece;
   public static Item waterdudeWishingPiece;
   public static Item redFlute;
   public static Item blueFlute;
   public static Item greenFlute;
   public static Item yellowFlute;
   public static Item whiteFlute;
   public static Item blackFlute;
   public static Item komalaCoffee;
   public static Item tapuCocoa;
   public static Item pinapJuice;
   public static Item roseradeTea;
   public static Item skittySmoothie;
   public static Item rareSoda;
   public static Item ultraRareSoda;
   public static Item colorfulShake;
   public static Item blueJuice;
   public static Item redJuice;
   public static Item pinkJuice;
   public static Item purpleJuice;
   public static Item yellowJuice;
   public static Item greenJuice;
   public static Item perilousSoup;
   public static Item sweetHeart_heal;
   public static Item blackAugurite;
   public static Item peatBlock;
   public static Item adamantCrystal;
   public static Item lustrousGlobe;
   public static Item griseousCore;

   public static void load() {
      adamantCrystal = new ItemOriginForm(EnumHeldItems.adamantCrystal, "adamant_crystal", EnumSpecies.Dialga);
      lustrousGlobe = new ItemOriginForm(EnumHeldItems.lustrousGlobe, "lustrous_globe", EnumSpecies.Palkia);
      griseousCore = new ItemOriginForm(EnumHeldItems.griseousCore, "griseous_core", EnumSpecies.Giratina);
      sweetHeart_heal = new ItemPotion("sweet_heart", new IMedicine[]{new MedicinePotion(new HealFixed(20))});
      komalaCoffee = new ItemPotion("komala_coffee", new IMedicine[]{new MedicinePotion(new HealFixed(100))});
      tapuCocoa = new ItemPotion("tapu_cocoa", new IMedicine[]{new MedicinePotion(new HealFixed(150))});
      pinapJuice = new ItemPotion("pinap_juice", new IMedicine[]{new MedicinePotion(new HealFixed(100))});
      roseradeTea = new ItemPotion("roserade_tea", new IMedicine[]{new MedicinePotion(new HealFixed(100))});
      skittySmoothie = new ItemPotion("skitty_smoothie", new IMedicine[]{new MedicinePotion(new HealFixed(150))});
      rareSoda = new ItemExpCandy("rare_soda", ExperienceGainType.RARE_SODA, -2);
      ultraRareSoda = new ItemExpCandy("ultra_rare_soda", ExperienceGainType.ULTRA_RARE_SODA, -4);
      blueJuice = new ItemJuiceShoppe(StatsType.SpecialAttack, "blue_juice");
      redJuice = new ItemJuiceShoppe(StatsType.Attack, "red_juice");
      pinkJuice = new ItemJuiceShoppe(StatsType.Speed, "pink_juice");
      purpleJuice = new ItemJuiceShoppe(StatsType.HP, "purple_juice");
      yellowJuice = new ItemJuiceShoppe(StatsType.Defence, "yellow_juice");
      greenJuice = new ItemJuiceShoppe(StatsType.SpecialDefence, "green_juice");
      colorfulShake = new PixelmonItem("colorful_shake");
      perilousSoup = new PixelmonItem("perilous_soup");
      aluminiumIngot = (new PixelmonItem("aluminium_ingot")).func_77637_a(CreativeTabs.field_78035_l);
      aluminiumPlate = (new PixelmonItem("aluminium_plate")).func_77637_a(CreativeTabs.field_78035_l);
      rareCandy = new ItemExpCandy("rare_candy", ExperienceGainType.RARE_CANDY, -1);
      xsCandy = new ItemExpCandy("xs_exp_candy", ExperienceGainType.EXTRA_SMALL_EXP_CANDY, 100);
      sCandy = new ItemExpCandy("s_exp_candy", ExperienceGainType.SMALL_EXP_CANDY, 800);
      mCandy = new ItemExpCandy("m_exp_candy", ExperienceGainType.MEDIUM_EXP_CANDY, 3000);
      lCandy = new ItemExpCandy("l_exp_candy", ExperienceGainType.LARGE_EXP_CANDY, 10000);
      xlCandy = new ItemExpCandy("xl_exp_candy", ExperienceGainType.EXTRA_LARGE_EXP_CANDY, 30000);
      potion = new ItemPotion("potion", new IMedicine[]{new MedicinePotion(new HealFixed(20))});
      superPotion = new ItemPotion("super_potion", new IMedicine[]{new MedicinePotion(new HealFixed(60))});
      hyperPotion = new ItemPotion("hyper_potion", new IMedicine[]{new MedicinePotion(new HealFixed(120))});
      maxPotion = new ItemPotion("max_potion", new IMedicine[]{new MedicinePotion(new HealFraction(1.0F))});
      freshWater = new ItemPotion("fresh_water", new IMedicine[]{new MedicinePotion(new HealFixed(30))});
      sodaPop = new ItemPotion("soda_pop", new IMedicine[]{new MedicinePotion(new HealFixed(50))});
      lemonade = new ItemPotion("lemonade", new IMedicine[]{new MedicinePotion(new HealFixed(70))});
      moomooMilk = new ItemPotion("moomoo_milk", new IMedicine[]{new MedicinePotion(new HealFixed(100))});
      curryBread = new ItemCurryKey(EnumCurryKey.BREAD);
      curryPasta = new ItemCurryKey(EnumCurryKey.PASTA);
      curryFriedFood = new ItemCurryKey(EnumCurryKey.FRIED_FOOD);
      curryInstantNoodles = new ItemCurryKey(EnumCurryKey.INSTANT_NOODLES);
      curryPrecookedBurger = new ItemCurryKey(EnumCurryKey.PRECOOKED_BURGER);
      currySausages = new ItemCurryKey(EnumCurryKey.SAUSAGES);
      curryBeanTin = new ItemCurryKey(EnumCurryKey.BEAN_TIN);
      curryMixedMushrooms = new ItemCurryKey(EnumCurryKey.MIXED_MUSHROOMS);
      curryPotatoPack = new ItemCurryKey(EnumCurryKey.POTATO_PACK);
      currySaladMix = new ItemCurryKey(EnumCurryKey.SALAD_MIX);
      currySpiceMix = new ItemCurryKey(EnumCurryKey.SPICE_MIX);
      curryBobsTin = new ItemCurryKey(EnumCurryKey.BOBS_TIN);
      curryBachsTin = new ItemCurryKey(EnumCurryKey.BACHS_TIN);
      curryBrittleBones = new ItemCurryKey(EnumCurryKey.BRITTLE_BONES);
      curryPungentRoot = new ItemCurryKey(EnumCurryKey.PUNGENT_ROOT);
      curryFreshCream = new ItemCurryKey(EnumCurryKey.FRESH_CREAM);
      curryPackagedCurry = new ItemCurryKey(EnumCurryKey.PACKAGED_CURRY);
      curryCoconutMilk = new ItemCurryKey(EnumCurryKey.COCONUT_MILK);
      currySmokePokeTail = new ItemCurryKey(EnumCurryKey.SMOKE_POKE_TAIL);
      curryLargeLeek = new ItemCurryKey(EnumCurryKey.LARGE_LEEK);
      curryFancyApple = new ItemCurryKey(EnumCurryKey.FANCY_APPLE);
      curryBoiledEgg = new ItemCurryKey(EnumCurryKey.BOILED_EGG);
      curryFruitBunch = new ItemCurryKey(EnumCurryKey.FRUIT_BUNCH);
      curryMoomooCheese = new ItemCurryKey(EnumCurryKey.MOOMOO_CHEESE);
      curryGigantamix = new ItemCurryKey(EnumCurryKey.GIGANTAMIX);
      dishCurry = new ItemCurryDish(EnumCurryKey.NONE);
      dishSausageCurry = new ItemCurryDish(EnumCurryKey.SAUSAGES);
      dishJuicyCurry = new ItemCurryDish(EnumCurryKey.BOBS_TIN);
      dishRichCurry = new ItemCurryDish(EnumCurryKey.BACHS_TIN);
      dishBeanMedleyCurry = new ItemCurryDish(EnumCurryKey.BEAN_TIN);
      dishToastCurry = new ItemCurryDish(EnumCurryKey.BREAD);
      dishPastaCurry = new ItemCurryDish(EnumCurryKey.PASTA);
      dishMushroomMedleyCurry = new ItemCurryDish(EnumCurryKey.MIXED_MUSHROOMS);
      dishSmokeTailCurry = new ItemCurryDish(EnumCurryKey.SMOKE_POKE_TAIL);
      dishLeekCurry = new ItemCurryDish(EnumCurryKey.LARGE_LEEK);
      dishAppleCurry = new ItemCurryDish(EnumCurryKey.FANCY_APPLE);
      dishBoneCurry = new ItemCurryDish(EnumCurryKey.BRITTLE_BONES);
      dishPotatoCurry = new ItemCurryDish(EnumCurryKey.POTATO_PACK);
      dishHerbMedleyCurry = new ItemCurryDish(EnumCurryKey.PUNGENT_ROOT);
      dishSaladCurry = new ItemCurryDish(EnumCurryKey.SALAD_MIX);
      dishFriedFoodCurry = new ItemCurryDish(EnumCurryKey.FRIED_FOOD);
      dishBoiledEggCurry = new ItemCurryDish(EnumCurryKey.BOILED_EGG);
      dishTropicalCurry = new ItemCurryDish(EnumCurryKey.FRUIT_BUNCH);
      dishCheeseCoveredCurry = new ItemCurryDish(EnumCurryKey.MOOMOO_CHEESE);
      dishSeasonedCurry = new ItemCurryDish(EnumCurryKey.SPICE_MIX);
      dishWhippedCreamCurry = new ItemCurryDish(EnumCurryKey.FRESH_CREAM);
      dishDecorativeCurry = new ItemCurryDish(EnumCurryKey.PACKAGED_CURRY);
      dishCoconutCurry = new ItemCurryDish(EnumCurryKey.COCONUT_MILK);
      dishInstantNoodleCurry = new ItemCurryDish(EnumCurryKey.INSTANT_NOODLES);
      dishBurgerSteakCurry = new ItemCurryDish(EnumCurryKey.PRECOOKED_BURGER);
      dishGigantamixCurry = new ItemCurryDish(EnumCurryKey.GIGANTAMIX);
      revive = new ItemRevive("revive", new IMedicine[]{new MedicineRevive(new HealFraction(0.5F))});
      maxRevive = new ItemRevive("max_revive", new IMedicine[]{new MedicineRevive(new HealFraction(1.0F))});
      ether = new ItemEther("ether", false);
      maxEther = new ItemEther("max_ether", true);
      elixir = new ItemElixir("elixir", false);
      maxElixir = new ItemElixir("max_elixir", true);
      goldBottleCap = new ItemBottleCap(EnumBottleCap.GOLD);
      silverBottleCap = new ItemBottleCap(EnumBottleCap.SILVER);
      ppUp = new ItemPPUp("pp_up", false);
      ppMax = new ItemPPUp("pp_max", true);
      MedicineStatus allStatuses = new MedicineStatus(new StatusType[]{StatusType.Burn, StatusType.Confusion, StatusType.Freeze, StatusType.Paralysis, StatusType.Poison, StatusType.PoisonBadly, StatusType.Sleep, StatusType.Infatuated});
      antidote = new ItemStatusAilmentHealer("antidote", new IMedicine[]{new MedicineStatus(new StatusType[]{StatusType.Poison, StatusType.PoisonBadly})});
      parlyzHeal = new ItemStatusAilmentHealer("paralyze_heal", new IMedicine[]{new MedicineStatus(new StatusType[]{StatusType.Paralysis})});
      awakening = new ItemStatusAilmentHealer("awakening", new IMedicine[]{new MedicineStatus(new StatusType[]{StatusType.Sleep})});
      burnHeal = new ItemStatusAilmentHealer("burn_heal", new IMedicine[]{new MedicineStatus(new StatusType[]{StatusType.Burn})});
      iceHeal = new ItemStatusAilmentHealer("ice_heal", new IMedicine[]{new MedicineStatus(new StatusType[]{StatusType.Freeze})});
      fullHeal = new ItemStatusAilmentHealer("full_heal", new IMedicine[]{allStatuses});
      fullRestore = new ItemPotion("full_restore", new IMedicine[]{new MedicinePotion(new HealFraction(1.0F)), allStatuses});
      rageCandyBar = new ItemPotion("rage_candy_bar", new IMedicine[]{allStatuses});
      lavaCookie = new ItemStatusAilmentHealer("lava_cookie", new IMedicine[]{allStatuses});
      oldGateau = new ItemStatusAilmentHealer("old_gateau", new IMedicine[]{allStatuses});
      casteliacone = new ItemStatusAilmentHealer("casteliacone", new IMedicine[]{allStatuses});
      lumioseGalette = new ItemStatusAilmentHealer("lumiose_galette", new IMedicine[]{allStatuses});
      shalourSable = new ItemStatusAilmentHealer("shalour_sable", new IMedicine[]{allStatuses});
      bigMalasada = new ItemStatusAilmentHealer("big_malasada", new IMedicine[]{allStatuses});
      HpUp = new IncreaseEV(EnumIncreaseEV.HpUp, "hp_up");
      Protein = new IncreaseEV(EnumIncreaseEV.Protein, "protein");
      Iron = new IncreaseEV(EnumIncreaseEV.Iron, "iron");
      Calcium = new IncreaseEV(EnumIncreaseEV.Calcium, "calcium");
      Zinc = new IncreaseEV(EnumIncreaseEV.Zinc, "zinc");
      Carbos = new IncreaseEV(EnumIncreaseEV.Carbos, "carbos");
      healthWing = new PixelmonItem("health_wing");
      muscleWing = new PixelmonItem("muscle_wing");
      resistWing = new PixelmonItem("resist_wing");
      geniusWing = new PixelmonItem("genius_wing");
      cleverWing = new PixelmonItem("clever_wing");
      swiftWing = new PixelmonItem("swift_wing");
      silverWing = new PixelmonItem("silver_wing");
      rainbowWing = new PixelmonItem("rainbow_wing");
      healthFeather = new IncreaseEV(EnumIncreaseEV.HealthFeather, "health_feather");
      muscleFeather = new IncreaseEV(EnumIncreaseEV.MuscleFeather, "muscle_feather");
      resistFeather = new IncreaseEV(EnumIncreaseEV.ResistFeather, "resist_feather");
      geniusFeather = new IncreaseEV(EnumIncreaseEV.GeniusFeather, "genius_feather");
      cleverFeather = new IncreaseEV(EnumIncreaseEV.CleverFeather, "clever_feather");
      swiftFeather = new IncreaseEV(EnumIncreaseEV.SwiftFeather, "swift_feather");
      xAttack = new ItemBattleItem(EnumBattleItems.xAttack, "x_attack");
      xDefence = new ItemBattleItem(EnumBattleItems.xDefence, "x_defence");
      xSpecialAttack = new ItemBattleItem(EnumBattleItems.xSpecialAttack, "x_special_attack");
      xSpecialDefence = new ItemBattleItem(EnumBattleItems.xSpecialDefence, "x_special_defence");
      xSpeed = new ItemBattleItem(EnumBattleItems.xSpeed, "x_speed");
      xAccuracy = new ItemBattleItem(EnumBattleItems.xAccuracy, "x_accuracy");
      direHit = new ItemBattleItem(EnumBattleItems.direHit, "dire_hit");
      guardSpec = new ItemBattleItem(EnumBattleItems.guardSpec, "guard_spec");
      maxMushroom = new ItemBattleItem(EnumBattleItems.maxMushroom, "max_mushroom");
      fluffyTail = new ItemBattleItem(EnumBattleItems.fluffyTail, "fluffy_tail");
      redFlute = new ItemBattleItem(EnumBattleItems.redFlute, "red_flute");
      greenFlute = new ItemBattleItem(EnumBattleItems.greenFlute, "green_flute");
      yellowFlute = new ItemBattleItem(EnumBattleItems.yellowFlute, "yellow_flute");
      blueFlute = new ItemBattleItem(EnumBattleItems.blueFlute, "blue_flute");
      blackFlute = new ItemFlute("black_flute", 0);
      whiteFlute = new ItemFlute("white_flute", 1);
      pomegBerry = (new DecreaseEV(EnumDecreaseEV.PomegBerry, EnumBerry.Pomeg, "pomeg_berry")).setFood(5, 0.5F).setPotionEffect(new PotionEffect(MobEffects.field_82731_v, 80, 0), 1.0F);
      kelpsyBerry = (new DecreaseEV(EnumDecreaseEV.KelpsyBerry, EnumBerry.Kelpsy, "kelpsy_berry")).setFood(5, 0.5F).setPotionEffect(new PotionEffect(MobEffects.field_82731_v, 80, 0), 1.0F);
      qualotBerry = (new DecreaseEV(EnumDecreaseEV.QualotBerry, EnumBerry.Qualot, "qualot_berry")).setFood(5, 0.5F).setPotionEffect(new PotionEffect(MobEffects.field_82731_v, 80, 0), 1.0F);
      hondewBerry = (new DecreaseEV(EnumDecreaseEV.HondewBerry, EnumBerry.Hondew, "hondew_berry")).setFood(5, 0.5F).setPotionEffect(new PotionEffect(MobEffects.field_82731_v, 80, 0), 1.0F);
      grepaBerry = (new DecreaseEV(EnumDecreaseEV.GrepaBerry, EnumBerry.Grepa, "grepa_berry")).setFood(5, 0.5F).setPotionEffect(new PotionEffect(MobEffects.field_82731_v, 80, 0), 1.0F);
      tamatoBerry = (new DecreaseEV(EnumDecreaseEV.TamatoBerry, EnumBerry.Tamato, "tamato_berry")).setFood(5, 0.5F).setPotionEffect(new PotionEffect(MobEffects.field_82731_v, 80, 0), 1.0F);
      cornnBerry = (new ItemBerry(EnumHeldItems.other, EnumBerry.Cornn, "cornn_berry")).setFood(2, 0.2F);
      magostBerry = (new ItemBerry(EnumHeldItems.other, EnumBerry.Magost, "magost_berry")).setFood(2, 0.2F);
      rabutaBerry = (new ItemBerry(EnumHeldItems.other, EnumBerry.Rabuta, "rabuta_berry")).setFood(2, 0.2F);
      nomelBerry = (new ItemBerry(EnumHeldItems.other, EnumBerry.Nomel, "nomel_berry")).setFood(2, 0.2F);
      spelonBerry = (new ItemBerry(EnumHeldItems.other, EnumBerry.Spelon, "spelon_berry")).setFood(2, 0.2F);
      pamtreBerry = (new ItemBerry(EnumHeldItems.other, EnumBerry.Pamtre, "pamtre_berry")).setFood(2, 0.2F);
      watmelBerry = (new ItemBerry(EnumHeldItems.other, EnumBerry.Watmel, "watmel_berry")).setFood(2, 0.2F);
      durinBerry = (new ItemBerry(EnumHeldItems.other, EnumBerry.Durin, "durin_berry")).setFood(2, 0.2F);
      belueBerry = (new ItemBerry(EnumHeldItems.other, EnumBerry.Belue, "belue_berry")).setFood(2, 0.2F);
      razzBerry = new ItemBerry(EnumHeldItems.other, EnumBerry.Razz, "razz_berry");
      nanabBerry = new ItemBerry(EnumHeldItems.other, EnumBerry.Nanab, "nanab_berry");
      pinapBerry = new ItemBerry(EnumHeldItems.other, EnumBerry.Pinap, "pinap_berry");
      healPowder = new ItemStatusAilmentHealer("heal_powder", new IMedicine[]{allStatuses});
      energyPowder = (new ItemPotion("energy_powder", new IMedicine[]{new MedicinePotion(new HealFixed(50))})).setFriendshipDecrease(5, 10);
      energyRoot = (new ItemPotion("energy_root", new IMedicine[]{new MedicinePotion(new HealFixed(200))})).setFriendshipDecrease(10, 15);
      revivalHerb = (new ItemRevive("revival_herb", new IMedicine[]{new MedicineRevive(new HealFraction(1.0F))})).setFriendshipDecrease(15, 20);
      fireStone = (new ItemEvolutionStone(EnumEvolutionStone.Firestone, "fire_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      waterStone = (new ItemEvolutionStone(EnumEvolutionStone.Waterstone, "water_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      moonStone = (new ItemEvolutionStone(EnumEvolutionStone.Moonstone, "moon_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      thunderStone = (new ItemEvolutionStone(EnumEvolutionStone.Thunderstone, "thunder_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      sunStone = (new ItemEvolutionStone(EnumEvolutionStone.Sunstone, "sun_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      leafStone = (new ItemEvolutionStone(EnumEvolutionStone.Leafstone, "leaf_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      dawnStone = (new ItemEvolutionStone(EnumEvolutionStone.Dawnstone, "dawn_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      duskStone = (new ItemEvolutionStone(EnumEvolutionStone.Duskstone, "dusk_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      shinyStone = (new ItemEvolutionStone(EnumEvolutionStone.Shinystone, "shiny_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      iceStone = (new ItemEvolutionStone(EnumEvolutionStone.Icestone, "ice_stone")).func_77637_a(PixelmonCreativeTabs.natural);
      sweetApple = (new PixelmonItem("sweet_apple")).func_77637_a(PixelmonCreativeTabs.natural);
      tartApple = (new PixelmonItem("tart_apple")).func_77637_a(PixelmonCreativeTabs.natural);
      redShard = new ItemShard(EnumOrbShard.RED);
      blueShard = new ItemShard(EnumOrbShard.BLUE);
      jadeShard = new ItemShard(EnumOrbShard.JADE);
      thunderStoneShard = (new PixelmonItem("thunder_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      leafStoneShard = (new PixelmonItem("leaf_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      waterStoneShard = (new PixelmonItem("water_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      fireStoneShard = (new PixelmonItem("fire_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      sunStoneShard = (new PixelmonItem("sun_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      moonStoneShard = (new PixelmonItem("moon_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      dawnStoneShard = (new PixelmonItem("dawn_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      duskStoneShard = (new PixelmonItem("dusk_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      shinyStoneShard = (new PixelmonItem("shiny_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      iceStoneShard = (new PixelmonItem("ice_stone_shard")).func_77637_a(PixelmonCreativeTabs.natural);
      wailmerPail = new ItemWailmerPail("wailmer_pail");
      oldRod = new ItemFishingRod(EnumRodType.OldRod, "old_rod");
      goodRod = new ItemFishingRod(EnumRodType.GoodRod, "good_rod");
      superRod = new ItemFishingRod(EnumRodType.SuperRod, "super_rod");
      oasFishingRod = new ItemFishingRod(EnumRodType.OasRod, "oas_rod");
      fishingLog = new ItemFishingLog("fishing_log");
      curryDex = new ItemCurryDex("curry_dex");
      tradeMonitor = new PixelmonItem("trade_monitor");
      tradeHolderRight = new PixelmonItem("trade_holder_right");
      LtradeHolderLeft = new PixelmonItem("trade_holder_left");
      tradePanel = new PixelmonItem("trade_panel");
      unoOrb = new ItemShrineOrb(EnumShrine.Articuno, "uno_orb");
      dosOrb = new ItemShrineOrb(EnumShrine.Zapdos, "dos_orb");
      tresOrb = new ItemShrineOrb(EnumShrine.Moltres, "tres_orb");
      orb = new PixelmonItem("orb");
      trainerEditor = new ItemNPCEditor();
      pokemonEditor = new ItemPokemonEditor();
      itemFinder = new ItemItemFinder();
      grottoSpawner = new ItemSpawnGrotto();
      denSpawner = new ItemSpawnDen();
      cloningMachineGreenItem = (new PixelmonItem("green_tank")).func_77637_a(CreativeTabs.field_78026_f);
      cloningMachineOrangeItem = (new PixelmonItem("orange_tank")).func_77637_a(CreativeTabs.field_78026_f);
      cloningMachineCordItem = (new PixelmonItem("cloner_cord")).func_77637_a(CreativeTabs.field_78026_f);
      hourglassGold = new ItemIsisHourglass(EnumCheatItemType.Gold);
      hourglassSilver = new ItemIsisHourglass(EnumCheatItemType.Silver);
      hirokusLensGold = new ItemHirokusLens(EnumCheatItemType.Gold);
      hirokusLensSilver = new ItemHirokusLens(EnumCheatItemType.Silver);
      hiddenIronDoorItem = new ItemHiddenDoor(PixelmonBlocks.hiddenIronDoor, "hidden_iron_door");
      hiddenWoodenDoorItem = new ItemHiddenDoor(PixelmonBlocks.hiddenWoodenDoor, "hidden_wooden_door");
      chisel = new ItemStatueMaker();
      gift = new ItemGift("gift_box");
      ranchUpgrade = new ItemRanchUpgrade();
      itemPixelmonSprite = new ItemPixelmonSprite();
      pixelmonPaintingItem = new ItemPixelmonPainting("painting");
      cameraItem = new ItemCamera();
      filmItem = (new GenericItem("film")).func_77637_a(CreativeTabs.field_78026_f);
      amethyst = (new GenericItem("amethyst")).func_77637_a(PixelmonCreativeTabs.natural);
      ruby = (new ItemRuby()).func_77637_a(PixelmonCreativeTabs.natural);
      crystal = (new GenericItem("crystal")).func_77637_a(PixelmonCreativeTabs.natural);
      sapphire = (new GenericItem("sapphire")).func_77637_a(PixelmonCreativeTabs.natural);
      siliconItem = (new GenericItem("silicon")).func_77637_a(PixelmonCreativeTabs.natural);
      treeItem = (new PixelmonItemBlock(PixelmonBlocks.treeBlock, "tree")).func_77637_a(PixelmonCreativeTabs.decoration);
      abilityCapsule = new ItemAbilityCapsule();
      abilityPatch = new ItemAbilityPatch();
      expAll = new ItemExpAll("exp_all");
      gracidea = new ItemGracidea();
      reveal_glass = new PixelmonItem("reveal_glass");
      repel = new ItemRepel(ItemRepel.EnumRepel.REPEL);
      superRepel = new ItemRepel(ItemRepel.EnumRepel.SUPER_REPEL);
      maxRepel = new ItemRepel(ItemRepel.EnumRepel.MAX_REPEL);
      redchain = (new PixelmonItem("red_chain")).setHasEffect(true).func_77625_d(1);
      porygonPieces = new PorygonPiece("porygon_piece");
      azureFlute = new ItemAzureFlute();
      sacredAsh = new ItemSacredAsh();
      machBike = new ItemBike(EnumBike.Mach);
      machBikeFrame = (new PixelmonItem("mach_bike_frame")).func_77637_a(CreativeTabs.field_78026_f);
      acroBike = new ItemBike(EnumBike.Acro);
      acroBikeFrame = (new PixelmonItem("acro_bike_frame")).func_77637_a(CreativeTabs.field_78026_f);
      handles = (new PixelmonItem("handles")).func_77637_a(CreativeTabs.field_78026_f);
      wheel = (new PixelmonItem("wheel")).func_77637_a(CreativeTabs.field_78026_f);
      incenseBurner = (new IncenseBurner("incense_burner")).func_77637_a(CreativeTabs.field_78026_f);
      zygardeCube = new ItemZygardeCube();
      gaeBolg = (new PixelmonItem("gaebolg")).func_77637_a(CreativeTabs.field_78040_i);
      questEditor = new ItemQuestEditor();
      mintSeeds = ((Item)(new ItemSeeds(PixelmonBlocks.mintCrop, Blocks.field_150458_ak)).func_77655_b("mint_seeds").setRegistryName("mint_seeds")).func_77637_a(CreativeTabs.field_78026_f);
      mint = new ItemMint();
      chippedPot = new PixelmonItem("chipped_pot");
      crackedPot = new PixelmonItem("cracked_pot");
      sweetBerry = new MilcerySweet(EnumHeldItems.sweetBerry, "berry_sweet");
      sweetClover = new MilcerySweet(EnumHeldItems.sweetClover, "clover_sweet");
      sweetFlower = new MilcerySweet(EnumHeldItems.sweetFlower, "flower_sweet");
      sweetHeart = new MilcerySweet(EnumHeldItems.sweetHeart, "love_sweet");
      sweetRibbon = new MilcerySweet(EnumHeldItems.sweetRibbon, "ribbon_sweet");
      sweetStar = new MilcerySweet(EnumHeldItems.sweetStar, "star_sweet");
      sweetStrawberry = new MilcerySweet(EnumHeldItems.sweetStrawberry, "strawberry_sweet");
      dynamaxCandy = new ItemDynamaxCandy();
      maxSoup = new ItemMaxSoup();
      galaricaTwig = (new PixelmonItem("galarica_twig")).func_77637_a(PixelmonCreativeTabs.natural);
      galaricaWreath = (new PixelmonItem("galarica_wreath")).func_77637_a(PixelmonCreativeTabs.natural);
      eyeOfLugia = (new PixelmonItem("eye_of_lugia")).func_77637_a(CreativeTabs.field_78026_f);
      scrollOfWaters = ((Item)(new ItemScroll(BlockScroll.Type.Waters)).setRegistryName("scroll_of_waters")).func_77655_b("scroll_of_waters").func_77637_a(CreativeTabs.field_78031_c);
      scrollOfDarkness = ((Item)(new ItemScroll(BlockScroll.Type.Darkness)).setRegistryName("scroll_of_darkness")).func_77655_b("scroll_of_darkness").func_77637_a(CreativeTabs.field_78031_c);
      keyStone = new ItemKeyItem(ItemKeyItem.KeyItem.KeyStone);
      wishingStar = new ItemKeyItem(ItemKeyItem.KeyItem.WishingStar);
      ovalCharm = new ItemKeyItem(ItemKeyItem.KeyItem.OvalCharm);
      shinyCharm = new ItemKeyItem(ItemKeyItem.KeyItem.ShinyCharm);
      expCharm = new ItemKeyItem(ItemKeyItem.KeyItem.ExpCharm);
      catchingCharm = new ItemKeyItem(ItemKeyItem.KeyItem.CatchingCharm);
      markCharm = new ItemKeyItem(ItemKeyItem.KeyItem.MarkCharm);
      uiElement = new ItemUIElement();
      armoriteOre = (new PixelmonItem("armorite_ore")).func_77637_a(CreativeTabs.field_78035_l);
      dyniteOre = (new PixelmonItem("dynite_ore")).func_77637_a(CreativeTabs.field_78035_l);
      wishingPiece = (new PixelmonItem("wishing_piece")).func_77637_a(CreativeTabs.field_78035_l);
      waterdudeWishingPiece = (new PixelmonItem("waterdude_wishing_piece")).setHasEffect(true).func_77637_a(CreativeTabs.field_78035_l);
      blackAugurite = new PixelmonItem("black_augurite");
      peatBlock = new PixelmonItem("peat_block");
   }

   public static ArrayList initializePotionElixirList() {
      ArrayList list = new ArrayList();
      list.add(potion);
      list.add(superPotion);
      list.add(hyperPotion);
      list.add(maxPotion);
      list.add(freshWater);
      list.add(sodaPop);
      list.add(lemonade);
      list.add(moomooMilk);
      list.add(ether);
      list.add(maxEther);
      list.add(elixir);
      list.add(maxElixir);
      list.add(fullRestore);
      list.add(revive);
      list.add(maxRevive);
      list.add(antidote);
      list.add(parlyzHeal);
      list.add(awakening);
      list.add(burnHeal);
      list.add(iceHeal);
      list.add(fullHeal);
      list.add(rageCandyBar);
      list.add(lavaCookie);
      list.add(oldGateau);
      list.add(casteliacone);
      list.add(lumioseGalette);
      list.add(shalourSable);
      list.add(bigMalasada);
      list.add(healPowder);
      list.add(energyPowder);
      list.add(energyRoot);
      list.add(revivalHerb);
      list.add(xAttack);
      list.add(xDefence);
      list.add(xSpecialAttack);
      list.add(xSpecialDefence);
      list.add(xSpeed);
      list.add(xAccuracy);
      list.add(direHit);
      list.add(guardSpec);
      list.add(maxMushroom);
      list.add(HpUp);
      list.add(Protein);
      list.add(Iron);
      list.add(Calcium);
      list.add(Zinc);
      list.add(Carbos);
      list.add(pomegBerry);
      list.add(kelpsyBerry);
      list.add(qualotBerry);
      list.add(hondewBerry);
      list.add(grepaBerry);
      list.add(tamatoBerry);
      list.add(sacredAsh);
      return list;
   }

   public static ArrayList getPotionElixirList() {
      if (potionElixirList.isEmpty()) {
         potionElixirList = initializePotionElixirList();
      }

      return potionElixirList;
   }

   public static ArrayList initializeEvostoneList() {
      ArrayList list = new ArrayList();
      list.add(fireStone);
      list.add(waterStone);
      list.add(moonStone);
      list.add(thunderStone);
      list.add(leafStone);
      list.add(sunStone);
      list.add(duskStone);
      list.add(dawnStone);
      list.add(shinyStone);
      return list;
   }

   public static List getAllItems() {
      if (allItemList == null) {
         initializeItemMap();
      }

      return allItemList;
   }

   public static Map getItemsMap() {
      if (allItemMap == null) {
         initializeItemMap();
      }

      return allItemMap;
   }

   public static Item getItemFromName(String itemName) {
      return (Item)getItemsMap().get(itemName.toLowerCase());
   }

   private static void initializeItemMap() {
      allItemMap = new HashMap();
      allItemList = new ArrayList();
      populateItemMap(PixelmonItems.class.getFields());
      populateItemMap(PixelmonItemsApricorns.class.getFields());
      populateItemMap(PixelmonItemsBadgeCases.class.getFields());
      populateItemMap(PixelmonItemsBadges.class.getFields());
      populateItemMap(PixelmonItemsFossils.class.getFields());
      populateItemMap(PixelmonItemsHeld.class.getFields());
      populateItemMap(PixelmonItemsMail.items);
      populateItemMap(PixelmonItemsPokeballs.class.getFields());
      populateItemMap((Object)PixelmonItemsTMs.HMs);
      populateItemMap(PixelmonItemsTMs.class.getFields());
      populateItemMap(PixelmonItemsTools.class.getFields());
      populateItemMap(PixelmonItemsLures.weakLures);
      populateItemMap(PixelmonItemsLures.strongLures);
      populateItemMap(PixelmonBlocks.class.getFields());
      populateItemMap(PixelmonBlocksApricornTrees.class.getFields());
      allItemMap = Collections.unmodifiableMap(allItemMap);
   }

   private static void populateItemMap(ArrayList items) {
      Iterator var1 = items.iterator();

      while(var1.hasNext()) {
         Object object = var1.next();
         populateItemMap(object);
      }

   }

   private static void populateItemMap(Field[] items) {
      Field[] var1 = items;
      int var2 = items.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Field item = var1[var3];

         try {
            Object object = item.get((Object)null);
            populateItemMap(object);
         } catch (Exception var6) {
         }
      }

   }

   private static void populateItemMap(Object object) {
      Item item = null;
      if (object instanceof Item) {
         item = (Item)object;
      } else if (object instanceof Block) {
         item = Item.func_150898_a((Block)object);
      }

      if (item != null) {
         ItemStack itemStack = new ItemStack(item);
         allItemMap.put(item.func_77667_c(itemStack).toLowerCase(), item);
         allItemMap.put(I18n.func_150826_b(item.func_77667_c(itemStack).toLowerCase() + ".name").toLowerCase(), item);
         allItemList.add(item);
      }

   }

   public static ArrayList getEvostoneList() {
      if (evostoneList.isEmpty()) {
         evostoneList = initializeEvostoneList();
      }

      return evostoneList;
   }

   public static void registerRenderers() {
      try {
         Field[] var0 = PixelmonItems.class.getFields();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            Field field = var0[var2];
            if (field.get((Object)null) instanceof Item) {
               Item item = (Item)field.get((Object)null);
               if (item.getRegistryName() == null) {
                  System.out.println(item.func_77658_a() + " missing registery name!");
               }

               ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
               if (item == itemFinder) {
                  ModelLoader.setCustomModelResourceLocation(item, 1, new ModelResourceLocation("pixelmon:item_finder_front", "inventory"));
                  ModelLoader.setCustomModelResourceLocation(item, 2, new ModelResourceLocation("pixelmon:item_finder_back", "inventory"));
                  ModelLoader.setCustomModelResourceLocation(item, 3, new ModelResourceLocation("pixelmon:item_finder_right", "inventory"));
                  ModelLoader.setCustomModelResourceLocation(item, 4, new ModelResourceLocation("pixelmon:item_finder_left", "inventory"));
                  ModelBakery.registerItemVariants(itemFinder, new ResourceLocation[]{itemFinder.getRegistryName(), new ResourceLocation(itemFinder.getRegistryName() + "_front"), new ResourceLocation(itemFinder.getRegistryName() + "_back"), new ResourceLocation(itemFinder.getRegistryName() + "_right"), new ResourceLocation(itemFinder.getRegistryName() + "_left")});
               } else if (item == ruby) {
                  ModelLoader.setCustomModelResourceLocation(item, 1, new ModelResourceLocation("pixelmon:ruby"));
                  ModelLoader.setCustomModelResourceLocation(item, 2, new ModelResourceLocation("pixelmon:ruby"));
                  ModelLoader.setCustomModelResourceLocation(item, 3, new ModelResourceLocation("pixelmon:ruby"));
               } else if (item == porygonPieces) {
                  ModelLoader.setCustomModelResourceLocation(item, 1, new ModelResourceLocation("pixelmon:porygon_piece_head"));
                  ModelLoader.setCustomModelResourceLocation(item, 2, new ModelResourceLocation("pixelmon:porygon_piece_body"));
                  ModelLoader.setCustomModelResourceLocation(item, 3, new ModelResourceLocation("pixelmon:porygon_piece_leg"));
                  ModelLoader.setCustomModelResourceLocation(item, 4, new ModelResourceLocation("pixelmon:porygon_piece_tail"));
               } else if (item == mint) {
                  EnumNature[] var5 = EnumNature.values();
                  int var6 = var5.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     EnumNature nature = var5[var7];
                     ModelLoader.setCustomModelResourceLocation(item, nature.ordinal(), new ModelResourceLocation("pixelmon:mint_" + nature.increasedStat.name().toLowerCase()));
                  }
               }
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      PixelmonBlocks.addToMapping(PixelmonBlocks.incenseBurner, incenseBurner);
      PixelmonBlocks.addToMapping(PixelmonBlocks.fullIncense, PixelmonItemsHeld.fullIncense);
      PixelmonBlocks.addToMapping(PixelmonBlocks.laxIncense, PixelmonItemsHeld.laxIncense);
      PixelmonBlocks.addToMapping(PixelmonBlocks.luckIncense, PixelmonItemsHeld.luckIncense);
      PixelmonBlocks.addToMapping(PixelmonBlocks.oddIncense, PixelmonItemsHeld.oddIncense);
      PixelmonBlocks.addToMapping(PixelmonBlocks.pureIncense, PixelmonItemsHeld.pureIncense);
      PixelmonBlocks.addToMapping(PixelmonBlocks.rockIncense, PixelmonItemsHeld.rockIncense);
      PixelmonBlocks.addToMapping(PixelmonBlocks.roseIncense, PixelmonItemsHeld.roseIncense);
      PixelmonBlocks.addToMapping(PixelmonBlocks.seaIncense, PixelmonItemsHeld.seaIncense);
      PixelmonBlocks.addToMapping(PixelmonBlocks.waveIncense, PixelmonItemsHeld.waveIncense);
   }
}
