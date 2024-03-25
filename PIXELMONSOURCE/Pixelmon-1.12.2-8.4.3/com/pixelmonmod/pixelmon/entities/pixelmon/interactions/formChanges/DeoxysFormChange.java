package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemFormChangeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumDeoxys;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DeoxysFormChange implements IFormChange {
   public boolean isValidItem(ItemStack stack) {
      Item item = stack.func_77973_b();
      return item == PixelmonItemsHeld.meteorite;
   }

   public boolean isValidPokemon(EntityPixelmon pixelmon) {
      return pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Deoxys}) && !pixelmon.isEvolving();
   }

   public boolean execute(EntityPixelmon pixelmon, ItemStack stack, EntityPlayerMP player) {
      ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, (Pokemon)null);
      if (Pixelmon.EVENT_BUS.post(event)) {
         return false;
      } else {
         int currentForm = pixelmon.getPokemonData().getForm();
         pixelmon.setForm(EnumDeoxys.getNextForm(currentForm, pixelmon.getPokemonData()));
         ChatHandler.sendChat(player, "pixelmon.abilities.changeform", pixelmon.getNickname());
         return true;
      }
   }
}
