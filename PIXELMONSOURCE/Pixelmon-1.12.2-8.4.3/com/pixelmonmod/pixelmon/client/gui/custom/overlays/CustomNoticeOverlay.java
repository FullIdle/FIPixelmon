package com.pixelmonmod.pixelmon.client.gui.custom.overlays;

import com.pixelmonmod.pixelmon.api.overlay.notice.EnumOverlayLayout;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class CustomNoticeOverlay {
   private static boolean enabled = false;
   private static Collection lines;
   private static OverlayGraphicType type;
   private static EnumOverlayLayout layout;
   private static int squareSize = 40;
   private static Pokemon pokemon;
   private static EntityPixelmon entity;
   private static float scale = 1.0F;
   private static ItemStack itemStack;

   public static void resetNotice() {
      enabled = false;
      lines = null;
      type = null;
      layout = null;
      pokemon = null;
      entity = null;
      itemStack = null;
   }

   public static void draw(ScaledResolution res) {
      Optional coordsOptional = GuiHelper.renderTooltip(res.func_78326_a() / 2, 15, lines, -16776961, -1, 100, true, true, 0.0F);
      if (coordsOptional.isPresent()) {
         int[] coords = (int[])coordsOptional.get();
         if (type != null) {
            switch (type) {
               case PokemonSprite:
                  drawPokemonSprites(coords);
                  break;
               case Pokemon3D:
                  drawPokemon3Ds(coords);
                  break;
               case ItemStack:
                  drawItemStacks(coords);
            }
         }
      }

   }

   public static void setEnabled(boolean enabled) {
      CustomNoticeOverlay.enabled = enabled;
   }

   public static boolean isEnabled() {
      return enabled;
   }

   public static void populate(List lines) {
      CustomNoticeOverlay.lines = lines;
   }

   public static void setPokemonSprite(PokemonSpec spec, EnumOverlayLayout layout) {
      CustomNoticeOverlay.layout = layout;
      type = OverlayGraphicType.PokemonSprite;
      pokemon = spec.create();
      if (pokemon == null) {
         throw new IllegalArgumentException(String.format("The spec '%s' is not valid.", spec.toString()));
      }
   }

   public static void setPokemon3D(PokemonSpec spec, EnumOverlayLayout layout) {
      CustomNoticeOverlay.layout = layout;
      type = OverlayGraphicType.Pokemon3D;
      entity = spec.create(Minecraft.func_71410_x().field_71441_e);
      if (entity == null) {
         throw new IllegalArgumentException(String.format("The spec '%s' is not valid.", spec.toString()));
      } else {
         scale = 15.0F / entity.field_70131_O;
      }
   }

   public static void setItemStack(ItemStack itemStack, EnumOverlayLayout layout) {
      CustomNoticeOverlay.layout = layout;
      type = OverlayGraphicType.ItemStack;
      CustomNoticeOverlay.itemStack = itemStack;
   }

   private static void drawPokemonSprites(int[] coords) {
      GuiHelper.bindPokemonSprite(pokemon, Minecraft.func_71410_x());
      switch (layout) {
         case LEFT:
            GuiHelper.drawImageQuad((double)(coords[0] - squareSize), 0.0, (double)squareSize, (float)squareSize, 1.0, 0.0, 0.0, 1.0, 0.0F);
            break;
         case RIGHT:
            GuiHelper.drawImageQuad((double)coords[2], 0.0, (double)squareSize, (float)squareSize, 1.0, 0.0, 0.0, 1.0, 0.0F);
            break;
         case LEFT_AND_RIGHT:
            GuiHelper.drawImageQuad((double)(coords[0] - squareSize), 0.0, (double)squareSize, (float)squareSize, 1.0, 0.0, 0.0, 1.0, 0.0F);
            GuiHelper.drawImageQuad((double)coords[2], 0.0, (double)squareSize, (float)squareSize, 0.0, 0.0, 1.0, 1.0, 0.0F);
      }

   }

   private static void drawPokemon3Ds(int[] coords) {
      switch (layout) {
         case LEFT:
            GuiHelper.drawEntity(entity, coords[0] - 15, squareSize - 5, scale, -60.0F, 0.0F);
            break;
         case RIGHT:
            GuiHelper.drawEntity(entity, coords[2] + 15, squareSize - 5, scale, 60.0F, 0.0F);
            break;
         case LEFT_AND_RIGHT:
            GuiHelper.drawEntity(entity, coords[0] - 15, squareSize - 5, scale, -60.0F, 0.0F);
            GuiHelper.drawEntity(entity, coords[2] + 15, squareSize - 5, scale, 60.0F, 0.0F);
      }

   }

   private static void drawItemStacks(int[] coords) {
      RenderHelper.func_74519_b();
      GlStateManager.func_179126_j();
      RenderItem ri = Minecraft.func_71410_x().func_175599_af();
      switch (layout) {
         case LEFT:
            ri.func_180450_b(itemStack, coords[0] - 20, 15);
            break;
         case RIGHT:
            ri.func_180450_b(itemStack, coords[2] + 5, 15);
            break;
         case LEFT_AND_RIGHT:
            ri.func_180450_b(itemStack, coords[0] - 20, 15);
            ri.func_180450_b(itemStack, coords[2] + 5, 15);
      }

      GlStateManager.func_179097_i();
   }
}
