package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionRainbowWing implements IInteraction {
   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && pixelmon.func_70902_q() == player) {
         Item item = itemstack.func_77973_b();
         Pokemon pokemon = pixelmon.getPokemonData();
         if (item == PixelmonItems.rainbowWing && pokemon.getFormEnum() != EnumSpecial.Rainbow) {
            if (pokemon.getSpecies().is(EnumSpecies.Dustox) && pokemon.getGender() == Gender.Male) {
               pokemon.setForm(EnumSpecial.Rainbow);
            } else if (pokemon.getSpecies().is(EnumSpecies.Beautifly) && pokemon.getGender() == Gender.Female) {
               pokemon.setForm(EnumSpecial.Rainbow);
            } else {
               if (!pokemon.getSpecies().getPossibleForms(false).contains(EnumSpecial.Rainbow)) {
                  return false;
               }

               pokemon.setForm(EnumSpecial.Rainbow);
            }

            if (!player.field_71075_bZ.field_75098_d) {
               player.func_184586_b(hand).func_190918_g(1);
            }

            return true;
         }
      }

      return false;
   }
}
