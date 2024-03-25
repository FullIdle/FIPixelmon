package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumSpecialTexture;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.forms.ICosmeticForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import net.minecraft.util.ResourceLocation;

public class GuiResources {
   public static ResourceLocation prefix = new ResourceLocation("pixelmon:textures/");
   public static ResourceLocation tradeGui;
   public static ResourceLocation heldItem;
   public static ResourceLocation overlaySimple;
   public static ResourceLocation overlayExtended;
   public static ResourceLocation pokemonInfoP1;
   public static ResourceLocation pokemonInfoP2;
   public static ResourceLocation levelUpPopup;
   public static ResourceLocation battleGui1;
   public static ResourceLocation battleGui2;
   public static ResourceLocation battleGui3;
   public static ResourceLocation battleGui1B;
   public static ResourceLocation battleGui3B;
   public static ResourceLocation battlePointer;
   public static ResourceLocation yesNo;
   public static ResourceLocation chooseMove;
   public static ResourceLocation chooseMove2;
   public static ResourceLocation choosePokemon;
   public static ResourceLocation selectCurrentPokemon;
   public static ResourceLocation itemGui2;
   public static ResourceLocation itemGui1;
   public static ResourceLocation pokecheckerPopup;
   public static ResourceLocation pixelmonCreativeInventory;
   public static ResourceLocation shinyCharm;
   public static ResourceLocation ovalCharm;
   public static ResourceLocation expCharm;
   public static ResourceLocation catchingCharm;
   public static ResourceLocation markCharm;
   public static ResourceLocation pixelmonOverlay;
   public static ResourceLocation pixelmonOverlayExtended2;
   public static ResourceLocation mcInventory;
   public static ResourceLocation pcResources;
   public static ResourceLocation pcPointer;
   public static ResourceLocation pcPointerGrabbed;
   public static ResourceLocation summarySummary;
   public static ResourceLocation rename;
   public static ResourceLocation summaryMoves;
   public static ResourceLocation summaryStats;
   public static ResourceLocation summaryTMs;
   public static ResourceLocation summaryRibbons;
   public static ResourceLocation parallelogram;
   public static ResourceLocation gmaxFactor;
   public static ResourceLocation types;
   public static ResourceLocation status;
   public static ResourceLocation shiny;
   public static ResourceLocation caught;
   public static ResourceLocation infinity;
   public static ResourceLocation eggNormal1;
   public static ResourceLocation eggNormal2;
   public static ResourceLocation eggNormal3;
   public static ResourceLocation eggTogepi1;
   public static ResourceLocation eggTogepi2;
   public static ResourceLocation eggTogepi3;
   public static ResourceLocation eggManaphy1;
   public static ResourceLocation eggManaphy2;
   public static ResourceLocation eggManaphy3;
   public static ResourceLocation available;
   public static ResourceLocation dock;
   public static ResourceLocation fainted;
   public static ResourceLocation faintedSelected;
   public static ResourceLocation normal;
   public static ResourceLocation released;
   public static ResourceLocation releasedSelected;
   public static ResourceLocation selected;
   public static ResourceLocation textbox;
   public static ResourceLocation beast;
   public static ResourceLocation cherish;
   public static ResourceLocation dive;
   public static ResourceLocation dusk;
   public static ResourceLocation fast;
   public static ResourceLocation friend;
   public static ResourceLocation great;
   public static ResourceLocation gs;
   public static ResourceLocation heal;
   public static ResourceLocation heavy;
   public static ResourceLocation level;
   public static ResourceLocation love;
   public static ResourceLocation lure;
   public static ResourceLocation luxury;
   public static ResourceLocation master;
   public static ResourceLocation moon;
   public static ResourceLocation nest;
   public static ResourceLocation net;
   public static ResourceLocation park;
   public static ResourceLocation poke;
   public static ResourceLocation premier;
   public static ResourceLocation quick;
   public static ResourceLocation repeat;
   public static ResourceLocation safari;
   public static ResourceLocation sport;
   public static ResourceLocation timer;
   public static ResourceLocation ultra;
   public static ResourceLocation dream;
   public static ResourceLocation targetArea;
   public static ResourceLocation targetAreaOver;
   public static ResourceLocation targetBox;
   public static ResourceLocation cooldown;
   public static ResourceLocation notarget;
   public static ResourceLocation moveSkillWheel;
   public static ResourceLocation moveSkillWheelCenter;
   public static ResourceLocation moveSkillSelect;
   public static ResourceLocation moveSkillPointer;
   public static ResourceLocation pokedexItemIcon;
   public static ResourceLocation wikiItemIcon;
   public static ResourceLocation trainerCardItemIcon;
   public static ResourceLocation pcSearchIcon;
   public static ResourceLocation pcRenameIcon;
   public static ResourceLocation pcWallpaperIcon;
   public static ResourceLocation shadow;
   public static final ResourceLocation buttonTexture;
   public static final ResourceLocation mouseOverTexture;
   public static final ResourceLocation questionMark;
   public static final ResourceLocation fishingLogBackground;
   public static final ResourceLocation fishingLogTitle;
   public static final ResourceLocation fishingLogCategory;
   public static final ResourceLocation fishingLogContainer;
   public static final ResourceLocation fishingMenuContainer;
   public static final ResourceLocation fishingLogInformationBackground;
   public static final ResourceLocation fishingLogWater;
   public static final ResourceLocation fishingLogBubble;
   public static final ResourceLocation fishingLogConfirmButton;
   public static final ResourceLocation pokemonFont;
   public static final ResourceLocation curryDexBackground;
   public static final ResourceLocation curryDexTab;
   public static final ResourceLocation curryDexTabL;
   public static final ResourceLocation curryDexTabR;
   public static final ResourceLocation curryClassCharizard;
   public static final ResourceLocation curryClassMilcery;
   public static final ResourceLocation curryClassCopperajah;
   public static final ResourceLocation curryClassWobbuffet;
   public static final ResourceLocation curryClassKoffing;
   public static final ResourceLocation curryClassBlank;
   public static ResourceLocation starterBackground;
   public static ResourceLocation starterBorders;
   public static ResourceLocation cwPanel;
   public static ResourceLocation dialogueInput;
   public static ResourceLocation evo;
   public static ResourceLocation button;
   public static ResourceLocation buttonOver;
   public static ResourceLocation background;
   public static ResourceLocation itemSlot;
   public static ResourceLocation itemSlotOver;
   public static ResourceLocation yesAndNo;
   public static ResourceLocation male;
   public static ResourceLocation female;
   public static ResourceLocation padlock;
   public static ResourceLocation pokedollar;
   public static ResourceLocation shopkeeper;
   public static ResourceLocation cameraOverlay;
   public static ResourceLocation cameraControls;
   public static ResourceLocation keyStoneSprite;
   public static ResourceLocation dynamaxSprite;
   public static ResourceLocation dynamaxBandSprite;
   public static ResourceLocation zRingSprite;
   public static ResourceLocation noItem;
   public static ResourceLocation dropDownIcon;
   public static ResourceLocation dynamaxBandBig;
   public static ResourceLocation keyStoneBig;
   public static ResourceLocation ovalCharmBig;
   public static ResourceLocation shinyCharmBig;
   public static ResourceLocation expCharmBig;
   public static ResourceLocation catchingCharmBig;
   public static ResourceLocation markCharmBig;
   public static ResourceLocation dustMite;
   public static ResourceLocation disabled;
   public static ResourceLocation rareCandy;
   public static ResourceLocation egg;
   public static ResourceLocation roundedButton;
   public static ResourceLocation roundedButtonOver;
   public static ResourceLocation pokerusStatus;
   public static ResourceLocation pokerusCured;
   public static ResourceLocation pokerusInfectious;
   public static ResourceLocation arrowLeft;
   public static ResourceLocation arrowLeftBackground;
   public static ResourceLocation arrowRight;
   public static ResourceLocation arrowRightBackground;
   public static ResourceLocation pokedexCaught;
   public static ResourceLocation pokedexForward;
   public static ResourceLocation pokedexBack;
   public static ResourceLocation pokedexStar;
   public static ResourceLocation vertexShader;
   public static ResourceLocation fragmentShader;
   public static final ResourceLocation backgroundTexture;
   public static final ResourceLocation eyeTexture;
   public static final ResourceLocation eyeClosedTexture;
   public static final ResourceLocation crossTexture;
   public static final ResourceLocation exclamation_mark;
   public static final ResourceLocation question_mark;
   public static final ResourceLocation arrow;
   public static final ResourceLocation back;
   public static final ResourceLocation discard;
   public static final ResourceLocation refresh;
   public static final ResourceLocation save;
   public static final ResourceLocation whitePixel;

