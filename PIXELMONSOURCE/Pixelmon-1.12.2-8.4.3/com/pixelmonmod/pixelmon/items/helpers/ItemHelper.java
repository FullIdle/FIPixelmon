package com.pixelmonmod.pixelmon.items.helpers;

import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.enums.EnumPlate;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;

public class ItemHelper {
   public static Item getResistanceBerry(EnumType type) {
      switch (type) {
         case Bug:
            return PixelmonItemsHeld.tangaBerry;
         case Ice:
            return PixelmonItemsHeld.yacheBerry;
         case Fire:
            return PixelmonItemsHeld.occaBerry;
         case Water:
            return PixelmonItemsHeld.passhoBerry;
         case Electric:
            return PixelmonItemsHeld.wacanBerry;
         case Grass:
            return PixelmonItemsHeld.rindoBerry;
         case Fighting:
            return PixelmonItemsHeld.chopleBerry;
         case Poison:
            return PixelmonItemsHeld.kebiaBerry;
         case Ground:
            return PixelmonItemsHeld.shucaBerry;
         case Flying:
            return PixelmonItemsHeld.cobaBerry;
         case Psychic:
            return PixelmonItemsHeld.payapaBerry;
         case Rock:
            return PixelmonItemsHeld.chartiBerry;
         case Ghost:
            return PixelmonItemsHeld.kasibBerry;
         case Dragon:
            return PixelmonItemsHeld.habanBerry;
         case Dark:
            return PixelmonItemsHeld.colburBerry;
         case Steel:
            return PixelmonItemsHeld.babiriBerry;
         case Normal:
            return PixelmonItemsHeld.chilanBerry;
         case Fairy:
            return PixelmonItemsHeld.roseliBerry;
         default:
            return PixelmonItemsHeld.oranBerry;
      }
   }

   public static Item getPlate(EnumType type) {
      return type == EnumType.Normal ? PixelmonItemsHeld.lifeOrb : EnumPlate.getItem(type);
   }

   public static Item getGem(EnumType type) {
      switch (type) {
         case Bug:
            return PixelmonItemsHeld.bugGem;
         case Ice:
            return PixelmonItemsHeld.iceGem;
         case Fire:
            return PixelmonItemsHeld.fireGem;
         case Water:
            return PixelmonItemsHeld.waterGem;
         case Electric:
            return PixelmonItemsHeld.electricGem;
         case Grass:
            return PixelmonItemsHeld.grassGem;
         case Fighting:
            return PixelmonItemsHeld.fightingGem;
         case Poison:
            return PixelmonItemsHeld.poisonGem;
         case Ground:
            return PixelmonItemsHeld.groundGem;
         case Flying:
            return PixelmonItemsHeld.flyingGem;
         case Psychic:
            return PixelmonItemsHeld.psychicGem;
         case Rock:
            return PixelmonItemsHeld.rockGem;
         case Ghost:
            return PixelmonItemsHeld.ghostGem;
         case Dragon:
            return PixelmonItemsHeld.dragonGem;
         case Dark:
            return PixelmonItemsHeld.darkGem;
         case Steel:
            return PixelmonItemsHeld.steelGem;
         case Normal:
            return PixelmonItemsHeld.normalGem;
         case Fairy:
            return PixelmonItemsHeld.fairyGem;
         default:
            return Items.field_151045_i;
      }
   }

   public static boolean isItemStackEqual(ItemStack a, ItemStack b) {
      if (a.func_77973_b() != b.func_77973_b()) {
         return false;
      } else if (a.func_77952_i() != b.func_77952_i()) {
         return false;
      } else if (a.func_77978_p() == null && b.func_77978_p() != null || b.func_77978_p() == null && a.func_77978_p() != null) {
         return false;
      } else if (a.func_77978_p() == null) {
         return true;
      } else {
         boolean size = a.func_77978_p().func_186856_d() == b.func_77978_p().func_186856_d();
         if (!size) {
            return false;
         } else {
            Iterator var3 = a.func_77978_p().func_150296_c().iterator();

            NBTPrimitive primitiveA;
            NBTPrimitive primitiveB;
            label53:
            do {
               NBTBase tagA;
               NBTBase tagB;
               do {
                  if (!var3.hasNext()) {
                     return true;
                  }

                  String key = (String)var3.next();
                  boolean hasKey = b.func_77978_p().func_74764_b(key);
                  tagA = a.func_77978_p().func_74781_a(key);
                  tagB = b.func_77978_p().func_74781_a(key);
                  if (!hasKey) {
                     return false;
                  }

                  if (tagA instanceof NBTPrimitive && tagB instanceof NBTPrimitive) {
                     primitiveA = (NBTPrimitive)tagA;
                     primitiveB = (NBTPrimitive)tagB;
                     continue label53;
                  }
               } while(tagA.equals(tagB));

               return false;
            } while(primitiveA.func_150286_g() == primitiveB.func_150286_g());

            return false;
         }
      }
   }
}
