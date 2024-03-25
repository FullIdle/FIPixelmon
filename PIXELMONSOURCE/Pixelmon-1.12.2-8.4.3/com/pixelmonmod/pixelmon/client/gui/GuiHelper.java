package com.pixelmonmod.pixelmon.client.gui;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.ScoreboardJustification;
import com.pixelmonmod.pixelmon.client.render.custom.FontRendererPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import com.pixelmonmod.pixelmon.util.helpers.CursorHelper;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

public class GuiHelper {
   private static Map fontRenderers = Maps.newConcurrentMap();
   private static WeakHashMap weakSpriteExistenceCheck = new WeakHashMap();

   public static boolean isMouseOverButton(GuiButton button, int mouseX, int mouseY) {
      return mouseX >= button.field_146128_h && mouseY >= button.field_146129_i && mouseX < button.field_146128_h + button.field_146120_f && mouseY < button.field_146129_i + button.field_146121_g;
   }

   public static void bindFontRenderer(String font, boolean extendedColours) {
      Minecraft mc = Minecraft.func_71410_x();
      if (!fontRenderers.containsKey("")) {
         fontRenderers.put("", mc.field_71466_p);
      }

      if (fontRenderers.containsKey(font)) {
         mc.field_71466_p = (FontRenderer)fontRenderers.get(font);
      } else {
         ResourceLocation fontResource = new ResourceLocation(font);
         if (extendedColours) {
            mc.field_71466_p = new FontRendererPixelmon((FontRenderer)fontRenderers.get(""), fontResource);
         } else {
            mc.field_71466_p = new FontRenderer(mc.field_71474_y, fontResource, mc.func_110434_K(), mc.func_152349_b());
         }

         fontRenderers.put(font, mc.field_71466_p);
         mc.field_110451_am.func_110542_a(mc.field_71466_p);
      }
   }

   public static void resetFontRenderer() {
      bindFontRenderer("", false);
   }

   public static void drawStringRightAligned(String text, float x, float y, int color) {
      drawStringRightAligned(text, x, y, color, false);
   }