   public static ResourceLocation shinySprite(String numString) {
      return new ResourceLocation(prefix + "sprites/shinypokemon/" + numString + ".png");
   }

   public static ResourceLocation sprite(String numString) {
      return new ResourceLocation(prefix + "sprites/pokemon/" + numString + ".png");
   }

   /** @deprecated */
   @Deprecated
   public static ResourceLocation getPokemonSprite(EnumSpecies species, int form, Gender gender, boolean specialTexture, boolean shiny) {
      return new ResourceLocation("pixelmon:textures/" + getSpritePath(species, form, gender, specialTexture, shiny) + ".png");
   }

   /** @deprecated */
   @Deprecated
   public static ResourceLocation getPokemonSprite(EnumSpecies species, int form, Gender gender, String customTexture, EnumSpecialTexture specialTexture, boolean shiny) {
      return getPokemonSprite(species, form, gender, customTexture, shiny);
   }

   public static ResourceLocation getPokemonSprite(EnumSpecies species, int form, Gender gender, String customTexture, boolean shiny) {
      return new ResourceLocation("pixelmon:textures/" + getSpritePath(species, form, gender, customTexture, shiny) + ".png");
   }

   public static ResourceLocation getPokemonSprite(Pokemon pokemon) {
      return getPokemonSprite(pokemon.getSpecies(), pokemon.getForm(), pokemon.getGender(), pokemon.getCustomTexture(), pokemon.isShiny());
   }

