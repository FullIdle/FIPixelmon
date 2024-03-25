package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemFormChangeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumTherian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class ForcesOfNatureFormChange implements IFormChange {
   public boolean isValidItem(ItemStack stack) {
      Item item = stack.func_77973_b();
      return item == PixelmonItems.reveal_glass;
   }

   public boolean isValidPokemon(EntityPixelmon pixelmon) {
      return pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Landorus, EnumSpecies.Thundurus, EnumSpecies.Tornadus, EnumSpecies.Enamorus}) && !pixelmon.isEvolving();
   }

   public boolean execute(EntityPixelmon pixelmon, ItemStack stack, EntityPlayerMP player) {
      ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, (Pokemon)null);
      if (Pixelmon.EVENT_BUS.post(event)) {
         return false;
      } else {
         if (pixelmon.getPokemonData().getForm() == EnumTherian.THERIAN.getForm()) {
            pixelmon.setForm(EnumTherian.INCARNATE);
         } else {
            pixelmon.setForm(EnumTherian.THERIAN);
         }

         pixelmon.func_130014_f_().func_184133_a((EntityPlayer)null, pixelmon.func_180425_c(), SoundEvents.field_187754_de, SoundCategory.PLAYERS, 1.0F, 1.0F);
         ChatHandler.sendChat(player, "pixelmon.abilities.changeform", pixelmon.getNickname());
         return true;
      }
   }
}
