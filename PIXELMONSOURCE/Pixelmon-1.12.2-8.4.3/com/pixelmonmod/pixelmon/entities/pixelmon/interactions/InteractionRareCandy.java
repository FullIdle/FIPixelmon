package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.RareCandyEvent;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemExpCandy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

public class InteractionRareCandy implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && entityPixelmon.func_70902_q() == player && itemstack.func_77973_b() instanceof ItemExpCandy) {
         Pokemon pokemon = entityPixelmon.getPokemonData();
         if (!pokemon.doesLevel()) {
            player.func_145747_a(new TextComponentTranslation("pixelmon.interaction.rarecandy", new Object[]{entityPixelmon.getNickname()}));
            return true;
         } else if (pokemon.getLevel() < PixelmonServerConfig.maxLevel && !Pixelmon.EVENT_BUS.post(new RareCandyEvent((EntityPlayerMP)player, entityPixelmon, itemstack, (ItemExpCandy)itemstack.func_77973_b()))) {
            ItemExpCandy candy = (ItemExpCandy)itemstack.func_77973_b();
            int expAward = candy.getAmount();
            if (expAward < 0) {
               while(expAward < 0) {
                  pokemon.getLevelContainer().awardEXP(pokemon.getExperienceToLevelUp(), candy.getGainType());
                  ++expAward;
               }
            } else {
               pokemon.getLevelContainer().awardEXP(expAward, candy.getGainType());
            }

            if (!player.field_71075_bZ.field_75098_d) {
               player.func_184586_b(hand).func_190918_g(1);
            }

            return true;
         } else {
            EnumSpecies old = pokemon.getSpecies();
            pokemon.tryEvolution();
            if (pokemon.getSpecies() != old && !player.field_71075_bZ.field_75098_d) {
               player.func_184586_b(hand).func_190918_g(1);
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
