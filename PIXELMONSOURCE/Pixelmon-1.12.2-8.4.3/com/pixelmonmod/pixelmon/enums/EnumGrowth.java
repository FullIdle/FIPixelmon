package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.nbt.NBTTagCompound;

public enum EnumGrowth implements IEnumForm {
   Pygmy(0, 0.5F, 5, 1),
   Runt(1, 0.75F, 10, 2),
   Small(2, 0.9F, 20, 3),
   Ordinary(3, 1.0F, 30, 4),
   Huge(4, 1.1F, 20, 5),
   Giant(5, 1.25F, 10, 6),
   Enormous(6, 1.5F, 5, 7),
   Ginormous(7, 1.66F, 0, 8),
   Microscopic(8, 0.33F, 0, 0);

   public float scaleValue;
   public int rarity;
   public int index;
   public int scaleOrdinal;
   public static List orderedList = (List)Arrays.stream(values()).sorted(Comparator.comparingInt((o) -> {
      return o.scaleOrdinal;
   })).collect(Collectors.toList());

   private EnumGrowth(int index, float scaleValue, int rarity, int scaleOrdinal) {
      this.index = index;
      this.scaleValue = scaleValue;
      this.rarity = rarity;
      this.scaleOrdinal = scaleOrdinal;
   }

   public static EnumGrowth getRandomGrowth() {
      int tot = 0;
      int rndm = RandomHelper.rand.nextInt(100);
      EnumGrowth[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumGrowth g = var2[var4];
         tot += g.rarity;
         if (rndm < tot) {
            return g;
         }
      }

      return Ordinary;
   }

   public static EnumGrowth getGrowthFromIndex(int index) {
      try {
         return values()[index];
      } catch (Exception var2) {
         return null;
      }
   }

   public static EnumGrowth getNextGrowthRestricted(EnumGrowth g) {
      int index = g.ordinal();
      if (index == 6) {
         index = 0;
      } else {
         ++index;
      }

      EnumGrowth[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumGrowth v = var2[var4];
         if (v.ordinal() == index) {
            return v;
         }
      }

      return null;
   }

   public static EnumGrowth getFromNBT(NBTTagCompound compound) {
      return compound.func_74764_b("Growth") ? getGrowthFromIndex(compound.func_74762_e("Growth")) : null;
   }

   public static EnumGrowth getPreviousGrowth(EnumGrowth g) {
      int index = g.scaleOrdinal;
      if (index == 0) {
         index = 8;
      } else {
         --index;
      }

      EnumGrowth[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumGrowth v = var2[var4];
         if (v.scaleOrdinal == index) {
            return v;
         }
      }

      return null;
   }

   public static EnumGrowth getNextGrowth(EnumGrowth g) {
      int index = g.scaleOrdinal;
      if (index == 8) {
         index = 0;
      } else {
         ++index;
      }

      EnumGrowth[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumGrowth v = var2[var4];
         if (v.scaleOrdinal == index) {
            return v;
         }
      }

      return null;
   }

   public static boolean hasGrowth(String name) {
      EnumGrowth[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumGrowth growth = var1[var3];
         if (growth.name().equalsIgnoreCase(name) || growth.getLocalizedName().equalsIgnoreCase(name)) {
            return true;
         }
      }

      return false;
   }

   public static EnumGrowth growthFromString(String name) {
      EnumGrowth[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumGrowth growth = var1[var3];
         if (growth.name().equalsIgnoreCase(name) || growth.getLocalizedName().equalsIgnoreCase(name)) {
            return growth;
         }
      }

      return null;
   }

   public static EnumGrowth getGrowthFromScaleOrdinal(Integer scaleOrdinal) {
      EnumGrowth[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumGrowth v = var1[var3];
         if (v.scaleOrdinal == scaleOrdinal) {
            return v;
         }
      }

      return null;
   }

   public String getUnlocalizedName() {
      return "enum.growth." + this.toString().toLowerCase();
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getName() {
      return this.name();
   }
}
