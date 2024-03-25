package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemInteractionEvent;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.items.ItemMint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionMint implements IInteraction {
   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (!player.field_70170_p.field_72995_K && hand != EnumHand.OFF_HAND && itemstack.func_77973_b() instanceof ItemMint) {
         Pokemon data = pixelmon.getPokemonData();
         if (data.getOwnerPlayer() != player) {
            return false;
         } else {
            EnumNature nature = ItemMint.getNature(itemstack);
            if (data.getMintNature() == nature || data.getMintNature() == null && data.getBaseNature() == nature) {
               ChatHandler.sendChat(player, "pixelmon.interaction.mint.identical", pixelmon.getNickname());
               return true;
            } else if (Pixelmon.EVENT_BUS.post(new ItemInteractionEvent(player, pixelmon, itemstack))) {
               return false;
            } else {
               data.setMintNature(nature);
               if (!player.field_71075_bZ.field_75098_d) {
                  player.func_184586_b(hand).func_190918_g(1);
               }

               ChatHandler.sendChat(player, "pixelmon.interaction.mint.changed", pixelmon.getNickname(), nature.getTranslatedName());
               return true;
            }
         }
      } else {
         return false;
      }
   }
}
