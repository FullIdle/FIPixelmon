package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

public enum EnumBossMode {
   NotBoss(0, -1, new Color(255, 255, 255), 1.0F, 0),
   Common(1, 50, new Color(192, 192, 192), 1.2F, 5),
   Uncommon(2, 40, new Color(0, 212, 63), 1.4F, 10),
   Rare(3, 30, new Color(0, 110, 255), 1.6F, 20),
   Epic(4, 20, new Color(159, 0, 217), 1.8F, 30),
   Legendary(5, 10, new Color(255, 162, 0), 2.0F, 40),
   Ultimate(6, 1, new Color(0, 255, 255), 2.2F, 50),
   Spooky(7, -1, new Color(194, 129, 199), 1.0F, 25),
   Drowned(8, -1, new Color(103, 201, 164), 2.5F, 100),
   Equal(9, -1, Color.WHITE, 1.0F, 0);

   public int index;
   public int rarity;
   public Color colour;
   public float scaleFactor;
   private int extraLevels;
   public float r;
   public float g;
   public float b;

   private EnumBossMode(int index, int rarity, Color colour, float scaleFactor, int extraLevels) {
      this.index = index;
      this.rarity = rarity;
      this.colour = colour;
      this.scaleFactor = scaleFactor;
      this.extraLevels = extraLevels;
      this.r = (float)colour.getRed() / 255.0F;
      this.g = (float)colour.getGreen() / 255.0F;
      this.b = (float)colour.getBlue() / 255.0F;
   }

   public static EnumBossMode getMode(int index) {
      if (index < 0) {
         index = 0;
      }

      if (index >= values().length) {
         index = Legendary.ordinal();
      }

      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return NotBoss;
      }
   }

   public static EnumBossMode getFromNBT(NBTTagCompound compound) {
      return compound.func_74764_b("BossMode") ? getMode(compound.func_74762_e("BossMode")) : NotBoss;
   }

   public static EnumBossMode getRandomMode() {
      int val = RandomHelper.rand.nextInt(100);
      int total = 0;
      EnumBossMode[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumBossMode b = var2[var4];
         if (b.rarity != -1) {
            total += b.rarity;
            if (val <= total) {
               return b;
            }
         }
      }

      return Rare;
   }

   public static EnumBossMode getRandomModeMega() {
      return values()[RandomHelper.rand.nextInt(3) + 4];
   }

   public boolean isMega() {
      return this.ordinal() >= 4 && this.ordinal() <= 6;
   }

   public int getColourInt() {
      return this.colour.getRGB();
   }

   public static EnumBossMode getNextMode(EnumBossMode bossMode) {
      int index = bossMode.ordinal();
      if (index == NotBoss.index) {
         index = Equal.index;
      } else if (index == Equal.index) {
         index = Uncommon.index;
      } else if (index == Ultimate.index) {
         index = NotBoss.index;
      } else {
         ++index;
      }

      return getMode(index);
   }

   public static boolean hasBossMode(String name) {
      EnumBossMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumBossMode mode = var1[var3];
         if (mode.name().equalsIgnoreCase(name)) {
            return true;
         }
      }

      return false;
   }

   public static EnumBossMode getBossMode(String name) {
      EnumBossMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumBossMode mode = var1[var3];
         if (mode.name().equalsIgnoreCase(name)) {
            return mode;
         }
      }

      return null;
   }

   public static EnumBossMode getWeightedBossMode() {
      List weights = PixelmonConfig.bossWeights;
      if (weights == null) {
         return null;
      } else {
         int totalWeight = 0;

         Integer weight;
         for(Iterator var2 = weights.iterator(); var2.hasNext(); totalWeight += weight) {
            weight = (Integer)var2.next();
         }

         int value = RandomHelper.rand.nextInt(totalWeight) + 1;
         int runningWeights = 0;
         int i = 0;

         for(Iterator var5 = weights.iterator(); var5.hasNext(); ++i) {
            Integer weight = (Integer)var5.next();
            runningWeights += weight;
            if (runningWeights >= value) {
               if (i > 6) {
                  i = 5;
               }

               return values()[i + 1];
            }
         }

         return null;
      }
   }

   public int getExtraLevels() {
      if (this != Equal && this != NotBoss) {
         List levels = PixelmonConfig.bossLevelIncreases;
         int ordinal = this.index - 1;
         return levels.size() > ordinal ? (Integer)levels.get(ordinal) : this.extraLevels;
      } else {
         return this.extraLevels;
      }
   }

   public float getCandyChance() {
      if (this.index > 0 && this.index < 7) {
         int index = this.index - 1;
         return PixelmonConfig.bossCandyChances.size() > index ? (Float)PixelmonConfig.bossCandyChances.get(index) : 0.0F;
      } else {
         return 0.0F;
      }
   }

   public String getBossText() {
      return I18n.func_74838_a("enum.trainerBoss." + this.toString().toLowerCase());
   }

   public boolean isBossPokemon() {
      return this.extraLevels > 0;
   }

   public boolean isMegaBossPokemon() {
      return this.isBossPokemon() && this.isMega();
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("enum.trainerBoss." + this.name().toLowerCase());
   }
}
