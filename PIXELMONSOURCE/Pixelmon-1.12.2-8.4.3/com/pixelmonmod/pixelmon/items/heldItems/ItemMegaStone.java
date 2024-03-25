package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.ArrayList;
import net.minecraft.creativetab.CreativeTabs;

public class ItemMegaStone extends ItemHeld {
   public final EnumSpecies pokemon;
   private final int form;
   private final ArrayList allowedForms;

   public ItemMegaStone(String itemName, EnumSpecies pokemon, int... allowedForms) {
      this(itemName, 1, pokemon, allowedForms);
   }

   public ItemMegaStone(String itemName, int form, EnumSpecies pokemon, int... allowedForms) {
      super(EnumHeldItems.megaStone, itemName);
      this.allowedForms = new ArrayList();
      this.form = form;
      if (pokemon.hasMega()) {
         this.pokemon = pokemon;
      } else {
         this.pokemon = null;
         this.func_77637_a((CreativeTabs)null);
      }

      int[] var5 = allowedForms;
      int var6 = allowedForms.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         int allowedForm = var5[var7];
         this.allowedForms.add(allowedForm);
      }

   }

   public int getForm(int base) {
      return this.form;
   }

   public boolean isFormAllowed(int form) {
      return this.allowedForms.isEmpty() || this.allowedForms.contains(form);
   }
}
