package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemFormChangeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystems;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayParticleSystem;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumKyurem;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;

public class KyuremFormChange implements IFormChange {
   public boolean isValidItem(ItemStack stack) {
      Item item = stack.func_77973_b();
      return item == PixelmonItemsHeld.dnaSplicers;
   }

   public boolean isValidPokemon(EntityPixelmon pixelmon) {
      return pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Kyurem}) && !pixelmon.isEvolving();
   }

   public boolean execute(EntityPixelmon pixelmon, ItemStack stack, EntityPlayerMP player) {
      PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
      if (pixelmon.getFormEnum() != EnumKyurem.NORMAL) {
         NBTTagCompound fusedPokemonNBT = pixelmon.getPokemonData().getPersistentData().func_74775_l("FusedPokemon");
         Pokemon fused = null;
         if (!fusedPokemonNBT.func_82582_d()) {
            try {
               fused = Pixelmon.pokemonFactory.create(fusedPokemonNBT);
            } catch (Exception var8) {
               var8.printStackTrace();
            }

            if (fused == null) {
               Pixelmon.LOGGER.error("Couldn't get fused Pokémon from NBT for player: " + player.func_70005_c_());
            }
         }

         ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, fused);
         if (Pixelmon.EVENT_BUS.post(event)) {
            return false;
         }

         if (fused != null) {
            storage.add(event.fusion);
         }

         pixelmon.getEntityData().func_82580_o("FusedPokemon");
         pixelmon.func_130014_f_().func_184148_a((EntityPlayer)null, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, SoundEvents.field_187534_aX, SoundCategory.NEUTRAL, 1.0F, 0.1F);
         Pixelmon.network.sendToDimension(new PlayParticleSystem(ParticleSystems.DISCHARGE, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, pixelmon.field_71093_bK, pixelmon.getPixelmonScale(), pixelmon.getPokemonData().isShiny(), new double[]{EnumSpecies.Zekrom.name.equals(fusedPokemonNBT.func_74779_i("Name")) ? 0.5 : 1.0, 0.5, EnumSpecies.Zekrom.name.equals(fusedPokemonNBT.func_74779_i("Name")) ? 1.0 : 0.5, 0.0}), pixelmon.field_71093_bK);
         pixelmon.getPokemonData().getMoveset().replaceMove("Fusion Bolt", new Attack("Scary Face"));
         pixelmon.getPokemonData().getMoveset().replaceMove("Fusion Flare", new Attack("Scary Face"));
         pixelmon.getPokemonData().getMoveset().replaceMove("Freeze Shock", new Attack("Glaciate"));
         pixelmon.getPokemonData().getMoveset().replaceMove("Ice Burn", new Attack("Glaciate"));
         pixelmon.getPokemonData().setForm(EnumKyurem.NORMAL);
      } else {
         Pokemon pokemon = storage.findOne((p) -> {
            return !p.isEgg() && p.isPokemon(new EnumSpecies[]{EnumSpecies.Reshiram, EnumSpecies.Zekrom});
         });
         if (pokemon == null) {
            ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.interaction.kyurem.notfound", pixelmon.func_145748_c_());
            return true;
         }

         ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, pokemon);
         if (Pixelmon.EVENT_BUS.post(event)) {
            return false;
         }

         pixelmon.func_130014_f_().func_184148_a((EntityPlayer)null, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, SoundEvents.field_187853_gC, SoundCategory.NEUTRAL, 0.3F, 0.2F);
         Pixelmon.network.sendToDimension(new PlayParticleSystem(ParticleSystems.RADIALTHUNDER, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, pixelmon.field_71093_bK, pixelmon.getPixelmonScale(), pixelmon.getPokemonData().isShiny(), new double[]{pokemon.isPokemon(new EnumSpecies[]{EnumSpecies.Zekrom}) ? 0.5 : 1.0, 0.5, pokemon.isPokemon(new EnumSpecies[]{EnumSpecies.Zekrom}) ? 1.0 : 0.5}), pixelmon.field_71093_bK);
         pokemon.ifEntityExists(EntityPixelmon::retrieve);
         storage.set((StoragePosition)pokemon.getStorageAndPosition().func_76340_b(), (Pokemon)null);
         NBTTagCompound pokenbt = new NBTTagCompound();
         pokemon.writeToNBT(pokenbt);
         pixelmon.getPokemonData().getPersistentData().func_74782_a("FusedPokemon", pokenbt);
         if (pokemon.isPokemon(new EnumSpecies[]{EnumSpecies.Zekrom})) {
            pixelmon.setForm(EnumKyurem.BLACK);
            pixelmon.getPokemonData().getMoveset().replaceMove("Scary Face", new Attack("Fusion Bolt"));
            pixelmon.getPokemonData().getMoveset().replaceMove("Glaciate", new Attack("Freeze Shock"));
         } else {
            pixelmon.setForm(EnumKyurem.WHITE);
            pixelmon.getPokemonData().getMoveset().replaceMove("Scary Face", new Attack("Fusion Flare"));
            pixelmon.getPokemonData().getMoveset().replaceMove("Glaciate", new Attack("Ice Burn"));
         }
      }

      return true;
   }
}
