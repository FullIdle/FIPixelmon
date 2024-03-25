package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import javax.annotation.Nonnull;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;

public enum EnumBike implements ITranslatable {
   Mach(EnumDyeColor.BLUE),
   Acro(EnumDyeColor.RED);

   public EnumDyeColor defaultColor;

   private EnumBike(EnumDyeColor color) {
      this.defaultColor = color;
   }

   @Nonnull
   public Item getItem() {
      switch (this) {
         case Mach:
            return PixelmonItems.machBike;
         case Acro:
            return PixelmonItems.acroBike;
         default:
            return null;
      }
   }

   public String getUnlocalizedName() {
      return this == Mach ? "item.mach_bike.name" : "item.acro_bike.name";
   }
}
