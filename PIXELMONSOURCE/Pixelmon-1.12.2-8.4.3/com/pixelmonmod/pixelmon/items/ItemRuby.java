package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.LakeTrioStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;

public class ItemRuby extends PixelmonItem {
   public ItemRuby() {
      super("ruby");
      this.func_77627_a(true);
      this.func_77656_e(0);
   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (!player.field_70170_p.field_72995_K && stack.func_77960_j() == 0) {
         EntityPlayerMP playerMP = (EntityPlayerMP)player;
         if (entity instanceof EntityPixelmon) {
            EntityPixelmon pixelmon = (EntityPixelmon)entity;
            EnumSpecies lakeGuardian = pixelmon.getSpecies();
            if (!pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Azelf}) && !pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Mesprit}) && !pixelmon.isPokemon(new EnumSpecies[]{EnumSpecies.Uxie})) {
               return false;
            }

            if (pixelmon.func_152114_e(player)) {
               LakeTrioStats lakeTrioStats = (LakeTrioStats)pixelmon.getPokemonData().getExtraStats(LakeTrioStats.class);
               if (lakeTrioStats.numEnchanted < PixelmonConfig.lakeTrioMaxEnchants) {
                  if (pixelmon.getPokemonData().getFriendship() >= 255) {
                     if (pixelmon.getLvl().getLevel() >= 60) {
                        ++lakeTrioStats.numEnchanted;
                        pixelmon.getLvl().setLevel(pixelmon.getLvl().getLevel() / 2 + 10);
                        pixelmon.getPokemonData().setFriendship(200);
                        ChatHandler.sendFormattedChat(playerMP, TextFormatting.GREEN, "ruby.success", pixelmon.getNickname());
                        pixelmon.update(new EnumUpdateType[]{EnumUpdateType.Stats});
                        pixelmon.update(new EnumUpdateType[]{EnumUpdateType.Friendship});
                        if (stack.func_190916_E() > 1) {
                           ItemStack newStack = new ItemStack(PixelmonItems.ruby, 1, lakeGuardian.getNationalPokedexInteger() - 479);
                           stack.func_190918_g(1);
                           DropItemHelper.giveItemStack(playerMP, newStack, false);
                        } else {
                           stack.func_77964_b(lakeGuardian.getNationalPokedexInteger() - 479);
                        }
                     } else {
                        ChatHandler.sendFormattedChat(playerMP, TextFormatting.GRAY, "ruby.fail.level", pixelmon.getNickname());
                     }
                  } else {
                     ChatHandler.sendFormattedChat(playerMP, TextFormatting.GRAY, "ruby.fail.happiness", pixelmon.getNickname());
                  }
               } else {
                  ChatHandler.sendFormattedChat(playerMP, TextFormatting.GRAY, "ruby.fail.count", pixelmon.getNickname());
               }
            } else {
               ChatHandler.sendFormattedChat(playerMP, TextFormatting.GRAY, "ruby.fail.owner", pixelmon.getNickname());
            }
         }
      }

      return false;
   }

   public String func_77667_c(ItemStack stack) {
      if (stack.func_77952_i() == 1) {
         return "item.ruby_uxie";
      } else if (stack.func_77952_i() == 2) {
         return "item.ruby_mesprit";
      } else {
         return stack.func_77952_i() == 3 ? "item.ruby_azelf" : "item.ruby";
      }
   }

   public boolean func_77636_d(ItemStack stack) {
      return stack.func_77960_j() > 0 || super.func_77636_d(stack);
   }

   public int getItemStackLimit(ItemStack stack) {
      return stack.func_77960_j() > 0 ? 1 : 64;
   }

   public void func_150895_a(CreativeTabs tab, NonNullList items) {
      if (this.func_194125_a(tab)) {
         for(int i = 0; i < 4; ++i) {
            items.add(new ItemStack(this, 1, i));
         }
      }

   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }
}
