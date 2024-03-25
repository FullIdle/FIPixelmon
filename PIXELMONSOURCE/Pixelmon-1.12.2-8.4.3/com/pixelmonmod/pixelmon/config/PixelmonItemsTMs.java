package com.pixelmonmod.pixelmon.config;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen1TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen2TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen3TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen4TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen5TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen6TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen7TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalRecords;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import com.pixelmonmod.pixelmon.items.ItemTM;
import com.pixelmonmod.pixelmon.items.ItemTechnicalMove;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBlankTechnicalMachine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;

public class PixelmonItemsTMs {
   public static final List HMs = new ArrayList();
   public static Item gen8TM;
   public static Item gen8BlankTM;
   public static Item gen8TR;
   public static Item gen8BlankTR;
   public static Item gen7TM;
   public static Item gen7BlankTM;
   public static Item gen6TM;
   public static Item gen6BlankTM;
   public static Item gen5TM;
   public static Item gen5BlankTM;
   public static Item gen4TM;
   public static Item gen4BlankTM;
   public static Item gen3TM;
   public static Item gen3BlankTM;
   public static Item gen2TM;
   public static Item gen2BlankTM;
   public static Item gen1TM;
   public static Item gen1BlankTM;
   private static final List HMNames = Lists.newArrayList(new String[]{"Cut", "Fly", "Surf", "Strength", "Defog", "Rock Smash", "Waterfall", "Rock Climb", "Whirlpool", "Dive"});

   public static ItemStack createStackFor(ITechnicalMove move) {
      return createStackFor(move, 1);
   }

   public static ItemStack createStackFor(ITechnicalMove move, int count) {
      NBTTagCompound compound = new NBTTagCompound();
      compound.func_74777_a("tm", (short)move.getId());
      ItemStack stack;
      if (move instanceof Gen8TechnicalRecords) {
         stack = new ItemStack(gen8TR, count, 0);
      } else if (move instanceof Gen8TechnicalMachines) {
         stack = new ItemStack(gen8TM, count, 0);
      } else if (move instanceof Gen7TechnicalMachines) {
         stack = new ItemStack(gen7TM, count, 0);
      } else if (move instanceof Gen6TechnicalMachines) {
         stack = new ItemStack(gen6TM, count, 0);
      } else if (move instanceof Gen5TechnicalMachines) {
         stack = new ItemStack(gen5TM, count, 0);
      } else if (move instanceof Gen4TechnicalMachines) {
         stack = new ItemStack(gen4TM, count, 0);
      } else if (move instanceof Gen3TechnicalMachines) {
         stack = new ItemStack(gen3TM, count, 0);
      } else if (move instanceof Gen2TechnicalMachines) {
         stack = new ItemStack(gen2TM, count, 0);
      } else {
         if (!(move instanceof Gen1TechnicalMachines)) {
            return null;
         }

         stack = new ItemStack(gen1TM, count, 0);
      }

      stack.func_77982_d(compound);
      return stack;
   }

   static void load() {
      Iterator var0 = HMNames.iterator();

      while(var0.hasNext()) {
         String hm = (String)var0.next();
         HMs.add(new ItemTM(hm, HMNames.indexOf(hm) + 1));
      }

      gen8TM = new ItemTechnicalMove("tm_gen8");
      gen8BlankTM = new ItemBlankTechnicalMachine("tm8");
      gen8TR = new ItemTechnicalMove("tr_gen8");
      gen8BlankTR = new ItemBlankTechnicalMachine("tr8");
      gen7TM = new ItemTechnicalMove("tm_gen7");
      gen7BlankTM = new ItemBlankTechnicalMachine("tm_gen7");
      gen6TM = new ItemTechnicalMove("tm_gen6");
      gen6BlankTM = new ItemBlankTechnicalMachine("tm_gen6");
      gen5TM = new ItemTechnicalMove("tm_gen5");
      gen5BlankTM = new ItemBlankTechnicalMachine("tm_gen5");
      gen4TM = new ItemTechnicalMove("tm_gen4");
      gen4BlankTM = new ItemBlankTechnicalMachine("tm_gen4");
      gen3TM = new ItemTechnicalMove("tm_gen3");
      gen3BlankTM = new ItemBlankTechnicalMachine("tm_gen3");
      gen2TM = new ItemTechnicalMove("tm_gen2");
      gen2BlankTM = new ItemBlankTechnicalMachine("tm_gen2");
      gen1TM = new ItemTechnicalMove("tm_gen1");
      gen1BlankTM = new ItemBlankTechnicalMachine("tm_gen1");
   }

   static void registerItems(RegistryEvent.Register event) {
      load();
      Iterator var1 = HMs.iterator();

      while(var1.hasNext()) {
         Item item = (Item)var1.next();
         event.getRegistry().register(item);
      }

      event.getRegistry().register(gen8TM);
      event.getRegistry().register(gen8BlankTM);
      event.getRegistry().register(gen8TR);
      event.getRegistry().register(gen8BlankTR);
      event.getRegistry().register(gen7TM);
      event.getRegistry().register(gen7BlankTM);
      event.getRegistry().register(gen6TM);
      event.getRegistry().register(gen6BlankTM);
      event.getRegistry().register(gen5TM);
      event.getRegistry().register(gen5BlankTM);
      event.getRegistry().register(gen4TM);
      event.getRegistry().register(gen4BlankTM);
      event.getRegistry().register(gen3TM);
      event.getRegistry().register(gen3BlankTM);
      event.getRegistry().register(gen2TM);
      event.getRegistry().register(gen2BlankTM);
      event.getRegistry().register(gen1TM);
      event.getRegistry().register(gen1BlankTM);
   }

   static void registerRenderers() {
      try {
         Iterator var0 = HMs.iterator();

         while(var0.hasNext()) {
            Item item = (Item)var0.next();
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
         }

         Item[] var9 = new Item[]{gen8BlankTM, gen8BlankTR, gen7BlankTM, gen6BlankTM, gen5BlankTM, gen4BlankTM, gen3BlankTM, gen2BlankTM, gen1BlankTM};
         int var10 = var9.length;

         int var2;
         Item base;
         for(var2 = 0; var2 < var10; ++var2) {
            base = var9[var2];
            ModelLoader.setCustomModelResourceLocation(base, 0, new ModelResourceLocation(base.getRegistryName(), "inventory"));
         }

         var9 = new Item[]{gen8TM, gen8TR, gen7TM, gen6TM, gen5TM, gen4TM, gen3TM, gen2TM, gen1TM};
         var10 = var9.length;

         for(var2 = 0; var2 < var10; ++var2) {
            base = var9[var2];
            ModelLoader.setCustomModelResourceLocation(base, 0, new ModelResourceLocation(base.getRegistryName(), "inventory"));
            ITechnicalMove[] var4 = ITechnicalMove.getAllFor(((ItemTechnicalMove)base).type());
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               ITechnicalMove move = var4[var6];
               ModelLoader.setCustomModelResourceLocation(base, move.getId(), new ModelResourceLocation(base.getRegistryName(), "inventory"));
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
