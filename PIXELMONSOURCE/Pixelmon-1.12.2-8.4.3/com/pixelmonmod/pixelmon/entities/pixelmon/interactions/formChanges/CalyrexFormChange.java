package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemFormChangeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumCalyrex;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;

public class CalyrexFormChange implements IFormChange {
   private static final String[] RIDER_MOVES = new String[]{"Glacial Lance", "Astral Barrage", "Tackle", "Tail Whip", "Double Kick", "Avalanche", "Hex", "Stomp", "Torment", "Confuse Ray", "Mist", "Haze", "Icicle Crash", "Shadow Ball", "Take Down", "Iron Defense", "Agility", "Thrash", "Taunt", "Disable", "Double-Edge", "Swords Dance", "Nasty Plot"};

   public boolean isValidItem(ItemStack stack) {
      Item item = stack.func_77973_b();
      return item == PixelmonItemsHeld.reins_of_unity;
   }

   public boolean isValidPokemon(EntityPixelmon pixelmon) {
      return pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Calyrex}) && !pixelmon.isEvolving();
   }

   public boolean execute(EntityPixelmon pixelmon, ItemStack stack, EntityPlayerMP player) {
      PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
      if (pixelmon.getFormEnum() != EnumCalyrex.ORDINARY) {
         NBTTagCompound fusedPokemonNBT = pixelmon.getPokemonData().getPersistentData().func_74775_l("FusedPokemon");
         Pokemon fused = null;
         if (!fusedPokemonNBT.func_82582_d()) {
            try {
               fused = Pixelmon.pokemonFactory.create(fusedPokemonNBT);
            } catch (Exception var13) {
               var13.printStackTrace();
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
         pixelmon.func_130014_f_().func_184148_a((EntityPlayer)null, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, SoundEvents.field_187726_cu, SoundCategory.NEUTRAL, 1.0F, 1.0F);
         Moveset moveset = pixelmon.getPokemonData().getMoveset();
         String[] var9 = RIDER_MOVES;
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String move = var9[var11];
            moveset.removeAttack(move);
         }

         if (moveset.isEmpty()) {
            moveset.add(new Attack("Confusion"));
         }

         pixelmon.getPokemonData().setForm(EnumCalyrex.ORDINARY);
      } else {
         Pokemon pokemon = storage.findOne((p) -> {
            return !p.isEgg() && p.isPokemon(new EnumSpecies[]{EnumSpecies.Glastrier, EnumSpecies.Spectrier});
         });
         if (pokemon == null) {
            ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.interaction.calyrex.notfound", pixelmon.func_145748_c_());
            return true;
         }

         ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, pokemon);
         if (Pixelmon.EVENT_BUS.post(event)) {
            return false;
         }

         pixelmon.func_130014_f_().func_184148_a((EntityPlayer)null, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, SoundEvents.field_187699_cl, SoundCategory.NEUTRAL, 1.0F, 1.0F);
         pokemon.ifEntityExists(EntityPixelmon::retrieve);
         storage.set((StoragePosition)pokemon.getStorageAndPosition().func_76340_b(), (Pokemon)null);
         NBTTagCompound pokenbt = new NBTTagCompound();
         pokemon.writeToNBT(pokenbt);
         pixelmon.getPokemonData().getPersistentData().func_74782_a("FusedPokemon", pokenbt);
         pixelmon.setForm(pokemon.getSpecies() == EnumSpecies.Glastrier ? EnumCalyrex.ICERIDER : EnumCalyrex.SHADOWRIDER);
         Attack specialMove = pokemon.getSpecies() == EnumSpecies.Glastrier ? new Attack("Glacial Lance") : new Attack("Astral Barrage");
         if (!pixelmon.getPokemonData().getMoveset().add(specialMove)) {
            LearnMoveController.sendLearnMove(player, pixelmon.getPokemonData().getUUID(), specialMove.getActualMove());
         }
      }

      return true;
   }
}
