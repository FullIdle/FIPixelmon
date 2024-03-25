package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.MaxSoupEvent;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGigantamaxPokemon;
import com.pixelmonmod.pixelmon.items.ItemMaxSoup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

public class InteractionMaxSoup implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && entityPixelmon.func_70902_q() == player && itemstack.func_77973_b() instanceof ItemMaxSoup) {
         Pokemon pokemon = entityPixelmon.getPokemonData();
         if (pokemon.hasGigantamaxFactor()) {
            player.func_145747_a(new TextComponentTranslation("pixelmon.interaction.maxsoup.alreadyhas", new Object[]{entityPixelmon.getNickname()}));
         } else if (EnumGigantamaxPokemon.hasGigantamaxForm(pokemon, true)) {
            if (!Pixelmon.EVENT_BUS.post(new MaxSoupEvent((EntityPlayerMP)player, entityPixelmon))) {
               pokemon.setGigantamaxFactor(true);
               player.func_145747_a(new TextComponentTranslation("pixelmon.interaction.maxsoup.success", new Object[]{entityPixelmon.getNickname()}));
               if (!player.field_71075_bZ.field_75098_d) {
                  player.func_184586_b(hand).func_190918_g(1);
                  ItemStack bowl = new ItemStack(Items.field_151054_z);
                  if (player.func_184586_b(hand).func_190926_b()) {
                     player.func_184611_a(hand, bowl);
                  } else if (!player.func_191521_c(bowl)) {
                     player.func_71019_a(bowl, true);
                  }
               }
            }
         } else {
            player.func_145747_a(new TextComponentTranslation("pixelmon.interaction.maxsoup.fail", new Object[]{entityPixelmon.getNickname()}));
         }

         return true;
      } else {
         return false;
      }
   }
}
