package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.awt.Color;

public enum EnumStatueTextureType implements ITranslatable {
   OriginalTexture(0),
   Shiny(1),
   Special(2),
   Stone(3),
   Gold(4),
   Bronze(5),
   Silver(6),
   Green(7, new Color(0, 212, 63)),
   Yellow(8, Color.YELLOW),
   Red(9, Color.RED),
   Orange(10, new Color(255, 162, 0)),
   Cyan(11, Color.CYAN),
   Blue(12, new Color(0, 110, 255)),
   Indigo(13, new Color(64, 0, 255)),
   Magenta(14, new Color(199, 0, 217)),
   Pink(15, Color.PINK),
   Gray(16, Color.GRAY),
   Brown(17, new Color(145, 82, 0));

   public int index;
   public EnumBossMode bossMode;
   public Color color;

   private EnumStatueTextureType(int index) {
      this(index, EnumBossMode.NotBoss);
   }

   private EnumStatueTextureType(int index, EnumBossMode bossMode) {
      this(index, bossMode, Color.WHITE);
   }

   private EnumStatueTextureType(int index, Color color) {
      this(index, EnumBossMode.NotBoss, color);
   }

   private EnumStatueTextureType(int index, EnumBossMode bossMode, Color color) {
      this.index = index;
      this.bossMode = bossMode;
      this.color = color;
   }

   public static EnumStatueTextureType getFromOrdinal(int value) {
      if (value >= values().length) {
         return Red;
      } else {
         EnumStatueTextureType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EnumStatueTextureType t = var1[var3];
            if (t.ordinal() == value) {
               return t;
            }
         }

         return null;
      }
   }

   public EnumStatueTextureType getNextType(EnumStatueTextureType t) {
      int index = t.ordinal();
      if (index >= values().length - 1) {
         index = 0;
      } else {
         ++index;
      }

      return getFromOrdinal(index);
   }

   public static EnumStatueTextureType getFromString(String name) {
      EnumStatueTextureType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumStatueTextureType t = var1[var3];
         if (t.toString().equalsIgnoreCase(name)) {
            return t;
         }
      }

      return OriginalTexture;
   }

   public String getUnlocalizedName() {
      return "enum.statuetex." + this.toString().toLowerCase();
   }
}
