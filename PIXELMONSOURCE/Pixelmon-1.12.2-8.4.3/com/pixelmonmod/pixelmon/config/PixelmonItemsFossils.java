package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.enums.items.EnumFossils;
import com.pixelmonmod.pixelmon.items.ItemCoveredFossil;
import com.pixelmonmod.pixelmon.items.ItemFossil;
import com.pixelmonmod.pixelmon.items.PixelmonItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PixelmonItemsFossils {
   public static Item helixFossil;
   public static Item domeFossil;
   public static Item oldAmber;
   public static Item rootFossil;
   public static Item clawFossil;
   public static Item skullFossil;
   public static Item armorFossil;
   public static Item coverFossil;
   public static Item plumeFossil;
   public static Item jawFossil;
   public static Item sailFossil;
   public static Item birdFossil;
   public static Item dinoFossil;
   public static Item drakeFossil;
   public static Item fishFossil;
   public static Item helixFossilCovered;
   public static Item domeFossilCovered;
   public static Item oldAmberCovered;
   public static Item rootFossilCovered;
   public static Item clawFossilCovered;
   public static Item skullFossilCovered;
   public static Item armorFossilCovered;
   public static Item coverFossilCovered;
   public static Item plumeFossilCovered;
   public static Item jawFossilCovered;
   public static Item sailFossilCovered;
   public static Item birdFossilCovered;
   public static Item dinoFossilCovered;
   public static Item drakeFossilCovered;
   public static Item fishFossilCovered;
   public static Item fossilMachineTank;
   public static Item fossilMachineDisplay;
   public static Item fossilMachineTop;
   public static Item fossilMachineBase;

   public static void load() {
      helixFossil = new ItemFossil(EnumFossils.HELIX);
      domeFossil = new ItemFossil(EnumFossils.DOME);
      oldAmber = new ItemFossil(EnumFossils.OLD_AMBER);
      rootFossil = new ItemFossil(EnumFossils.ROOT);
      clawFossil = new ItemFossil(EnumFossils.CLAW);
      skullFossil = new ItemFossil(EnumFossils.SKULL);
      armorFossil = new ItemFossil(EnumFossils.ARMOR);
      coverFossil = new ItemFossil(EnumFossils.COVER);
      plumeFossil = new ItemFossil(EnumFossils.PLUME);
      jawFossil = new ItemFossil(EnumFossils.JAW);
      sailFossil = new ItemFossil(EnumFossils.SAIL);
      birdFossil = new ItemFossil(EnumFossils.BIRD);
      dinoFossil = new ItemFossil(EnumFossils.DINO);
      drakeFossil = new ItemFossil(EnumFossils.DRAKE);
      fishFossil = new ItemFossil(EnumFossils.FISH);
      fossilMachineTank = (new PixelmonItem("fossil_machine_tank")).func_77637_a(CreativeTabs.field_78026_f);
      fossilMachineDisplay = (new PixelmonItem("fossil_machine_display")).func_77637_a(CreativeTabs.field_78026_f);
      fossilMachineTop = (new PixelmonItem("fossil_machine_top")).func_77637_a(CreativeTabs.field_78026_f);
      fossilMachineBase = (new PixelmonItem("fossil_machine_base")).func_77637_a(CreativeTabs.field_78026_f);
      helixFossilCovered = new ItemCoveredFossil((ItemFossil)helixFossil, EnumFossils.HELIX);
      domeFossilCovered = new ItemCoveredFossil((ItemFossil)domeFossil, EnumFossils.DOME);
      oldAmberCovered = new ItemCoveredFossil((ItemFossil)oldAmber, EnumFossils.OLD_AMBER);
      rootFossilCovered = new ItemCoveredFossil((ItemFossil)rootFossil, EnumFossils.ROOT);
      clawFossilCovered = new ItemCoveredFossil((ItemFossil)clawFossil, EnumFossils.CLAW);
      skullFossilCovered = new ItemCoveredFossil((ItemFossil)skullFossil, EnumFossils.SKULL);
      armorFossilCovered = new ItemCoveredFossil((ItemFossil)armorFossil, EnumFossils.ARMOR);
      coverFossilCovered = new ItemCoveredFossil((ItemFossil)coverFossil, EnumFossils.COVER);
      plumeFossilCovered = new ItemCoveredFossil((ItemFossil)plumeFossil, EnumFossils.PLUME);
      jawFossilCovered = new ItemCoveredFossil((ItemFossil)jawFossil, EnumFossils.JAW);
      sailFossilCovered = new ItemCoveredFossil((ItemFossil)sailFossil, EnumFossils.SAIL);
      birdFossilCovered = new ItemCoveredFossil((ItemFossil)birdFossil, EnumFossils.BIRD);
      dinoFossilCovered = new ItemCoveredFossil((ItemFossil)dinoFossil, EnumFossils.DINO);
      drakeFossilCovered = new ItemCoveredFossil((ItemFossil)drakeFossil, EnumFossils.DRAKE);
      fishFossilCovered = new ItemCoveredFossil((ItemFossil)fishFossil, EnumFossils.FISH);
   }

   public static ItemStack getRandomFossil() {
      List enabledFossils = new ArrayList(ItemCoveredFossil.fossilList.size());
      Iterator var1 = ItemCoveredFossil.fossilList.iterator();

      while(var1.hasNext()) {
         ItemCoveredFossil fossil = (ItemCoveredFossil)var1.next();
         if (PixelmonConfig.isGenerationEnabled(fossil.getGeneration())) {
            enabledFossils.add(fossil);
         }
      }

      if (enabledFossils.isEmpty()) {
         return new ItemStack(Blocks.field_150347_e);
      } else {
         return new ItemStack((Item)RandomHelper.getRandomElementFromList(enabledFossils));
      }
   }

   public static ItemStack getCleanFromEnum(EnumFossils prevFossil) {
      switch (prevFossil) {
         case HELIX:
            return new ItemStack(helixFossil);
         case DOME:
            return new ItemStack(domeFossil);
         case OLD_AMBER:
            return new ItemStack(oldAmber);
         case ROOT:
            return new ItemStack(rootFossil);
         case CLAW:
            return new ItemStack(clawFossil);
         case SKULL:
            return new ItemStack(skullFossil);
         case ARMOR:
            return new ItemStack(armorFossil);
         case COVER:
            return new ItemStack(coverFossil);
         case PLUME:
            return new ItemStack(plumeFossil);
         case JAW:
            return new ItemStack(jawFossil);
         case SAIL:
            return new ItemStack(sailFossil);
         case BIRD:
            return new ItemStack(birdFossil);
         case DINO:
            return new ItemStack(dinoFossil);
         case DRAKE:
            return new ItemStack(drakeFossil);
         case FISH:
            return new ItemStack(fishFossil);
         default:
            return ItemStack.field_190927_a;
      }
   }
}
