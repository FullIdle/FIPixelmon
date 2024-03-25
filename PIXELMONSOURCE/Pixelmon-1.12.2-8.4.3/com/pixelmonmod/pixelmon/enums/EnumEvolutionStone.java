package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.items.IEnumItem;
import com.pixelmonmod.pixelmon.items.ItemEvolutionStone;
import net.minecraft.item.Item;

public enum EnumEvolutionStone implements IEnumItem {
   Firestone,
   Thunderstone,
   Waterstone,
   Sunstone,
   Leafstone,
   Dawnstone,
   Duskstone,
   Moonstone,
   Shinystone,
   Icestone;

   public static EnumEvolutionStone getEvolutionStone(String name) {
      try {
         return valueOf(name);
      } catch (Exception var2) {
         return null;
      }
   }

   public static boolean hasEvolutionStone(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }

   public ItemEvolutionStone getItem(int useless) {
      Item result = null;
      switch (this) {
         case Dawnstone:
            result = PixelmonItems.dawnStone;
            break;
         case Duskstone:
            result = PixelmonItems.duskStone;
            break;
         case Firestone:
            result = PixelmonItems.fireStone;
            break;
         case Leafstone:
            result = PixelmonItems.leafStone;
            break;
         case Moonstone:
            result = PixelmonItems.moonStone;
            break;
         case Shinystone:
            result = PixelmonItems.shinyStone;
            break;
         case Sunstone:
            result = PixelmonItems.sunStone;
            break;
         case Thunderstone:
            result = PixelmonItems.thunderStone;
            break;
         case Waterstone:
            result = PixelmonItems.waterStone;
      }

      return (ItemEvolutionStone)result;
   }

   public int numTypes() {
      return 1;
   }
}
