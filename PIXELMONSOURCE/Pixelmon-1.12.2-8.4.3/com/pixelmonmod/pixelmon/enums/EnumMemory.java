package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import net.minecraft.item.Item;

public enum EnumMemory {
   DRAGON(185, 66, 79),
   DARK(112, 140, 173),
   GROUND(179, 143, 103),
   FIGHTING(225, 136, 109),
   FIRE(245, 86, 89),
   ICE(185, 229, 232),
   BUG(201, 240, 126),
   STEEL(173, 175, 177),
   GRASS(124, 221, 91),
   PSYCHIC(253, 176, 173),
   FAIRY(245, 176, 217),
   FLYING(72, 191, 247),
   WATER(106, 147, 252),
   GHOST(134, 131, 164),
   ROCK(212, 192, 153),
   POISON(192, 132, 252),
   ELECTRIC(251, 192, 101);

   private int r;
   private int g;
   private int b;

   private EnumMemory(int r, int g, int b) {
      this.r = r;
      this.g = g;
      this.b = b;
   }

   public int getRed() {
      return this.r;
   }

   public int getGreen() {
      return this.g;
   }

   public int getBlue() {
      return this.b;
   }

   public static EnumMemory getMemoryForItem(Item memory) {
      if (memory == PixelmonItemsHeld.dragonMemory) {
         return DRAGON;
      } else if (memory == PixelmonItemsHeld.darkMemory) {
         return DARK;
      } else if (memory == PixelmonItemsHeld.groundMemory) {
         return GROUND;
      } else if (memory == PixelmonItemsHeld.fightingMemory) {
         return FIGHTING;
      } else if (memory == PixelmonItemsHeld.fireMemory) {
         return FIRE;
      } else if (memory == PixelmonItemsHeld.iceMemory) {
         return ICE;
      } else if (memory == PixelmonItemsHeld.bugMemory) {
         return BUG;
      } else if (memory == PixelmonItemsHeld.steelMemory) {
         return STEEL;
      } else if (memory == PixelmonItemsHeld.grassMemory) {
         return GRASS;
      } else if (memory == PixelmonItemsHeld.psychicMemory) {
         return PSYCHIC;
      } else if (memory == PixelmonItemsHeld.fairyMemory) {
         return FAIRY;
      } else if (memory == PixelmonItemsHeld.flyingMemory) {
         return FLYING;
      } else if (memory == PixelmonItemsHeld.waterMemory) {
         return WATER;
      } else if (memory == PixelmonItemsHeld.ghostMemory) {
         return GHOST;
      } else if (memory == PixelmonItemsHeld.rockMemory) {
         return ROCK;
      } else if (memory == PixelmonItemsHeld.poisonMemory) {
         return POISON;
      } else {
         return memory == PixelmonItemsHeld.electricMemory ? ELECTRIC : null;
      }
   }

   public Item getItem() {
      switch (this) {
         case FLYING:
            return PixelmonItemsHeld.flyingMemory;
         case ELECTRIC:
            return PixelmonItemsHeld.electricMemory;
         case FIGHTING:
            return PixelmonItemsHeld.fightingMemory;
         case STEEL:
            return PixelmonItemsHeld.steelMemory;
         case PSYCHIC:
            return PixelmonItemsHeld.psychicMemory;
         case DRAGON:
            return PixelmonItemsHeld.dragonMemory;
         case DARK:
            return PixelmonItemsHeld.darkMemory;
         case GROUND:
            return PixelmonItemsHeld.groundMemory;
         case FIRE:
            return PixelmonItemsHeld.fireMemory;
         case FAIRY:
            return PixelmonItemsHeld.fairyMemory;
         case ROCK:
            return PixelmonItemsHeld.rockMemory;
         case POISON:
            return PixelmonItemsHeld.poisonMemory;
         case ICE:
            return PixelmonItemsHeld.iceMemory;
         case BUG:
            return PixelmonItemsHeld.bugMemory;
         case GRASS:
            return PixelmonItemsHeld.grassMemory;
         case WATER:
            return PixelmonItemsHeld.waterMemory;
         case GHOST:
            return PixelmonItemsHeld.ghostMemory;
         default:
            return null;
      }
   }
}
