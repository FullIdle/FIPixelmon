package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumOrigin;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemOriginForm extends ItemHeld {
   protected final EnumSpecies species;

   public ItemOriginForm(EnumHeldItems heldItemType, String itemName, EnumSpecies species) {
      super(heldItemType, itemName);
      this.species = species;
      this.field_77777_bU = 16;
      this.func_77637_a(PixelmonCreativeTabs.held);
   }

   public boolean interact(EntityPixelmon pokemon, ItemStack itemstack, EntityPlayer player) {
      if (!Objects.equals(pokemon.func_184753_b(), player.func_110124_au())) {
         return false;
      } else if (!Objects.equals(this.species, pokemon.getSpecies())) {
         return false;
      } else {
         new EvolutionQuery(pokemon, pokemon.getFormEnum() == EnumOrigin.ORIGIN ? EnumOrigin.NORMAL.ordinal() : EnumOrigin.ORIGIN.ordinal());
         return true;
      }
   }
}
