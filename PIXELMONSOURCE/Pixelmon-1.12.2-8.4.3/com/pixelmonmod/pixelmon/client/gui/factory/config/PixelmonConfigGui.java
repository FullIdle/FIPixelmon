package com.pixelmonmod.pixelmon.client.gui.factory.config;

import com.pixelmonmod.pixelmon.config.Node;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import info.pixelmon.repack.ninja.leaping.configurate.commented.CommentedConfigurationNode;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PixelmonConfigGui extends GuiConfig {
   public PixelmonConfigGui(GuiScreen parentScreen) {
      super(parentScreen, getConfigElements(), "pixelmon", false, false, I18n.func_135052_a("pixelmon.config.title", new Object[0]));
   }

   private static List getConfigElements() {
      List elements = new ArrayList();
      Map map = new HashMap();
      CommentedConfigurationNode defaults = (CommentedConfigurationNode)ReflectionHelper.getPrivateValue(PixelmonConfig.class, (Object)null, new String[]{"defaults"});
      PixelmonConfig.Category[] var3 = PixelmonConfig.Category.values();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         PixelmonConfig.Category category = var3[var5];
         if (category.shouldShowInGui()) {
            List configElements = new ArrayList();
            DummyConfigElement.DummyCategoryElement element = new DummyConfigElement.DummyCategoryElement(category.getTitle(), "pixelmon.config." + category.getTitle().toLowerCase().replaceAll(" ", ""), configElements);
            element.setRequiresMcRestart(category.requiresMcRestart());
            elements.add(element);
            map.put(category.getTitle(), element);
         }
      }

      try {
         Field[] var16 = PixelmonConfig.class.getDeclaredFields();
         var4 = var16.length;

         for(var5 = 0; var5 < var4; ++var5) {
            Field field = var16[var5];
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.getAnnotation(Node.class) != null) {
               Node node = (Node)field.getAnnotation(Node.class);
               PixelmonConfig.Category category = node.category();
               if (category.shouldShowInGui()) {
                  String name = node.nameOverride().isEmpty() ? field.getName() : node.nameOverride();
                  CommentedConfigurationNode categoryNode = defaults;
                  String[] var11 = category.getTitle().split("\\.");
                  int var12 = var11.length;

                  for(int var13 = 0; var13 < var12; ++var13) {
                     String cat = var11[var13];
                     categoryNode = categoryNode.getNode(cat);
                  }

                  CommentedConfigurationNode defaultNode = categoryNode.getNode(name);
                  Object defaultValue;
                  if (defaultNode.hasListChildren()) {
                     defaultValue = defaultNode.getList((o) -> {
                        return o;
                     });
                  } else {
                     defaultValue = defaultNode.getValue();
                  }

                  ReflectionNodeElement element = new ReflectionNodeElement(field, defaultValue);
                  if (category == PixelmonConfig.Category.SPAWNING || category == PixelmonConfig.Category.SPAWNINGGENS) {
                     element.setReloadFunction((o) -> {
                        PixelmonSpawning.totalReload();
                     });
                  }

                  ((DummyConfigElement.DummyCategoryElement)map.get(category.getTitle())).getChildElements().add(element);
               }
            }
         }
      } catch (Throwable var15) {
         var15.printStackTrace();
      }

      return elements;
   }
}
