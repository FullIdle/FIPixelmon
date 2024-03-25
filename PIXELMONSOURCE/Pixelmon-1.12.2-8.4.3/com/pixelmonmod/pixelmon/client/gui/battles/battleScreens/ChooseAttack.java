package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.MaxMoveConverter;
import com.pixelmonmod.pixelmon.battles.attacks.TargetingInfo;
import com.pixelmonmod.pixelmon.battles.attacks.ZMove;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.SoundHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetStruggle;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class ChooseAttack extends BattleGui {
   private static boolean evoButtonHovered = false;
   private EvoButton evoButton;

   public ChooseAttack(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.evoButton = new EvoButton(99);
      this.buttons.add(new MoveButton(this, 0));
      this.buttons.add(new MoveButton(this, 1));
      this.buttons.add(new MoveButton(this, 2));
      this.buttons.add(new MoveButton(this, 3));
      this.buttons.add(this.evoButton);
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      PixelmonInGui data = this.bm.getCurrentPokemon();
      if (data != null && data.health <= 0.0F) {
         this.bm.selectedMove();
      } else {
         int areaForMoves = this.field_146294_l - 40 - 80 - 8;
         int bwidth = Math.min(areaForMoves - 10, 300) / 2;
         boolean hovering = false;
         PixelmonInGui pixelmon = this.bm.getCurrentPokemon();
         Iterator var10 = this.buttons.iterator();

         while(var10.hasNext()) {
            GuiButton button = (GuiButton)var10.next();
            if (button instanceof MoveButton) {
               MoveButton move = (MoveButton)button;
               if (pixelmon == null) {
                  button.field_146125_m = false;
               } else {
                  Moveset moveset = pixelmon.moveset;
                  if (moveset.stream().noneMatch((attack) -> {
                     return attack != null && !attack.getDisabled() && attack.pp > 0;
                  })) {
                     if (button.field_146127_k == 0) {
                        button.field_146125_m = true;
                        ((MoveButton)button).struggle();
                        this.parent.setTargeting(pixelmon, move.getAttack(), -1, -1);
                     } else {
                        button.field_146125_m = false;
                     }
                  } else if (moveset.size() >= move.field_146127_k + 1) {
                     move.setupAttack(this.bm, pixelmon);
                     move.field_146125_m = true;
                     if (move.func_146115_a()) {
                        this.drawAttackInfo(move.attack);
                        if (move.field_146125_m && move.field_146124_l && move.attack != null) {
                           this.parent.setTargeting(pixelmon, move.getAttack(), -1, -1);
                           hovering = true;
                        }
                     }
                  } else {
                     move.field_146125_m = false;
                  }

                  if (move.field_146127_k % 2 == 0) {
                     move.field_146128_h = this.detailsStart - bwidth * 2 - 8;
                  } else {
                     move.field_146128_h = this.detailsStart - bwidth - 4;
                  }

                  if (move.field_146127_k < 2) {
                     move.field_146129_i = this.field_146295_m - 70 - 6;
                  } else {
                     move.field_146129_i = this.field_146295_m - 35 - 3;
                  }

                  move.field_146120_f = bwidth;
                  move.field_146121_g = 34;
               }
            } else if (button instanceof EvoButton) {
               EvoButton move = (EvoButton)button;
               int badgeWidth = 40;
               move.field_146128_h = this.detailsStart - bwidth - 6 - badgeWidth / 2;
               move.field_146129_i = this.field_146295_m - 35 - 3 - badgeWidth / 2;
               if (move.func_146115_a() && move.field_146125_m && move.field_146124_l && move.pressable) {
                  hovering = true;
               }
            } else if (button.func_146115_a() && button.field_146125_m && button.field_146124_l) {
               hovering = true;
            }
         }

         if (pixelmon != null && this.buttons.stream().filter((it) -> {
            return it instanceof MoveButton;
         }).noneMatch(GuiButton::func_146115_a)) {
            this.parent.setTargeting(pixelmon, (Attack)pixelmon.moveset.stream().filter(Objects::nonNull).findFirst().get(), -1, -1);
         }

         GuiHelper.drawBattleCursor(hovering, (float)mouseX, (float)mouseY, this.field_73735_i);
         this.drawBackground(width, height, mouseX, mouseY);
         this.drawButtons(mouseX, mouseY);
         this.chatArea = this.field_146294_l - 40 - 80 - bwidth * 2 - 10;
         if (this.chatArea > 160) {
            try {
               if (data != null) {
                  this.parent.battleLog.setActiveMessage(I18n.func_135052_a("gui.mainmenu.whatdo", new Object[]{data.nickname}));
               }

               this.parent.battleLog.drawElement(50, height - 80, Math.min(this.chatArea, 260), 80, false);
            } catch (Exception var14) {
               var14.printStackTrace();
            }
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   private void drawAttackInfo(Attack attack) {
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      if (!this.handleButtonClick(mouseX, mouseY)) {
         if (!this.parent.battleLog.processUpDown(mouseX, mouseY)) {
            PixelmonInGui pixelmon = this.bm.getCurrentPokemon();
            if (this.evoButton.func_146116_c(this.field_146297_k, mouseX, mouseY)) {
               if (!this.bm.megaEvolving && !this.bm.dynamaxing) {
                  if (this.bm.oldGen.isYes() && (this.bm.canMegaEvolve(pixelmon) || this.bm.canUltraBurst(pixelmon))) {
                     this.bm.megaEvolving = true;
                     SoundHelper.playSound(SoundEvents.field_187616_bj);
                  } else if (this.bm.oldGen.isNo() && this.bm.canDynamax(pixelmon)) {
                     this.bm.dynamaxing = true;
                     SoundHelper.playSound(SoundEvents.field_187692_g);
                  } else if (this.bm.oldGen.isYes() && this.bm.canUseZMove(pixelmon)) {
                     this.bm.showZMoves = !this.bm.showZMoves;
                     if (this.bm.showZMoves) {
                        SoundHelper.playSound(SoundEvents.field_187649_bu);
                     }
                  }
               } else {
                  this.bm.megaEvolving = this.bm.dynamaxing = false;
               }

            } else {
               Iterator var6 = this.buttons.iterator();

               MoveButton button;
               do {
                  GuiButton b;
                  do {
                     do {
                        if (!var6.hasNext()) {
                           return;
                        }

                        b = (GuiButton)var6.next();
                        if (pixelmon == null) {
                           return;
                        }
                     } while(!(b instanceof MoveButton));

                     button = (MoveButton)b;
                     if (button.getAttack() == null) {
                        return;
                     }
                  } while(!b.func_146116_c(this.field_146297_k, mouseX, mouseY));

                  if (b.field_146127_k == 0 && button.getAttack().isAttack("Struggle") && pixelmon.moveset.stream().noneMatch((attack) -> {
                     return attack != null && !attack.getDisabled() && attack.pp > 0;
                  })) {
                     this.bm.selectedActions.add(new SetStruggle(this.bm.getCurrentPokemon().pokemonUUID, this.bm.targetted, this.bm.battleControllerIndex));
                     this.bm.selectedMove();
                     return;
                  }
               } while(this.bm.showZMoves && button.getAttack().getMove().getZMove(pixelmon.species, pixelmon.form, pixelmon.heldItem, false) == null);

               if (this.parent.canSelectTarget(button.getAttack())) {
                  this.bm.selectedAttack = button.field_146127_k;
                  this.bm.mode = BattleMode.ChooseTargets;
               } else {
                  this.parent.setTargeting(pixelmon, button.getAttack(), -1, -1);
                  int moveIndex = button.field_146127_k + (this.bm.showZMoves ? 4 : 0);
                  this.bm.selectedActions.add(new com.pixelmonmod.pixelmon.comm.packetHandlers.battles.ChooseAttack(pixelmon.pokemonUUID, this.bm.targetted, moveIndex, this.bm.battleControllerIndex, this.bm.megaEvolving, this.bm.dynamaxing));
                  this.bm.selectedMove();
               }

            }
         }
      }
   }

   public static class EvoButton extends GuiButton {
      public static final ResourceLocation MAIN_BADGE = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_badge.png");
      public static final ResourceLocation BLANK_BADGE = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_blank_badge.png");
      public static final ResourceLocation MEGA_BADGE = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_badge_mega_hollow.png");
      public static final ResourceLocation DYNA_BADGE = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_badge_dynamax_hollow.png");
      public static final ResourceLocation ZMOVE_BADGE = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_badge_zmove_hollow.png");
      public static final ResourceLocation MEGA_BADGE_GREYSCALE = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_badge_mega_grey.png");
      public static final ResourceLocation DYNA_BADGE_GREYSCALE = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_badge_dynamax_grey.png");
      public static final ResourceLocation ZMOVE_BADGE_GREYSCALE = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_badge_zmove_grey.png");
      private boolean isPressed;
      private int hoverTimer = 0;
      private int timer = 0;
      private final ClientBattleManager bm;
      public boolean pressable = false;

      public EvoButton(int index) {
         super(index, 0, 0, 40, 40, "");
         this.bm = ClientProxy.battleManager;
      }

      public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
         int r = this.field_146120_f;
         int centerX = this.field_146128_h + this.field_146120_f / 2;
         int centerY = this.field_146129_i + this.field_146121_g / 2;
         this.field_146123_n = Math.sqrt((double)((mouseX - centerX) * (mouseX - centerX) + (mouseY - centerY) * (mouseY - centerY))) < (double)((float)r / 2.0F);
         ChooseAttack.evoButtonHovered = this.field_146123_n;
         int range = 250;
         if (this.field_146125_m) {
            PixelmonInGui pixelmon = this.bm.getCurrentPokemon();
            boolean canPokemonMegaEvolve = this.bm.canMegaEvolve(pixelmon) || this.bm.canUltraBurst(pixelmon);
            boolean canPokemonZMove = this.bm.canUseZMove(pixelmon);
            boolean canPokemonDynamax = this.bm.canDynamax(pixelmon);
            boolean canMegaEvolve = this.bm.oldGen.isYes() && canPokemonMegaEvolve;
            boolean canZMove = this.bm.oldGen.isYes() && canPokemonZMove;
            boolean canDynamax = this.bm.oldGen.isNo() && canPokemonDynamax;
            boolean isMegaEvolving = canMegaEvolve && this.bm.megaEvolving;
            boolean isUsingZMove = canZMove && this.bm.showZMoves;
            boolean isDynamaxing = canDynamax && this.bm.dynamaxing;
            int mode = 0;
            boolean disabled = false;
            ResourceLocation texture;
            if (canPokemonMegaEvolve) {
               if (canMegaEvolve) {
                  texture = MEGA_BADGE;
                  if (isMegaEvolving) {
                     mode = 2;
                  } else if (this.hoverTimer > 0) {
                     mode = 1;
                  }

                  this.pressable = this.field_146123_n;
               } else {
                  disabled = true;
                  texture = MEGA_BADGE_GREYSCALE;
               }
            } else if (canPokemonZMove) {
               if (canZMove) {
                  texture = ZMOVE_BADGE;
                  if (isUsingZMove) {
                     mode = 2;
                  } else if (this.hoverTimer > 0) {
                     mode = 1;
                  }

                  this.pressable = this.field_146123_n;
               } else {
                  disabled = true;
                  texture = ZMOVE_BADGE_GREYSCALE;
               }
            } else if (canPokemonDynamax) {
               if (canDynamax) {
                  texture = DYNA_BADGE;
                  if (isDynamaxing) {
                     mode = 2;
                  } else if (this.hoverTimer > 0) {
                     mode = 1;
                  }

                  this.pressable = this.field_146123_n;
               } else {
                  disabled = true;
                  texture = DYNA_BADGE_GREYSCALE;
               }
            } else {
               texture = MAIN_BADGE;
            }

            int mod;
            int value;
            float dif;
            if (disabled && this.field_146123_n && mc.field_71462_r != null) {
               mod = this.hoverTimer % range;
               value = mod >= range / 2 ? range - 1 - mod : mod;
               dif = Math.min(1.0F, (float)value * 0.005F + 0.3F);
               GuiHelper.drawCenteredString(I18n.func_135052_a("gui.mainmenu.disabled", new Object[0]), (float)this.field_146128_h + (float)this.field_146120_f / 2.0F, (float)(this.field_146129_i - 40), (new Color(1.0F, 1.0F - dif, 1.0F - dif, 1.0F)).getRGB(), true);
            } else {
               if (mode == 0) {
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               } else if (mode == 1) {
                  mod = this.hoverTimer % range;
                  value = mod >= range / 2 ? range - 1 - mod : mod;
                  dif = (float)value * 0.0025F;
                  GlStateManager.func_179131_c(1.0F - dif, 1.0F - dif, 1.0F - dif, 1.0F);
               } else {
                  this.hoverTimer = 0;
                  Color color = new Color(Color.HSBtoRGB((float)(this.timer % 400) / 400.0F, 0.8F, 0.9F));
                  GlStateManager.func_179131_c((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
               }

               GuiHelper.drawImage(BLANK_BADGE, (double)this.field_146128_h, (double)this.field_146129_i, (double)this.field_146120_f, (double)this.field_146121_g, this.field_73735_i);
            }

            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GuiHelper.drawImage(texture, (double)this.field_146128_h, (double)this.field_146129_i, (double)this.field_146120_f, (double)this.field_146121_g, this.field_73735_i);
         } else {
            GuiHelper.drawImage(MAIN_BADGE, (double)this.field_146128_h, (double)this.field_146129_i, (double)this.field_146120_f, (double)this.field_146121_g, this.field_73735_i);
         }

         if (this.field_146123_n) {
            ++this.hoverTimer;
         } else if (this.hoverTimer % range != 0) {
            ++this.hoverTimer;
         } else {
            this.hoverTimer = 0;
         }

         ++this.timer;
      }

      public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
         return this.isPressed = super.func_146116_c(mc, mouseX, mouseY);
      }

      public void func_146118_a(int mouseX, int mouseY) {
         this.isPressed = false;
      }
   }

   public static class MoveButton extends GuiButton {
      private static final ResourceLocation MOVE_LEFT = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_left.png");
      private static final ResourceLocation MOVE_RIGHT = new ResourceLocation("pixelmon", "textures/gui/battle/moveselection_right.png");
      private static final Color COLOR = new Color(13, 143, 214);
      private static final Color DARKCOLOR = new Color(7, 68, 101);
      private Attack attack;
      private String moveName;
      private int displayedPower;
      private boolean isPressed;
      private int hoverTimer = 0;
      private int timer = 0;
      private ChooseAttack parent;

      public MoveButton(ChooseAttack parent, int index) {
         super(index, 0, 0, 0, 0, "");
         this.parent = parent;
         this.field_146125_m = false;
      }

      public Attack getAttack() {
         return this.attack;
      }

      public void struggle() {
         this.attack = new Attack("Struggle");
         this.moveName = this.attack.getMove().getLocalizedName();
      }

      public void setupAttack(ClientBattleManager bm, PixelmonInGui pig) {
         Moveset moveset = pig.moveset;
         this.attack = moveset.get(this.field_146127_k);
         if (bm.showZMoves) {
            ZMove zMove = this.attack.getMove().getZMove(pig.getBaseStats().getSpecies(), pig.form, pig.heldItem, false);
            if (zMove != null) {
               this.moveName = zMove.getLocalizedName();
            } else {
               this.moveName = this.attack.getMove().getLocalizedName();
            }
         } else if (!bm.dynamaxing && !pig.pokemonUUID.equals(bm.dynamax)) {
            this.moveName = this.attack.getMove().getLocalizedName();
            this.displayedPower = this.attack.getMove().getBasePower();
         } else {
            Attack maxMove;
            if (pig.gmaxFactor) {
               maxMove = MaxMoveConverter.getGMaxMoveFromAttack(this.attack, (PixelmonWrapper)null, pig.species, pig.species.getFormEnum(pig.form));
            } else {
               maxMove = MaxMoveConverter.getMaxMoveFromAttack(this.attack, (PixelmonWrapper)null);
            }

            this.moveName = maxMove.getMove().getLocalizedName();
            this.displayedPower = maxMove.overridePower;
         }

      }

      public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
         this.field_146123_n = !ChooseAttack.evoButtonHovered && mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
         int range = 180;
         if (this.field_146125_m && this.attack != null) {
            GlStateManager.func_179112_b(770, 771);
            GlStateManager.func_179147_l();
            boolean struggle = this.attack.isAttack("Struggle");
            float x;
            if (!this.attack.canUseMove() && !struggle) {
               GlStateManager.func_179131_c(0.5F, 0.5F, 0.5F, 1.0F);
            } else if (this.hoverTimer > 0) {
               int mod = this.hoverTimer % range;
               int value = range - (mod >= range / 2 ? range - 1 - mod : mod);
               x = Math.min(1.0F, (float)value / (float)range);
               GlStateManager.func_179131_c(0.5F + 0.5F * x, 0.8F + 0.2F * x, 0.5F + 0.5F * x, 1.0F);
            }

            GuiHelper.bindFontRenderer("", true);
            boolean isLeft = this.field_146127_k % 2 == 0;
            mc.field_71446_o.func_110577_a(isLeft ? MOVE_LEFT : MOVE_RIGHT);
            GuiHelper.drawImage((double)this.field_146128_h, (double)this.field_146129_i, (double)this.field_146120_f, (double)this.field_146121_g, this.field_73735_i);
            GuiHelper.drawScaledCenteredSplitString(this.moveName, (float)this.field_146128_h + (float)this.field_146120_f / 2.0F, (float)(this.field_146129_i + 9), 0, 16.0F, this.field_146120_f - 4, false);
            if (!struggle) {
               GuiHelper.drawBar((double)this.field_146128_h, (double)(this.field_146129_i + 26), (double)(this.field_146120_f + 6), 8.0, (float)this.attack.pp / (float)this.attack.getMaxPP(), this.attack.canUseMove() ? COLOR : DARKCOLOR);
               GuiHelper.drawScaledCenteredString(this.attack.pp + "/" + this.attack.getMaxPP(), (float)this.field_146128_h + (float)this.field_146120_f / 2.0F, (float)this.field_146129_i + 27.5F, this.attack.canUseMove() ? 15790320 : 12632256, 12.0F);
               EnumType type = this.moveName.equalsIgnoreCase("Max Guard") ? EnumType.Normal : this.attack.getType();
               mc.field_71446_o.func_110577_a(GuiResources.types);
               x = type.textureX;
               float y = type.textureY;
               GuiHelper.drawImageQuad(isLeft ? (double)(this.field_146128_h + 6) : (double)(this.field_146128_h + this.field_146120_f - 22), (double)((float)this.field_146129_i + (float)this.field_146121_g / 2.0F - 12.0F), 16.0, 16.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), this.field_73735_i);
            } else {
               GuiHelper.drawBar((double)this.field_146128_h, (double)(this.field_146129_i + 26), (double)(this.field_146120_f + 6), 8.0, 1.0F, COLOR);
               mc.field_71446_o.func_110577_a(GuiResources.infinity);
               GuiHelper.drawImageQuad((double)((float)this.field_146128_h + (float)this.field_146120_f / 2.0F - 6.0F), (double)((float)this.field_146129_i + 23.75F), 12.0, 12.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            }

            GuiHelper.resetFontRenderer();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            boolean showGlobalInfo = false;
            boolean showAttackInfo = false;
            if (mc.field_71462_r != null && this.field_146127_k == 0) {
               showGlobalInfo = true;
               Iterator var18 = this.parent.buttons.iterator();

               while(var18.hasNext()) {
                  GuiButton button = (GuiButton)var18.next();
                  if (button instanceof MoveButton) {
                     MoveButton mb = (MoveButton)button;
                     if (mb.field_146123_n) {
                        showGlobalInfo = false;
                        break;
                     }
                  }
               }

               this.parent.parent.showGlobalInfo = showGlobalInfo;
            }

            if (this.field_146123_n) {
               ++this.hoverTimer;
               showAttackInfo = true;
               showGlobalInfo = false;
            } else if (this.hoverTimer % range != 0) {
               ++this.hoverTimer;
            } else {
               this.hoverTimer = 0;
            }

            if (mc.field_71462_r != null) {
               int w = mc.field_71462_r.field_146294_l;
               int h = mc.field_71462_r.field_146295_m;
               if (showAttackInfo) {
                  int accuracy = this.attack.getMove().getAccuracy();
                  if (accuracy < 0) {
                     accuracy = 100;
                  }

                  GuiHelper.drawCenteredSquashedString(mc.field_71466_p, this.moveName, false, 75.0, w - 40, h - 76, 16777215, false);
                  GuiHelper.drawScaledCenteredSplitString(this.attack.getAttackCategory().getLocalizedName(), (float)(w - 40), (float)(h - 35), 16777215, 16.0F, 100, false);
                  GuiHelper.drawScaledCenteredSplitString(I18n.func_135052_a("gui.battle.powerval", new Object[]{this.displayedPower}), (float)(w - 40), (float)(h - 24), 16777215, 16.0F, 100, false);
                  GuiHelper.drawScaledCenteredSplitString(I18n.func_135052_a("gui.battle.accuracyval", new Object[]{accuracy}), (float)(w - 40), (float)(h - 13), 16777215, 16.0F, 100, false);
                  TargetingInfo ti = this.attack.getMove().getTargetingInfo();
                  GlStateManager.func_179094_E();
                  GlStateManager.func_179109_b(0.0F, -1.0F, 0.0F);
                  Gui.func_73734_a(w - 50, h - 62, w - 30, h - 52, -14475232);
                  Gui.func_73734_a(w - 50, h - 50, w - 30, h - 40, -14475232);
                  Gui.func_73734_a(w - 72, h - 62, w - 52, h - 52, -14475232);
                  Gui.func_73734_a(w - 72, h - 50, w - 52, h - 40, -14475232);
                  Gui.func_73734_a(w - 28, h - 62, w - 8, h - 52, -14475232);
                  Gui.func_73734_a(w - 28, h - 50, w - 8, h - 40, -14475232);
                  Gui.func_73734_a(w - 50 + 1, h - 62 + 1, w - 30 - 1, h - 52 - 1, ti.hitsAdjacentFoe ? -15888426 : -6249825);
                  Gui.func_73734_a(w - 50 + 1, h - 50 + 1, w - 30 - 1, h - 40 - 1, ti.hitsAdjacentAlly ? -15888426 : -6249825);
                  Gui.func_73734_a(w - 72 + 1, h - 62 + 1, w - 52 - 1, h - 52 - 1, ti.hitsOppositeFoe ? -15888426 : -6249825);
                  Gui.func_73734_a(w - 72 + 1, h - 50 + 1, w - 52 - 1, h - 40 - 1, ti.hitsSelf ? -15888426 : -6249825);
                  Gui.func_73734_a(w - 28 + 1, h - 62 + 1, w - 8 - 1, h - 52 - 1, ti.hitsExtendedFoe ? -15888426 : -6249825);
                  Gui.func_73734_a(w - 28 + 1, h - 50 + 1, w - 8 - 1, h - 40 - 1, ti.hitsExtendedAlly ? -15888426 : -6249825);
                  GlStateManager.func_179121_F();
               }
            }

            ++this.timer;
         }

      }

      public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
         Attack attack = this.getAttack();
         return attack == null || (attack.canUseMove() || attack.isAttack("Struggle")) && (this.isPressed = super.func_146116_c(mc, mouseX, mouseY));
      }

      public void func_146118_a(int mouseX, int mouseY) {
         this.isPressed = false;
      }
   }
}
