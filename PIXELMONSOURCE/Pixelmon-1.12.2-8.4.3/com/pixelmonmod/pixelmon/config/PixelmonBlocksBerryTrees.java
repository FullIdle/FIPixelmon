package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.blocks.BlockBerryTree;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.items.PixelmonItemBlock;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;

public class PixelmonBlocksBerryTrees {
   public static ArrayList berryBlocks = new ArrayList();

   static void load() {
      EnumBerry[] var0 = EnumBerry.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumBerry berry = var0[var2];
         if (berry.isImplemented) {
            berryBlocks.add((BlockBerryTree)(new BlockBerryTree(berry)).func_149663_c("berrytree_" + berry.name().toLowerCase()));
         }
      }

   }

   static void registerBlocks(RegistryEvent.Register event) {
      Iterator var1 = berryBlocks.iterator();

      while(var1.hasNext()) {
         BlockBerryTree block = (BlockBerryTree)var1.next();
         PixelmonBlocks.registerBlock(block, (Class)PixelmonItemBlock.class, (String)("berrytree_" + block.getType().name().toLowerCase()));
      }

   }

   public static void registerModels() {
      Iterator var0 = berryBlocks.iterator();

      while(var0.hasNext()) {
         BlockBerryTree block = (BlockBerryTree)var0.next();
         ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(block), 0, new ModelResourceLocation("pixelmon:berrytree_" + block.getType().name().toLowerCase(), "inventory"));
      }

   }
}
