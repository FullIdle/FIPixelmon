package com.pixelmonmod.tcg.duel;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.api.card.personalization.CardBack;
import com.pixelmonmod.tcg.api.registries.CardBackRegistry;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.TCGResources;
import com.pixelmonmod.tcg.helper.CardHelper;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

public class RenderCard {
   public static Minecraft mc = Minecraft.func_71410_x();

   @SideOnly(Side.CLIENT)
   public static void startQuads() {
      GlStateManager.func_179123_a();
      GlStateManager.func_179094_E();
      GlStateManager.func_179147_l();
      GlStateManager.func_179140_f();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179129_p();
      int bright = 240;
      int brightX = bright % 65536;
      int brightY = bright / 65536;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)brightX, (float)brightY);
   }

   @SideOnly(Side.CLIENT)
   public static void endQuads() {
      GlStateManager.func_179084_k();
      GlStateManager.func_179089_o();
      GlStateManager.func_179145_e();
      GlStateManager.func_179099_b();
      GlStateManager.func_179121_F();
   }

   public static Tessellator te() {
      return Tessellator.func_178181_a();
   }

   public static BufferBuilder wr() {
      return te().func_178180_c();
   }

   @SideOnly(Side.CLIENT)
   public static void drawCardBase(double x, double y, double z, CommonCardState c, boolean isRotated, PlayerCommonState me, PlayerCommonState opp) {
      if (c != null) {
         if (c.getCardType().isCosmetic()) {
            mc.field_71446_o.func_110577_a(CardHelper.getCosmeticBackgroundTexture(c));
         }

         if (c.hasTransformation()) {
            mc.field_71446_o.func_110577_a(CardHelper.getBackgroundTexture(c.getTransformation().getCardType(), c.getTransformation().getMainEnergy(), c.getTransformation().isSpecial()));
         } else {
            mc.field_71446_o.func_110577_a(CardHelper.getBackgroundTexture(c.getData().getCardType(), c.getMainEnergy(), c.getData().isSpecial()));
         }
      } else if (me != null) {
         CardBack cardBack = CardBackRegistry.get(me.getCardBackID());
         if (cardBack == null) {
            cardBack = CardBackRegistry.STANDARD;
         }

         mc.field_71446_o.func_110577_a(cardBack.getFile());
      } else {
         mc.field_71446_o.func_110577_a(CardBackRegistry.STANDARD.getFile());
      }

      startQuads();
      if (isRotated) {
         handleRotation(x, y, z, 90, 1.0F);
      }

      wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
      wr().func_181662_b(x, y, z).func_187315_a(1.0, 1.0).func_181675_d();
      wr().func_181662_b(x + 2.0, y, z).func_187315_a(1.0, 0.0).func_181675_d();
      wr().func_181662_b(x + 2.0, y, z - 1.5).func_187315_a(0.0, 0.0).func_181675_d();
      wr().func_181662_b(x, y, z - 1.5).func_187315_a(0.0, 1.0).func_181675_d();
      te().func_78381_a();
      endQuads();
   }

   public static void handleRotation(double x, double y, double z, int rotation, float scale) {
      if (rotation == 90) {
         GL11.glTranslated(x + 1.5, y, z);
         GL11.glRotated(90.0, 0.0, 1.0, 0.0);
         GL11.glScalef(scale, 1.0F, scale);
         GL11.glTranslated(-x, -y, -z);
      } else if (rotation == 270) {
         GL11.glTranslated(x + 1.5, y, z);
         GL11.glRotated(270.0, 0.0, 1.0, 0.0);
         GL11.glScalef(scale, 1.0F, scale);
         GL11.glTranslated(-x, -y, -z);
      } else if (rotation != 180) {
         GL11.glTranslated(x + 1.0, y, z - 0.75);
         GL11.glRotated(180.0, 0.0, 1.0, 0.0);
         GL11.glTranslated(-x - 1.0, -y, -z + 0.75);
      }

   }

   @SideOnly(Side.CLIENT)
   public static void drawCard(double x, double y, double z, CommonCardState card, int rotation, FontRenderer f, float t, PlayerCommonState me, PlayerCommonState opp) {
      if (card != null) {
         startQuads();
         handleRotation(x, y, z, rotation, 1.0F);
         drawCardBase(x, y, z, card, false, me, opp);
         drawName(x, y, z, card, f);
         drawStage(x, y, z, card);
         GlStateManager.func_179123_a();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         drawType(x, y, z, card);
         GlStateManager.func_179084_k();
         GlStateManager.func_179099_b();
         if (card instanceof PokemonCardState) {
            GlStateManager.func_179123_a();
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b(770, 771);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            drawEffects(x, y, z, (PokemonCardState)card, f);
            GlStateManager.func_179084_k();
            GlStateManager.func_179099_b();
         } else if (StringUtils.isNotBlank(card.getDescription())) {
            String cardText = card.getDescription();
            double dx = 1.02;
            double dz = -1.39;
            drawUnlocalizedString(f, x + dx, y - 2.0 + 1.0E-4, z + dz, cardText, 2105376, true, 90, 1.0F, true, 0, "");
         }

         drawHP(x, y, z, card, f);
         drawSprite(x, y, z, card);
         if (card instanceof PokemonCardState) {
            PokemonCardState pokemon = (PokemonCardState)card;
            GlStateManager.func_179123_a();
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b(770, 771);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            drawWeakness(x, y, z, pokemon, f);
            drawResistance(x, y, z, pokemon, f);
            GlStateManager.func_179084_k();
            GlStateManager.func_179099_b();
         }

         GlStateManager.func_179123_a();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.5F);
         drawSet(x, y, z, card);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         drawRarity(x, y, z, card);
         GlStateManager.func_179084_k();
         GlStateManager.func_179099_b();
         if (card instanceof PokemonCardState) {
            GlStateManager.func_179123_a();
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b(770, 771);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            drawPrevEvolution(x, y, z, (PokemonCardState)card, f);
            drawRetreat(x, y, z, (PokemonCardState)card, me, opp);
            GlStateManager.func_179084_k();
            GlStateManager.func_179099_b();
         }

         endQuads();
      }

   }

   @SideOnly(Side.CLIENT)
   private static void drawSet(double x, double y, double z, CommonCardState card) {
      y += 0.001;
      if (card.getCardType() != CardType.ENERGY) {
         if (card.hasTransformation()) {
            if (card.getTransformation().getSetID() == 1) {
               return;
            }

            mc.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/icons/sets/" + card.getTransformation().getSetID() + ".png"));
         } else {
            if (card.getSetID() == 1) {
               return;
            }

            mc.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/icons/sets/" + card.getSetID() + ".png"));
         }

         double size = 0.25;
         wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
         ++x;
         --z;
         wr().func_181662_b(x, y, z).func_187315_a(1.0, 1.0).func_181675_d();
         wr().func_181662_b(x + size, y, z).func_187315_a(1.0, 0.0).func_181675_d();
         wr().func_181662_b(x + size, y, z - size).func_187315_a(0.0, 0.0).func_181675_d();
         wr().func_181662_b(x, y, z - size).func_187315_a(0.0, 1.0).func_181675_d();
         te().func_78381_a();
      }

   }

   @SideOnly(Side.CLIENT)
   public static void drawCardOrBase(double x, double y, double z, CommonCardState card, int rotation, FontRenderer f, float t, PlayerClientOpponentState me, PlayerClientOpponentState opp) {
      if (card.getData().getID() != ImmutableCard.FACE_DOWN_ID) {
         drawCard(x, y, z, card, rotation, f, t, me, opp);
      } else {
         drawCardBase(x, y, z, (CommonCardState)null, false, me, opp);
      }

   }

   @SideOnly(Side.CLIENT)
   public static void drawSprite(double x, double y, double z, CommonCardState card) {
      y += 0.001;
      if (card.getCardType() != CardType.ENERGY) {
         if (card.hasTransformation()) {
            if (card.getTransformation().getHasCustomTex()) {
               mc.field_71446_o.func_110577_a(card.getTransformation().getCustomTexture());
               x -= 0.055;
            } else {
               if (card.getTransformation().getPokemonID() == 0) {
                  return;
               }

               card.bindPokemonSprite(mc);
            }
         } else if (card.getData().getHasCustomTex()) {
            mc.field_71446_o.func_110577_a(card.getData().getCustomTexture());
            x -= 0.055;
         } else {
            if (card.getPokemonID() == 0) {
               return;
            }

            card.bindPokemonSprite(mc);
         }

         double size = 0.6;
         wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
         ++x;
         z -= 0.45;
         wr().func_181662_b(x, y, z).func_187315_a(1.0, 1.0).func_181675_d();
         wr().func_181662_b(x + size, y, z).func_187315_a(1.0, 0.0).func_181675_d();
         wr().func_181662_b(x + size, y, z - size).func_187315_a(0.0, 0.0).func_181675_d();
         wr().func_181662_b(x, y, z - size).func_187315_a(0.0, 1.0).func_181675_d();
         te().func_78381_a();
      }

   }

   @SideOnly(Side.CLIENT)
   static void drawEnergySprite(double x, double y, double z, int rotation, Energy energy) {
      drawSprite(x, y, z, 1.0, rotation, energy.getHighResTexture());
   }

   @SideOnly(Side.CLIENT)
   static void drawSprite(double x, double y, double z, double scale, int rotation, ResourceLocation resource) {
      mc.field_71446_o.func_110577_a(resource);
      double size = 0.25 * scale;
      y += 0.001;
      startQuads();
      wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
      ++x;
      z -= 0.45;
      wr().func_181662_b(x, y, z).func_187315_a(1.0, 1.0).func_181675_d();
      wr().func_181662_b(x + size, y, z).func_187315_a(1.0, 0.0).func_181675_d();
      wr().func_181662_b(x + size, y, z - size).func_187315_a(0.0, 0.0).func_181675_d();
      wr().func_181662_b(x, y, z - size).func_187315_a(0.0, 1.0).func_181675_d();
      te().func_78381_a();
      endQuads();
   }

   @SideOnly(Side.CLIENT)
   private static void drawPrevEvolution(double x, double y, double z, PokemonCardState card, FontRenderer f) {
      y += 0.001;
      if (card.getPreviousPokemonID() > 0) {
         if (card.getPreviousPokemonID() == 10000) {
            mc.field_71446_o.func_110577_a(TCGResources.FOSSIL);
         } else {
            card.bindPreviousPokemonSprite(mc);
         }

         double size = 0.25;
         wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
         ++x;
         z -= 0.065;
         wr().func_181662_b(x, y, z).func_187315_a(1.0, 1.0).func_181675_d();
         wr().func_181662_b(x + size, y, z).func_187315_a(1.0, 0.0).func_181675_d();
         wr().func_181662_b(x + size, y, z - size).func_187315_a(0.0, 0.0).func_181675_d();
         wr().func_181662_b(x, y, z - size).func_187315_a(0.0, 1.0).func_181675_d();
         te().func_78381_a();
      }

   }

   @SideOnly(Side.CLIENT)
   private static void drawHP(double x, double y, double z, CommonCardState card, FontRenderer f) {
      CardType ct = card.getCardType();
      if (ct.isPokemon()) {
         int cardHealth = card.getHP();
         double dy = -1.99;
         double dx = 1.92;
         double dz = -1.42;
         drawUnlocalizedString(f, x + dx, y + dy, z + dz, Integer.toString(cardHealth) + "HP", 2105376, false, 90, 1.0F, "");
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
   }

   @SideOnly(Side.CLIENT)
   public static void drawName(double x, double y, double z, CommonCardState card, FontRenderer f) {
      GlStateManager.func_179094_E();
      double dx = 1.7835;
      double dy = -1.99;
      double dz = -0.995;
      double nameSize = (double)f.func_78256_a(LanguageMapTCG.translateKey(card.getName().toLowerCase()));
      double widthConstant = 98.0;
      if (card.getCardType() == CardType.STAGE1 || card.getCardType() == CardType.STAGE2 || card.getCardType() == CardType.EX || card.getCardType() == CardType.LVLX || card.getCardType() == CardType.GX) {
         widthConstant -= 25.5;
      }

      if (nameSize > widthConstant) {
         double scaleFactor = 1.0 - (nameSize - widthConstant) / widthConstant;
         GlStateManager.func_179137_b(0.0, 0.0, z + dz);
         GlStateManager.func_179139_a(1.0, 1.0, scaleFactor);
         GlStateManager.func_179137_b(0.0, 0.0, -(z + dz));
      }

      drawUnlocalizedString(f, x + dx, y + dy, z + dz, card.getName(), 2105376, true, 90, 1.0F, "");
      GlStateManager.func_179121_F();
   }

   @SideOnly(Side.CLIENT)
   private static void drawEffects(double x, double y, double z, PokemonCardState card, FontRenderer f) {
      y += 0.001;
      double offset = 0.11;
      double dx;
      double dz;
      if (card.getCardType() != CardType.STADIUM && card.getCardType() != CardType.TRAINER && card.getDescription() == null) {
         int line = 0;
         dx = 1.02;
         dz = -1.39;
         double size = 0.08;
         AbilityCard cardAbility = card.getAbility();
         double rightZ;
         double offsetE;
         if (cardAbility != null) {
            ResourceLocation texture = CardHelper.getAbilityTexture(cardAbility);
            if (texture != null) {
               double o = -0.1;
               mc.field_71446_o.func_110577_a(texture);
               wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
               wr().func_181662_b(x + dx + o, y, z + dz + size).func_187315_a(1.0, 1.0).func_181675_d();
               wr().func_181662_b(x + dx + size + o, y, z + dz + size).func_187315_a(1.0, 0.0).func_181675_d();
               wr().func_181662_b(x + dx + size + o, y, z + dz).func_187315_a(0.0, 0.0).func_181675_d();
               wr().func_181662_b(x + dx + o, y, z + dz).func_187315_a(0.0, 1.0).func_181675_d();
               te().func_78381_a();
               int line = 0;
               GlStateManager.func_179094_E();
               String name = "§l" + LanguageMapTCG.translateKey(cardAbility.getName().toLowerCase());
               double nameSize = (double)mc.field_71466_p.func_78256_a(name);
               rightZ = 120.0;
               if (nameSize > rightZ) {
                  offsetE = 1.0 - (nameSize - rightZ) / rightZ;
                  GlStateManager.func_179137_b(0.0, 0.0, z + dz + size + 0.01 + 0.01);
                  GlStateManager.func_179139_a(1.0, 1.0, offsetE);
                  GlStateManager.func_179137_b(0.0, 0.0, -(z + dz + size + 0.01 + 0.01));
               }

               drawUnlocalizedString(f, x + dx - (double)line * offset, y - 2.0, z + dz + size + 0.01 + 0.01, cardAbility.getName(), 2105376, true, 90, 1.0F, "§l");
               line = line + 1;
               GlStateManager.func_179121_F();
               if (cardAbility.getDescription() != null) {
                  int linesForAbility = card.getAttacksStatus() != null && card.getAttacksStatus().length > 0 ? 2 : 5;
                  if (card.getAttacksStatus().length == 1 && !card.getAttacksStatus()[0].hasDescription()) {
                     linesForAbility = 4;
                  }

                  drawUnlocalizedString(f, x + dx - (double)line * offset, y - 2.0, z + dz, cardAbility.getDescription(), 4473924, true, 90, 1.0F, true, linesForAbility, "§o");
                  line += linesForAbility;
               }
            }
         }

         int[] descriptionLines = CardHelper.calculateAttackDescriptionLines(card.getAttacksStatus(), cardAbility != null, 94, f);

         for(int attackIndex = 0; attackIndex < card.getAttacksStatus().length; ++attackIndex) {
            PokemonAttackStatus attack = card.getAttacksStatus()[attackIndex];
            AttackCard cardAttackData = attack.getData();
            if (cardAttackData != null) {
               for(int i = 0; i < cardAttackData.getEnergy().length; ++i) {
                  double moveDist = (size + 0.01) * (double)i;
                  double o = -0.1;
                  mc.field_71446_o.func_110577_a(cardAttackData.getEnergy()[i].getTexture());
                  wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
                  wr().func_181662_b(x + dx - (double)line * offset + o, y, z + dz + moveDist + size).func_187315_a(1.0, 1.0).func_181675_d();
                  wr().func_181662_b(x + dx + size - (double)line * offset + o, y, z + dz + moveDist + size).func_187315_a(1.0, 0.0).func_181675_d();
                  wr().func_181662_b(x + dx + size - (double)line * offset + o, y, z + dz + moveDist).func_187315_a(0.0, 0.0).func_181675_d();
                  wr().func_181662_b(x + dx - (double)line * offset + o, y, z + dz + moveDist).func_187315_a(0.0, 1.0).func_181675_d();
                  te().func_78381_a();
               }

               String modifier = cardAttackData.getModifier() == null ? "" : cardAttackData.getModifier().toString();
               int damage = cardAttackData.getDamage();
               rightZ = -0.25;
               if (damage != 0) {
                  String damageString = Integer.toString(damage) + modifier;
                  if (attack.getDamageBonus() != 0) {
                     damageString = damageString + " +" + attack.getDamageBonus();
                  }

                  drawUnlocalizedString(f, x + dx - (double)line * offset, y - 2.0, z + rightZ, damageString, 2105376, true, 90, 1.0F, "");
               }

               offsetE = Math.max((size + 0.01) * 3.0, (size + 0.01) * (double)cardAttackData.getEnergy().length);
               GlStateManager.func_179094_E();
               String name = LanguageMapTCG.translateKey(cardAttackData.getName().toLowerCase());
               double nameSize = (double)f.func_78256_a(name);
               double widthConstant = 68.0;
               double offsetT = (double)(Math.max(3, cardAttackData.getEnergy().length) * 6 + 1);
               nameSize -= offsetT;
               if (nameSize > widthConstant) {
                  double scaleFactor = 1.0 - (nameSize - widthConstant) / widthConstant;
                  GlStateManager.func_179137_b(0.0, 0.0, z + dz + 0.01 + offsetE);
                  GlStateManager.func_179139_a(1.0, 1.0, scaleFactor);
                  GlStateManager.func_179137_b(0.0, 0.0, -(z + dz + 0.01 + offsetE));
               }

               drawUnlocalizedString(f, x + dx - (double)line * offset, y - 2.0, z + dz + 0.01 + offsetE, cardAttackData.getName(), 2105376, true, 90, 1.0F, "");
               GlStateManager.func_179121_F();
               if (cardAttackData.getText() != null) {
                  drawUnlocalizedString(f, x + dx - (double)(line + 1) * offset, y - 2.0, z + dz, cardAttackData.getText(), 4473924, true, 90, 1.0F, true, descriptionLines[attackIndex], "§o");
               }
            }

            line += descriptionLines[attackIndex] + 1;
         }
      } else {
         String cardText = card.getDescription();
         dx = 1.12;
         dz = -1.39;
         if (card.getCardType() == CardType.ENERGY) {
            drawUnlocalizedString(f, x + dx - 1.0 * offset, y - 2.0, z + dz, cardText, 14540253, true, 90, 1.0F, true, 3, "");
         } else {
            drawUnlocalizedString(f, x + dx - 1.0 * offset, y - 2.0, z + dz, cardText, 2105376, true, 90, 1.0F, true, 8, "");
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public static void drawRarity(double x, double y, double z, CommonCardState card) {
      y += 0.001;
      if (card.getCardType() != CardType.ENERGY) {
         CardRarity cardRarity = card.getRarity();
         if (cardRarity != null) {
            mc.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/icons/" + cardRarity.getUnlocalizedName() + ".png"));
            float hx = 0.1F;
            float wx = 0.1F;
            ++x;
            z -= 0.05;
            wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
            wr().func_181662_b(x, y, z).func_187315_a(1.0, 1.0).func_181675_d();
            wr().func_181662_b(x + (double)hx, y, z).func_187315_a(1.0, 0.0).func_181675_d();
            wr().func_181662_b(x + (double)hx, y, z - (double)wx).func_187315_a(0.0, 0.0).func_181675_d();
            wr().func_181662_b(x, y, z - (double)wx).func_187315_a(0.0, 1.0).func_181675_d();
            te().func_78381_a();
         }
      }

   }

   @SideOnly(Side.CLIENT)
   private static void drawRetreat(double x, double y, double z, PokemonCardState card, PlayerCommonState me, PlayerCommonState opp) {
      y += 0.001;
      if (card.getCardType().isPokemon() && card.getRetreatCost() > 0) {
         int costModifier = LogicHelper.getCostModifier(me, opp);
         costModifier = Math.max(costModifier, -card.getRetreatCost());
         int renderCost = card.getRetreatCost() + costModifier;
         mc.field_71446_o.func_110577_a(TCGResources.ENERGY_COLORLESS);
         double size = 0.08;
         double dx = 0.11;
         double dz = -0.24;
         double modo = size / 2.0 * (double)renderCost;

         for(int i = 0; i < renderCost; ++i) {
            wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
            double moveDist = size * (double)i - modo;
            wr().func_181662_b(x + dx, y, z + dz + moveDist).func_187315_a(1.0, 1.0).func_181675_d();
            wr().func_181662_b(x + dx + size, y, z + dz + moveDist).func_187315_a(1.0, 0.0).func_181675_d();
            wr().func_181662_b(x + dx + size, y, z + dz + moveDist - size).func_187315_a(0.0, 0.0).func_181675_d();
            wr().func_181662_b(x + dx, y, z + dz + moveDist - size).func_187315_a(0.0, 1.0).func_181675_d();
            te().func_78381_a();
         }
      }

   }

   @SideOnly(Side.CLIENT)
   private static void drawWeakness(double x, double y, double z, PokemonCardState card, FontRenderer f) {
      y += 0.001;
      Energy energy = card.getWeakness();
      if (energy != null) {
         double size = 0.08;
         double dx = 0.11;
         double dz = -1.24;
         mc.field_71446_o.func_110577_a(energy.getTexture());
         String modifierString = card.getWeaknessModifier().toString() + Integer.toString(card.getWeaknessValue());
         wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
         wr().func_181662_b(x + dx, y, z + dz).func_187315_a(1.0, 1.0).func_181675_d();
         wr().func_181662_b(x + dx + size, y, z + dz).func_187315_a(1.0, 0.0).func_181675_d();
         wr().func_181662_b(x + dx + size, y, z + dz - size).func_187315_a(0.0, 0.0).func_181675_d();
         wr().func_181662_b(x + dx, y, z + dz - size).func_187315_a(0.0, 1.0).func_181675_d();
         te().func_78381_a();
         drawUnlocalizedString(f, x + dx + 0.1, y - 2.0, z + dz + 0.01, modifierString, 14540253, true, 90, 1.0F, "");
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   @SideOnly(Side.CLIENT)
   private static void drawResistance(double x, double y, double z, PokemonCardState card, FontRenderer f) {
      y += 0.001;
      Energy energy = card.getResistance();
      if (energy != null) {
         double size = 0.08;
         double dx = 0.11;
         double dz = -0.83;
         mc.field_71446_o.func_110577_a(energy.getTexture());
         String modifierString = card.getResistanceModifier().toString() + Integer.toString(card.getResistanceValue());
         wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
         wr().func_181662_b(x + dx, y, z + dz).func_187315_a(1.0, 1.0).func_181675_d();
         wr().func_181662_b(x + dx + size, y, z + dz).func_187315_a(1.0, 0.0).func_181675_d();
         wr().func_181662_b(x + dx + size, y, z + dz - size).func_187315_a(0.0, 0.0).func_181675_d();
         wr().func_181662_b(x + dx, y, z + dz - size).func_187315_a(0.0, 1.0).func_181675_d();
         te().func_78381_a();
         drawUnlocalizedString(f, x + dx + 0.1, y - 2.0, z + dz + 0.01, modifierString, 14540253, true, 90, 1.0F, "");
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   @SideOnly(Side.CLIENT)
   private static void drawStage(double x, double y, double z, CommonCardState card) {
      CardType ct = card.getCardType();
      y += 0.001;
      if (ct != CardType.ENERGY) {
         mc.field_71446_o.func_110577_a(ct.getIcon());
         float hd = 0.14F;
         float wd = 0.38F;
         ++x;
         --z;
         y += 0.001;
         wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
         wr().func_181662_b(x, y, z).func_187315_a(1.0, 1.0).func_181675_d();
         wr().func_181662_b(x + (double)hd, y, z).func_187315_a(1.0, 0.0).func_181675_d();
         wr().func_181662_b(x + (double)hd, y, z - (double)wd).func_187315_a(0.0, 0.0).func_181675_d();
         wr().func_181662_b(x, y, z - (double)wd).func_187315_a(0.0, 1.0).func_181675_d();
         te().func_78381_a();
      }

   }

   @SideOnly(Side.CLIENT)
   private static void drawType(double x, double y, double z, CommonCardState c) {
      CardType ct = c.getCardType();
      y += 0.001;
      if (ct == CardType.ENERGY || ct.isPokemon() && c.getMainEnergy() != null) {
         float size = 0.12F;
         ++x;
         z -= 0.7;
         if (c.getSecondaryEnergy() != null) {
            mc.field_71446_o.func_110577_a(c.getMainEnergy().getTexture());
            wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
            wr().func_181662_b(x, y, z).func_187315_a(1.0, 0.0).func_181675_d();
            wr().func_181662_b(x + 0.2, y, z).func_187315_a(0.0, 0.0).func_181675_d();
            wr().func_181662_b(x + 0.2, y, z - 0.2).func_187315_a(0.0, 1.0).func_181675_d();
            wr().func_181662_b(x, y, z - 0.2).func_187315_a(1.0, 1.0).func_181675_d();
            te().func_78381_a();
            mc.field_71446_o.func_110577_a(c.getSecondaryEnergy().getTexture());
            wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
            wr().func_181662_b(x, y, z).func_187315_a(1.0, 0.0).func_181675_d();
            wr().func_181662_b(x + 0.2, y, z).func_187315_a(0.0, 0.0).func_181675_d();
            wr().func_181662_b(x + 0.2, y, z - 0.2).func_187315_a(0.0, 1.0).func_181675_d();
            wr().func_181662_b(x, y, z - 0.2).func_187315_a(1.0, 1.0).func_181675_d();
            te().func_78381_a();
         } else {
            mc.field_71446_o.func_110577_a(c.getMainEnergy().getTexture());
            wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
            wr().func_181662_b(x, y, z).func_187315_a(1.0, 1.0).func_181675_d();
            wr().func_181662_b(x + (double)size, y, z).func_187315_a(1.0, 0.0).func_181675_d();
            wr().func_181662_b(x + (double)size, y, z - (double)size).func_187315_a(0.0, 0.0).func_181675_d();
            wr().func_181662_b(x, y, z - (double)size).func_187315_a(0.0, 1.0).func_181675_d();
            te().func_78381_a();
         }
      }

   }

   @SideOnly(Side.CLIENT)
   static void drawUnlocalizedString(FontRenderer fontRenderer, double x, double y, double z, String unlocalizedString, int color, boolean isUnicode, int rotation, float scale, String formatting) {
      drawUnlocalizedString(fontRenderer, x, y, z, unlocalizedString, color, isUnicode, rotation, scale, false, 0, formatting);
   }

   @SideOnly(Side.CLIENT)
   private static void drawUnlocalizedString(FontRenderer fontRenderer, double x, double y, double z, String unlocalizedString, int color, boolean isUnicode, int rotation, float scale, boolean isExtended, int maxLines, String formatting) {
      unlocalizedString = LanguageMapTCG.translateKey(unlocalizedString.toLowerCase());
      float var13 = 0.775F;
      float baseScale = 0.016666668F * var13 * scale;
      GlStateManager.func_179094_E();
      float scaleFactor = PixelmonConfig.scaleModelsUp ? 1.3F : 1.0F;
      GlStateManager.func_179109_b((float)x + 0.0F, (float)y + 0.7F + 1.0F * scaleFactor, (float)z);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      if (rotation == 180) {
         GL11.glRotated(90.0, 1.0, 0.0, 0.0);
         GL11.glRotated(180.0, 0.0, 0.0, 1.0);
         GL11.glRotated(-90.0, 0.0, 0.0, 1.0);
      }

      if (rotation == 90) {
         GL11.glRotated((double)rotation, 1.0, 0.0, 0.0);
         GL11.glRotated((double)(-rotation), 0.0, 0.0, 1.0);
      }

      GlStateManager.func_179152_a(-baseScale, -baseScale, baseScale);
      GlStateManager.func_179140_f();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      fontRenderer.func_78264_a(isUnicode);
      if (isExtended) {
         if (maxLines == 0) {
            fontRenderer.func_78279_b(formatting + unlocalizedString, 0, 0, 94, color);
         } else {
            List lines = fontRenderer.func_78271_c(unlocalizedString, 94);
            int count = Math.min(lines.size(), maxLines);

            for(int i = 0; i < count; ++i) {
               if (i != maxLines - 1) {
                  lines.set(i, (String)lines.get(i) + " ");
               } else {
                  String lastLine = ((String)lines.get(i)).trim();
                  if (fontRenderer.func_78256_a(lastLine + "...") > 94 || lines.size() > maxLines) {
                     while(fontRenderer.func_78256_a(lastLine + "...") > 94) {
                        lastLine = lastLine.substring(0, lastLine.length() - 1);
                     }

                     if (lastLine.endsWith(",") || lastLine.endsWith(".") || lastLine.endsWith(":")) {
                        lastLine = lastLine.substring(0, lastLine.length() - 1);
                     }

                     lines.set(i, lastLine.trim() + "...");
                  }
               }

               fontRenderer.func_78276_b(formatting + (String)lines.get(i), 0, i * 8, color);
            }
         }
      } else {
         fontRenderer.func_78276_b(formatting + unlocalizedString, 0, 0, color);
      }

      if (isUnicode) {
         fontRenderer.func_78264_a(false);
      }

      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179121_F();
   }
}