   /** @deprecated */
   @Deprecated
   public static String getSpritePath(EnumSpecies species, int form, Gender gender, boolean specialTexture, boolean shiny) {
      if (specialTexture) {
         shiny = false;
      }

      IEnumForm formEnum = species.getFormEnum(form);
      String path = "sprites/" + (shiny && form != EnumSpecial.Online.getForm() ? "shinypokemon/" : "pokemon/") + species.getNationalPokedexNumber();
      path = path + formEnum.getSpriteSuffix(shiny);
      if (!(formEnum instanceof Gender) && EnumSpecies.mfSprite.contains(species) && !specialTexture) {
         path = path + gender.getSpriteSuffix(shiny);
      }

      return path;
   }

   /** @deprecated */
   @Deprecated
   public static String getSpritePath(EnumSpecies species, int form, Gender gender, String customTexture, EnumSpecialTexture specialTexture, boolean shiny) {
      return getSpritePath(species, form, gender, customTexture, shiny);
   }

   public static String getSpritePath(EnumSpecies species, int form, Gender gender, String customTexture, boolean shiny) {
      IEnumForm formEnum = species.getFormEnum(form);
      boolean custom = !customTexture.isEmpty() && (!(formEnum instanceof ICosmeticForm) || !((ICosmeticForm)formEnum).isCosmetic());
      if (shiny && formEnum instanceof ICosmeticForm && ((ICosmeticForm)formEnum).isCosmetic()) {
         shiny = ((ICosmeticForm)formEnum).hasShiny(species);
      }

      String path = "sprites/";
      String folder;
      if (custom) {
         folder = "custom-" + customTexture + "/";
      } else {
         folder = shiny ? "shinypokemon/" : "pokemon/";
      }

      path = path + folder + species.getNationalPokedexNumber();
      if ((!(formEnum instanceof ICosmeticForm) || !((ICosmeticForm)formEnum).isCosmetic() || form == 0) && !(formEnum instanceof Gender) && EnumSpecies.mfSprite.contains(species) && !custom) {
         path = path + gender.getSpriteSuffix(shiny);
      }

      path = path + formEnum.getSpriteSuffix(shiny);
      return path;
   }

   public static ResourceLocation getEggSprite(EnumSpecies species, int eggCycles) {
      if (species == EnumSpecies.Togepi) {
         return eggCycles > 10 ? eggTogepi1 : (eggCycles > 5 ? eggTogepi2 : eggTogepi3);
      } else if (species == EnumSpecies.Manaphy) {
         return eggCycles > 10 ? eggManaphy1 : (eggCycles > 5 ? eggManaphy2 : eggManaphy3);
      } else {
         return eggCycles > 10 ? eggNormal1 : (eggCycles > 5 ? eggNormal2 : eggNormal3);
      }
   }

