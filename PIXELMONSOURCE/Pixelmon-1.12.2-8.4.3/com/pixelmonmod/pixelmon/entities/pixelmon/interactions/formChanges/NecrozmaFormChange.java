package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemFormChangeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystems;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayParticleSystem;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumMega;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;

public class NecrozmaFormChange implements IFormChange {
   public boolean isValidItem(ItemStack stack) {
      Item item = stack.func_77973_b();
      return item == PixelmonItemsHeld.n_lunarizer || item == PixelmonItemsHeld.n_solarizer;
   }

   public boolean isValidPokemon(EntityPixelmon pixelmon) {
      return pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Necrozma}) && !pixelmon.isEvolving();
   }

   public boolean execute(EntityPixelmon pixelmon, ItemStack stack, EntityPlayerMP player) {
      Item item = stack.func_77973_b();
      Pokemon pokemon = pixelmon.getPokemonData();
      EnumSpecies partner = item == PixelmonItemsHeld.n_lunarizer ? EnumSpecies.Lunala : EnumSpecies.Solgaleo;
      EnumNecrozma toForm = partner == EnumSpecies.Lunala ? EnumNecrozma.DAWN : EnumNecrozma.DUSK;
      PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.func_110124_au());
      if (pokemon.getFormEnum() == toForm) {
         NBTTagCompound fusedPokemonNBT = pokemon.getPersistentData().func_74775_l("FusedPokemon");
         Pokemon fused = null;
         if (!fusedPokemonNBT.func_82582_d()) {
            try {
               fused = Pixelmon.pokemonFactory.create(fusedPokemonNBT);
            } catch (Exception var12) {
               var12.printStackTrace();
            }

            if (fused == null) {
               Pixelmon.LOGGER.error("Couldn't get fused Pokémon from NBT for player: " + player.func_70005_c_());
            }
         }

         ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, fused);
         if (Pixelmon.EVENT_BUS.post(event)) {
            return false;
         }

         pixelmon.func_130014_f_().func_184148_a((EntityPlayer)null, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, SoundEvents.field_187534_aX, SoundCategory.NEUTRAL, 1.0F, 0.1F);
         Pixelmon.network.sendToDimension(new PlayParticleSystem(ParticleSystems.DISCHARGE, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, pixelmon.field_71093_bK, pixelmon.getPixelmonScale(), pixelmon.getPokemonData().isShiny(), new double[]{partner == EnumSpecies.Solgaleo ? 1.0 : 0.5, 0.5, partner != EnumSpecies.Solgaleo ? 1.0 : 0.5, 0.0}), player.field_71093_bK);
         pokemon.setForm(EnumMega.Normal);
         if ((pokemon.getMoveset().removeAttack("Sunsteel Strike") || pokemon.getMoveset().removeAttack("Moongeist Beam")) && pokemon.getMoveset().size() == 0) {
            pokemon.getMoveset().add(new Attack("Confusion"));
         }

         if (fused != null) {
            storage.add(event.fusion);
         }

         pixelmon.getEntityData().func_82580_o("FusedPokemon");
      } else if (pokemon.getFormEnum() == EnumNecrozma.NORMAL || pokemon.getFormEnum() == EnumMega.Normal) {
         Pokemon toFuse = storage.findOne((pk) -> {
            return pk.getSpecies() == partner;
         });
         if (toFuse != null) {
            ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, toFuse);
            if (Pixelmon.EVENT_BUS.post(event)) {
               return false;
            }

            toFuse.ifEntityExists(EntityPixelmon::retrieve);
            pixelmon.func_130014_f_().func_184148_a((EntityPlayer)null, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, SoundEvents.field_187853_gC, SoundCategory.NEUTRAL, 0.3F, 0.2F);
            Pixelmon.network.sendToDimension(new PlayParticleSystem(ParticleSystems.RADIALTHUNDER, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, pixelmon.field_71093_bK, pixelmon.getPixelmonScale(), pixelmon.getPokemonData().isShiny(), new double[]{partner == EnumSpecies.Solgaleo ? 1.0 : 0.5, 0.5, partner != EnumSpecies.Solgaleo ? 1.0 : 0.5}), pixelmon.field_71093_bK);
            storage.set(toFuse.getPosition(), (Pokemon)null);
            if (pokemon.getPersistentData().func_74764_b("FusedPokemon")) {
               storage.add(Pixelmon.pokemonFactory.create(pokemon.getPersistentData().func_74775_l("FusedPokemon")));
            }

            pokemon.getPersistentData().func_74782_a("FusedPokemon", toFuse.writeToNBT(new NBTTagCompound()));
            pokemon.setForm(toForm);
            Attack specialMove = partner == EnumSpecies.Solgaleo ? new Attack("Sunsteel Strike") : new Attack("Moongeist Beam");
            if (!pokemon.getMoveset().add(specialMove)) {
               LearnMoveController.sendLearnMove(player, pokemon.getUUID(), specialMove.getActualMove());
            }
         }
      }

      return true;
   }
}
