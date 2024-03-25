package com.pixelmonmod.pixelmon.enums.items;

import com.pixelmonmod.pixelmon.config.PixelmonItemsApricorns;
import com.pixelmonmod.pixelmon.items.IEnumItem;
import com.pixelmonmod.pixelmon.items.ItemApricorn;
import com.pixelmonmod.pixelmon.items.ItemApricornCooked;
import net.minecraft.item.Item;

public enum EnumApricorns implements IEnumItem {
   Black(72, 88),
   White(73, 89),
   Pink(74, 90),
   Green(75, 91),
   Blue(76, 92),
   Yellow(77, 93),
   Red(78, 94);

   public int iconIndex;
   public int meltedIconIndex;

   private EnumApricorns(int iconIndex, int meltedIconIndex) {
      this.iconIndex = iconIndex;
      this.meltedIconIndex = meltedIconIndex;
   }

   public ItemApricorn apricorn() {
      switch (this) {
         case Black:
            return (ItemApricorn)PixelmonItemsApricorns.apricornBlack;
         case Blue:
            return (ItemApricorn)PixelmonItemsApricorns.apricornBlue;
         case Green:
            return (ItemApricorn)PixelmonItemsApricorns.apricornGreen;
         case Pink:
            return (ItemApricorn)PixelmonItemsApricorns.apricornPink;
         case Red:
            return (ItemApricorn)PixelmonItemsApricorns.apricornRed;
         case White:
            return (ItemApricorn)PixelmonItemsApricorns.apricornWhite;
         case Yellow:
            return (ItemApricorn)PixelmonItemsApricorns.apricornYellow;
         default:
            return null;
      }
   }

   public ItemApricornCooked cookedApricorn() {
      switch (this) {
         case Black:
            return (ItemApricornCooked)PixelmonItemsApricorns.apricornBlackCooked;
         case Blue:
            return (ItemApricornCooked)PixelmonItemsApricorns.apricornBlueCooked;
         case Green:
            return (ItemApricornCooked)PixelmonItemsApricorns.apricornGreenCooked;
         case Pink:
            return (ItemApricornCooked)PixelmonItemsApricorns.apricornPinkCooked;
         case Red:
            return (ItemApricornCooked)PixelmonItemsApricorns.apricornRedCooked;
         case White:
            return (ItemApricornCooked)PixelmonItemsApricorns.apricornWhiteCooked;
         case Yellow:
            return (ItemApricornCooked)PixelmonItemsApricorns.apricornYellowCooked;
         default:
            return null;
      }
   }

   public Item getItem(int type) {
      switch (type) {
         case 0:
            return this.apricorn();
         case 1:
            return this.cookedApricorn();
         default:
            return null;
      }
   }

   public int numTypes() {
      return 2;
   }

   public static boolean hasApricorn(String apricorn) {
      try {
         return valueOf(apricorn) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}