   static {
      tradeGui = new ResourceLocation(prefix + "gui/tradeGui.png");
      heldItem = new ResourceLocation(prefix + "helditem.png");
      overlaySimple = new ResourceLocation(prefix + "gui/pixelmonOverlaySimple.png");
      overlayExtended = new ResourceLocation(prefix + "gui/pixelmonOverlay.png");
      pokemonInfoP1 = new ResourceLocation(prefix + "gui/pokemonInfoP1.png");
      pokemonInfoP2 = new ResourceLocation(prefix + "gui/pokemonInforP2.png");
      levelUpPopup = new ResourceLocation(prefix + "gui/levelUpPopUp.png");
      battleGui1 = new ResourceLocation(prefix + "gui/battleGui1.png");
      battleGui2 = new ResourceLocation(prefix + "gui/battleGui2.png");
      battleGui3 = new ResourceLocation(prefix + "gui/battleGui3.png");
      battleGui1B = new ResourceLocation(prefix + "gui/battleGui1B.png");
      battleGui3B = new ResourceLocation(prefix + "gui/battleGui3B.png");
      battlePointer = new ResourceLocation(prefix + "gui/battle/pointer.png");
      yesNo = new ResourceLocation(prefix + "gui/yesNo.png");
      chooseMove = new ResourceLocation(prefix + "gui/chooseMove.png");
      chooseMove2 = new ResourceLocation(prefix + "gui/chooseMove2.png");
      choosePokemon = new ResourceLocation(prefix + "gui/choosePokemon.png");
      selectCurrentPokemon = new ResourceLocation(prefix + "gui/selectCurrentPokemon.png");
      itemGui2 = new ResourceLocation(prefix + "gui/itemGui2.png");
      itemGui1 = new ResourceLocation(prefix + "gui/itemGui1_Test.png");
      pokecheckerPopup = new ResourceLocation(prefix + "gui/pokecheckerPopup.png");
      pixelmonCreativeInventory = new ResourceLocation(prefix + "gui/PixelmonCreativeInventory.png");
      shinyCharm = new ResourceLocation(prefix + "gui/inventory/shiny_charm.png");
      ovalCharm = new ResourceLocation(prefix + "gui/inventory/oval_charm.png");
      expCharm = new ResourceLocation(prefix + "gui/inventory/exp_charm.png");
      catchingCharm = new ResourceLocation(prefix + "gui/inventory/catching_charm.png");
      markCharm = new ResourceLocation(prefix + "gui/inventory/mark_charm.png");
      pixelmonOverlay = new ResourceLocation(prefix + "gui/pixelmonOverlay.png");
      pixelmonOverlayExtended2 = new ResourceLocation(prefix + "gui/pixelmonOverlayExtended2.png");
      mcInventory = new ResourceLocation("minecraft:textures/gui/container/inventory.png");
      pcResources = new ResourceLocation(prefix + "gui/pc/resources.png");
      pcPointer = new ResourceLocation(prefix + "gui/pc/pointer.png");
      pcPointerGrabbed = new ResourceLocation(prefix + "gui/pc/pointer_grabbed.png");
      summarySummary = new ResourceLocation(prefix + "gui/summarySummary.png");
      rename = new ResourceLocation(prefix + "gui/rename.png");
      summaryMoves = new ResourceLocation(prefix + "gui/summaryMoves.png");
      summaryStats = new ResourceLocation(prefix + "gui/summaryStats.png");
      summaryTMs = new ResourceLocation(prefix + "gui/summaryTMs.png");
      summaryRibbons = new ResourceLocation(prefix + "gui/summaryRibbons.png");
      parallelogram = new ResourceLocation(prefix + "gui/parallelogram.png");
      gmaxFactor = new ResourceLocation(prefix + "gui/gmaxfactor.png");
      types = new ResourceLocation(prefix + "gui/types.png");
      status = new ResourceLocation(prefix + "gui/status.png");
      shiny = new ResourceLocation(prefix + "sprites/shinypokemon/star.png");
      caught = new ResourceLocation(prefix + "sprites/pokemon/pokeball.png");
      infinity = new ResourceLocation(prefix + "gui/infinity.png");
      eggNormal1 = new ResourceLocation(prefix + "sprites/eggs/egg1.png");
      eggNormal2 = new ResourceLocation(prefix + "sprites/eggs/egg2.png");
      eggNormal3 = new ResourceLocation(prefix + "sprites/eggs/egg3.png");
      eggTogepi1 = new ResourceLocation(prefix + "sprites/eggs/togepi1.png");
      eggTogepi2 = new ResourceLocation(prefix + "sprites/eggs/togepi2.png");
      eggTogepi3 = new ResourceLocation(prefix + "sprites/eggs/togepi3.png");
      eggManaphy1 = new ResourceLocation(prefix + "sprites/eggs/manaphy1.png");
      eggManaphy2 = new ResourceLocation(prefix + "sprites/eggs/manaphy2.png");
      eggManaphy3 = new ResourceLocation(prefix + "sprites/eggs/manaphy3.png");
      available = new ResourceLocation(prefix + "gui/overlay/available.png");
      dock = new ResourceLocation(prefix + "gui/overlay/dock.png");
      fainted = new ResourceLocation(prefix + "gui/overlay/fainted.png");
      faintedSelected = new ResourceLocation(prefix + "gui/overlay/selected-fainted.png");
      normal = new ResourceLocation(prefix + "gui/overlay/normal.png");
      released = new ResourceLocation(prefix + "gui/overlay/released.png");
      releasedSelected = new ResourceLocation(prefix + "gui/overlay/selected-released.png");
      selected = new ResourceLocation(prefix + "gui/overlay/selected.png");
      textbox = new ResourceLocation(prefix + "gui/overlay/ui.png");
      beast = new ResourceLocation(prefix + "gui/overlay/beast.png");
      cherish = new ResourceLocation(prefix + "gui/overlay/cherish.png");
      dive = new ResourceLocation(prefix + "gui/overlay/dive.png");
      dusk = new ResourceLocation(prefix + "gui/overlay/dusk.png");
      fast = new ResourceLocation(prefix + "gui/overlay/fast.png");
      friend = new ResourceLocation(prefix + "gui/overlay/friend.png");
      great = new ResourceLocation(prefix + "gui/overlay/great.png");
      gs = new ResourceLocation(prefix + "gui/overlay/gs.png");
      heal = new ResourceLocation(prefix + "gui/overlay/heal.png");
      heavy = new ResourceLocation(prefix + "gui/overlay/heavy.png");
      level = new ResourceLocation(prefix + "gui/overlay/level.png");
      love = new ResourceLocation(prefix + "gui/overlay/love.png");
      lure = new ResourceLocation(prefix + "gui/overlay/lure.png");
      luxury = new ResourceLocation(prefix + "gui/overlay/luxury.png");
      master = new ResourceLocation(prefix + "gui/overlay/master.png");
      moon = new ResourceLocation(prefix + "gui/overlay/moon.png");
      nest = new ResourceLocation(prefix + "gui/overlay/nest.png");
      net = new ResourceLocation(prefix + "gui/overlay/net.png");
      park = new ResourceLocation(prefix + "gui/overlay/park.png");
      poke = new ResourceLocation(prefix + "gui/overlay/poke.png");
      premier = new ResourceLocation(prefix + "gui/overlay/premier.png");
      quick = new ResourceLocation(prefix + "gui/overlay/quick.png");
      repeat = new ResourceLocation(prefix + "gui/overlay/repeat.png");
      safari = new ResourceLocation(prefix + "gui/overlay/safari.png");
      sport = new ResourceLocation(prefix + "gui/overlay/sport.png");
      timer = new ResourceLocation(prefix + "gui/overlay/timer.png");
      ultra = new ResourceLocation(prefix + "gui/overlay/ultra.png");
      dream = new ResourceLocation(prefix + "gui/overlay/dream.png");
      targetArea = new ResourceLocation(prefix + "gui/overlay/targetArea.png");
      targetAreaOver = new ResourceLocation(prefix + "gui/overlay/targetAreaOverlay.png");
      targetBox = new ResourceLocation(prefix + "gui/overlay/targetBox.png");
      cooldown = new ResourceLocation(prefix + "gui/overlay/cooldown.png");
      notarget = new ResourceLocation(prefix + "gui/overlay/notarget.png");
      moveSkillWheel = new ResourceLocation(prefix + "gui/overlay/externalmoves/externalmovewheel.png");
      moveSkillWheelCenter = new ResourceLocation(prefix + "gui/overlay/externalmoves/externalmovecoin.png");
      moveSkillSelect = new ResourceLocation(prefix + "gui/overlay/externalmoves/select.png");
      moveSkillPointer = new ResourceLocation(prefix + "gui/overlay/externalmoves/externalmoveselector.png");
      pokedexItemIcon = new ResourceLocation(prefix + "items/pokedex.png");
      wikiItemIcon = new ResourceLocation(prefix + "gui/wikiIcon.png");
      trainerCardItemIcon = new ResourceLocation(prefix + "gui/trainercards/trainercard_icon.png");
      pcSearchIcon = new ResourceLocation(prefix + "gui/pc/icons/search.png");
      pcRenameIcon = new ResourceLocation(prefix + "gui/pc/icons/rename.png");
      pcWallpaperIcon = new ResourceLocation(prefix + "gui/pc/icons/wallpaper.png");
      shadow = new ResourceLocation(prefix + "gui/starter/ShadowLarge.png");
      buttonTexture = new ResourceLocation(prefix + "gui/starter/starterHolder.png");
      mouseOverTexture = new ResourceLocation(prefix + "gui/starter/moStarter.png");
      questionMark = new ResourceLocation(prefix + "gui/starter/questionmark.png");
      fishingLogBackground = new ResourceLocation(prefix + "gui/fishinglog/background.png");
      fishingLogTitle = new ResourceLocation(prefix + "gui/fishinglog/title.png");
      fishingLogCategory = new ResourceLocation(prefix + "gui/fishinglog/category.png");
      fishingLogContainer = new ResourceLocation(prefix + "gui/fishinglog/container.png");
      fishingMenuContainer = new ResourceLocation(prefix + "gui/fishinglog/menucontainer.png");
      fishingLogInformationBackground = new ResourceLocation(prefix + "gui/fishinglog/informationbackground.png");
      fishingLogWater = new ResourceLocation(prefix + "gui/fishinglog/water.png");
      fishingLogBubble = new ResourceLocation(prefix + "gui/fishinglog/bubble.png");
      fishingLogConfirmButton = new ResourceLocation(prefix + "gui/fishinglog/confirmbutton.png");
      pokemonFont = new ResourceLocation(prefix + "gui/pokemonfont.png");
      curryDexBackground = new ResourceLocation(prefix + "gui/currydex/background.png");
      curryDexTab = new ResourceLocation(prefix + "gui/currydex/tab.png");
      curryDexTabL = new ResourceLocation(prefix + "gui/currydex/tab-l.png");
      curryDexTabR = new ResourceLocation(prefix + "gui/currydex/tab-r.png");
      curryClassCharizard = new ResourceLocation(prefix + "gui/currydex/charizard_class.png");
      curryClassMilcery = new ResourceLocation(prefix + "gui/currydex/milcery_class.png");
      curryClassCopperajah = new ResourceLocation(prefix + "gui/currydex/copperajah_class.png");
      curryClassWobbuffet = new ResourceLocation(prefix + "gui/currydex/wobbuffet_class.png");
      curryClassKoffing = new ResourceLocation(prefix + "gui/currydex/koffing_class.png");
      curryClassBlank = new ResourceLocation(prefix + "gui/currydex/blank_class.png");
      starterBackground = new ResourceLocation(prefix + "gui/starter/background.png");
      starterBorders = new ResourceLocation(prefix + "gui/starter/borders.png");
      cwPanel = new ResourceLocation(prefix + "gui/starter/cwpanel.png");
      dialogueInput = new ResourceLocation(prefix + "gui/dialogue/text_input.png");
      evo = new ResourceLocation(prefix + "gui/evolution/Evolution.png");
      button = new ResourceLocation(prefix + "gui/evolution/Button.png");
      buttonOver = new ResourceLocation(prefix + "gui/evolution/ButtonOver.png");
      background = new ResourceLocation(prefix + "gui/drops/Drops1.png");
      itemSlot = new ResourceLocation(prefix + "gui/drops/Drops2.png");
      itemSlotOver = new ResourceLocation(prefix + "gui/drops/Drops2Over.png");
      yesAndNo = new ResourceLocation(prefix + "gui/yesAndNo.png");
      male = new ResourceLocation(prefix + "gui/male.png");
      female = new ResourceLocation(prefix + "gui/female.png");
      padlock = new ResourceLocation(prefix + "gui/padlock.png");
      pokedollar = new ResourceLocation(prefix + "gui/pokedollar.png");
      shopkeeper = new ResourceLocation(prefix + "gui/shopkeeper.png");
      cameraOverlay = new ResourceLocation(prefix + "gui/cameraOverlay.png");
      cameraControls = new ResourceLocation(prefix + "gui/cameraControls.png");
      keyStoneSprite = new ResourceLocation(prefix + "gui/megaItems/keystone.png");
      dynamaxSprite = new ResourceLocation(prefix + "gui/megaItems/dynamax_icon.png");
      dynamaxBandSprite = new ResourceLocation(prefix + "gui/megaItems/dynamax.png");
      zRingSprite = new ResourceLocation(prefix + "gui/megaItems/z.png");
      noItem = new ResourceLocation(prefix + "gui/megaItems/noitem.png");
      dropDownIcon = new ResourceLocation(prefix + "gui/dropdown.png");
      dynamaxBandBig = new ResourceLocation(prefix + "gui/inventory/dynamax_band.png");
      keyStoneBig = new ResourceLocation(prefix + "gui/inventory/keystone.png");
      ovalCharmBig = new ResourceLocation(prefix + "gui/inventory/oval_charm.png");
      shinyCharmBig = new ResourceLocation(prefix + "gui/inventory/shiny_charm.png");
      expCharmBig = new ResourceLocation(prefix + "gui/inventory/exp_charm.png");
      catchingCharmBig = new ResourceLocation(prefix + "gui/inventory/catching_charm.png");
      markCharmBig = new ResourceLocation(prefix + "gui/inventory/mark_charm.png");
      dustMite = new ResourceLocation(prefix + "gui/inventory/pixel.png");
      disabled = new ResourceLocation(prefix + "gui/inventory/disabled.png");
      rareCandy = new ResourceLocation(prefix + "items/healingitems/rarecandy.png");
      egg = new ResourceLocation(prefix + "sprites/eggs/egg1.png");
      roundedButton = new ResourceLocation("pixelmon:textures/gui/acceptDeny/button.png");
      roundedButtonOver = new ResourceLocation("pixelmon:textures/gui/acceptDeny/buttonOver.png");
      pokerusStatus = new ResourceLocation(prefix + "gui/pokerus.png");
      pokerusCured = new ResourceLocation(prefix + "gui/pokeruscured.png");
      pokerusInfectious = new ResourceLocation(prefix + "gui/pokerusinfectious.png");
      arrowLeft = new ResourceLocation(prefix + "gui/button/arrow_left.png");
      arrowLeftBackground = new ResourceLocation(prefix + "gui/button/arrow_left_bg.png");
      arrowRight = new ResourceLocation(prefix + "gui/button/arrow_right.png");
      arrowRightBackground = new ResourceLocation(prefix + "gui/button/arrow_right_bg.png");
      pokedexCaught = new ResourceLocation(prefix + "gui/pokedex/caughticon.png");
      pokedexForward = new ResourceLocation(prefix + "gui/pokedex/pokedexforward.png");
      pokedexBack = new ResourceLocation(prefix + "gui/pokedex/pokedexback.png");
      pokedexStar = new ResourceLocation(prefix + "gui/pokedex/star.png");
      vertexShader = new ResourceLocation("pixelmon:render/toon_shader.vert");
      fragmentShader = new ResourceLocation("pixelmon:render/toon_shader.frag");
      backgroundTexture = new ResourceLocation("pixelmon", "textures/gui/quests/bg.png");
      eyeTexture = new ResourceLocation("pixelmon", "textures/gui/quests/eye.png");
      eyeClosedTexture = new ResourceLocation("pixelmon", "textures/gui/quests/eye_closed.png");
      crossTexture = new ResourceLocation("pixelmon", "textures/gui/quests/cross.png");
      exclamation_mark = new ResourceLocation("pixelmon", "textures/gui/quests/exclamation_mark.png");
      question_mark = new ResourceLocation("pixelmon", "textures/gui/quests/question_mark.png");
      arrow = new ResourceLocation("pixelmon", "textures/gui/quests/arrow.png");
      back = new ResourceLocation("pixelmon", "textures/gui/quests/back.png");
      discard = new ResourceLocation("pixelmon", "textures/gui/quests/discard.png");
      refresh = new ResourceLocation("pixelmon", "textures/gui/quests/refresh.png");
      save = new ResourceLocation("pixelmon", "textures/gui/quests/save.png");
      whitePixel = new ResourceLocation("pixelmon", "textures/gui/white.png");
   }
}
