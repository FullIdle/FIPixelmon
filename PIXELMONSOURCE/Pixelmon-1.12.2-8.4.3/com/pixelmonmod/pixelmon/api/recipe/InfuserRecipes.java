package com.pixelmonmod.pixelmon.api.recipe;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.items.ItemJuiceShoppe;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import org.apache.commons.lang3.tuple.Pair;

public class InfuserRecipes {
   private static final InfuserRecipes INFUSER_BASE = new InfuserRecipes();
   private final Map resultsList = Maps.newHashMap();

   public static InfuserRecipes instance() {
      return INFUSER_BASE;
   }

   void addRecipeForIngredients(ItemStack ingredient1, ItemStack ingredient2, int ticks, ItemStack itemStack) {
      this.resultsList.put(new Tuple(ingredient1, ingredient2), new Tuple(itemStack, ticks));
   }

   public Pair getRecipe(ItemStack stack1, ItemStack stack2) {
      if (PixelmonConfig.berryJuiceCrafting && stack1.func_77973_b() instanceof ItemBerry && stack2.func_77973_b() instanceof ItemBerry) {
         EnumBerry essence = ((ItemBerry)stack1.func_77973_b()).getBerry();
         EnumBerry solvent = ((ItemBerry)stack2.func_77973_b()).getBerry();
         if (essence.color == solvent.color) {
            Item juice = null;
            switch (essence.color) {
               case RED:
                  juice = PixelmonItems.redJuice;
                  break;
               case PURPLE:
                  juice = PixelmonItems.purpleJuice;
                  break;
               case PINK:
                  juice = PixelmonItems.pinkJuice;
                  break;
               case GREEN:
                  juice = PixelmonItems.greenJuice;
                  break;
               case YELLOW:
                  juice = PixelmonItems.yellowJuice;
                  break;
               case BLUE:
                  juice = PixelmonItems.blueJuice;
            }

            NBTTagCompound base = new NBTTagCompound();
            base.func_74768_a("Stats_Boost", ItemJuiceShoppe.getJuiceTag(essence, solvent));
            ItemStack juiceStack = new ItemStack(juice, 1, 0);
            juiceStack.func_77982_d(base);
            return Pair.of(new Tuple(new ItemStack(stack1.func_77973_b()), new ItemStack(stack2.func_77973_b())), new Tuple(juiceStack, 200));
         } else {
            NBTTagCompound base = new NBTTagCompound();
            base.func_74768_a("Stats_Boost", this.getShakeTag(essence, solvent));
            ItemStack shakeStack = new ItemStack(PixelmonItems.colorfulShake, 1, 0);
            shakeStack.func_77982_d(base);
            return Pair.of(new Tuple(new ItemStack(stack1.func_77973_b()), new ItemStack(stack2.func_77973_b())), new Tuple(shakeStack, 200));
         }
      } else {
         Iterator var3 = this.resultsList.entrySet().iterator();

         Map.Entry s;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            s = (Map.Entry)var3.next();
         } while(!((ItemStack)((Tuple)s.getKey()).func_76341_a()).func_77969_a(stack1) || stack1.func_190916_E() < ((ItemStack)((Tuple)s.getKey()).func_76341_a()).func_190916_E() || !((ItemStack)((Tuple)s.getKey()).func_76340_b()).func_77969_a(stack2) || stack2.func_190916_E() < ((ItemStack)((Tuple)s.getKey()).func_76340_b()).func_190916_E());

         return Pair.of(s.getKey(), s.getValue());
      }
   }

   private int getShakeTag(EnumBerry essence, EnumBerry solvent) {
      if (essence.juiceGroup == 1) {
         switch (solvent.juiceGroup) {
            case 1:
               return 12;
            case 2:
               return 16;
            case 3:
               return 24;
         }
      }

      if (essence.juiceGroup == 2) {
         switch (solvent.juiceGroup) {
            case 1:
               return 16;
            case 2:
               return 24;
            case 3:
               return 32;
         }
      }

      if (essence.juiceGroup == 3) {
         switch (solvent.juiceGroup) {
            case 1:
               return 24;
            case 2:
            case 3:
               return 32;
         }
      }

      return 10;
   }

   public boolean isValidEssence(ItemStack stack) {
      Iterator var2 = this.resultsList.keySet().iterator();

      Tuple in;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         in = (Tuple)var2.next();
      } while(!((ItemStack)in.func_76341_a()).func_77969_a(stack));

      return true;
   }

   public boolean isValidSolvent(ItemStack stack) {
      Iterator var2 = this.resultsList.keySet().iterator();

      Tuple in;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         in = (Tuple)var2.next();
      } while(!((ItemStack)in.func_76340_b()).func_77969_a(stack));

      return true;
   }
}
