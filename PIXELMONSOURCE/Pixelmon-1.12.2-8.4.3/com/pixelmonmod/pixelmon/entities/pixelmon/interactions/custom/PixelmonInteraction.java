package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.custom;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity4Interactions;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityBreeding;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.EnumMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public abstract class PixelmonInteraction {
   public int maxInteractions;
   public int counter = 400;
   private static EnumMap pixelmonInteractions = Maps.newEnumMap(EnumSpecies.class);

   public PixelmonInteraction(int maxInteractions) {
      this.maxInteractions = maxInteractions;
   }

   public boolean processInteract(Entity1Base pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (pixelmon instanceof Entity4Interactions) {
         Entity4Interactions poke = (Entity4Interactions)pixelmon;
         poke.setNumInteractions(poke.getNumInteractions() - 1);
      } else if (pixelmon instanceof EntityBreeding) {
         EntityBreeding poke = (EntityBreeding)pixelmon;
         poke.setNumInteractions(poke.getNumInteractions() - 1);
      }

      if (this.counter <= 0) {
         this.resetCounter(pixelmon);
      }

      return true;
   }

   public void resetCounter(Entity1Base pixelmon) {
      if (!pixelmon.field_70170_p.field_72995_K) {
         this.counter = pixelmon.func_70681_au().nextInt(600) + 800;
      }

   }

   public static PixelmonInteraction getInteraction(EnumSpecies species) {
      if (pixelmonInteractions.containsKey(species)) {
         Class c = (Class)pixelmonInteractions.get(species);

         try {
            return (PixelmonInteraction)c.getConstructor().newInstance();
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return null;
   }

   static {
      pixelmonInteractions.put(EnumSpecies.Miltank, MiltankInteraction.class);
      pixelmonInteractions.put(EnumSpecies.Camerupt, CameruptInteraction.class);
      pixelmonInteractions.put(EnumSpecies.Mareep, ShearInteraction.class);
      pixelmonInteractions.put(EnumSpecies.Wooloo, ShearInteraction.class);
      pixelmonInteractions.put(EnumSpecies.Dubwool, ShearInteraction.class);
   }
}
