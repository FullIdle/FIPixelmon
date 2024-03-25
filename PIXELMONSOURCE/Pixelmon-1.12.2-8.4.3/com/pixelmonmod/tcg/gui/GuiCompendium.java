package com.pixelmonmod.tcg.gui;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.helper.CardHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiCompendium extends GuiScreen {
   private GuiButton common;
   private GuiButton uncommon;
   private GuiButton rare;
   private GuiButton holorare;
   private GuiButton ultrarare;
   private GuiButton secretrare;
   private GuiButton grass;
   private GuiButton fire;
   private GuiButton water;
   private GuiButton lightning;
   private GuiButton fighting;
   private GuiButton psychic;
   private GuiButton colorless;
   private GuiButton darkness;
   private GuiButton metal;
   private GuiButton dragon;
   private GuiButton fairy;
   private GuiButton base;
   private GuiButton jung;
   private GuiButton foss;
   private GuiButton tero;
   private GuiButton gymh;
   private GuiButton wbsp;
   private List selectedRarities = Lists.newArrayList();
   private List selectedEnergies = Lists.newArrayList();
   private List selectedSets = Lists.newArrayList();
   private List cardList = CardRegistry.getAll();
   private final int SCROLL_SPEED = 70;
   private ImmutableCard renderLast = null;
   private int renderLastPos = 0;
   private boolean mousePressed = false;
   private boolean isMouseInGrid = false;
   private boolean tooFarLeft = false;
   private boolean tooFarRight = false;
   private boolean canScroll = true;
   private int scrollModifier = 0;
   public boolean isMouseClicked = false;

   public void func_73876_c() {
      super.func_73876_c();
      this.handleMouseWheel();
   }

   public void func_73866_w_() {
      this.field_146292_n.clear();
      int xOffset = this.field_146294_l / 2 - 142;
      int yOffset = this.field_146295_m / 2 - 118;
      int togglebuttonWidth = 20;
      int togglebuttonHeight = 20;
      int buttonID = 0;
      int spacingXC = 22;
      int spacingYC = 22;
      int spacingX = 22;
      int spacingY = 22;
      this.common = new GuiButton(buttonID++, xOffset + spacingXC * 4, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.common);
      this.uncommon = new GuiButton(buttonID++, xOffset + spacingXC * 5, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.uncommon);
      this.rare = new GuiButton(buttonID++, xOffset + spacingXC * 6, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.rare);
      this.holorare = new GuiButton(buttonID++, xOffset + spacingXC * 7, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.holorare);
      this.ultrarare = new GuiButton(buttonID++, xOffset + spacingXC * 8, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.ultrarare);
      this.grass = new GuiButton(buttonID++, xOffset + spacingX * 1, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.grass);
      this.fire = new GuiButton(buttonID++, xOffset + spacingX * 2, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.fire);
      this.water = new GuiButton(buttonID++, xOffset + spacingX * 3, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.water);
      this.lightning = new GuiButton(buttonID++, xOffset + spacingX * 4, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.lightning);
      this.fighting = new GuiButton(buttonID++, xOffset + spacingX * 5, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.fighting);
      this.psychic = new GuiButton(buttonID++, xOffset + spacingX * 6, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.psychic);
      this.colorless = new GuiButton(buttonID++, xOffset + spacingX * 7, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.colorless);
      this.darkness = new GuiButton(buttonID++, xOffset + spacingX * 8, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.darkness);
      this.metal = new GuiButton(buttonID++, xOffset + spacingX * 9, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.metal);
      this.dragon = new GuiButton(buttonID++, xOffset + spacingX * 10, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.dragon);
      this.fairy = new GuiButton(buttonID++, xOffset + spacingX * 11, yOffset + spacingY * 2, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.fairy);
      this.base = new GuiButton(buttonID++, (int)((double)xOffset + (double)spacingXC * 3.5), yOffset + spacingYC * 0, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.base);
      this.jung = new GuiButton(buttonID++, (int)((double)xOffset + (double)spacingXC * 4.5), yOffset + spacingYC * 0, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.jung);
      this.foss = new GuiButton(buttonID++, (int)((double)xOffset + (double)spacingXC * 5.5), yOffset + spacingYC * 0, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.foss);
      this.tero = new GuiButton(buttonID++, (int)((double)xOffset + (double)spacingXC * 6.5), yOffset + spacingYC * 0, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.tero);
      this.gymh = new GuiButton(buttonID++, (int)((double)xOffset + (double)spacingXC * 7.5), yOffset + spacingYC * 0, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.gymh);
      this.wbsp = new GuiButton(buttonID++, (int)((double)xOffset + (double)spacingXC * 8.5), yOffset + spacingYC * 0, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.wbsp);
   }

   private GuiTarget getGuiTarget(int cardIndex, int size, ScaledResolution res) {
      int sizeModXA = -26;
      int sizeModXB = 27;
      int sizeModYA = -42;
      int sizeModYB = 40;
      return new GuiTarget(res.func_78326_a() / 2 + cardIndex * 250 / 4 - (size - 1) * 125 / 4 + this.scrollModifier / 4 - sizeModXB, res.func_78328_b() / 2 - sizeModYB, sizeModXB - sizeModXA, sizeModYB - sizeModYA, 0.0F);
   }

   protected void func_146284_a(GuiButton guibutton) {
      this.scrollModifier = 0;
      switch (guibutton.field_146127_k) {
         case 0:
            if (this.selectedRarities.contains(CardRarity.COMMON)) {
               this.selectedRarities.remove(CardRarity.COMMON);
            } else {
               this.selectedRarities.add(CardRarity.COMMON);
            }
            break;
         case 1:
            if (this.selectedRarities.contains(CardRarity.UNCOMMON)) {
               this.selectedRarities.remove(CardRarity.UNCOMMON);
            } else {
               this.selectedRarities.add(CardRarity.UNCOMMON);
            }
            break;
         case 2:
            if (this.selectedRarities.contains(CardRarity.RARE)) {
               this.selectedRarities.remove(CardRarity.RARE);
            } else {
               this.selectedRarities.add(CardRarity.RARE);
            }
            break;
         case 3:
            if (this.selectedRarities.contains(CardRarity.HOLORARE)) {
               this.selectedRarities.remove(CardRarity.HOLORARE);
            } else {
               this.selectedRarities.add(CardRarity.HOLORARE);
            }
            break;
         case 4:
            if (this.selectedRarities.contains(CardRarity.ULTRARARE)) {
               this.selectedRarities.remove(CardRarity.ULTRARARE);
            } else {
               this.selectedRarities.add(CardRarity.ULTRARARE);
            }
            break;
         case 5:
            if (this.selectedEnergies.contains(Energy.GRASS)) {
               this.selectedEnergies.remove(Energy.GRASS);
            } else {
               this.selectedEnergies.add(Energy.GRASS);
            }
            break;
         case 6:
            if (this.selectedEnergies.contains(Energy.FIRE)) {
               this.selectedEnergies.remove(Energy.FIRE);
            } else {
               this.selectedEnergies.add(Energy.FIRE);
            }
            break;
         case 7:
            if (this.selectedEnergies.contains(Energy.WATER)) {
               this.selectedEnergies.remove(Energy.WATER);
            } else {
               this.selectedEnergies.add(Energy.WATER);
            }
            break;
         case 8:
            if (this.selectedEnergies.contains(Energy.LIGHTNING)) {
               this.selectedEnergies.remove(Energy.LIGHTNING);
            } else {
               this.selectedEnergies.add(Energy.LIGHTNING);
            }
            break;
         case 9:
            if (this.selectedEnergies.contains(Energy.FIGHTING)) {
               this.selectedEnergies.remove(Energy.FIGHTING);
            } else {
               this.selectedEnergies.add(Energy.FIGHTING);
            }
            break;
         case 10:
            if (this.selectedEnergies.contains(Energy.PSYCHIC)) {
               this.selectedEnergies.remove(Energy.PSYCHIC);
            } else {
               this.selectedEnergies.add(Energy.PSYCHIC);
            }
            break;
         case 11:
            if (this.selectedEnergies.contains(Energy.COLORLESS)) {
               this.selectedEnergies.remove(Energy.COLORLESS);
            } else {
               this.selectedEnergies.add(Energy.COLORLESS);
            }
            break;
         case 12:
            if (this.selectedEnergies.contains(Energy.DARKNESS)) {
               this.selectedEnergies.remove(Energy.DARKNESS);
            } else {
               this.selectedEnergies.add(Energy.DARKNESS);
            }
            break;
         case 13:
            if (this.selectedEnergies.contains(Energy.METAL)) {
               this.selectedEnergies.remove(Energy.METAL);
            } else {
               this.selectedEnergies.add(Energy.METAL);
            }
            break;
         case 14:
            if (this.selectedEnergies.contains(Energy.DRAGON)) {
               this.selectedEnergies.remove(Energy.DRAGON);
            } else {
               this.selectedEnergies.add(Energy.DRAGON);
            }
            break;
         case 15:
            if (this.selectedEnergies.contains(Energy.FAIRY)) {
               this.selectedEnergies.remove(Energy.FAIRY);
            } else {
               this.selectedEnergies.add(Energy.FAIRY);
            }
            break;
         case 16:
            if (this.selectedSets.contains(1)) {
               this.selectedSets.remove(1);
            } else {
               this.selectedSets.add(1);
            }
            break;
         case 17:
            if (this.selectedSets.contains(2)) {
               this.selectedSets.remove(2);
            } else {
               this.selectedSets.add(2);
            }
            break;
         case 18:
            if (this.selectedSets.contains(3)) {
               this.selectedSets.remove(3);
            } else {
               this.selectedSets.add(3);
            }
            break;
         case 19:
            if (this.selectedSets.contains(4)) {
               this.selectedSets.remove(4);
            } else {
               this.selectedSets.add(4);
            }
            break;
         case 20:
            if (this.selectedSets.contains(5)) {
               this.selectedSets.remove(5);
            } else {
               this.selectedSets.add(5);
            }
            break;
         case 21:
            if (this.selectedSets.contains(1000)) {
               for(int i = 0; i < this.selectedSets.size(); ++i) {
                  if ((Integer)this.selectedSets.get(i) == 1000) {
                     this.selectedSets.remove(i);
                  }
               }
            } else {
               this.selectedSets.add(1000);
            }
      }

      List cardList = Lists.newArrayList(CardRegistry.getAll());
      List toRemove = new ArrayList();
      if (!this.selectedRarities.isEmpty()) {
         cardList.stream().filter((card) -> {
            return !this.selectedRarities.contains(card.getRarity());
         }).forEach((card) -> {
            toRemove.add(card);
         });
         cardList.removeAll(toRemove);
         toRemove.clear();
      }

      if (!this.selectedEnergies.isEmpty()) {
         cardList.stream().filter((card) -> {
            return !this.selectedEnergies.contains(card.getMainEnergy());
         }).forEach((card) -> {
            toRemove.add(card);
         });
         cardList.removeAll(toRemove);
         toRemove.clear();
      }

      if (!this.selectedSets.isEmpty()) {
         cardList.stream().filter((card) -> {
            return !this.selectedSets.contains(card.getSetID());
         }).forEach((card) -> {
            toRemove.add(card);
         });
         cardList.removeAll(toRemove);
         toRemove.clear();
      }

      this.cardList = cardList;
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (!this.selectedSets.isEmpty() && !this.selectedSets.contains(1)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.15F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/1.png"));
      GuiHelper.drawImageQuad((double)(this.base.field_146128_h + 2), (double)(this.base.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedSets.isEmpty() && !this.selectedSets.contains(2)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.15F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/2.png"));
      GuiHelper.drawImageQuad((double)(this.jung.field_146128_h + 2), (double)(this.jung.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedSets.isEmpty() && !this.selectedSets.contains(3)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.15F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/3.png"));
      GuiHelper.drawImageQuad((double)(this.foss.field_146128_h + 2), (double)(this.foss.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedSets.isEmpty() && !this.selectedSets.contains(4)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.15F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/4.png"));
      GuiHelper.drawImageQuad((double)(this.tero.field_146128_h + 2), (double)(this.tero.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedSets.isEmpty() && !this.selectedSets.contains(5)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.15F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/5.png"));
      GuiHelper.drawImageQuad((double)(this.gymh.field_146128_h + 2), (double)(this.gymh.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedSets.isEmpty() && !this.selectedSets.contains(1000)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.15F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/1000.png"));
      GuiHelper.drawImageQuad((double)(this.wbsp.field_146128_h + 2), (double)(this.wbsp.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedRarities.isEmpty() && !this.selectedRarities.contains(CardRarity.COMMON)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/common.png"));
      GuiHelper.drawImageQuad((double)(this.common.field_146128_h + 2), (double)(this.common.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedRarities.isEmpty() && !this.selectedRarities.contains(CardRarity.UNCOMMON)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/uncommon.png"));
      GuiHelper.drawImageQuad((double)(this.uncommon.field_146128_h + 2), (double)(this.uncommon.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedRarities.isEmpty() && !this.selectedRarities.contains(CardRarity.RARE)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/rare.png"));
      GuiHelper.drawImageQuad((double)(this.rare.field_146128_h + 2), (double)(this.rare.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedRarities.isEmpty() && !this.selectedRarities.contains(CardRarity.HOLORARE)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/holo_rare.png"));
      GuiHelper.drawImageQuad((double)(this.holorare.field_146128_h + 2), (double)(this.holorare.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedRarities.isEmpty() && !this.selectedRarities.contains(CardRarity.ULTRARARE)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/ultra_rare.png"));
      GuiHelper.drawImageQuad((double)(this.ultrarare.field_146128_h + 2), (double)(this.ultrarare.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.GRASS)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.GRASS.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.grass.field_146128_h + 3), (double)(this.grass.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.FIRE)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.FIRE.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.fire.field_146128_h + 3), (double)(this.fire.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.WATER)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.WATER.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.water.field_146128_h + 3), (double)(this.water.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.LIGHTNING)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.LIGHTNING.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.lightning.field_146128_h + 3), (double)(this.lightning.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.FIGHTING)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.FIGHTING.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.fighting.field_146128_h + 3), (double)(this.fighting.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.PSYCHIC)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.PSYCHIC.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.psychic.field_146128_h + 3), (double)(this.psychic.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.COLORLESS)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.COLORLESS.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.colorless.field_146128_h + 3), (double)(this.colorless.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.DARKNESS)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.DARKNESS.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.darkness.field_146128_h + 3), (double)(this.darkness.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.METAL)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.METAL.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.metal.field_146128_h + 3), (double)(this.metal.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.DRAGON)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.DRAGON.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.dragon.field_146128_h + 3), (double)(this.dragon.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      if (!this.selectedEnergies.isEmpty() && !this.selectedEnergies.contains(Energy.FAIRY)) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.35F);
      } else {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(Energy.FAIRY.getHighResTexture());
      GuiHelper.drawImageQuad((double)(this.fairy.field_146128_h + 3), (double)(this.fairy.field_146129_i + 3), 14.0, 14.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      ScaledResolution res = new ScaledResolution(this.field_146297_k);
      int size = this.cardList.size();
      boolean isMouseOut = true;

      int cardX;
      for(int cardIndex = 0; cardIndex < size; ++cardIndex) {
         ImmutableCard card = (ImmutableCard)this.cardList.get(cardIndex);
         if (card == null) {
            return;
         }

         GuiTarget target = this.getGuiTarget(cardIndex, size, res);
         this.isMouseInGrid = target.isInside(mouseX, mouseY);
         float scaleFactor = 0.5F;
         if (this.isMouseInGrid) {
            this.renderLast = card;
            this.renderLastPos = cardIndex;
            isMouseOut = false;
            this.canScroll = false;
         } else {
            this.canScroll = true;
            GL11.glPushMatrix();
            GL11.glScaled((double)scaleFactor, (double)scaleFactor, (double)scaleFactor);
            cardX = (int)((float)(res.func_78326_a() * 2 + cardIndex * 250 - (size - 1) * 125 + this.scrollModifier) / (scaleFactor * 2.0F));
            int cardY = (int)((float)(res.func_78328_b() * 2) / (scaleFactor * 2.0F));
            float cardZ = this.isMouseInGrid ? this.field_73735_i + 0.1F : this.field_73735_i;
            float opacity = 1.0F;
            int printX = -32;
            int printY = 105;
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.func_179141_d();
            GlStateManager.func_179140_f();
            if (card != null) {
               if (card.getRarity() != null && card.getRarity() != CardRarity.SECRETRARE && card.getMainEnergy() != null && card.getCardType() != null && card.getCardType().isPokemon()) {
                  this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "textures/items/" + card.getMainEnergy().getUnlocalizedName() + "/cardSprite_" + card.getMainEnergy().getUnlocalizedName() + "_" + card.getRarity().getFileName() + ".png"));
                  GuiHelper.drawImageQuad((double)(cardX / 2 + printX), (double)(cardY / 2 + printY), 64.0, 64.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
               }

               if (card.getRarity() != null && card.getRarity() != CardRarity.SECRETRARE && card.getMainEnergy() != null && card.getCardType() != null && card.getCardType() == CardType.ENERGY) {
                  this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "textures/items/" + card.getMainEnergy().getUnlocalizedName() + "/cardSprite_" + card.getMainEnergy().getUnlocalizedName() + "_" + card.getRarity().getFileName() + ".png"));
                  GuiHelper.drawImageQuad((double)(cardX / 2 + printX), (double)(cardY / 2 + printY), 64.0, 64.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
               }
            }

            GlStateManager.func_179118_c();
            CardHelper.drawCard(new CommonCardState(card), this.field_146297_k, cardX, cardY, cardZ - 0.1F, opacity, 0.0, (PlayerClientMyState)null, (PlayerClientOpponentState)null);
            GL11.glScaled((double)(-scaleFactor), (double)(-scaleFactor), (double)(-scaleFactor));
            GL11.glPopMatrix();
            if (cardIndex == 0) {
               this.tooFarLeft = cardX > 285;
            }

            if (cardIndex == size - 1) {
               this.tooFarRight = cardX < this.field_146294_l + 1180;
            }
         }
      }

      if (isMouseOut) {
         this.renderLast = null;
         this.renderLastPos = 0;
      }

      if (this.renderLast != null) {
         GuiTarget target = this.getGuiTarget(this.renderLastPos, size, res);
         this.isMouseInGrid = target.isInside(mouseX, mouseY);
         float scaleFactor = 0.5F;
         if (this.isMouseInGrid) {
            scaleFactor = 1.0F;
         }

         float opacity = 1.0F;
         GL11.glPushMatrix();
         GL11.glScaled((double)scaleFactor, (double)scaleFactor, (double)scaleFactor);
         int cardX = (int)((float)(res.func_78326_a() * 2 + this.renderLastPos * 250 - (size - 1) * 125 + this.scrollModifier) / (scaleFactor * 2.0F));
         cardX = (int)((float)(res.func_78328_b() * 2) / (scaleFactor * 2.0F));
         float cardZ = this.isMouseInGrid ? this.field_73735_i + 0.1F : this.field_73735_i;
         CardHelper.drawCard(new CommonCardState(this.renderLast), this.field_146297_k, cardX, cardX, cardZ - 0.1F, opacity, 0.0, (PlayerClientMyState)null, (PlayerClientOpponentState)null);
         GL11.glScaled((double)(-scaleFactor), (double)(-scaleFactor), (double)(-scaleFactor));
         GL11.glPopMatrix();
         if (Mouse.isButtonDown(0) && !this.isMouseClicked) {
            this.isMouseClicked = true;
            if (this.field_146297_k.field_71439_g.func_70003_b(4, "tcg give")) {
               this.field_146297_k.field_71439_g.func_71165_d("/tcg give card " + this.field_146297_k.field_71439_g.getDisplayNameString() + " " + this.renderLast.getCode());
            }
         }
      }

      GlStateManager.func_179084_k();
      if (!Mouse.isButtonDown(0) && this.isMouseClicked) {
         this.isMouseClicked = false;
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   private void handleMouseWheel() {
      int mousewheelDirection = Mouse.getDWheel();
      if ((mousewheelDirection >= 120 || mousewheelDirection <= -120) && this.canScroll && (!this.tooFarRight || mousewheelDirection > -120) && (!this.tooFarLeft || mousewheelDirection < 120)) {
         this.scrollModifier += 70 * mousewheelDirection / 120;
      }

   }
}
