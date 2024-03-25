package com.pixelmonmod.pixelmon.enums.items;

import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumCustomModel;
import com.pixelmonmod.pixelmon.items.IEnumItem;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.items.ItemPokeballDisc;
import com.pixelmonmod.pixelmon.items.ItemPokeballLid;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public enum EnumPokeballs implements IEnumItem, ITranslatable {
   PokeBall(0, 1.0, "poke_ball", 5, 15),
   GreatBall(1, 1.5, "great_ball", 2, 35),
   UltraBall(2, 2.0, "ultra_ball", 1, 55),
   MasterBall(3, 255.0, "master_ball", 0, 0),
   LevelBall(4, 1.0, "level_ball", 2, 35),
   MoonBall(5, 1.0, "moon_ball", 2, 35),
   FriendBall(6, 1.0, "friend_ball", 3, 35),
   LoveBall(7, 1.0, "love_ball", 3, 35),
   SafariBall(8, -1.0, "safari_ball", 3, 35),
   HeavyBall(9, 1.0, "heavy_ball", 3, 35),
   FastBall(10, 1.0, "fast_ball", 3, 35),
   RepeatBall(11, 1.0, "repeat_ball", 3, 35),
   TimerBall(12, 1.0, "timer_ball", 3, 35),
   NestBall(13, 1.0, "nest_ball", 3, 35),
   NetBall(14, 1.0, "net_ball", 3, 35),
   DiveBall(15, 1.0, "dive_ball", 3, 35),
   LuxuryBall(16, 1.0, "luxury_ball", 3, 35),
   HealBall(17, 1.0, "heal_ball", 3, 35),
   DuskBall(18, 1.0, "dusk_ball", 3, 35),
   PremierBall(19, 1.0, "premier_ball", 3, 25),
   SportBall(20, 1.0, "sport_ball", 3, 25),
   QuickBall(21, 1.0, "quick_ball", 3, 35),
   ParkBall(22, 255.0, "park_ball", 0, 0),
   LureBall(23, 1.0, "lure_ball", 3, 35),
   CherishBall(24, 1.0, "cherish_ball", 0, 0),
   GSBall(25, 1.0, "gs_ball", 0, 0),
   BeastBall(26, 1.0, "beast_ball", 0, 0),
   DreamBall(27, 1.0, "dream_ball", 0, 0);

   private double ballBonus;
   private int index;
   private String directory;
   private String filenamePrefix;
   public int quantityMade;
   public int breakChance;

   private EnumPokeballs(int index, double ballBonus, String filenamePrefix, int quantityMade, int chanceBreak) {
      this.ballBonus = ballBonus;
      this.index = index;
      this.filenamePrefix = filenamePrefix;
      this.quantityMade = quantityMade;
      this.breakChance = chanceBreak;
   }

   public double getBallBonus() {
      return this.ballBonus;
   }

   public int getIndex() {
      return this.index;
   }

   public int getBreakChance() {
      return this.breakChance;
   }

   public String getTexture() {
      return this.filenamePrefix.replace("_", "") + ".png";
   }

   public String getFlashRedTexture() {
      return this.filenamePrefix.replace("_", "") + "flashing.png";
   }

   public String getFilenamePrefix() {
      return this.filenamePrefix;
   }

   public String getUnlocalizedName() {
      return "item." + this.filenamePrefix + ".name";
   }

   public static EnumPokeballs getFromIndex(int index) {
      return index >= 0 && index < values().length ? values()[index] : PokeBall;
   }

   public ItemPokeball getItem() {
      return PixelmonItemsPokeballs.getItemFromEnum(this);
   }

   public ItemPokeballLid getLid() {
      return PixelmonItemsPokeballs.getLidFromEnum(this);
   }

   public ItemPokeballDisc getDisc() {
      return PixelmonItemsPokeballs.getDiscFromEnum(this);
   }

   public Item getItem(int type) {
      switch (type) {
         case 0:
            return this.getItem();
         case 1:
            return this.getLid();
         case 2:
            return this.getDisc();
         default:
            return null;
      }
   }

   public int numTypes() {
      return 3;
   }

   public static boolean hasPokeball(String pokeball) {
      try {
         return valueOf(pokeball) != null;
      } catch (Exception var2) {
         return false;
      }
   }

   public String getTextureDirectory() {
      String path = "pixelmon:textures/pokeballs/";
      return this.directory == null ? path : this.directory;
   }

   public ResourceLocation getTextureLocation() {
      return new ResourceLocation(this.getTextureDirectory() + this.getTexture());
   }

   public boolean isPokeball(EnumPokeballs... pokeballs) {
      EnumPokeballs[] var2 = pokeballs;
      int var3 = pokeballs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumPokeballs pokeball = var2[var4];
         if (this == pokeball) {
            return true;
         }
      }

      return false;
   }

   public void setTextureDirectory(String newDirectory) {
      try {
         this.directory = newDirectory.charAt(newDirectory.length() - 1) == '/' ? newDirectory : newDirectory + '/';
      } catch (NullPointerException var3) {
         this.directory = null;
      }

   }

   public ModelPokeballs getModel() {
      EnumCustomModel model;
      switch (this) {
         case MasterBall:
            model = EnumCustomModel.Masterball;
            break;
         case CherishBall:
            model = EnumCustomModel.Cherishball;
            break;
         case GreatBall:
            model = EnumCustomModel.Greatball;
            break;
         case HeavyBall:
            model = EnumCustomModel.Heavyball;
            break;
         case NetBall:
            model = EnumCustomModel.Netball;
            break;
         case TimerBall:
            model = EnumCustomModel.Timerball;
            break;
         case BeastBall:
            model = EnumCustomModel.Beastball;
            break;
         default:
            model = EnumCustomModel.Pokeball;
      }

      return new ModelPokeballs(model);
   }

   public static EnumPokeballs getPokeballFromString(String name) {
      EnumPokeballs[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumPokeballs ball = var1[var3];
         if (ball.filenamePrefix.equalsIgnoreCase(name)) {
            return ball;
         }
      }

      return null;
   }
}
