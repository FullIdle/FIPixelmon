package com.pixelmonmod.tcg.helper;

import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.tcg.api.card.Energy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

public class EssenceHelper {
   public static void givePlayerEssenceFromType(EntityPlayer player, EnumType type, int essenceToGive) {
      player.getEntityData().func_74768_a("EnergyEssence" + StringUtils.capitalize(Energy.getEnergyFromType(type).getUnlocalizedName()), getPlayerEssenceFromType(player, type) + essenceToGive);
      player.func_145747_a(new TextComponentString(essenceToGive + " " + TextFormatting.BOLD + StringUtils.capitalize(Energy.getEnergyFromType(type).getUnlocalizedName()) + TextFormatting.RESET + " essence was added to your inventory!"));
   }

   public static void givePlayerEssenceFromEnergy(EntityPlayer player, Energy energy, int essenceToGive) {
      player.getEntityData().func_74768_a("EnergyEssence" + StringUtils.capitalize(energy.getUnlocalizedName()), getPlayerEssenceFromEnergy(player, energy) + essenceToGive);
      player.func_145747_a(new TextComponentString(essenceToGive + " " + TextFormatting.BOLD + StringUtils.capitalize(energy.getUnlocalizedName()) + TextFormatting.RESET + " essence was added to your inventory!"));
   }

   public static void setPlayerEssenceFromType(EntityPlayer player, EnumType type, int essence) {
      player.getEntityData().func_74768_a("EnergyEssence" + StringUtils.capitalize(Energy.getEnergyFromType(type).getUnlocalizedName()), essence);
   }

   public static void setPlayerEssenceFromEnergy(EntityPlayer player, Energy energy, int essence) {
      player.getEntityData().func_74768_a("EnergyEssence" + StringUtils.capitalize(energy.getUnlocalizedName()), essence);
   }

   public static void clearEssenceFromEnergy(EntityPlayer player, Energy energy) {
      setPlayerEssenceFromEnergy(player, energy, 0);
   }

   public static void clearEssenceFromType(EntityPlayer player, EnumType type) {
      setPlayerEssenceFromType(player, type, 0);
   }

   public static int getPlayerEssenceFromEnergy(EntityPlayer player, Energy energy) {
      return player.getEntityData().func_74762_e("EnergyEssence" + StringUtils.capitalize(energy.getUnlocalizedName()));
   }

   public static int getPlayerEssenceFromType(EntityPlayer player, EnumType type) {
      return player.getEntityData().func_74762_e("EnergyEssence" + StringUtils.capitalize(Energy.getEnergyFromType(type).getUnlocalizedName()));
   }
}
