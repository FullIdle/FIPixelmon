package com.pixelmonmod.pixelmon.client.render.custom;

import java.awt.Color;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class FontRendererPixelmon extends FontRenderer {
   private static FontRendererPixelmon instance;
   private FontRenderer fontRenderer;
   private boolean optifine;

   public FontRendererPixelmon(GameSettings gameSettingsIn, TextureManager textureManagerIn, FontRenderer fontRenderer) {
      super(gameSettingsIn, fontRenderer.field_111273_g, textureManagerIn, fontRenderer.field_78293_l);
      this.fontRenderer = fontRenderer;

      try {
         Class.forName("Config");
         this.optifine = true;
      } catch (Exception var5) {
         this.optifine = false;
      }

   }

   public FontRendererPixelmon(FontRenderer fontRenderer, ResourceLocation font) {
      super(Minecraft.func_71410_x().field_71474_y, font, Minecraft.func_71410_x().field_71446_o, fontRenderer.field_78293_l);
      this.fontRenderer = fontRenderer;

      try {
         Class.forName("Config");
         this.optifine = true;
      } catch (Exception var4) {
         this.optifine = false;
      }

   }

   public void func_78255_a(String text, boolean shadow) {
      for(int i = 0; i < text.length(); ++i) {
         char c0 = text.charAt(i);
         int i1;
         int j1;
         float f;
         if (c0 == 167 && i + 1 < text.length()) {
            if (i + 7 < text.length() && text.charAt(i + 1) == '#') {
               String colorCode = text.substring(i + 2, i + 8);
               boolean carryOn = true;
               char[] var16 = colorCode.toCharArray();
               int var18 = var16.length;

               for(int var9 = 0; var9 < var18; ++var9) {
                  char colorCodeChar = var16[var9];
                  int test = "0123456789abcdefABCDEF".indexOf(colorCodeChar);
                  if (test == -1) {
                     carryOn = false;
                     break;
                  }
               }

               if (carryOn) {
                  this.field_78303_s = false;
                  this.field_78302_t = false;
                  this.field_78299_w = false;
                  this.field_78300_v = false;
                  this.field_78301_u = false;
                  Color color = Color.decode("0x" + colorCode);
                  f = 3.3F;
                  int shadowMin = 42;
                  if (shadow) {
                     color = new Color((int)Math.max((float)shadowMin, (float)color.getRed() / f), (int)Math.max((float)shadowMin, (float)color.getBlue() / f), (int)Math.max((float)shadowMin, (float)color.getGreen() / f), (int)this.field_78305_q);
                  }

                  this.setColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, this.field_78305_q);
                  this.field_78304_r = color.getRGB();
                  i += 7;
               }
            } else {
               i1 = "0123456789abcdefklmnor".indexOf(String.valueOf(text.charAt(i + 1)).toLowerCase(Locale.ROOT).charAt(0));
               if (i1 < 16) {
                  this.field_78303_s = false;
                  this.field_78302_t = false;
                  this.field_78299_w = false;
                  this.field_78300_v = false;
                  this.field_78301_u = false;
                  if (i1 < 0 || i1 > 15) {
                     i1 = 15;
                  }

                  if (shadow) {
                     i1 += 16;
                  }

                  j1 = this.field_78285_g[i1];
                  this.field_78304_r = j1;
                  this.setColor((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, this.field_78305_q);
               } else if (i1 == 16) {
                  this.field_78303_s = true;
               } else if (i1 == 17) {
                  this.field_78302_t = true;
               } else if (i1 == 18) {
                  this.field_78299_w = true;
               } else if (i1 == 19) {
                  this.field_78300_v = true;
               } else if (i1 == 20) {
                  this.field_78301_u = true;
               } else if (i1 == 21) {
                  this.field_78303_s = false;
                  this.field_78302_t = false;
                  this.field_78299_w = false;
                  this.field_78300_v = false;
                  this.field_78301_u = false;
                  this.setColor(this.field_78291_n, this.field_78292_o, this.field_78306_p, this.field_78305_q);
               }

               ++i;
            }
         } else {
            i1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c0);
            if (this.field_78303_s && i1 != -1) {
               j1 = this.func_78263_a(c0);

               char c1;
               do {
                  i1 = this.field_78289_c.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".length());
                  c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".charAt(i1);
               } while(j1 != this.func_78263_a(c1));

               c0 = c1;
            }

            float f1 = i1 != -1 && !this.field_78293_l && !this.optifine ? 1.0F : 0.5F;
            boolean flag = (c0 == 0 || i1 == -1 || this.field_78293_l) && shadow;
            if (flag) {
               this.field_78295_j -= f1;
               this.field_78296_k -= f1;
            }

            f = this.func_181559_a(c0, this.field_78301_u);
            if (flag) {
               this.field_78295_j += f1;
               this.field_78296_k += f1;
            }

            if (this.field_78302_t) {
               this.field_78295_j += f1;
               if (flag) {
                  this.field_78295_j -= f1;
                  this.field_78296_k -= f1;
               }

               this.func_181559_a(c0, this.field_78301_u);
               this.field_78295_j -= f1;
               if (flag) {
                  this.field_78295_j += f1;
                  this.field_78296_k += f1;
               }

               ++f;
            }

            this.doDraw(f);
         }
      }

   }

   public int func_78256_a(String text) {
      if (text == null) {
         return 0;
      } else {
         int i = 0;
         boolean flag = false;

         for(int j = 0; j < text.length(); ++j) {
            char c0 = text.charAt(j);
            int k = this.func_78263_a(c0);
            if (j + 1 != text.length() && c0 == 167 && text.charAt(j + 1) == '#') {
               j += 7;
            } else {
               if (k < 0 && j < text.length() - 1) {
                  ++j;
                  c0 = text.charAt(j);
                  if (c0 != 'l' && c0 != 'L') {
                     if (c0 == 'r' || c0 == 'R') {
                        flag = false;
                     }
                  } else {
                     flag = true;
                  }

                  k = 0;
               }

               i += k;
               if (flag && k > 0) {
                  ++i;
               }
            }
         }

         return i;
      }
   }

   public static void init() {
      Minecraft mc = Minecraft.func_71410_x();
      instance = new FontRendererPixelmon(mc.field_71474_y, mc.field_71446_o, mc.field_71466_p);
      if (mc.field_71474_y.field_74363_ab != null) {
         instance.func_78264_a(mc.func_152349_b());
         instance.func_78275_b(mc.field_135017_as.func_135044_b());
      }

      mc.field_110451_am.func_110542_a(instance);
   }

   public static FontRendererPixelmon getInstance() {
      return instance;
   }
}
