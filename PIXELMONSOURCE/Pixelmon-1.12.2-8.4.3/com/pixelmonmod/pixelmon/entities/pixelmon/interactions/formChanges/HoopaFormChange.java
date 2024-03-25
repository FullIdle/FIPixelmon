package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemFormChangeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystems;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayParticleSystem;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumHoopa;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HoopaFormChange implements IFormChange {
   public boolean isValidItem(ItemStack stack) {
      Item item = stack.func_77973_b();
      return item == PixelmonItemsHeld.prison_bottle;
   }

   public boolean isValidPokemon(EntityPixelmon pixelmon) {
      return pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Hoopa}) && !pixelmon.isEvolving();
   }

   public boolean execute(EntityPixelmon pixelmon, ItemStack stack, EntityPlayerMP player) {
      if (pixelmon.getPokemonData().getForm() != 1) {
         ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, (Pokemon)null);
         if (Pixelmon.EVENT_BUS.post(event)) {
            return false;
         } else {
            Pixelmon.network.sendToDimension(new PlayParticleSystem(ParticleSystems.SLINGRING, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, pixelmon.field_71093_bK, pixelmon.getPixelmonScale(), pixelmon.getPokemonData().isShiny(), new double[0]), pixelmon.field_71093_bK);
            pixelmon.setForm(EnumHoopa.UNBOUND);
            pixelmon.getEntityData().func_74772_a("unboundTime", System.currentTimeMillis());
            ChatHandler.sendChat(player, "pixelmon.abilities.changeform", pixelmon.getNickname());
            return true;
         }
      } else {
         return false;
      }
   }
}
