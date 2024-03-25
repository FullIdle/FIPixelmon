package com.pixelmonmod.tcg.config;

import com.pixelmonmod.tcg.api.card.CardRarity;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TCGConfig {
   private static final TCGConfig instance = new TCGConfig();
   private static Configuration config;
   public boolean essenceForRaidWins;
   public boolean essenceForPacks;
   private boolean loseEssenceOnDeath;
   private boolean typeTwoDropsEssence;
   private double typeTwoEssenceDropRate;
   /** @deprecated */
   @Deprecated
   public double essenceDropRate;
   private double rarePokemonEssenceModifier;
   public int randomPackEssenceCost;
   public int specificPackEssenceCost;
   public int commonCost;
   public int uncommonCost;
   public int rareCost;
   public int holorareCost;
   public int ultrarareCost;
   public int secretrareCost;
   public int cardDisenchantEssenceDivisor;
   private double baseEssence;
   private double twentyFiveEssenceMultiplier;
   private double fiftyEssenceMultiplier;
   private double seventyFiveEssenceMultiplier;
   private double ninetyNineEssenceMultiplier;
   private double oneHundredEssenceMultiplier;
   public int raidBaseEssence;
   public int raidOneStarEssenceMultiplier;
   public int raidTwoStarEssenceMultiplier;
   public int raidThreeStarEssenceMultiplier;
   public int raidFourStarEssenceMultiplier;
   public int raidFiveStarEssenceMultiplier;
   public int savedUIScale = -1;
   public ConfigCategory categoryTCG;

   public static synchronized TCGConfig getInstance() {
      return instance;
   }

   public void loadConfig(FMLPreInitializationEvent event) {
      config = new Configuration(event.getSuggestedConfigurationFile());
      config.load();
      this.loadSettings();
   }

   public void loadSettings() {
      this.categoryTCG = config.getCategory("TCG");
      config.addCustomCategoryComment("TCG", "Options surrounding crafting in TCG");
      this.essenceForPacks = config.get("TCG", "Essence for packs", true, "Can essence be exchanged for packs?").getBoolean();
      this.essenceForRaidWins = config.get("TCG", "Essence for raid wins", true, "Can you get essence from winning raids?").getBoolean();
      this.loseEssenceOnDeath = config.get("TCG", "Lose Essence on death", false, "Should a player lose essence when they die").getBoolean();
      this.essenceDropRate = config.get("TCG", "Essence drop modifier", 1.0, "Value which to multiply essence drop rate by. Doesn't affect raids").getDouble();
      this.typeTwoDropsEssence = config.get("TCG", "Type Two Drops Essence", true, "Should a pokemon's second type drop essence?").getBoolean();
      this.typeTwoEssenceDropRate = config.get("TCG", "Type Two Essence Drop Modifier", 0.25, "Value which to multiply type two essence drop rate by. Doesn't affect raids").getDouble();
      this.commonCost = config.get("TCG Crafting", "Common cost", 25, "").getInt();
      this.uncommonCost = config.get("TCG Crafting", "Uncommon cost", 100, "").getInt();
      this.rareCost = config.get("TCG Crafting", "Rare cost", 500, "").getInt();
      this.holorareCost = config.get("TCG Crafting", "Holorare cost", 1250, "").getInt();
      this.ultrarareCost = config.get("TCG Crafting", "Ultrarare cost", 3000, "").getInt();
      this.secretrareCost = config.get("TCG Crafting", "Secretrare cost", 6000, "").getInt();
      this.cardDisenchantEssenceDivisor = config.get("TCG Crafting", "Essence Disenchant Divisor", 4, "The amount of the cost of a rarity of a card to give back if disenchanted. Defaults to 4, so a common card by default would give back 25/4 default").getInt();
      this.randomPackEssenceCost = config.get("TCG Crafting", "Random Pack Cost", 50, "The amount of each essence you need to exchange for a random pack.").getInt();
      this.specificPackEssenceCost = config.get("TCG Crafting", "Specific Pack Cost", 300, "The amount of each essence you need to exchange for a specific pack.").getInt();
      this.baseEssence = config.get("TCG", "Base Essence", 1.0, "The base amount of essence a battle should give.").getDouble();
      this.twentyFiveEssenceMultiplier = config.get("TCG", "Level 25 and Under Essence Multiplier", 0.25, "The multiplier of essence a level 25 or under pokemon should give").getDouble();
      this.fiftyEssenceMultiplier = config.get("TCG", "Level 26-50 Essence Multiplier", 0.5, "The amount of a level 26-50 pokemon should give").getDouble();
      this.seventyFiveEssenceMultiplier = config.get("TCG", "Level 51-75 Essence Multiplier", 0.75, "The multiplier of essence a level 51-75 pokemon should give").getDouble();
      this.ninetyNineEssenceMultiplier = config.get("TCG", "Level 76-99 Essence Multiplier", 1.0, "The multiplier of essence a level 76-99 pokemon should give").getDouble();
      this.oneHundredEssenceMultiplier = config.get("TCG", "Level 100 Essence Multiplier", 1.25, "The multiplier of essence a level 100 pokemon should give").getDouble();
      this.raidBaseEssence = config.get("TCG", "Raid Base Essence", 35, "The base amount of essence a player gets for defeating a raid").getInt();
      this.raidOneStarEssenceMultiplier = config.get("TCG", "Raid One Star Multiplier", 1, "Multiplies the raid base essence depending on the amount of stars in a raid").getInt();
      this.raidTwoStarEssenceMultiplier = config.get("TCG", "Raid Two Star Multiplier", 1, "Multiplies the raid base essence depending on the amount of stars in a raid").getInt();
      this.raidThreeStarEssenceMultiplier = config.get("TCG", "Raid Three Star Multiplier", 2, "Multiplies the raid base essence depending on the amount of stars in a raid").getInt();
      this.raidFourStarEssenceMultiplier = config.get("TCG", "Raid Four Star Multiplier", 3, "Multiplies the raid base essence depending on the amount of stars in a raid").getInt();
      this.raidFiveStarEssenceMultiplier = config.get("TCG", "Raid Five Star Multiplier", 5, "Multiplies the raid base essence depending on the amount of stars in a raid").getInt();
      this.rarePokemonEssenceModifier = (double)config.get("TCG", "Rare Pokemon Essence Multiplier", 2, "The amount of essence received is multiplied by this number if a Pixelmon is considered a rare (Legend, Mythical, UB, etc)").getInt();
      this.savedUIScale = config.get("TCG", "UI scale", -1, "Do not edit").getInt();
      if (config.hasChanged()) {
         config.save();
      }

   }

   public int getCostForRarity(CardRarity r) {
      switch (r) {
         case COMMON:
            return getInstance().commonCost;
         case UNCOMMON:
            return getInstance().uncommonCost;
         case RARE:
            return getInstance().rareCost;
         case HOLORARE:
            return getInstance().holorareCost;
         case ULTRARARE:
            return getInstance().ultrarareCost;
         default:
            return 0;
      }
   }

   public int getRaidEssence(int stars) {
      int essence = getInstance().raidBaseEssence;
      switch (stars) {
         case 1:
            return essence * getInstance().raidOneStarEssenceMultiplier;
         case 2:
            return essence * getInstance().raidTwoStarEssenceMultiplier;
         case 3:
            return essence * getInstance().raidThreeStarEssenceMultiplier;
         case 4:
            return essence * getInstance().raidFourStarEssenceMultiplier;
         case 5:
            return essence * getInstance().raidFiveStarEssenceMultiplier;
         default:
            return essence;
      }
   }

   public double getLevelEssenceMultiplier(double level) {
      if (level <= 25.0) {
         return getInstance().twentyFiveEssenceMultiplier;
      } else if (level <= 50.0) {
         return getInstance().fiftyEssenceMultiplier;
      } else if (level <= 75.0) {
         return getInstance().seventyFiveEssenceMultiplier;
      } else if (level <= 99.0) {
         return getInstance().ninetyNineEssenceMultiplier;
      } else {
         return level >= 100.0 ? getInstance().oneHundredEssenceMultiplier : 1.0;
      }
   }

   public boolean loseEssenceOnDeath() {
      return this.loseEssenceOnDeath;
   }

   public double getRarePokemonEssenceModifier() {
      return this.rarePokemonEssenceModifier;
   }

   public double getTwentyFiveEssenceMultiplier() {
      return this.twentyFiveEssenceMultiplier;
   }

   public double getFiftyEssenceMultiplier() {
      return this.fiftyEssenceMultiplier;
   }

   public double getSeventyFiveEssenceMultiplier() {
      return this.seventyFiveEssenceMultiplier;
   }

   public double getNinetyNineEssenceMultiplier() {
      return this.ninetyNineEssenceMultiplier;
   }

   public double getOneHundredEssenceMultiplier() {
      return this.oneHundredEssenceMultiplier;
   }

   public boolean typeTwoDropsEssence() {
      return this.typeTwoDropsEssence;
   }

   public double getTypeTwoEssenceDropRate() {
      return this.typeTwoEssenceDropRate;
   }

   public double getEssenceDropRate() {
      return this.essenceDropRate;
   }

   public double getBaseEssence() {
      return this.baseEssence;
   }
}
