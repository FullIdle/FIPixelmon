package com.pixelmonmod.tcg.helper;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.api.card.personalization.CardBack;
import com.pixelmonmod.tcg.api.registries.CardBackRegistry;
import com.pixelmonmod.tcg.duel.RenderDuel;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.PokemonCardStatus;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.TCGResources;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public class CardHelper {
   public static final int TEXTURE_WIDTH = 216;
   public static final int TEXTURE_HEIGHT = 335;
   public static final int MAX_TEXT_WIDTH = 94;

   private static void drawStrikethrough(CommonCardState card, int cardX, int cardY, Minecraft mc) {
      if (card instanceof PokemonCardState) {
         PokemonCardState pokemon = (PokemonCardState)card;
         int[] descriptionLines = calculateAttackDescriptionLines(pokemon.getAttacksStatus(), pokemon.getAbility() != null, 94, mc.field_71466_p);
         int line = pokemon.getAbility() != null ? 2 : 0;

         for(int i = 0; i < pokemon.getAttacksStatus().length; ++i) {
            PokemonAttackStatus attackStatus = pokemon.getAttacksStatus()[i];
            if (attackStatus.isDisabled()) {
               int y = cardY / 2 + line * 9 + 5;
               RenderDuel.draw2DLine((double)(cardX / 2 - 48 + Math.max(3, attackStatus.getData().getEnergy().length) * 6 + 1), (double)y, (double)(cardX / 2 + 44), (double)y, 0.0F, 0.0F, 0.0F, 1.0F);
            }

            line += descriptionLines[i] + 1;
         }
      }

   }

   public static void drawCardBack(Minecraft mc, int w, int h, float z, PlayerCommonState me) {
      if (me != null) {
         drawCardBack(mc, w, h, z, CardBackRegistry.get(me.getCardBackID()));
      } else {
         drawCardBack(mc, w, h, z, CardBackRegistry.STANDARD);
      }

   }

   public static void drawCardBack(Minecraft mc, int w, int h, float z, CardBack cb) {
      GlStateManager.func_179140_f();
      if (cb != null) {
         mc.field_71446_o.func_110577_a(cb.getFile());
      } else {
         mc.field_71446_o.func_110577_a(CardBackRegistry.STANDARD.getFile());
      }

      com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 54), (double)(h / 2 - 83), 108.0, 167.0F, 0.0, 0.0, 1.0, 1.0, z);
      GlStateManager.func_179145_e();
   }

   public static void drawCard(CommonCardState card, Minecraft mc, int w, int h, float z, float o, double rot, PlayerClientMyState me, PlayerClientOpponentState opp) {
      GlStateManager.func_179141_d();
      GlStateManager.func_179140_f();
      GlStateManager.func_179147_l();
      GlStateManager.func_179094_E();
      if (rot != 0.0) {
         GlStateManager.func_179109_b((float)(w / 2), (float)(h / 2), 0.0F);
         GlStateManager.func_179114_b((float)rot, 0.0F, 0.0F, 1.0F);
         GlStateManager.func_179109_b((float)(-w / 2), (float)(-h / 2), 0.0F);
      }

      FontRenderer f = mc.field_71466_p;
      int textColor = 2105376;
      if (card.getMainEnergy() == Energy.DARKNESS || card.getMainEnergy() == Energy.RAINBOW || card.getData() != null && card.getData().isSpecial() && card.getCardType() == CardType.ENERGY) {
         textColor = 14540253;
      }

      resetColour(o);

      try {
         drawCardBackground(card, mc, w, h, z, o);
      } catch (NullPointerException var14) {
         TCG.logger.warn("Issue drawing card: " + card.getName());
      }

      resetColour(o);
      drawTypings(card, mc, w, h, z);
      resetColour(o);
      drawCardType(card, mc, w, h, z);
      resetColour(o);
      drawRarity(card, mc, w, h, z);
      resetColour(o);
      drawSet(card, mc, w, h, z);
      resetColour(o);
      drawSprite(card, mc, w, h, z);
      resetColour(o);
      PokemonCardState temp;
      if (card instanceof PokemonCardState) {
         drawPrevEvolution((PokemonCardState)card, mc, w, h, z);
         resetColour(o);
         drawHP((PokemonCardState)card, f, w, h, textColor, o);
         resetColour(o);
         drawRetreat((PokemonCardState)card, mc, w, h, z, me, opp);
         resetColour(o);
      } else if (card.getCardType().isPokemon()) {
         temp = new PokemonCardState(card.getData(), 0);
         drawPrevEvolution(temp, mc, w, h, z);
         resetColour(o);
         drawHP(temp, f, w, h, textColor, o);
         resetColour(o);
         drawRetreat(temp, mc, w, h, z, me, opp);
         resetColour(o);
      } else if (card.getCardType() == CardType.TRAINER) {
         TrainerCardState trainer = new TrainerCardState(card.getData());
         PokemonCardState temp = new PokemonCardState(trainer.getData().getEffect(), trainer, 0);
         drawPrevEvolution(temp, mc, w, h, z);
         resetColour(o);
         drawHP(temp, f, w, h, textColor, o);
         resetColour(o);
      }

      drawCardName(card, f, w, h, o);
      resetColour(o);
      drawCardEffects(card, mc, w, h, z, textColor, o);
      resetColour(o);
      PokemonCardStatus status;
      if (card instanceof PokemonCardState) {
         temp = (PokemonCardState)card;
         status = temp.getStatus();
         drawWeakness(temp, mc, w, h, z, o);
         resetColour(o);
         drawResistance(temp, mc, w, h, z, o);
         resetColour(o);
         drawConditions(status.getConditions(), w, h, z, mc);
         resetColour(o);
         drawEnergies(temp.getAttachments(), w, h, z, mc);
         resetColour(o);
         drawStrikethrough(card, w, h, mc);
         resetColour(o);
         drawDamage(status.getDamage(), w, h, z, mc, o);
         resetColour(o);
      } else if (card.getCardType().isPokemon()) {
         temp = new PokemonCardState(card.getData(), 0);
         drawWeakness(temp, mc, w, h, z, o);
         resetColour(o);
         drawResistance(temp, mc, w, h, z, o);
         resetColour(o);
         status = temp.getStatus();
         drawConditions(status.getConditions(), w, h, z, mc);
         resetColour(o);
         drawDamage(status.getDamage(), w, h, z, mc, o);
         resetColour(o);
         drawEnergies(temp.getAttachments(), w, h, z, mc);
         resetColour(o);
         drawStrikethrough(temp, w, h, mc);
         resetColour(o);
      }

      GlStateManager.func_179121_F();
      GlStateManager.func_179145_e();
      GlStateManager.func_179118_c();
      GlStateManager.func_179084_k();
   }

   private static void drawEnergies(List attachments, int cardX, int cardY, float zLevel, Minecraft mc) {
      int energyCount = 0;
      if (!attachments.isEmpty()) {
         Iterator var6 = attachments.iterator();

         while(true) {
            while(var6.hasNext()) {
               CommonCardState attachment = (CommonCardState)var6.next();
               if (attachment.getCardType() == CardType.ENERGY) {
                  mc.field_71446_o.func_110577_a(attachment.getMainEnergy().getHighResTexture());
                  com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(cardX - 100 + energyCount++ * 28) / 2.0, ((double)cardY + 158.0) / 2.0, 12.0, 12.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
                  if (attachment.getSecondaryEnergy() != null) {
                     mc.field_71446_o.func_110577_a(attachment.getSecondaryEnergy().getHighResTexture());
                     com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(cardX - 100 + energyCount++ * 28) / 2.0, (double)(cardY + 158) / 2.0, 12.0, 12.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
                  }
               } else if (attachment.getAbility() != null && attachment.getAbility().getEffect() != null) {
                  List energies = attachment.getAbility().getEffect().getEnergyEquivalence(attachment);
                  if (energies != null) {
                     Iterator var9 = energies.iterator();

                     while(var9.hasNext()) {
                        CommonCardState energy = (CommonCardState)var9.next();
                        mc.field_71446_o.func_110577_a(energy.getMainEnergy().getHighResTexture());
                        com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(cardX - 100 + energyCount++ * 28) / 2.0, ((double)cardY + 158.0) / 2.0, 12.0, 12.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
                     }
                  }
               }
            }

            return;
         }
      }
   }

   private static void drawDamage(int damage, int cardX, int cardY, float zLevel, Minecraft mc, float o) {
      if (damage > 0) {
         mc.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/damage.png"));
         com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(cardX + 75) / 2.0, (double)(cardY - 180) / 2.0, 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         String damageString = Integer.toString(damage);
         drawString(damageString, (cardX + 101) / 2, (cardY - 163) / 2, true, ((int)(o * 255.0F) << 24) + 16777215, false, mc.field_71466_p);
      }

   }

   private static void drawConditions(List conditions, int cardX, int cardY, float zLevel, Minecraft mc) {
      Iterator var5 = conditions.iterator();

      while(var5.hasNext()) {
         Pair condition = (Pair)var5.next();
         switch ((CardCondition)condition.getLeft()) {
            case BURNT:
            case POISONED:
               mc.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/" + ((CardCondition)condition.getLeft()).getName().toLowerCase() + ".png"));
               com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(cardX + 75) / 2.0, (double)(cardY - (condition.getLeft() == CardCondition.BURNT ? 85 : 135)) / 2.0, 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         }
      }

   }

   public static int getBackgroundColor(Energy e) {
      switch (e) {
         case COLORLESS:
            return -287449635;
         case DARKNESS:
            return -300871407;
         case DRAGON:
            return -294291424;
         case FAIRY:
            return -285249815;
         case FIGHTING:
            return -286889216;
         case FIRE:
            return -285269504;
         case GRASS:
            return -301936356;
         case LIGHTNING:
            return -285225967;
         case METAL:
            return -293107833;
         case PSYCHIC:
            return -294256426;
         case WATER:
            return -301956865;
         default:
            return 0;
      }
   }

   public static int getForegroundColor(Energy e) {
      switch (e) {
         case COLORLESS:
            return -16777216;
         case DARKNESS:
         case DRAGON:
         case FAIRY:
         case FIGHTING:
         case FIRE:
         case GRASS:
         case LIGHTNING:
         case METAL:
         case PSYCHIC:
         case WATER:
            return -1;
         default:
            return 0;
      }
   }

   public static ResourceLocation getCosmeticBackgroundTexture(CommonCardState c) {
      return c.getData().getCustomTexture();
   }

   public static ResourceLocation getBackgroundTexture(CardType cardType, Energy energy, boolean isSpecial) {
      switch (cardType) {
         case EX:
            return new ResourceLocation("tcg:gui/cards/background/cardbase_" + energy.getUnlocalizedName() + "_EX.png");
         case BASIC:
            if (energy != null) {
               return new ResourceLocation("tcg:gui/cards/background/cardbase_" + energy.getUnlocalizedName() + "_basic.png");
            }

            return new ResourceLocation("tcg:gui/cards/background/cardbase_trainer_base.png");
         case ENERGY:
            if (isSpecial) {
               return new ResourceLocation("tcg:gui/cards/background/cardbase_" + energy.getUnlocalizedName() + "_senergy.png");
            }

            return new ResourceLocation("tcg:gui/cards/background/cardbase_" + energy.getUnlocalizedName() + "_energy.png");
         case TRAINER:
         case STADIUM:
         case SUPPORTER:
            return new ResourceLocation("tcg:gui/cards/background/cardbase_trainer_base.png");
         default:
            return new ResourceLocation("tcg:gui/cards/background/cardbase_" + energy.getUnlocalizedName() + ".png");
      }
   }

   private static ResourceLocation getMaskTexture(CardType cardType, Energy energy) {
      switch (cardType) {
         case EX:
            return new ResourceLocation("tcg:gui/cards/background/cardbase_" + energy.getUnlocalizedName() + "_EX_mask.png");
         case BASIC:
            if (energy != null) {
               return new ResourceLocation("tcg:gui/cards/background/cardbase_" + energy.getUnlocalizedName() + "_basic_mask.png");
            }

            return new ResourceLocation("tcg:gui/cards/background/cardbase_trainer_base_mask.png");
         case ENERGY:
            return new ResourceLocation("tcg:gui/cards/background/cardbase_" + energy.getUnlocalizedName() + "_energy_mask.png");
         case TRAINER:
         case STADIUM:
         case SUPPORTER:
            return new ResourceLocation("tcg:gui/cards/background/cardbase_trainer_base_mask.png");
         default:
            return new ResourceLocation("tcg:gui/cards/background/cardbase_" + energy.getUnlocalizedName() + "_mask.png");
      }
   }

   public static ResourceLocation getAbilityTexture(AbilityCard cardAbility) {
      switch (cardAbility.getType()) {
         case BODY:
            return new ResourceLocation("tcg:gui/cards/icons/green.png");
         case POWER:
            return new ResourceLocation("tcg:gui/cards/icons/red.png");
         default:
            return null;
      }
   }

   private static void drawTypings(CommonCardState card, Minecraft mc, int w, int h, float z) {
      CardType ct = card.getCardType();
      if (ct.isPokemon() && card.getMainEnergy() != null) {
         if (card.getSecondaryEnergy() != null) {
            mc.field_71446_o.func_110577_a(card.getMainEnergy().getTexture());
            com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 16), (double)(h / 2 - 78), 10.0, 10.0F, 0.0, 0.0, 1.0, 1.0, z);
            mc.field_71446_o.func_110577_a(card.getSecondaryEnergy().getTexture());
            com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 6), (double)(h / 2 - 78), 10.0, 10.0F, 0.0, 0.0, 1.0, 1.0, z);
         } else {
            mc.field_71446_o.func_110577_a(card.getMainEnergy().getTexture());
            com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 6), (double)(h / 2 - 78), 10.0, 10.0F, 0.0, 0.0, 1.0, 1.0, z);
         }
      }

   }

   private static void drawCardType(CommonCardState card, Minecraft mc, int w, int h, float z) {
      CardType ct = card.getCardType();
      if (ct != CardType.ENERGY) {
         mc.field_71446_o.func_110577_a(ct.getIcon());
         com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 48), (double)(h / 2 - 66), 27.0, 11.0F, 0.0, 0.0, 1.0, 1.0, z);
      }

   }

   private static void drawHP(PokemonCardState card, FontRenderer f, int w, int h, int color, float o) {
      int cardHealth = card.getHP();
      if (cardHealth > 0) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(0.3, 0.3, 0.0);
         drawString(Integer.toString(cardHealth) + "HP", w / 2 - 48, h / 2 - 76, false, ((int)(o * 255.0F) << 24) + 16777215 - color * 3, false, f);
         GlStateManager.func_179121_F();
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(-0.3, -0.3, 0.0);
         drawString(Integer.toString(cardHealth) + "HP", w / 2 - 48, h / 2 - 76, false, ((int)(o * 255.0F) << 24) + 16777215 - color * 3, false, f);
         GlStateManager.func_179121_F();
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(0.3, -0.3, 0.0);
         drawString(Integer.toString(cardHealth) + "HP", w / 2 - 48, h / 2 - 76, false, ((int)(o * 255.0F) << 24) + 16777215 - color * 3, false, f);
         GlStateManager.func_179121_F();
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(-0.3, 0.3, 0.0);
         drawString(Integer.toString(cardHealth) + "HP", w / 2 - 48, h / 2 - 76, false, ((int)(o * 255.0F) << 24) + 16777215 - color * 3, false, f);
         GlStateManager.func_179121_F();
         drawString(Integer.toString(cardHealth) + "HP", w / 2 - 48, h / 2 - 76, false, ((int)(o * 255.0F) << 24) + color, false, f);
      }

   }

   private static void drawCardName(CommonCardState card, FontRenderer f, int w, int h, float o) {
      boolean previousFlag = f.func_82883_a();
      f.func_78264_a(true);
      String cardName = LanguageMapTCG.translateKey(card.getName().toLowerCase());
      GlStateManager.func_179094_E();
      double nameSize = (double)f.func_78256_a(cardName);
      double widthConstant = 66.0;
      int offset = 0;
      if (card.getCardType() == CardType.ENERGY) {
         offset = 27;
      }

      if (card.getCardType() == CardType.STAGE1 || card.getCardType() == CardType.STAGE2 || card.getCardType() == CardType.EX || card.getCardType() == CardType.LVLX || card.getCardType() == CardType.GX) {
         widthConstant -= 17.0;
      }

      nameSize -= (double)offset;
      if (nameSize > widthConstant) {
         double scaleFactor = 1.0 - (nameSize - widthConstant) / widthConstant;
         GlStateManager.func_179109_b((float)(w / 2 - 18 - offset), 0.0F, 0.0F);
         GlStateManager.func_179139_a(scaleFactor, 1.0, 1.0);
         GlStateManager.func_179109_b((float)(-(w / 2 - 18 - offset)), 0.0F, 0.0F);
      }

      f.func_78276_b(cardName, w / 2 - 18 - offset, h / 2 - 65, ((int)(o * 255.0F) << 24) + 2105376);
      f.func_78264_a(previousFlag);
      GlStateManager.func_179121_F();
   }

   private static void drawSprite(CommonCardState card, Minecraft mc, int w, int h, float z) {
      if (!card.getCardType().isCosmetic()) {
         if (card.getCardType() != CardType.ENERGY) {
            if (card.hasTransformation()) {
               if (card.getTransformation().getHasCustomTex()) {
                  mc.field_71446_o.func_110577_a(card.getTransformation().getCustomTexture());
                  com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 23), (double)(h / 2 - 53), 47.0, 47.0F, 0.0, 0.0, 1.0, 1.0, z);
               } else {
                  card.bindPokemonSprite(mc);
                  com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 32), (double)(h / 2 - 67), 60.0, 60.0F, 0.0, 0.0, 1.0, 1.0, z);
               }
            } else if (card.getData().getHasCustomTex()) {
               mc.field_71446_o.func_110577_a(card.getData().getCustomTexture());
               com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 23), (double)(h / 2 - 53), 47.0, 47.0F, 0.0, 0.0, 1.0, 1.0, z);
            } else {
               card.bindPokemonSprite(mc);
               com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 32), (double)(h / 2 - 67), 60.0, 60.0F, 0.0, 0.0, 1.0, 1.0, z);
            }
         } else if (card.getData().getCustomTexture() != null) {
            mc.field_71446_o.func_110577_a(card.getData().getCustomTexture());
            if (card.getData().isSpecial()) {
               com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 36), (double)(h / 2 - 50 + 10), 72.0, 72.0F, 0.0, 0.0, 1.0, 1.0, z);
            } else {
               com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 36), (double)(h / 2 - 36 + 10), 72.0, 72.0F, 0.0, 0.0, 1.0, 1.0, z);
            }
         }

      }
   }

   private static void drawPrevEvolution(PokemonCardState card, Minecraft mc, int w, int h, float z) {
      if (card.getCardType().isPokemon() && card.getPreviousPokemonID() > 0) {
         if (card.getPreviousPokemonID() == 10000) {
            mc.field_71446_o.func_110577_a(TCGResources.FOSSIL);
         } else {
            card.bindPreviousPokemonSprite(mc);
         }

         com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 + 31), (double)(h / 2 - 68), 19.0, 19.0F, 0.0, 0.0, 1.0, 1.0, z);
      }

   }

   private static void drawCardEffects(CommonCardState card, Minecraft mc, int w, int h, float z, int t, float o) {
      boolean previousUnicodeFlag = mc.field_71466_p.func_82883_a();
      int resetColor = t;
      mc.field_71466_p.func_78264_a(true);
      if (card.getCardType() != CardType.STADIUM && card.getCardType() != CardType.TRAINER && (card.getDescription() == null || card.getDescription().isEmpty())) {
         int lineCount = 0;
         AbilityCard cardAbility = card.getAbility();
         PokemonAttackStatus[] attacks = null;
         if (card instanceof PokemonCardState) {
            attacks = ((PokemonCardState)card).getAttacksStatus();
         } else if (card.getData().getAttacks() != null) {
            attacks = (PokemonAttackStatus[])Arrays.stream(card.getData().getAttacks()).map(PokemonAttackStatus::new).toArray((x$0) -> {
               return new PokemonAttackStatus[x$0];
            });
         }

         if (cardAbility != null) {
            mc.field_71446_o.func_110577_a(getAbilityTexture(cardAbility));
            com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 46), (double)(h / 2 + 1), 6.0, 6.0F, 0.0, 0.0, 1.0, 1.0, z);
            GlStateManager.func_179094_E();
            String name = "§l" + LanguageMapTCG.translateKey(cardAbility.getName().toLowerCase());
            double nameSize = (double)mc.field_71466_p.func_78256_a(name);
            double widthConstant = 88.0;
            int altColor = -12303292;
            if (nameSize > widthConstant) {
               double scaleFactor = 1.0 - (nameSize - widthConstant) / widthConstant;
               GlStateManager.func_179109_b((float)(w / 2 - 46 + 8), 0.0F, 0.0F);
               GlStateManager.func_179139_a(scaleFactor, 1.0, 1.0);
               GlStateManager.func_179109_b((float)(-(w / 2 - 46 + 8)), 0.0F, 0.0F);
            }

            if (cardAbility.getEffect() == null) {
               t = 15597568;
               altColor = 14483456;
            }

            mc.field_71466_p.func_78276_b(name, w / 2 - 46 + 8, h / 2, ((int)(o * 255.0F) << 24) + t);
            GlStateManager.func_179121_F();
            String description = "§o" + LanguageMapTCG.translateKey(cardAbility.getDescription().toLowerCase());
            ++lineCount;
            int linesForAbility = attacks != null && attacks.length > 0 ? 2 : 5;
            if (attacks.length == 1 && !attacks[0].hasDescription()) {
               linesForAbility = 4;
            }

            lineCount += drawMultiline(description, linesForAbility, mc.field_71466_p, w / 2 - 46, h / 2 + 9, 94, altColor);
            t = resetColor;
            resetColour(o);
         }

         if (attacks != null) {
            int[] descriptionLines = calculateAttackDescriptionLines(attacks, cardAbility != null, 94, mc.field_71466_p);

            for(int i = 0; i < attacks.length; ++i) {
               int bonus = 0;
               if (card instanceof PokemonCardState) {
                  bonus = ((PokemonCardState)card).getAttacksStatus()[i].getDamageBonus();
               }

               lineCount += drawAttack(attacks[i], bonus, lineCount, descriptionLines[i], 94, mc, mc.field_71466_p, w, h, z, t, o);
            }
         }
      } else {
         if (card.getData().getEffect() == null && !card.getData().getCode().equalsIgnoreCase("BASE96") && !card.getData().getCode().equalsIgnoreCase("TERO80")) {
            t = 15597568;
         }

         if (card.getCardType() == CardType.ENERGY) {
            drawCardText(card.getData(), 4, 3, 94, mc.field_71466_p, w, h, o, t);
         } else {
            drawCardText(card.getData(), -1, 8, 94, mc.field_71466_p, w, h, o, t);
         }
      }

      mc.field_71466_p.func_78264_a(previousUnicodeFlag);
   }

   public static int[] calculateAttackDescriptionLines(PokemonAttackStatus[] attacks, boolean hasAbility, int maxWidth, FontRenderer f) {
      boolean previousUnicodeFlag = f.func_82883_a();
      f.func_78264_a(true);
      int attackCount = 0;
      PokemonAttackStatus[] var6 = attacks;
      int var7 = attacks.length;

      int totalLines;
      for(totalLines = 0; totalLines < var7; ++totalLines) {
         PokemonAttackStatus attack = var6[totalLines];
         if (attack != null) {
            ++attackCount;
         }
      }

      int availableLines = 6;
      if (hasAbility) {
         availableLines -= 3;
      }

      availableLines -= attackCount;
      int[] totalDescriptionLines = new int[attacks.length];
      totalLines = 0;

      for(int i = 0; i < attacks.length; ++i) {
         if (attacks[i] != null && attacks[i].hasDescription()) {
            List lines = f.func_78271_c(attacks[i].getLocalizedDescription(), maxWidth);
            totalDescriptionLines[i] = lines.size();
            totalLines += lines.size();
         }
      }

      int[] result = new int[attacks.length];
      if (totalLines <= availableLines) {
         result = totalDescriptionLines;
      } else {
         int averageLines = availableLines / attackCount;
         int extraLines = availableLines;

         for(int i = 0; i < attacks.length; ++i) {
            result[i] = Math.min(totalDescriptionLines[i], averageLines);
            extraLines -= result[i];
         }

         boolean updated = true;

         while(extraLines > 0 && updated) {
            updated = false;

            for(int i = 0; i < attacks.length; ++i) {
               if (result[i] < totalDescriptionLines[i]) {
                  int newLines = Math.min(totalDescriptionLines[i], result[i] + extraLines);
                  extraLines -= newLines - result[i];
                  result[i] = newLines;
                  updated = true;
               }
            }
         }
      }

      f.func_78264_a(previousUnicodeFlag);
      return result;
   }

   public static int calculateTrainerDescriptionLines(ImmutableCard effect, int maxWidth, FontRenderer f) {
      boolean previousUnicodeFlag = f.func_82883_a();
      f.func_78264_a(true);
      int availableLines = 8;
      int totalDescriptionLines = 0;
      int totalLines = 0;
      if (effect != null && effect.getDescription() != null) {
         List lines = f.func_78271_c(effect.getDescription(), maxWidth);
         totalDescriptionLines = lines.size();
         totalLines += lines.size();
      }

      int result = false;
      int result;
      if (totalLines <= availableLines) {
         result = totalDescriptionLines;
      } else {
         result = Math.min(totalDescriptionLines, availableLines);
         int extraLines = availableLines - result;
         boolean updated = true;

         while(extraLines > 0 && updated) {
            updated = false;
            if (result < totalDescriptionLines) {
               int newLines = Math.min(totalDescriptionLines, result + extraLines);
               extraLines -= newLines - result;
               result = newLines;
               updated = true;
            }
         }
      }

      f.func_78264_a(previousUnicodeFlag);
      return result;
   }

   private static int drawAttack(PokemonAttackStatus attack, int bonus, int pos, int descriptionLineCount, int maxWidth, Minecraft mc, FontRenderer f, int width, int height, float zLevel, int color, float o) {
      if (attack == null) {
         return 0;
      } else {
         int verticalOffset = pos * 9;
         int altColor = -12303292;
         AttackCard data = attack.getData();
         if (data.getText() != null && data.getEffects() != null && data.getEffects() != null && data.getEffects().isEmpty()) {
         }

         int lineCount;
         for(lineCount = 0; lineCount < data.getEnergy().length; ++lineCount) {
            mc.field_71446_o.func_110577_a(attack.getData().getEnergy()[lineCount].getTexture());
            com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(width / 2 - 46 + lineCount * 6), (double)(height / 2 + 2 + verticalOffset), 6.0, 6.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         }

         lineCount = 1;
         String modifier = data.getModifier() == null ? "" : data.getModifier().toString();
         int damage = data.getDamage();
         String damageString;
         if (damage != 0) {
            damageString = Integer.toString(damage) + modifier;
            if (bonus > 0) {
               damageString = Integer.toString(damage) + "+" + Integer.toString(bonus);
            } else if (bonus < 0) {
               damageString = Integer.toString(damage) + "-" + Integer.toString(-bonus);
            }

            f.func_78276_b(damageString, width / 2 + 45 - f.func_78256_a(damageString), height / 2 + verticalOffset, ((int)(o * 255.0F) << 24) + color);
         }

         GlStateManager.func_179094_E();
         damageString = LanguageMapTCG.translateKey(data.getName().toLowerCase());
         double nameSize = (double)f.func_78256_a(damageString);
         double widthConstant = 42.0;
         double offset = (double)(Math.max(3, data.getEnergy().length) * 6 + 1);
         nameSize -= offset;
         if (nameSize > widthConstant) {
            double scaleFactor = 1.0 - (nameSize - widthConstant) / widthConstant;
            GlStateManager.func_179109_b((float)(width / 2 - 46 + (int)offset), 0.0F, 0.0F);
            GlStateManager.func_179139_a(scaleFactor, 1.0, 1.0);
            GlStateManager.func_179109_b((float)(-(width / 2 - 46 + (int)offset)), 0.0F, 0.0F);
         }

         f.func_78276_b(damageString, width / 2 - 46 + (int)offset, height / 2 + verticalOffset, ((int)(o * 255.0F) << 24) + color);
         GlStateManager.func_179121_F();
         if (attack.hasDescription()) {
            lineCount += drawMultiline("§o" + attack.getLocalizedDescription(), descriptionLineCount, f, width / 2 - 46, height / 2 + 9 + verticalOffset, maxWidth, altColor);
         }

         resetColour(o);
         return lineCount;
      }
   }

   private static int drawCardText(ImmutableCard card, int pos, int lines, int maxWidth, FontRenderer f, int width, int height, float o, int color) {
      if (card != null) {
         int verticalOffset = pos * 9;
         int lineCount = 0;
         if (card.getDescription() != null) {
            lineCount += drawMultiline(LanguageMapTCG.translateKey(card.getDescription().toLowerCase()), lines, f, width / 2 - 46, height / 2 + 9 + verticalOffset, maxWidth, color);
         }

         resetColour(o);
         return lineCount;
      } else {
         return 0;
      }
   }

   private static void drawRarity(CommonCardState card, Minecraft mc, int w, int h, float z) {
      if (card.getCardType() != CardType.ENERGY) {
         CardRarity cardRarity = card.getRarity();
         if (cardRarity != null) {
            mc.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/icons/" + cardRarity.getUnlocalizedName() + ".png"));
            com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 + 42), (double)(h / 2 - 9), 8.0, 8.0F, 0.0, 0.0, 1.0, 1.0, z);
         }
      }

   }

   private static void drawSet(CommonCardState card, Minecraft mc, int w, int h, float z) {
      if (card.getCardType() != CardType.ENERGY && card.getSetID() != 1) {
         int setID = card.getSetID();
         resetColour(0.5F);
         if (setID <= 1000) {
            mc.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/icons/sets/" + setID + ".png"));
            com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 46), (double)(h / 2 - 21), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, z);
         }

         resetColour(1.0F);
      }

   }

   private static void drawRetreat(PokemonCardState card, Minecraft mc, int w, int h, float z, PlayerClientMyState me, PlayerClientOpponentState opp) {
      if (card.getCardType().isPokemon() && card.getRetreatCost() != 0) {
         int costModifier = LogicHelper.getCostModifier(me, opp);
         costModifier = Math.max(costModifier, -card.getRetreatCost());
         int renderCost = card.getRetreatCost() + costModifier;
         mc.field_71446_o.func_110577_a(TCGResources.ENERGY_COLORLESS);
         int modo = 3 * renderCost;

         for(int i = 0; i < renderCost; ++i) {
            com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 + 31 + 6 * i - modo), (double)(h / 2 + 68), 6.0, 6.0F, 0.0, 0.0, 1.0, 1.0, z);
         }
      }

   }

   private static void drawWeakness(PokemonCardState card, Minecraft mc, int w, int h, float z, float o) {
      if (card.getCardType().isPokemon() && card.getWeakness() != null) {
         mc.field_71466_p.func_78264_a(true);
         mc.field_71446_o.func_110577_a(card.getWeakness().getTexture());
         String weaknessString = card.getWeaknessModifier().toString() + Integer.toString(card.getWeaknessValue());
         int weaknessWidth = mc.field_71466_p.func_78256_a(weaknessString);
         com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 38 - weaknessWidth / 2), (double)(h / 2 + 68), 6.0, 6.0F, 0.0, 0.0, 1.0, 1.0, z);
         mc.field_71466_p.func_78276_b(weaknessString, w / 2 - 31 - weaknessWidth / 2, h / 2 + 66, ((int)(o * 255.0F) << 24) + 12303291);
         mc.field_71466_p.func_78264_a(false);
      }

   }

   private static void drawResistance(PokemonCardState card, Minecraft mc, int w, int h, float z, float o) {
      if (card.getCardType().isPokemon() && card.getResistance() != null) {
         mc.field_71466_p.func_78264_a(true);
         mc.field_71446_o.func_110577_a(card.getResistance().getTexture());
         String resistanceString = card.getResistanceModifier().toString() + Integer.toString(card.getResistanceValue());
         int resistanceWidth = mc.field_71466_p.func_78256_a(resistanceString);
         com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 7 - resistanceWidth / 2), (double)(h / 2 + 68), 6.0, 6.0F, 0.0, 0.0, 1.0, 1.0, z);
         mc.field_71466_p.func_78276_b(resistanceString, w / 2 - resistanceWidth / 2, h / 2 + 66, ((int)(o * 255.0F) << 24) + 12303291);
         mc.field_71466_p.func_78264_a(false);
      }

   }

   public static void resetColour(float opacity) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, opacity);
   }

   private static void drawCardBackground(CommonCardState card, Minecraft mc, int w, int h, float z, float o) {
      if (card.getCardType().isCosmetic()) {
         mc.field_71446_o.func_110577_a(getCosmeticBackgroundTexture(card));
      } else {
         mc.field_71446_o.func_110577_a(getBackgroundTexture(card.getCardType(), card.getMainEnergy(), card.getData().isSpecial()));
      }

      com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 54), (double)(h / 2 - 83), 108.0, 167.0F, 0.0, 0.0, 1.0, 1.0, z);
      if (card.getData().isHolo()) {
         resetColour(o);
         GlStateManager.func_179141_d();
         GlStateManager.func_179124_c(0.42F, 0.42F, 0.42F);
         GlStateManager.func_179147_l();
         GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.DST_ALPHA);
         GlStateManager.func_179140_f();
         mc.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/holo/holo.png"));
         GlStateManager.func_179128_n(5890);
         GlStateManager.func_179094_E();
         GlStateManager.func_179152_a(8.0F, 8.0F, 8.0F);
         float f0 = (float)(Minecraft.func_71386_F() % 3000L) / 3000.0F / 8.0F;
         GlStateManager.func_179109_b(f0, 0.0F, 0.0F);
         GlStateManager.func_179114_b(-20.0F, 0.0F, 0.0F, 1.0F);
         com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 54 + 8), (double)(h / 2 - 83 + 32), 90.0, 43.0F, 0.0, 0.0, 1.0, 1.0, z);
         GlStateManager.func_179121_F();
         GlStateManager.func_179094_E();
         GlStateManager.func_179152_a(8.0F, 8.0F, 8.0F);
         float f1 = (float)(Minecraft.func_71386_F() % 4873L) / 4873.0F / 8.0F;
         GlStateManager.func_179109_b(-f1, 0.0F, 0.0F);
         GlStateManager.func_179114_b(20.0F, 0.0F, 0.0F, 1.0F);
         com.pixelmonmod.pixelmon.client.gui.GuiHelper.drawImageQuad((double)(w / 2 - 54 + 8), (double)(h / 2 - 83 + 32), 90.0, 43.0F, 0.0, 0.0, 1.0, 1.0, z);
         GlStateManager.func_179121_F();
         GlStateManager.func_179128_n(5888);
         GlStateManager.func_179112_b(770, 771);
      }

   }

   private static int drawMultiline(String description, int maxLines, FontRenderer f, int x, int y, int maxWidth, int color) {
      List lines = f.func_78271_c(description, maxWidth);
      int lineCount = lines.size();
      if (lines.size() > maxLines) {
         description = "";

         for(int i = 0; i < maxLines; ++i) {
            if (i != maxLines - 1) {
               description = description + (String)lines.get(i) + " ";
            } else {
               String lastLine;
               for(lastLine = ((String)lines.get(i)).trim(); f.func_78256_a(lastLine + "...") > maxWidth; lastLine = lastLine.substring(0, lastLine.length() - 1)) {
               }

               description = description + lastLine.trim() + "...";
            }
         }

         lineCount = maxLines;
      }

      f.func_78279_b(description, x, y, 94, color);
      return lineCount;
   }

   private static void drawString(String content, int x, int y, boolean centered, int color, boolean unicode, FontRenderer f) {
      boolean prevFlag = f.func_82883_a();
      f.func_78264_a(false);
      if (centered) {
         x -= f.func_78256_a(content) / 2;
      }

      f.func_78276_b(content, x, y, color);
      f.func_78264_a(prevFlag);
   }
}
