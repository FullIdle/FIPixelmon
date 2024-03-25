package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.enums.items.EnumApricorns;
import com.pixelmonmod.pixelmon.items.ItemApricorn;
import com.pixelmonmod.pixelmon.items.ItemApricornCooked;
import java.lang.reflect.Field;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class PixelmonItemsApricorns {
   public static Item apricornBlack;
   public static Item apricornWhite;
   public static Item apricornPink;
   public static Item apricornGreen;
   public static Item apricornBlue;
   public static Item apricornYellow;
   public static Item apricornRed;
   public static Item apricornBlackCooked;
   public static Item apricornWhiteCooked;
   public static Item apricornPinkCooked;
   public static Item apricornGreenCooked;
   public static Item apricornBlueCooked;
   public static Item apricornYellowCooked;
   public static Item apricornRedCooked;

   public static void load() {
      apricornBlack = new ItemApricorn(EnumApricorns.Black);
      apricornWhite = new ItemApricorn(EnumApricorns.White);
      apricornPink = new ItemApricorn(EnumApricorns.Pink);
      apricornGreen = new ItemApricorn(EnumApricorns.Green);
      apricornBlue = new ItemApricorn(EnumApricorns.Blue);
      apricornYellow = new ItemApricorn(EnumApricorns.Yellow);
      apricornRed = new ItemApricorn(EnumApricorns.Red);
      apricornBlackCooked = new ItemApricornCooked(EnumApricorns.Black);
      apricornWhiteCooked = new ItemApricornCooked(EnumApricorns.White);
      apricornPinkCooked = new ItemApricornCooked(EnumApricorns.Pink);
      apricornGreenCooked = new ItemApricornCooked(EnumApricorns.Green);
      apricornBlueCooked = new ItemApricornCooked(EnumApricorns.Blue);
      apricornYellowCooked = new ItemApricornCooked(EnumApricorns.Yellow);
      apricornRedCooked = new ItemApricornCooked(EnumApricorns.Red);
   }

   public static Item getCookedApricorn(EnumApricorns type) {
      try {
         Field[] var1 = PixelmonItemsApricorns.class.getFields();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Field field = var1[var3];
            Item item = (Item)field.get((Object)null);
            if (item instanceof ItemApricornCooked && ((ItemApricornCooked)item).apricorn == type) {
               return item;
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return apricornBlackCooked;
   }

   public static Item getApricorn(EnumApricorns type) {
      try {
         Field[] var1 = PixelmonItemsApricorns.class.getFields();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Field field = var1[var3];
            Item item = (Item)field.get((Object)null);
            if (item instanceof ItemApricorn && ((ItemApricorn)item).apricorn == type) {
               return item;
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return apricornBlack;
   }

   static void registerFurnaceRecipes(FurnaceRecipes recipes) {
      recipes.func_151396_a(apricornBlack, new ItemStack(apricornBlackCooked), 0.1F);
      recipes.func_151396_a(apricornWhite, new ItemStack(apricornWhiteCooked), 0.1F);
      recipes.func_151396_a(apricornPink, new ItemStack(apricornPinkCooked), 0.1F);
      recipes.func_151396_a(apricornGreen, new ItemStack(apricornGreenCooked), 0.1F);
      recipes.func_151396_a(apricornBlue, new ItemStack(apricornBlueCooked), 0.1F);
      recipes.func_151396_a(apricornYellow, new ItemStack(apricornYellowCooked), 0.1F);
      recipes.func_151396_a(apricornRed, new ItemStack(apricornRedCooked), 0.1F);
   }
}
