package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.items.heldItems.ItemMail;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;

public class PixelmonItemsMail {
   private static String[] mailTypes = new String[]{"air", "bloom", "brick", "bridged", "bridgem", "bridges", "bridgev", "bubble", "dream", "fab", "favored", "flame", "glitter", "grass", "greet", "heart", "inquiry", "like", "mech", "mosaic", "orange", "reply", "retro", "rsvp", "snow", "space", "steel", "thanks", "wave", "wood"};
   public static ArrayList items;

   static void registerMailItems(RegistryEvent.Register event) {
      String[] var1 = mailTypes;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String name = var1[var3];
         ItemMail mailItem = new ItemMail(name);
         event.getRegistry().register(mailItem);
         items.add(mailItem);
         PixelmonItemsHeld.getHeldItemList().add(mailItem);
      }

   }

   static void registerRenderers() {
      Iterator var0 = items.iterator();

      while(var0.hasNext()) {
         ItemMail itemMail = (ItemMail)var0.next();
         ModelLoader.setCustomModelResourceLocation(itemMail, 0, new ModelResourceLocation(itemMail.getRegistryName(), "inventory"));
         ModelLoader.setCustomModelResourceLocation(itemMail, 1, new ModelResourceLocation(itemMail.getRegistryName().toString() + "_closed", "inventory"));
         ModelBakery.registerItemVariants(itemMail, new ResourceLocation[]{itemMail.getRegistryName(), new ResourceLocation(itemMail.getRegistryName() + "_closed")});
      }

   }

   static {
      items = new ArrayList(mailTypes.length);
   }
}
