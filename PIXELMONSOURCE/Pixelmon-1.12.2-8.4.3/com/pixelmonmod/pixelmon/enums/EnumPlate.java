package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import net.minecraft.item.Item;

public enum EnumPlate {
   DRACO(185, 66, 79),
   DREAD(112, 140, 173),
   EARTH(179, 143, 103),
   FIST(225, 136, 109),
   FLAME(245, 86, 89),
   ICICLE(185, 229, 232),
   INSECT(201, 240, 126),
   IRON(173, 175, 177),
   MEADOW(124, 221, 91),
   MIND(253, 176, 173),
   PIXIE(245, 176, 217),
   SKY(72, 191, 247),
   SPLASH(106, 147, 252),
   SPOOKY(134, 131, 164),
   STONE(212, 192, 153),
   TOXIC(192, 132, 252),
   ZAP(251, 192, 101);

   private int r;
   private int g;
   private int b;

   private EnumPlate(int r, int g, int b) {
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

   public static EnumPlate getPlateForItem(Item plate) {
      if (plate == PixelmonItemsHeld.dracoPlate) {
         return DRACO;
      } else if (plate == PixelmonItemsHeld.dreadPlate) {
         return DREAD;
      } else if (plate == PixelmonItemsHeld.earthPlate) {
         return EARTH;
      } else if (plate == PixelmonItemsHeld.fistPlate) {
         return FIST;
      } else if (plate == PixelmonItemsHeld.flamePlate) {
         return FLAME;
      } else if (plate == PixelmonItemsHeld.iciclePlate) {
         return ICICLE;
      } else if (plate == PixelmonItemsHeld.insectPlate) {
         return INSECT;
      } else if (plate == PixelmonItemsHeld.ironPlate) {
         return IRON;
      } else if (plate == PixelmonItemsHeld.meadowPlate) {
         return MEADOW;
      } else if (plate == PixelmonItemsHeld.mindPlate) {
         return MIND;
      } else if (plate == PixelmonItemsHeld.pixiePlate) {
         return PIXIE;
      } else if (plate == PixelmonItemsHeld.skyPlate) {
         return SKY;
      } else if (plate == PixelmonItemsHeld.splashPlate) {
         return SPLASH;
      } else if (plate == PixelmonItemsHeld.spookyPlate) {
         return SPOOKY;
      } else if (plate == PixelmonItemsHeld.stonePlate) {
         return STONE;
      } else if (plate == PixelmonItemsHeld.toxicPlate) {
         return TOXIC;
      } else {
         return plate == PixelmonItemsHeld.zapPlate ? ZAP : null;
      }
   }

   public Item getItem() {
      switch (this) {
         case SKY:
            return PixelmonItemsHeld.skyPlate;
         case ZAP:
            return PixelmonItemsHeld.zapPlate;
         case FIST:
            return PixelmonItemsHeld.fistPlate;
         case IRON:
            return PixelmonItemsHeld.ironPlate;
         case MIND:
            return PixelmonItemsHeld.mindPlate;
         case DRACO:
            return PixelmonItemsHeld.dracoPlate;
         case DREAD:
            return PixelmonItemsHeld.dreadPlate;
         case EARTH:
            return PixelmonItemsHeld.earthPlate;
         case FLAME:
            return PixelmonItemsHeld.flamePlate;
         case PIXIE:
            return PixelmonItemsHeld.pixiePlate;
         case STONE:
            return PixelmonItemsHeld.stonePlate;
         case TOXIC:
            return PixelmonItemsHeld.toxicPlate;
         case ICICLE:
            return PixelmonItemsHeld.iciclePlate;
         case INSECT:
            return PixelmonItemsHeld.insectPlate;
         case MEADOW:
            return PixelmonItemsHeld.meadowPlate;
         case SPLASH:
            return PixelmonItemsHeld.splashPlate;
         case SPOOKY:
            return PixelmonItemsHeld.spookyPlate;
         default:
            return null;
      }
   }

   public static Item getItem(EnumType type) {
      switch (type) {
         case Flying:
            return PixelmonItemsHeld.skyPlate;
         case Electric:
            return PixelmonItemsHeld.zapPlate;
         case Fighting:
            return PixelmonItemsHeld.fistPlate;
         case Steel:
            return PixelmonItemsHeld.ironPlate;
         case Psychic:
            return PixelmonItemsHeld.mindPlate;
         case Dragon:
            return PixelmonItemsHeld.dracoPlate;
         case Dark:
            return PixelmonItemsHeld.dreadPlate;
         case Ground:
            return PixelmonItemsHeld.earthPlate;
         case Fire:
            return PixelmonItemsHeld.flamePlate;
         case Fairy:
            return PixelmonItemsHeld.pixiePlate;
         case Rock:
            return PixelmonItemsHeld.stonePlate;
         case Poison:
            return PixelmonItemsHeld.toxicPlate;
         case Ice:
            return PixelmonItemsHeld.iciclePlate;
         case Bug:
            return PixelmonItemsHeld.insectPlate;
         case Grass:
            return PixelmonItemsHeld.meadowPlate;
         case Water:
            return PixelmonItemsHeld.splashPlate;
         case Ghost:
            return PixelmonItemsHeld.spookyPlate;
         default:
            return null;
      }
   }
}
