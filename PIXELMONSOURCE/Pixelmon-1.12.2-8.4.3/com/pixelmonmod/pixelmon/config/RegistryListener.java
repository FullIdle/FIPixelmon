package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.decorative.BlockUnown;
import com.pixelmonmod.pixelmon.config.recipes.PotionRecipe;
import com.pixelmonmod.pixelmon.config.recipes.StatsRecipe;
import com.pixelmonmod.pixelmon.config.recipes.StatusHealRecipe;
import com.pixelmonmod.pixelmon.config.recipes.UnownBlockRecipe;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(
   modid = "pixelmon"
)
public class RegistryListener {
   @SubscribeEvent
   public static void onRegisterBlocks(RegistryEvent.Register event) {
      PixelmonBlocks.load();
      PixelmonBlocksBerryTrees.load();
      PixelmonBlocksApricornTrees.load();
      PixelmonBlocks.registerBlocks(event);
      PixelmonBlocksBerryTrees.registerBlocks(event);
      registerAllFields(PixelmonBlocksApricornTrees.class, Block.class, event.getRegistry());
      TileEntityRegistry.registerTileEntities();
   }

   @SubscribeEvent
   public static void onRemapBlocks(RegistryEvent.MissingMappings event) {
      RemapHandler.remapBlocks(event.getMappings());
   }

   @SubscribeEvent
   public static void onRegisterItems(RegistryEvent.Register event) {
      PixelmonItems.load();
      PixelmonItemsHeld.load();
      PixelmonItemsTools.load();
      PixelmonItemsBadges.load();
      PixelmonItemsValuables.load();
      PixelmonItemsQuests.load();
      PixelmonItemsFossils.load();
      PixelmonItemsPokeballs.load();
      PixelmonItemsApricorns.load();
      PixelmonItemsBadgeCases.load();
      PixelmonItemsLures.load();
      registerAllFields(PixelmonItems.class, Item.class, event.getRegistry());
      registerAllFields(PixelmonItemsHeld.class, Item.class, event.getRegistry());
      registerAllFields(PixelmonItemsTools.class, Item.class, event.getRegistry());
      registerAllFields(PixelmonItemsBadges.class, Item.class, event.getRegistry());
      registerAllFields(PixelmonItemsValuables.class, Item.class, event.getRegistry());
      registerAllFields(PixelmonItemsQuests.class, Item.class, event.getRegistry());
      registerAllFields(PixelmonItemsFossils.class, Item.class, event.getRegistry());
      registerAllFields(PixelmonItemsPokeballs.class, Item.class, event.getRegistry());
      registerAllFields(PixelmonItemsApricorns.class, Item.class, event.getRegistry());
      registerAllFields(PixelmonItemsBadgeCases.class, Item.class, event.getRegistry());
      PixelmonItemsTMs.registerItems(event);
      PixelmonItemsMail.registerMailItems(event);
      PixelmonItemsLures.registerItems(event);
      Pixelmon.proxy.fixModelDefs();
      Pixelmon.proxy.registerBlockModels();
      PixelmonOres.populateOres();
      PixelmonOres.registerOres();
   }

   @SubscribeEvent
   public static void onRemapItems(RegistryEvent.MissingMappings event) {
      RemapHandler.remapItems(event.getMappings());
   }

   @SubscribeEvent
   public static void onRegisterRecipes(RegistryEvent.Register event) {
      for(int i = 0; i < BlockUnown.getNumUnownBlocks(); ++i) {
         event.getRegistry().register(new UnownBlockRecipe(i));
      }

      FurnaceRecipes furnaceRecipes = FurnaceRecipes.func_77602_a();
      PixelmonOres.registerFurnaceRecipes(furnaceRecipes);
      PixelmonItemsApricorns.registerFurnaceRecipes(furnaceRecipes);
      BrewingRecipeRegistry.addRecipe(new PotionRecipe());
      BrewingRecipeRegistry.addRecipe(new StatsRecipe());
      BrewingRecipeRegistry.addRecipe(new StatusHealRecipe());
   }

   @SubscribeEvent
   public static void onRegisterEntities(RegistryEvent.Register event) {
      PixelmonEntityList.registerEntities();
   }

   @SubscribeEvent
   public static void onRemapEntities(RegistryEvent.MissingMappings event) {
      RemapHandler.remapEntities(event.getMappings());
   }

   @SubscribeEvent
   public static void onRegisterSounds(RegistryEvent.Register event) {
      PixelSounds.registerSounds(event.getRegistry());
   }

   @SubscribeEvent
   public static void onRemapSounds(RegistryEvent.MissingMappings event) {
      event.getMappings().forEach(RegistryEvent.MissingMappings.Mapping::ignore);
      event.getAllMappings().forEach((s) -> {
         if (s.key.toString().equalsIgnoreCase("minecraft:entity.experience_orb.touch")) {
            s.ignore();
         }

      });
   }

   @SubscribeEvent
   public static void onRegisterModels(ModelRegistryEvent event) {
      PixelmonItems.registerRenderers();
      registerItemRenderersByFields(PixelmonItemsHeld.class);
      registerItemRenderersByFields(PixelmonItemsTools.class);
      registerItemRenderersByFields(PixelmonItemsBadges.class);
      registerItemRenderersByFields(PixelmonItemsValuables.class);
      registerItemRenderersByFields(PixelmonItemsQuests.class);
      registerItemRenderersByFields(PixelmonItemsFossils.class);
      registerItemRenderersByFields(PixelmonItemsPokeballs.class);
      registerItemRenderersByFields(PixelmonItemsApricorns.class);
      registerItemRenderersByFields(PixelmonItemsBadgeCases.class);
      PixelmonItemsTMs.registerRenderers();
      PixelmonItemsMail.registerRenderers();
      PixelmonItemsLures.registerRenderers();
   }

   @SubscribeEvent
   public static void registerPotions(RegistryEvent.Register event) {
      IForgeRegistry registry = event.getRegistry();
      registry.register(PixelmonPotions.repel.func_188413_j());
   }

   @SubscribeEvent
   public static void onRemapPotions(RegistryEvent.MissingMappings event) {
      RemapHandler.remapPotions(event.getMappings());
   }

   private static void registerAllFields(Class clazzWithFields, Class type, IForgeRegistry registry) {
      try {
         Field[] var3 = clazzWithFields.getFields();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && type.isInstance(field.get((Object)null))) {
               registry.register((IForgeRegistryEntry)field.get((Object)null));
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   @SideOnly(Side.CLIENT)
   private static void registerItemRenderersByFields(Class clazzWithFields) {
      try {
         Field[] var1 = clazzWithFields.getFields();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Field field = var1[var3];
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.get((Object)null) instanceof Item) {
               Item item = (Item)field.get((Object)null);
               ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
