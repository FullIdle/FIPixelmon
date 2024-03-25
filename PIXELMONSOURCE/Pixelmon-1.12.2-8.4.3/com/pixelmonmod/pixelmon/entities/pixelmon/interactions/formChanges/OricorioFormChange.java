package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemFormChangeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumOricorio;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

public class OricorioFormChange implements IFormChange {
   public boolean isValidItem(ItemStack stack) {
      return true;
   }

   public boolean isValidPokemon(EntityPixelmon pixelmon) {
      return pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Oricorio}) && !pixelmon.isEvolving();
   }

   public boolean execute(EntityPixelmon pixelmon, ItemStack stack, EntityPlayerMP player) {
      Item item = stack.func_77973_b();
      Pokemon pokemon = pixelmon.getPokemonData();
      EnumOricorio toForm = null;
      if (item instanceof ItemMultiTexture) {
         Block block = ((ItemMultiTexture)item).func_179223_d();
         int meta = stack.func_77960_j();
         if (block.getRegistryName().toString().equals("minecraft:red_flower")) {
            if (meta == 0) {
               toForm = EnumOricorio.BAILE;
            } else if (meta == 2) {
               toForm = EnumOricorio.SENSU;
            } else if (meta == 7) {
               toForm = EnumOricorio.PAU;
            }
         } else if (block.getRegistryName().toString().equals("minecraft:yellow_flower")) {
            toForm = EnumOricorio.POMPOM;
         }

         if (toForm != null && pokemon.getFormEnum() != toForm) {
            ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, (Pokemon)null);
            if (Pixelmon.EVENT_BUS.post(event)) {
               return false;
            }

            pokemon.setForm(toForm);
            if (!player.func_184812_l_()) {
               stack.func_190918_g(1);
            }

            ChatHandler.sendChat(player, "pixelmon.abilities.changeform", pokemon.getDisplayName());
            return true;
         }
      }

      return false;
   }
}
