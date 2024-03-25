package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.enums.items.EnumBadgeCase;
import com.pixelmonmod.pixelmon.items.ItemBadgeCase;
import net.minecraft.item.Item;

public class PixelmonItemsBadgeCases {
   public static Item badgecaseBlack;
   public static Item badgecaseWhite;
   public static Item badgecasePink;
   public static Item badgecaseGreen;
   public static Item badgecaseBlue;
   public static Item badgecaseYellow;
   public static Item badgecaseRed;

   public static void load() {
      badgecaseBlack = new ItemBadgeCase(EnumBadgeCase.Black);
      badgecaseWhite = new ItemBadgeCase(EnumBadgeCase.White);
      badgecasePink = new ItemBadgeCase(EnumBadgeCase.Pink);
      badgecaseGreen = new ItemBadgeCase(EnumBadgeCase.Green);
      badgecaseBlue = new ItemBadgeCase(EnumBadgeCase.Blue);
      badgecaseYellow = new ItemBadgeCase(EnumBadgeCase.Yellow);
      badgecaseRed = new ItemBadgeCase(EnumBadgeCase.Red);
   }
}
