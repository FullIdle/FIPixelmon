package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.DynamaxCandyEvent;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.ItemDynamaxCandy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

public class InteractionDynamaxCandy implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && entityPixelmon.func_70902_q() == player && itemstack.func_77973_b() instanceof ItemDynamaxCandy) {
         Pokemon pokemon = entityPixelmon.getPokemonData();
         if (pokemon.getDynamaxLevel() >= 10) {
            player.func_145747_a(new TextComponentTranslation("pixelmon.interaction.dynamaxcandy.fail", new Object[]{entityPixelmon.getNickname()}));
            return true;
         }

         if (!Pixelmon.EVENT_BUS.post(new DynamaxCandyEvent((EntityPlayerMP)player, entityPixelmon))) {
            pokemon.changeDynamaxLevel(1);
            player.func_145747_a(new TextComponentTranslation("pixelmon.interaction.dynamaxcandy.success", new Object[]{entityPixelmon.getNickname()}));
            if (!player.field_71075_bZ.field_75098_d) {
               player.func_184586_b(hand).func_190918_g(1);
            }

            return true;
         }
      }

      return false;
   }
}