   public static void drawStringRightAligned(String text, float x, float y, int color, boolean dropShadow) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      x -= (float)fontRenderer.func_78256_a(text);
      fontRenderer.func_175065_a(text, x, y, color, dropShadow);
   }

   public static void drawScaledString(String text, float x, float y, int color, float fontSize) {
      float scalar = fontSize / 16.0F;
      GlStateManager.func_179152_a(scalar, scalar, 1.0F);
      Minecraft.func_71410_x().field_71466_p.func_175065_a(text, x / scalar, y / scalar, color, false);
      GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
      GlStateManager.func_179152_a(1.0F / scalar, 1.0F / scalar, 1.0F);
   }

   public static void drawScaledSquashedString(String text, float x, float y, int color, float fontSize, double widthFactor) {
      float scalar = fontSize / 16.0F;
      FontRenderer fr = Minecraft.func_71410_x().field_71466_p;
      boolean previousFlag = fr.func_82883_a();
      fr.func_78264_a(fr.func_82883_a());
      double nameSize = (double)fr.func_78256_a(text);
      int offset = 0;
      nameSize -= (double)offset;
      GlStateManager.func_179094_E();
      if (nameSize > widthFactor) {
         double scaleFactor = widthFactor / nameSize;
         GlStateManager.func_179109_b(x, 0.0F, 0.0F);
         GlStateManager.func_179139_a(scaleFactor, 1.0, 1.0);
         GlStateManager.func_179109_b(-x, 0.0F, 0.0F);
      }

      GlStateManager.func_179152_a(scalar, scalar, 1.0F);
      fr.func_78276_b(text, (int)(x / scalar), (int)(y / scalar), color);
      fr.func_78264_a(previousFlag);
      GlStateManager.func_179121_F();
      GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
   }

   public static void drawScaledCenteredString(String text, float x, float y, int color, float fontSize) {
      float scalar = fontSize / 16.0F;
      GlStateManager.func_179152_a(scalar, scalar, 1.0F);
      drawCenteredString(text, x / scalar, y / scalar, color);
      GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
      GlStateManager.func_179152_a(1.0F / scalar, 1.0F / scalar, 1.0F);
   }

   public static void drawScaledStringWithOutline(String text, float x, float y, int color, int outlineColor, float fontSize, float outline) {
      for(int i = 0; i < text.length(); x += (float)Minecraft.func_71410_x().field_71466_p.func_78263_a(text.charAt(i++))) {
         String character = text.substring(i, i + 1);
         drawScaledCenteredString(character, x, y - outline / 4.0F, outlineColor, fontSize);
         drawScaledCenteredString(character, x, y, color, fontSize - outline);
         if (i == 0) {
            x -= (float)(Minecraft.func_71410_x().field_71466_p.func_78263_a(text.charAt(i)) / 2);
         }
      }

   }

   public static void drawScaledStringRightAligned(String text, float x, float y, int color, boolean dropShadow, float fontSize) {
      float scalar = fontSize / 16.0F;
      GlStateManager.func_179152_a(scalar, scalar, 1.0F);
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      x = (float)((double)x - (double)((float)fontRenderer.func_78256_a(text) * fontSize) / 16.0);
      fontRenderer.func_175065_a(text, x / scalar, y / scalar, color, dropShadow);
      GlStateManager.func_179152_a(1.0F / scalar, 1.0F / scalar, 1.0F);
   }

   public static String splitStringToFit(String s, int font, int width) {
      int i = 0;

      while(true) {
         while(true) {
            while(i < s.length()) {
               int j;
               for(j = 0; j < width; j = (int)((double)j + (double)Minecraft.func_71410_x().field_71466_p.func_78263_a(s.charAt(i)) * ((double)font / 16.0))) {
                  ++i;
                  if (i >= s.length()) {
                     return s;
                  }
               }

               if (s.substring(0, i).contains(" ")) {
                  for(j = i; j > 0; --j) {
                     if (s.charAt(j) == ' ') {
                        s = s.substring(0, j).trim() + "\n" + s.substring(j).trim();
                        break;
                     }
                  }
               } else {
                  s = s.substring(0, i).trim() + "\n" + s.substring(i).trim();
               }
            }

            return s;
         }
      }
   }

   public static void drawImageQuad(double x, double y, double w, float h, double us, double vs, double ue, double ve, float zLevel) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      buffer.func_181662_b(x, y + (double)h, (double)zLevel).func_187315_a(us, ve).func_181675_d();
      buffer.func_181662_b(x + w, y + (double)h, (double)zLevel).func_187315_a(ue, ve).func_181675_d();
      buffer.func_181662_b(x + w, y, (double)zLevel).func_187315_a(ue, vs).func_181675_d();
      buffer.func_181662_b(x, y, (double)zLevel).func_187315_a(us, vs).func_181675_d();
      tessellator.func_78381_a();
   }

   public static void drawImage(double x, double y, double width, double height, float zLevel) {
      drawImageQuad(x, y, width, (float)height, 0.0, 0.0, 1.0, 1.0, zLevel);
   }

   public static void drawImage(ResourceLocation texture, double x, double y, double width, double height, float zLevel) {
      Minecraft.func_71410_x().func_110434_K().func_110577_a(texture);
      drawImageQuad(x, y, width, (float)height, 0.0, 0.0, 1.0, 1.0, zLevel);
   }

   public static void drawScaledImage(ResourceLocation texture, double scale, double x, double y, double width, double height, float zLevel) {
      Minecraft.func_71410_x().func_110434_K().func_110577_a(texture);
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(x, y, 0.0);
      GlStateManager.func_179139_a(scale, scale, 0.0);
      drawImageQuad(0.0, 0.0, width, (float)height, 0.0, 0.0, 1.0, 1.0, zLevel);
      GlStateManager.func_179121_F();
   }

   public static void drawSquashedString(FontRenderer fr, String text, boolean unicode, double widthFactor, float x, float y, int color, boolean shadow) {
      boolean previousFlag = fr.func_82883_a();
      fr.func_78264_a(unicode);
      GlStateManager.func_179094_E();
      double nameSize = (double)fr.func_78256_a(text);
      int offset = 0;
      nameSize -= (double)offset;
      if (nameSize > widthFactor) {
         double scaleFactor = widthFactor / nameSize;
         GlStateManager.func_179109_b(x, 0.0F, 0.0F);
         GlStateManager.func_179139_a(scaleFactor, 1.0, 1.0);
         GlStateManager.func_179109_b(-x, 0.0F, 0.0F);
      }

      if (shadow) {
         fr.func_175063_a(text, x, y, color);
      } else {
         fr.func_78276_b(text, (int)x, (int)y, color);
      }

      fr.func_78264_a(previousFlag);
      GlStateManager.func_179121_F();
   }

   public static void drawCenteredSquashedString(FontRenderer fr, String text, boolean unicode, double widthFactor, int x, int y, int color, boolean shadow) {
      boolean previousFlag = fr.func_82883_a();
      fr.func_78264_a(unicode);
      GlStateManager.func_179094_E();
      double nameSize = (double)fr.func_78256_a(text);
      int offset = 0;
      nameSize -= (double)offset;
      if (nameSize > widthFactor) {
         double scaleFactor = widthFactor / nameSize;
         GlStateManager.func_179109_b((float)x, 0.0F, 0.0F);
         GlStateManager.func_179139_a(scaleFactor, 1.0, 1.0);
         GlStateManager.func_179109_b((float)(-x), 0.0F, 0.0F);
      }

      GlStateManager.func_179137_b(-nameSize / 2.0, 0.0, 0.0);
      if (shadow) {
         fr.func_175063_a(text, (float)x, (float)y, color);
      } else {
         fr.func_78276_b(text, x, y, color);
      }

      fr.func_78264_a(previousFlag);
      GlStateManager.func_179121_F();
   }

   public static void bindPokemonSprite(Pokemon pokemon, Minecraft mc) {
      if (pokemon.isEgg()) {
         mc.field_71446_o.func_110577_a(GuiResources.getEggSprite(pokemon.getSpecies(), pokemon.getEggCycles()));
      } else {
         if (pokemon.getFormEnum() == EnumSpecial.Online && pokemon.getOwnerPlayerUUID() != null) {
            PlayerExtraDataStore.get(pokemon.getOwnerPlayerUUID()).checkPokemon(pokemon);
         }

         ResourceLocation rl = GuiResources.getPokemonSprite(pokemon.getSpecies(), pokemon.getForm(), pokemon.getGender(), pokemon.getCustomTexture(), pokemon.isShiny());
         if (!weakSpriteExistenceCheck.containsKey(rl)) {
            weakSpriteExistenceCheck.put(rl, Pixelmon.proxy.resourceLocationExists(rl));
         }

         boolean exists = weakSpriteExistenceCheck.get(rl) == Boolean.TRUE;
         if (!exists) {
            rl = GuiResources.getPokemonSprite(pokemon.getSpecies(), ((IEnumForm)pokemon.getSpecies().getDefaultForms().get(0)).getForm(), pokemon.getGender(), "", pokemon.isShiny());
         }

         mc.field_71446_o.func_110577_a(rl);
      }

      texParameterReset();
   }

   public static void bindPokemonSprite(PixelmonInGui pokemon, Minecraft mc) {
      ResourceLocation rl = GuiResources.getPokemonSprite(pokemon.species, pokemon.form, pokemon.getGender(), pokemon.customTexture, pokemon.shiny);
      if (!weakSpriteExistenceCheck.containsKey(rl)) {
         weakSpriteExistenceCheck.put(rl, Pixelmon.proxy.resourceLocationExists(rl));
      }

      boolean exists = (Boolean)weakSpriteExistenceCheck.get(rl);
      if (exists) {
         mc.field_71446_o.func_110577_a(rl);
      } else {
         mc.field_71446_o.func_110577_a(GuiResources.getPokemonSprite(pokemon.species, ((IEnumForm)pokemon.species.getDefaultForms().get(0)).getForm(), pokemon.getGender(), "", pokemon.shiny));
      }

      texParameterReset();
   }

   public static void bindPokemonSprite(EnumSpecies species, int form, Gender gender, String customTexture, boolean isShiny, int eggCycles, Minecraft mc) {
      if (eggCycles > 0) {
         mc.field_71446_o.func_110577_a(GuiResources.getEggSprite(species, eggCycles));
      } else {
         ResourceLocation rl = GuiResources.getPokemonSprite(species, form, gender, customTexture, isShiny);
         if (!weakSpriteExistenceCheck.containsKey(rl)) {
            weakSpriteExistenceCheck.put(rl, Pixelmon.proxy.resourceLocationExists(rl));
         }

         boolean exists = (Boolean)weakSpriteExistenceCheck.get(rl);
         if (exists) {
            mc.field_71446_o.func_110577_a(rl);
         } else {
            mc.field_71446_o.func_110577_a(GuiResources.getPokemonSprite(species, form, gender, "", isShiny));
         }
      }

      texParameterReset();
   }

   public static void bindPokemonSprite(PokemonSpec spec, Minecraft mc) {
      EnumSpecies species = EnumSpecies.getFromNameAnyCaseNoTranslate(spec.name);
      if (species != null) {
         if (spec.egg != null && spec.egg) {
            mc.field_71446_o.func_110577_a(GuiResources.getEggSprite(species, species.getBaseStats().getEggCycles()));
         } else {
            int form = spec.form == null || spec.formInvert != null && spec.formInvert ? -1 : spec.form;
            Gender gender = spec.gender != null ? Gender.getGender((short)spec.gender) : Gender.Male;
            boolean shiny = spec.shiny == Boolean.TRUE;
            mc.field_71446_o.func_110577_a(GuiResources.getPokemonSprite(species, form, gender, "", shiny));
         }

         texParameterReset();
      }
   }

   public static void texParameterReset() {
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexParameteri(3553, 10242, 33071);
      GL11.glTexParameteri(3553, 10243, 33071);
   }

   public static void drawScoreboard(int top, ScoreboardJustification justification, int x, int alpha, String title, Collection lines, Collection scores) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      int tempWidth = getLongestStringWidth(lines);
      if (scores != null) {
         tempWidth += getLongestStringWidth(scores) + 20;
      }

      if (fontRenderer.func_78256_a(title) > tempWidth) {
         tempWidth = fontRenderer.func_78256_a(title);
      }

      int justifiedWidth = justification == ScoreboardJustification.RIGHT ? tempWidth : (justification == ScoreboardJustification.CENTER ? tempWidth / 2 : 0);
      int left = x - justifiedWidth - 3;
      int boxHeight = (lines.size() + 1) * 10;
      Gui.func_73734_a(left, top, left + tempWidth, top + boxHeight, alpha);
      drawCenteredString(title, (float)left + (float)tempWidth / 2.0F, (float)top, -1);
      int y = top;
      Iterator var13 = lines.iterator();

      String line;
      float var10002;
      while(var13.hasNext()) {
         line = (String)var13.next();
         var10002 = (float)(left + 3);
         y += 10;
         fontRenderer.func_175063_a(line, var10002, (float)y, -1);
      }

      y = top;
      if (scores != null) {
         var13 = scores.iterator();

         while(var13.hasNext()) {
            line = (String)var13.next();
            var10002 = (float)(left + tempWidth - 3 - fontRenderer.func_78256_a(line));
            y += 10;
            fontRenderer.func_175063_a(line, var10002, (float)y, -1);
         }
      }

   }

   public static int getLongestStringWidth(Collection lines) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      int longestStringWidth = 0;
      Iterator var3 = lines.iterator();

      while(var3.hasNext()) {
         String line = (String)var3.next();
         int currentStringWidth = fontRenderer.func_78256_a(line);
         if (currentStringWidth > longestStringWidth) {
            longestStringWidth = currentStringWidth;
         }
      }

      return longestStringWidth;
   }

   public static Optional renderTooltip(int x, int y, int gradient1, int background, int alpha, boolean centerBox, boolean centerText, String... tooltipData) {
      Collection tooltipCollection = Arrays.asList(tooltipData);
      return renderTooltip(x, y, tooltipCollection, gradient1, background, alpha, centerBox, centerText);
   }

   public static Optional renderTooltip(int x, int y, Collection tooltipData, int gradient1, int background, int alpha, boolean centerBox, boolean centerText, float zLevel) {
      if (tooltipData.isEmpty()) {
         return Optional.empty();
      } else {
         boolean lighting = GL11.glGetBoolean(2896);
         if (lighting) {
            RenderHelper.func_74518_a();
         }

         FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
         int longestStringWidth = getLongestStringWidth(tooltipData);
         int left = x;
         int top = y;
         if (centerBox) {
            left = x - longestStringWidth / 2;
         }

         int boxHeight = 8;
         if (tooltipData.size() > 1) {
            boxHeight += (tooltipData.size() - 1) * 10;
         }

         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b(0.0F, 0.0F, 1.0F);
         background = background & 16777215 | alpha << 24;
         Gui.func_73734_a(left - 3, y - 4, left + longestStringWidth + 3, y - 3, background);
         Gui.func_73734_a(left - 3, y + boxHeight + 3, left + longestStringWidth + 3, y + boxHeight + 4, background);
         Gui.func_73734_a(left - 3, y - 3, left + longestStringWidth + 3, y + boxHeight + 3, background);
         Gui.func_73734_a(left - 4, y - 3, left - 3, y + boxHeight + 3, background);
         Gui.func_73734_a(left + longestStringWidth + 3, y - 3, left + longestStringWidth + 4, y + boxHeight + 3, background);
         int gradient2 = (gradient1 & 16777215) >> 1 | gradient1 & -16777216;
         drawGradientRect(left - 3, y - 3 + 1, zLevel, left - 3 + 1, y + boxHeight + 3 - 1, gradient1, gradient2);
         drawGradientRect(left + longestStringWidth + 2, y - 3 + 1, zLevel, left + longestStringWidth + 3, y + boxHeight + 3 - 1, gradient1, gradient2);
         drawGradientRect(left - 3, y - 3, zLevel, left + longestStringWidth + 3, y - 3 + 1, gradient1, gradient1);
         drawGradientRect(left - 3, y + boxHeight + 2, zLevel, left + longestStringWidth + 3, y + boxHeight + 3, gradient2, gradient2);

         for(Iterator var16 = tooltipData.iterator(); var16.hasNext(); top += 10) {
            String line = (String)var16.next();
            if (!centerText) {
               fontRenderer.func_175063_a(line, (float)left, (float)top, -1);
            } else {
               drawCenteredString(line, (float)(left + longestStringWidth / 2), (float)top, -1);
            }
         }

         GlStateManager.func_179121_F();
         if (!lighting) {
            RenderHelper.func_74518_a();
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         return Optional.of(new int[]{left - 4, top - 4, left + longestStringWidth + 4, top + boxHeight + 4});
      }
   }

   public static Optional renderTooltip(int x, int y, Collection tooltipData, int gradient1, int background, int alpha, boolean centerBox, boolean centerText) {
      return renderTooltip(x, y, tooltipData, gradient1, background, alpha, centerBox, centerText, 300.0F);
   }

   public static void drawString(String text, float x, float y, int color) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      fontRenderer.func_175065_a(text, x, y, color, false);
   }

   public static int getStringWidth(String text) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      return fontRenderer.func_78256_a(text);
   }

   public static void drawCenteredString(String text, float x, float y, int color) {
      drawCenteredString(text, x, y, color, false);
   }

   public static void drawCenteredString(String text, float x, float y, int color, boolean dropShadow) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      fontRenderer.func_175065_a(text, x - (float)(fontRenderer.func_78256_a(text) / 2), y, color, dropShadow);
   }

   public static void drawCenteredSplitString(String text, float x, float y, int maxLength, int color) {
      drawCenteredSplitString(text, x, y, maxLength, color, true);
   }

   public static void drawCenteredSplitString(String text, float x, float y, int maxLength, int color, boolean dropShadow) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      int textWidth = fontRenderer.func_78256_a(text);
      int numLines = Math.max(1, (int)Math.ceil((double)((float)textWidth / (float)maxLength)));
      int lineWidth = text.length() / numLines;
      List splitStrings = new ArrayList(numLines);
      int lastIndex = 0;

      int currentY;
      int i;
      for(currentY = 0; currentY < numLines - 1; ++currentY) {
         i = text.indexOf(" ", lastIndex + lineWidth);
         if (i > -1) {
            splitStrings.add(text.substring(lastIndex, i + 1));
            lastIndex = i + 1;
         }
      }

      if (lastIndex < text.length()) {
         splitStrings.add(text.substring(lastIndex));
      }

      numLines = splitStrings.size();
      currentY = (int)y - 6 * (numLines - 1);

      for(i = 0; i < numLines; ++i) {
         drawCenteredString((String)splitStrings.get(i), x, (float)currentY, color, dropShadow);
         currentY += 12;
      }

   }

   public static void drawCenteredLimitedString(String text, float x, float y, int maxLength, int color) {
      drawCenteredString(getLimitedString(text, maxLength), x, y, color, true);
   }

   public static String getLimitedString(String text, int maxLength) {
      String ellipses = "...";
      if (maxLength <= 3) {
         return ellipses;
      } else {
         FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
         int textLength = text.length();
         String drawText = text;
         if (textLength >= 4) {
            int textWidth = 0;

            for(int i = 0; i < text.length(); ++i) {
               textWidth += fontRenderer.func_78263_a(text.charAt(i));
               if (textWidth > maxLength) {
                  drawText = text.substring(0, i - 3) + ellipses;
                  break;
               }
            }
         }

         return drawText;
      }
   }

   public static List splitString(String text, int maxLength) {
      List splitString = new ArrayList();
      if (text.isEmpty()) {
         splitString.add("");
      } else {
         FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
         String[] lines = text.split("\n");
         String[] var5 = lines;
         int var6 = lines.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String line = var5[var7];
            splitString.addAll(fontRenderer.func_78271_c(line, maxLength));
         }
      }

      return splitString;
   }

   public static String removeNewLine(String string) {
      int length = string.length();
      return length > 0 && string.charAt(length - 1) == '\n' ? string.substring(0, length - 1) : string;
   }

   public static void drawGradientRect(int left, int top, float zLevel, int right, int bottom, int startColour, int endColour) {
      drawGradientRect(left, top, zLevel, right, bottom, startColour, endColour, false);
   }

   public static void drawGradientRect(int left, int top, float zLevel, int right, int bottom, int startColour, int endColour, boolean horizontal) {
      float startAlpha = (float)(startColour >> 24 & 255) / 255.0F;
      float startRed = (float)(startColour >> 16 & 255) / 255.0F;
      float startGreen = (float)(startColour >> 8 & 255) / 255.0F;
      float startBlue = (float)(startColour & 255) / 255.0F;
      float endAlpha = (float)(endColour >> 24 & 255) / 255.0F;
      float endRed = (float)(endColour >> 16 & 255) / 255.0F;
      float endGreen = (float)(endColour >> 8 & 255) / 255.0F;
      float endBlue = (float)(endColour & 255) / 255.0F;
      drawGradientRect(left, top, zLevel, right, bottom, new Vector4f(startRed, startGreen, startBlue, startAlpha), new Vector4f(endRed, endGreen, endBlue, endAlpha), horizontal);
   }

   public static void drawGradientRect(int left, int top, float zLevel, int right, int bottom, Vector4f startColour, Vector4f endColour, boolean horizontal) {
      float startAlpha = startColour.getW();
      float startRed = startColour.getX();
      float startGreen = startColour.getY();
      float startBlue = startColour.getZ();
      float endAlpha = endColour.getW();
      float endRed = endColour.getX();
      float endGreen = endColour.getY();
      float endBlue = endColour.getZ();
      GlStateManager.func_179090_x();
      GlStateManager.func_179147_l();
      GlStateManager.func_179118_c();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179103_j(7425);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      buffer.func_181662_b((double)left, (double)top, (double)zLevel).func_181666_a(startRed, startGreen, startBlue, startAlpha).func_181675_d();
      if (horizontal) {
         buffer.func_181662_b((double)left, (double)bottom, (double)zLevel).func_181666_a(startRed, startGreen, startBlue, startAlpha).func_181675_d();
      } else {
         buffer.func_181662_b((double)left, (double)bottom, (double)zLevel).func_181666_a(endRed, endGreen, endBlue, endAlpha).func_181675_d();
      }

      buffer.func_181662_b((double)right, (double)bottom, (double)zLevel).func_181666_a(endRed, endGreen, endBlue, endAlpha).func_181675_d();
      if (horizontal) {
         buffer.func_181662_b((double)right, (double)top, (double)zLevel).func_181666_a(endRed, endGreen, endBlue, endAlpha).func_181675_d();
      } else {
         buffer.func_181662_b((double)right, (double)top, (double)zLevel).func_181666_a(startRed, startGreen, startBlue, startAlpha).func_181675_d();
      }

      tessellator.func_78381_a();
      GlStateManager.func_179103_j(7424);
      GlStateManager.func_179084_k();
      GlStateManager.func_179141_d();
      GlStateManager.func_179098_w();
   }

   public static void drawBar(double x, double y, double width, double height, float percent, Color color) {
      drawBar(x, y, width, height, percent, color, false);
   }

   public static void drawBar(double x, double y, double width, double height, float percent, Color color, boolean flip) {
      GlStateManager.func_179091_B();
      GlStateManager.func_179142_g();
      GlStateManager.func_179094_E();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      GlStateManager.func_179090_x();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      double barWidth = width - 6.0;
      buffer.func_181662_b(x, y, 0.0).func_181666_a(0.4F, 0.4F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b(x, y + height, 0.0).func_181666_a(0.4F, 0.4F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b(x + barWidth, y + height, 0.0).func_181666_a(0.4F, 0.4F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b(x + barWidth, y, 0.0).func_181666_a(0.4F, 0.4F, 0.4F, 1.0F).func_181675_d();
      if (percent > 1.0F) {
         percent = 1.0F;
      }

      float curWidth = (float)((double)percent * barWidth);
      if (flip) {
         buffer.func_181662_b(x + (barWidth - (double)curWidth), y, 0.0).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).func_181675_d();
         buffer.func_181662_b(x + (barWidth - (double)curWidth), y + height, 0.0).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).func_181675_d();
         buffer.func_181662_b(x + barWidth, y + height, 0.0).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).func_181675_d();
         buffer.func_181662_b(x + barWidth, y, 0.0).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).func_181675_d();
      } else {
         buffer.func_181662_b(x, y, 0.0).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).func_181675_d();
         buffer.func_181662_b(x, y + height, 0.0).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).func_181675_d();
         buffer.func_181662_b(x + (double)curWidth, y + height, 0.0).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).func_181675_d();
         buffer.func_181662_b(x + (double)curWidth, y, 0.0).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).func_181675_d();
      }

      tessellator.func_78381_a();
      GlStateManager.func_179121_F();
      GlStateManager.func_179098_w();
      GlStateManager.func_179101_C();
      GlStateManager.func_179119_h();
   }

   public static void drawRectWithSemicircleEnds(double x, double y, double z, double w, double h, int segments, Color color) {
      prepareSmoothDrawing(color);
      double segment = Math.PI / (double)segments;
      double rad = h / 2.0;
      GlStateManager.func_179137_b(x, y, z);

      for(int pass = 1; pass <= 2; ++pass) {
         if (pass == 2 && color == Color.WHITE) {
            GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA_SATURATE, DestFactor.ONE);
         } else {
            GlStateManager.func_179112_b(770, 771);
         }

         GL11.glBegin(6);

         int i;
         double dx;
         double dy;
         for(i = 0; i <= segments; ++i) {
            dx = -Math.sin(segment * (double)i) * rad;
            dy = Math.cos(segment * (double)i) * rad;
            GL11.glVertex3d(dx, dy + rad, 0.0);
         }

         GL11.glEnd();
         GL11.glBegin(6);

         for(i = 0; i <= segments; ++i) {
            dx = Math.sin(segment * (double)i) * rad;
            dy = Math.cos(segment * (double)i) * rad;
            GL11.glVertex3d(w + dx, dy + rad, 0.0);
         }

         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(0.0, 0.0, 0.0);
         GL11.glVertex3d(w, 0.0, 0.0);
         GL11.glVertex3d(w, h, 0.0);
         GL11.glVertex3d(0.0, h, 0.0);
         GL11.glEnd();
      }

      endSmoothDrawing();
   }

   public static void drawCircle(double x, double y, double z, double d, int segments, Color color) {
      drawEllipse(x, y, z, d, d, segments, color);
   }

   public static void drawEllipse(double x, double y, double z, double w, double h, int segments, Color color) {
      prepareSmoothDrawing(color);
      double segment = 6.283185307179586 / (double)segments;
      double wr = w / 2.0;
      double hr = h / 2.0;
      GlStateManager.func_179137_b(x, y, z);

      for(int pass = 1; pass <= 2; ++pass) {
         if (pass == 2 && color == Color.WHITE) {
            GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA_SATURATE, DestFactor.ONE);
         } else {
            GlStateManager.func_179112_b(770, 771);
         }

         GL11.glBegin(6);

         for(int i = 0; i <= segments; ++i) {
            double dx = Math.sin(segment * (double)i) * wr;
            double dy = Math.cos(segment * (double)i) * hr;
            GL11.glVertex3d(dx + wr, dy + hr, 0.0);
         }

         GL11.glEnd();
      }

      endSmoothDrawing();
   }

   public static void prepareSmoothDrawing(Color color) {
      GlStateManager.func_179094_E();
      GL11.glEnable(2881);
      GL11.glHint(3155, 4354);
      GlStateManager.func_179129_p();
      GlStateManager.func_179090_x();
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      GlStateManager.func_179097_i();
      GlStateManager.func_179132_a(false);
   }

   public static void drawQuad(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Color color) {
      prepareSmoothDrawing(color);

      for(int pass = 1; pass <= 2; ++pass) {
         if (pass == 2 && color == Color.WHITE) {
            GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA_SATURATE, DestFactor.ONE);
         } else {
            GlStateManager.func_179112_b(770, 771);
         }

         GL11.glBegin(7);
         GL11.glVertex3d(x1, y1, z1);
         GL11.glVertex3d(x2, y2, z2);
         GL11.glVertex3d(x3, y3, z3);
         GL11.glVertex3d(x4, y4, z4);
         GL11.glEnd();
      }

      endSmoothDrawing();
   }

   public static void endSmoothDrawing() {
      GL11.glDisable(2881);
      GlStateManager.func_179098_w();
      GlStateManager.func_179121_F();
      GlStateManager.func_179112_b(770, 771);
   }

   public static void drawEntity(EntityLivingBase ent, int x, int y, float scale, float rotationYaw, float rotationPitch) {
      GlStateManager.func_179142_g();
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)x, (float)y, 50.0F);
      GlStateManager.func_179152_a(-scale, scale, scale);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      float f2 = ent.field_70761_aq;
      float f3 = ent.field_70177_z;
      float f4 = ent.field_70125_A;
      float f5 = ent.field_70758_at;
      float f6 = ent.field_70759_as;
      GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.func_74519_b();
      GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-((float)Math.atan((double)(rotationPitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
      ent.field_70761_aq = (float)Math.atan((double)(rotationYaw / 40.0F)) * 20.0F;
      ent.field_70177_z = (float)Math.atan((double)(rotationYaw / 40.0F)) * 40.0F;
      ent.field_70125_A = -((float)Math.atan((double)(rotationPitch / 40.0F))) * 20.0F;
      ent.field_70759_as = ent.field_70177_z;
      ent.field_70758_at = ent.field_70177_z;
      GlStateManager.func_179109_b(0.0F, (float)ent.func_70033_W(), 0.0F);
      RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
      renderManager.field_78735_i = 180.0F;
      renderManager.func_188391_a(ent, 0.0, 0.0, 0.0, 0.0F, 1.0F, false);
      ent.field_70761_aq = f2;
      ent.field_70177_z = f3;
      ent.field_70125_A = f4;
      ent.field_70758_at = f5;
      ent.field_70759_as = f6;
      GlStateManager.func_179121_F();
      RenderHelper.func_74518_a();
      GlStateManager.func_179101_C();
      GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
      GlStateManager.func_179090_x();
      GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
   }

   public static void drawPokemonInfoChooseMove(PixelmonInGui pokemon, int width, int height, float zLevel) {
      Minecraft mc = Minecraft.func_71410_x();
      bindPokemonSprite(pokemon, mc);
      drawImageQuad((double)(width / 2 - 114), (double)(height / 2 - 76), 64.0, 64.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
      drawCenteredString(pokemon.getDisplayName(), (float)(width / 2 - 82), (float)(height / 2 + 8), 16777215);
      EnumType type1 = pokemon.getBaseStats().getType1();
      EnumType type2 = pokemon.getBaseStats().getType2();
      float x = type1.textureX;
      float y = type1.textureY;
      float x1 = 0.0F;
      float y1 = 0.0F;
      if (type2 != null) {
         x1 = type2.textureX;
         y1 = type2.textureY;
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      mc.field_71446_o.func_110577_a(GuiResources.types);
      if (type2 != null && type2 != EnumType.Mystery) {
         drawImageQuad((double)(width / 2 - 83), (double)(height / 2 - 84), 21.0, 21.0F, (double)(x1 / 1792.0F), (double)(y1 / 768.0F), (double)((x1 + 240.0F) / 1792.0F), (double)((y1 + 240.0F) / 768.0F), zLevel);
         drawImageQuad((double)(width / 2 - 107), (double)(height / 2 - 84), 21.0, 21.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), zLevel);
      } else {
         drawImageQuad((double)(width / 2 - 93), (double)(height / 2 - 84), 21.0, 21.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), zLevel);
      }

      mc.field_71466_p.func_78276_b(I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + pokemon.level, width / 2 - 80, height / 2 - 94, 16777215);
      mc.field_71466_p.func_78276_b(I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " " + pokemon.getDexNumber(), width / 2 - 122, height / 2 - 94, 16777215);
   }

   public static void closeScreen() {
      Keyboard.enableRepeatEvents(false);
      Minecraft.func_71410_x().field_71439_g.func_71053_j();
   }

   public static void switchFocus(int keyInt, List textFields) {
      GuiTextField[] textFieldArray = new GuiTextField[textFields.size()];
      switchFocus(keyInt, (GuiTextField[])textFields.toArray(textFieldArray));
   }

   public static void switchFocus(int keyInt, GuiTextField... textFields) {
      if (keyInt == 15) {
         for(int i = 0; i < textFields.length; ++i) {
            GuiTextField textField = textFields[i];
            if (textField.func_146206_l()) {
               textField.func_146195_b(false);
               textFields[(i + 1) % textFields.length].func_146195_b(true);
               return;
            }
         }

         textFields[0].func_146195_b(true);
      }

   }

   public static void drawAttackInfoBox(float zLevel, int width, int height) {
      Minecraft mc = Minecraft.func_71410_x();
      mc.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GlStateManager.func_179124_c(0.5F, 0.5F, 0.5F);
      drawImageQuad((double)(width / 2 + 100), (double)(height / 2 - 90), 100.0, 140.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      String text = I18n.func_135052_a("gui.choosemoveset.attackdetails", new Object[0]);
      mc.field_71466_p.func_78276_b(text, width + 150 - mc.field_71466_p.func_78256_a(text) / 2, height - 92, 0);
   }

   public static void drawAttackInfoList(Attack a, int width, int height) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      int y = height / 2 - 85;
      int x = width / 2 + 105;
      String powerString = I18n.func_135052_a("gui.choosemoveset.power", new Object[0]) + ": ";
      if (a.getMove().getBasePower() > 0) {
         powerString = powerString + a.getMove().getBasePower();
      } else {
         powerString = powerString + "--";
      }

      fontRenderer.func_78276_b(powerString, x, y + 3, 0);
      String accuracyString = I18n.func_135052_a("gui.battle.accuracy", new Object[0]) + ": ";
      if (a.getMove().getAccuracy() > 0) {
         accuracyString = accuracyString + a.getMove().getAccuracy();
      } else {
         accuracyString = accuracyString + "--";
      }

      fontRenderer.func_78276_b(accuracyString, x, y + 13, 0);
      fontRenderer.func_78276_b(I18n.func_135052_a("nbt.pp.name", new Object[0]) + " " + a.pp + "/" + a.getMaxPP(), x, y + 23, 0);
      String typeString = I18n.func_135052_a("gui.battle.type", new Object[0]) + " ";
      fontRenderer.func_78276_b(typeString, x, y + 33, 0);
      fontRenderer.func_78276_b(a.getMove().getAttackType().getLocalizedName(), x + fontRenderer.func_78256_a(typeString), y + 33, a.getMove().getAttackType().getColor());
      String category = a.getMove().getAttackCategory().getLocalizedName();
      fontRenderer.func_78276_b(category, x, y + 43, 0);
      fontRenderer.func_78279_b(I18n.func_135052_a("attack." + a.getMove().getAttackName().replace(" ", "_").toLowerCase() + ".description", new Object[0]), x, y + 58, 95, 0);
   }

   public static void drawAttackInfoMoveset(Attack attack, int y, int width, int height) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      fontRenderer.func_78276_b(I18n.func_135052_a("gui.replaceattack.power", new Object[0]), width / 2 - 120, height / 2 + 60, 16777215);
      fontRenderer.func_78276_b(I18n.func_135052_a("gui.replaceattack.accuracy", new Object[0]), width / 2 - 120, height / 2 + 70, 16777215);
      int bpextra = 0;
      int acextra = 0;
      if (attack.getMove().getBasePower() >= 100) {
         bpextra = fontRenderer.func_78263_a('0');
      }

      if (attack.getMove().getAccuracy() >= 100) {
         acextra = fontRenderer.func_78263_a('0');
      }

      String powerString = attack.getMove().getBasePower() > 0 ? "" + attack.getMove().getBasePower() : "--";
      fontRenderer.func_78276_b(powerString, width / 2 - 60 - bpextra, height / 2 + 60, 16777215);
      String accuracyString = attack.getMove().getAccuracy() > 0 ? "" + attack.getMove().getAccuracy() : "--";
      fontRenderer.func_78276_b(accuracyString, width / 2 - 60 - acextra, height / 2 + 70, 16777215);
      fontRenderer.func_78276_b(attack.getMove().getAttackCategory().getLocalizedName(), width / 2 - 120, height / 2 + 80, 16777215);
      fontRenderer.func_78279_b(I18n.func_135052_a("attack." + attack.getMove().getAttackName().toLowerCase().replace(" ", "_") + ".description", new Object[0]), width / 2 - 20, y, 135, 16777215);
   }

   public static void drawMoveset(Moveset moveset, int width, int height, float zLevel) {
      Minecraft mc = Minecraft.func_71410_x();

      for(int i = 0; i < moveset.size(); ++i) {
         Attack move = moveset.get(i);
         mc.field_71466_p.func_78276_b(move.getMove().getLocalizedName(), width / 2 + 11, height / 2 - 85 + 22 * i, 16777215);
         mc.field_71466_p.func_78276_b(move.pp + "/" + move.getMaxPP(), width / 2 + 90, height / 2 - 83 + 22 * i, 16777215);
         float x = move.getMove().getAttackType().textureX;
         float y = move.getMove().getAttackType().textureY;
         mc.field_71446_o.func_110577_a(GuiResources.types);
         drawImageQuad((double)(width / 2 - 23), (double)(height / 2 - 92 + 22 * i), 21.0, 21.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), zLevel);
      }

   }

   public static int toColourValue(float red, float green, float blue, float alpha) {
      int r = (int)(red * 255.0F) & 255;
      int g = (int)(green * 255.0F) & 255;
      int b = (int)(blue * 255.0F) & 255;
      int a = (int)(alpha * 255.0F) & 255;
      int rgb = (a << 24) + (r << 16) + (b << 8) + g;
      return rgb;
   }

   public static EntityPixelmon getEntity(UUID pokemonUUID) {
      Minecraft mc = Minecraft.func_71410_x();

      for(int i = 0; i < mc.field_71441_e.field_72996_f.size(); ++i) {
         Entity e = (Entity)mc.field_71441_e.field_72996_f.get(i);
         if (e instanceof EntityPixelmon) {
            EntityPixelmon pokemon = (EntityPixelmon)e;
            if (pokemon.getPokemonData().getUUID().equals(pokemonUUID)) {
               return pokemon;
            }
         }
      }

      return null;
   }

   public static void drawDialogueBox(GuiScreen screen, String name, List chatText, float zLevel) {
      drawDialogueBox(screen, name, String.join("\n", chatText), zLevel);
   }

   public static void drawDialogueBox(GuiScreen screen, ITextComponent name, ITextComponent chatText, float zLevel) {
      drawDialogueBox(screen, name.func_150254_d(), chatText.func_150254_d(), zLevel);
   }

   public static void drawDialogueBox(GuiScreen screen, String name, String chatText, float zLevel) {
      screen.field_146297_k.field_71446_o.func_110577_a(GuiResources.evo);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      drawImageQuad((double)(screen.field_146294_l / 2 - 120), (double)(screen.field_146295_m / 4 - 40), 240.0, 80.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
      screen.field_146297_k.field_71466_p.func_78276_b(name, screen.field_146294_l / 2 - 107, screen.field_146295_m / 4 - 32, 16777215);
      int index = 0;
      Iterator var5 = screen.field_146297_k.field_71466_p.func_78271_c(chatText, 200).iterator();

      while(var5.hasNext()) {
         String text = (String)var5.next();
         screen.field_146297_k.field_71466_p.func_78276_b(text, screen.field_146294_l / 2 - screen.field_146297_k.field_71466_p.func_78256_a(text) / 2, screen.field_146295_m / 4 - 20 + index++ * 12, 16777215);
      }

   }

   public static void drawBattleTimer(GuiScreen gui, int time) {
      time = Math.max(time, 0);
      String timerString = I18n.func_135052_a("battlecontroller.afktimer", new Object[0]) + time;
      int color = time <= 5 ? 16711680 : 16777215;
      gui.func_73731_b(gui.field_146297_k.field_71466_p, timerString, gui.field_146294_l - gui.field_146297_k.field_71466_p.func_78256_a(timerString) - 5, 5, color);
   }

   public static void drawPokemonHoverInfo(Pokemon pokemon, int x, int y) {
      String form = "";
      if (pokemon.getCustomTexture() != "") {
         form = pokemon.getCustomTexture().substring(0, 1).toUpperCase() + pokemon.getCustomTexture().substring(1);
         if (I18n.func_188566_a("customtexture." + pokemon.getCustomTexture() + ".name")) {
            form = I18n.func_135052_a("customtexture." + pokemon.getCustomTexture() + ".name", new Object[0]);
         }

         form = I18n.func_135052_a("gui.screenpokechecker.form", new Object[]{form});
      } else if (I18n.func_188566_a(pokemon.getFormEnum().getUnlocalizedName()) && !I18n.func_135052_a(pokemon.getFormEnum().getUnlocalizedName(), new Object[0]).equals(I18n.func_135052_a("gui.trainereditor.normal", new Object[0])) && !I18n.func_135052_a(pokemon.getFormEnum().getUnlocalizedName(), new Object[0]).equals(I18n.func_135052_a("pixelmon.generic.form.noform", new Object[0]))) {
         form = I18n.func_135052_a("gui.screenpokechecker.form", new Object[]{I18n.func_135052_a(pokemon.getFormEnum().getUnlocalizedName(), new Object[0])});
      }

      if (x - 104 < 0) {
         x = 104;
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (!Objects.equals(form, "") && !pokemon.isEgg()) {
         GuiScreen.func_73734_a(x - 104, y - 2, x + 2, y + 30, -1437248170);
      } else {
         GuiScreen.func_73734_a(x - 104, y - 2, x + 2, y + 20, -1437248170);
      }

      String name = pokemon.getDisplayName();
      Minecraft.func_71410_x().field_71466_p.func_78276_b(name, x - 102, y, 16777215);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(GuiResources.pixelmonOverlay);
      if (!pokemon.isEgg()) {
         if (pokemon.getGender() != Gender.None) {
            if (pokemon.getGender() == Gender.Male) {
               Minecraft.func_71410_x().field_71446_o.func_110577_a(GuiResources.male);
            } else if (pokemon.getGender() == Gender.Female) {
               Minecraft.func_71410_x().field_71446_o.func_110577_a(GuiResources.female);
            }

            drawImageQuad((double)(Minecraft.func_71410_x().field_71466_p.func_78256_a(name) + x - 101), (double)y, 5.0, 8.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
         }

         int y2 = y;
         if (form != "") {
            Minecraft.func_71410_x().field_71466_p.func_78276_b(form, x - 102, y + Minecraft.func_71410_x().field_71466_p.field_78288_b, 16777215);
            y2 = y + Minecraft.func_71410_x().field_71466_p.field_78288_b;
         }

         String level = I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + pokemon.getLevel();
         Minecraft.func_71410_x().field_71466_p.func_78276_b(level, x - 101, y2 + Minecraft.func_71410_x().field_71466_p.field_78288_b + 1, 16777215);
         String health = pokemon.getHealth() > 0 ? I18n.func_135052_a("nbt.hp.name", new Object[0]) + " " + pokemon.getHealth() + "/" + pokemon.getMaxHealth() : I18n.func_135052_a("gui.creativeinv.fainted", new Object[0]);
         Minecraft.func_71410_x().field_71466_p.func_78276_b(health, x - 97 + Minecraft.func_71410_x().field_71466_p.func_78256_a(level), y2 + Minecraft.func_71410_x().field_71466_p.field_78288_b + 1, 16777215);
         if (!pokemon.getHeldItem().func_190926_b()) {
            Minecraft.func_71410_x().func_175599_af().func_175042_a(pokemon.getHeldItem(), x - 16, y - 1);
         }
      }

   }

   public static void bindPokeballTexture(EnumPokeballs ball) {
      ResourceLocation tex = null;
      switch (ball) {
         case DiveBall:
            tex = GuiResources.dive;
            break;
         case FastBall:
            tex = GuiResources.fast;
            break;
         case DuskBall:
            tex = GuiResources.dusk;
            break;
         case FriendBall:
            tex = GuiResources.friend;
            break;
         case GreatBall:
            tex = GuiResources.great;
            break;
         case HealBall:
            tex = GuiResources.heal;
            break;
         case HeavyBall:
            tex = GuiResources.heavy;
            break;
         case LevelBall:
            tex = GuiResources.level;
            break;
         case LoveBall:
            tex = GuiResources.love;
            break;
         case LuxuryBall:
            tex = GuiResources.luxury;
            break;
         case MasterBall:
            tex = GuiResources.master;
            break;
         case MoonBall:
            tex = GuiResources.moon;
            break;
         case NestBall:
            tex = GuiResources.nest;
            break;
         case NetBall:
            tex = GuiResources.net;
            break;
         case DreamBall:
            tex = GuiResources.dream;
            break;
         case PokeBall:
            tex = GuiResources.poke;
            break;
         case PremierBall:
            tex = GuiResources.premier;
            break;
         case RepeatBall:
            tex = GuiResources.repeat;
            break;
         case SafariBall:
            tex = GuiResources.safari;
            break;
         case TimerBall:
            tex = GuiResources.timer;
            break;
         case UltraBall:
            tex = GuiResources.ultra;
            break;
         case CherishBall:
            tex = GuiResources.cherish;
            break;
         case GSBall:
            tex = GuiResources.gs;
            break;
         case LureBall:
            tex = GuiResources.lure;
            break;
         case ParkBall:
            tex = GuiResources.park;
            break;
         case QuickBall:
            tex = GuiResources.quick;
            break;
         case SportBall:
            tex = GuiResources.sport;
            break;
         case BeastBall:
            tex = GuiResources.beast;
      }

      Minecraft.func_71410_x().field_71446_o.func_110577_a(tex);
   }

   public static void drawScaledCenteredSplitString(String text, float x, float y, int color, float fontSize, int maxLength, boolean dropShadow) {
      float scalar = fontSize / 16.0F;
      GlStateManager.func_179152_a(scalar, scalar, 1.0F);
      drawCenteredSplitString(text, x / scalar, y / scalar, maxLength, color, dropShadow);
      GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
      GlStateManager.func_179152_a(1.0F / scalar, 1.0F / scalar, 1.0F);
   }

   public static void drawScaledCenteredSplitString(String text, float x, float y, int color, float fontSize, int maxLength) {
      drawScaledCenteredSplitString(text, x, y, color, fontSize, maxLength, true);
   }

   public static GuiScreen getCurrentScreen() {
      return Minecraft.func_71410_x().field_71462_r;
   }

   public static void bindTexture(ResourceLocation resourceLocation) {
      Minecraft.func_71410_x().func_110434_K().func_110577_a(resourceLocation);
   }

   public static void drawBattleCursor(boolean draw, float x, float y, float z) {
      if (draw) {
         CursorHelper.setCursor(CursorHelper.TRANSPARENT_CURSOR);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         Minecraft.func_71410_x().func_110434_K().func_110577_a(GuiResources.battlePointer);
         drawImageQuad((double)x, (double)y, 10.5, 9.5F, 0.0, 0.0, 1.0, 1.0, z + 3.0F);
      } else {
         CursorHelper.setCursor(CursorHelper.DEFAULT_CURSOR);
      }

   }

   public static void disableMinecraftUI() {
      Minecraft.func_71410_x().field_71474_y.field_74319_N = true;
      GuiIngameForge.renderHotbar = false;
      GuiIngameForge.renderObjective = false;
      GuiIngameForge.renderHealth = false;
      GuiIngameForge.renderArmor = false;
      GuiIngameForge.renderAir = false;
      GuiIngameForge.renderExperiance = false;
      GuiIngameForge.renderHealthMount = false;
      GuiIngameForge.renderFood = false;
   }

   public static void enableMinecraftUI() {
      Minecraft.func_71410_x().field_71474_y.field_74319_N = false;
      GuiIngameForge.renderHotbar = true;
      GuiIngameForge.renderObjective = true;
      GuiIngameForge.renderHealth = true;
      GuiIngameForge.renderArmor = true;
      GuiIngameForge.renderAir = true;
      GuiIngameForge.renderExperiance = true;
      GuiIngameForge.renderHealthMount = true;
      GuiIngameForge.renderFood = true;
   }
}
