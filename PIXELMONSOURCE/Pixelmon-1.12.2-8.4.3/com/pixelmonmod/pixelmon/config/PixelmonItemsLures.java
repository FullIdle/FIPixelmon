package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.items.ItemLureCasing;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class PixelmonItemsLures {
   public static Item weakLureCasing;
   public static Item strongLureCasing;
   public static ArrayList weakLures = new ArrayList();
   public static ArrayList strongLures = new ArrayList();

   public static void load() {
      ItemLure.LureType[] var0 = ItemLure.LureType.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         ItemLure.LureType type = var0[var2];
         weakLures.add(new ItemLure(type, ItemLure.LureStrength.WEAK));
         strongLures.add(new ItemLure(type, ItemLure.LureStrength.STRONG));
      }

      weakLureCasing = new ItemLureCasing(ItemLure.LureStrength.WEAK);
      strongLureCasing = new ItemLureCasing(ItemLure.LureStrength.STRONG);
   }

   public static void registerItems(RegistryEvent.Register event) {
      ArrayList var10000 = weakLures;
      IForgeRegistry var10001 = event.getRegistry();
      var10000.forEach(var10001::register);
      var10000 = strongLures;
      var10001 = event.getRegistry();
      var10000.forEach(var10001::register);
      event.getRegistry().register(weakLureCasing);
      event.getRegistry().register(strongLureCasing);
   }

   public static void registerRenderers() {
      weakLures.forEach((lure) -> {
         ModelLoader.setCustomModelResourceLocation(lure, 0, new ModelResourceLocation(lure.getRegistryName(), "inventory"));
      });
      strongLures.forEach((lure) -> {
         ModelLoader.setCustomModelResourceLocation(lure, 0, new ModelResourceLocation(lure.getRegistryName(), "inventory"));
      });
      ModelLoader.setCustomModelResourceLocation(weakLureCasing, 0, new ModelResourceLocation(weakLureCasing.getRegistryName(), "inventory"));
      ModelLoader.setCustomModelResourceLocation(strongLureCasing, 0, new ModelResourceLocation(strongLureCasing.getRegistryName(), "inventory"));
   }

   public static ItemLure getLure(ItemLure.LureType type, ItemLure.LureStrength strength) {
      return (ItemLure)CollectionHelper.find(strength == ItemLure.LureStrength.WEAK ? weakLures : strongLures, (lure) -> {
         return ((ItemLure)lure).type == type;
      });
   }

   @SideOnly(Side.CLIENT)
   public static void registerItemLayers() {
      IItemColor colour = (stack, tint) -> {
         if (tint != 1) {
            return 16777215;
         } else {
            ItemLure lure = (ItemLure)stack.func_77973_b();
            if (lure.type == ItemLure.LureType.SHINY) {
               return 16570112;
            } else {
               return lure.type == ItemLure.LureType.HA ? 16318605 : lure.type.type.getColor();
            }
         }
      };
      Iterator var1 = weakLures.iterator();

      Item item;
      while(var1.hasNext()) {
         item = (Item)var1.next();
         Minecraft.func_71410_x().getItemColors().func_186730_a(colour, new Item[]{item});
      }

      var1 = strongLures.iterator();

      while(var1.hasNext()) {
         item = (Item)var1.next();
         Minecraft.func_71410_x().getItemColors().func_186730_a(colour, new Item[]{item});
      }

   }
}
