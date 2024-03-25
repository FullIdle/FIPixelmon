package com.pixelmonmod.tcg.api.card;

import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.tcg.gui.TCGResources;
import com.pixelmonmod.tcg.helper.EssenceHelper;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.EssenceSyncPacket;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

public enum Energy {
   GRASS("grass", TCGResources.ENERGY_GRASS, TCGResources.ENERGY_GRASS_HD, TCGResources.ENERGY_GRASS_HDA),
   FIRE("fire", TCGResources.ENERGY_FIRE, TCGResources.ENERGY_FIRE_HD, TCGResources.ENERGY_FIRE_HDA),
   WATER("water", TCGResources.ENERGY_WATER, TCGResources.ENERGY_WATER_HD, TCGResources.ENERGY_WATER_HDA),
   LIGHTNING("lightning", TCGResources.ENERGY_LIGHTNING, TCGResources.ENERGY_LIGHTNING_HD, TCGResources.ENERGY_LIGHTNING_HDA),
   FIGHTING("fighting", TCGResources.ENERGY_FIGHTING, TCGResources.ENERGY_FIGHTING_HD, TCGResources.ENERGY_FIGHTING_HDA),
   PSYCHIC("psychic", TCGResources.ENERGY_PSYCHIC, TCGResources.ENERGY_PSYCHIC_HD, TCGResources.ENERGY_PSYCHIC_HDA),
   COLORLESS("colorless", TCGResources.ENERGY_COLORLESS, TCGResources.ENERGY_COLORLESS_HD, TCGResources.ENERGY_COLORLESS_HDA),
   DARKNESS("darkness", TCGResources.ENERGY_DARKNESS, TCGResources.ENERGY_DARKNESS_HD, TCGResources.ENERGY_DARKNESS_HDA),
   METAL("metal", TCGResources.ENERGY_METAL, TCGResources.ENERGY_METAL_HD, TCGResources.ENERGY_METAL_HDA),
   DRAGON("dragon", TCGResources.ENERGY_DRAGON, TCGResources.ENERGY_DRAGON_HD, TCGResources.ENERGY_DRAGON_HDA),
   FAIRY("fairy", TCGResources.ENERGY_FAIRY, TCGResources.ENERGY_FAIRY_HD, TCGResources.ENERGY_FAIRY_HDA),
   RAINBOW("rainbow", TCGResources.ENERGY_RAINBOW, TCGResources.ENERGY_RAINBOW_HD, TCGResources.ENERGY_RAINBOW_HDA);

   private final String unlocalizedName;
   private final ResourceLocation resourceLocation;
   private final ResourceLocation highRes;
   private final ResourceLocation highResAlt;

   private Energy(String unlocalizedName, ResourceLocation resourceLocation, ResourceLocation highRes, ResourceLocation highResAlt) {
      this.unlocalizedName = unlocalizedName;
      this.resourceLocation = resourceLocation;
      this.highRes = highRes;
      this.highResAlt = highResAlt;
   }

   public static Energy getEnergyFromString(String name) {
      Energy[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Energy value = var1[var3];
         if (value.getUnlocalizedName().equalsIgnoreCase(name)) {
            return value;
         }
      }

      return null;
   }

   public static void givePlayerEssence(ICommandSender sender, EntityPlayer receiver, String energy, int amount) {
      Energy e = getEnergyFromString(energy);
      if (receiver != null) {
         if (e == null) {
            if (sender != null) {
               sender.func_145747_a(new TextComponentString("Cannot find energy with ID " + energy));
            }

         } else {
            int c = receiver.getEntityData().func_74762_e("EnergyEssence" + StringUtils.capitalize(e.getUnlocalizedName()));
            receiver.getEntityData().func_74768_a("EnergyEssence" + StringUtils.capitalize(e.getUnlocalizedName()), c + amount);
            PacketHandler.net.sendTo(new EssenceSyncPacket((EntityPlayerMP)receiver), (EntityPlayerMP)receiver);
            receiver.func_145747_a(new TextComponentString(amount + " " + TextFormatting.BOLD + StringUtils.capitalize(energy) + TextFormatting.RESET + " essence was added to your inventory!"));
         }
      }
   }

   public static void copyAllEssence(EntityPlayer source, EntityPlayer target) {
      if (source != null && target != null) {
         Energy[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Energy e = var2[var4];
            if (e != null) {
               int sourceEssence = source.getEntityData().func_74762_e("EnergyEssence" + StringUtils.capitalize(e.getUnlocalizedName()));
               if (sourceEssence != 0) {
                  target.getEntityData().func_74768_a("EnergyEssence" + StringUtils.capitalize(e.getUnlocalizedName()), sourceEssence);
               }
            }
         }

         PacketHandler.net.sendTo(new EssenceSyncPacket((EntityPlayerMP)target), (EntityPlayerMP)target);
      }
   }

   public static void resetEssence(EntityPlayer player) {
      if (player != null) {
         Energy[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Energy e = var1[var3];
            if (e != null) {
               int source_essence = EssenceHelper.getPlayerEssenceFromEnergy(player, e);
               if (source_essence != 0) {
                  EssenceHelper.clearEssenceFromEnergy(player, e);
               }
            }
         }

         PacketHandler.net.sendTo(new EssenceSyncPacket((EntityPlayerMP)player), (EntityPlayerMP)player);
      }
   }

   public static void givePlayerEssence(EntityPlayer receiver, String energy, int amount) {
      givePlayerEssence((ICommandSender)null, receiver, energy, amount);
   }

   public static Energy getEnergyFromType(EnumType type) {
      switch (type) {
         case Normal:
         case Flying:
            return COLORLESS;
         case Fire:
            return FIRE;
         case Water:
         case Ice:
            return WATER;
         case Ground:
         case Rock:
         case Fighting:
            return FIGHTING;
         case Ghost:
         case Poison:
         case Psychic:
            return PSYCHIC;
         case Bug:
         case Grass:
            return GRASS;
         case Dark:
            return DARKNESS;
         case Steel:
            return METAL;
         case Dragon:
            return DRAGON;
         case Fairy:
            return FAIRY;
         case Electric:
            return LIGHTNING;
         default:
            return COLORLESS;
      }
   }

   public ResourceLocation getTexture() {
      return this.resourceLocation;
   }

   public ResourceLocation getHighResTexture() {
      return this.highRes;
   }

   public ResourceLocation getHighResAlt() {
      return this.highResAlt;
   }

   public String getUnlocalizedName() {
      return this.unlocalizedName;
   }
}
