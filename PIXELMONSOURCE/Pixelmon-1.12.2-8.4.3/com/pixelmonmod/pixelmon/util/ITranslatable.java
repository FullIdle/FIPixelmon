package com.pixelmonmod.pixelmon.util;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public interface ITranslatable {
   String getUnlocalizedName();

   default String getLocalizedName() {
      return I18n.func_74838_a(this.getUnlocalizedName());
   }

   default TextComponentTranslation getTranslatedName() {
      return new TextComponentTranslation(this.getUnlocalizedName(), new Object[0]);
   }
}
