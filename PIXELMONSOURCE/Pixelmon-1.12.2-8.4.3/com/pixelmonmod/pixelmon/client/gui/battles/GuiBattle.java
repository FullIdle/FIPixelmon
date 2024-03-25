package com.pixelmonmod.pixelmon.client.gui.battles;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.MaxMoveConverter;
import com.pixelmonmod.pixelmon.battles.attacks.TargetingInfo;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.camera.GuiChattableCamera;
import com.pixelmonmod.pixelmon.client.gui.GuiEvolve;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiItemDrops;
import com.pixelmonmod.pixelmon.client.gui.GuiMegaItem;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleLogElement;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleScreen;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.ChooseAttack;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.MegaEvolution;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.old_gui.chooseMove.ChooseEther;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.old_gui.chooseMove.ReplaceAttack;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.ChooseTargets;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.LevelUpScreen;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.WaitingScreen;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.bag.ApplyToPokemon;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.bag.ChooseBag;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.pokemon.ChoosePokemon;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.pokemon.EnforcedSwitch;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.yesNo.YesNoForfeit;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.yesNo.YesNoReplaceMove;
import com.pixelmonmod.pixelmon.client.gui.battles.pokemonOverlays.OverlayBase;
import com.pixelmonmod.pixelmon.client.gui.battles.pokemonOverlays.OverlayNew;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiChatExtension;
import com.pixelmonmod.pixelmon.client.music.BattleMusic;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BattleGuiClosed;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.RemoveSpectator;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.gui.GuiBattleUpdatePacket;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.storage.ClientData;
import com.pixelmonmod.pixelmon.util.helpers.CursorHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiBattle extends GuiChattableCamera {
   private int guiWidth = 300;
   private int guiHeight = 60;
   public ClientBattleManager bm;
   ArrayList screenList = new ArrayList();
   BattleScreen currentScreen = null;
   public OverlayBase pokemonOverlay = null;
   private GuiButton stopSpectateButton = null;
   public BattleLogElement battleLog;
   public boolean showGlobalInfo = true;
   boolean first = true;
   public int mouseOverButton = 0;
   private int shutdownDelay = 0;
   private int tick;

   public GuiBattle() {
      EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
      player.func_70637_d(false);
      player.func_70095_a(false);
      player.field_70702_br = 0.0F;
      player.field_191988_bg = 0.0F;
      GuiPixelmonOverlay.isVisible = false;
      this.bm = ClientProxy.battleManager;
      this.battleLog = new BattleLogElement(this.bm);
      this.screenList.add(new ApplyToPokemon(this, BattleMode.ApplyToPokemon));
      this.screenList.add(new ChooseAttack(this, BattleMode.ChooseAttack));
      this.screenList.add(new ChooseBag(this, BattleMode.ChooseBag));
      this.screenList.add(new ChooseEther(this));
      this.screenList.add(new ChoosePokemon(this, BattleMode.ChoosePokemon));
      this.screenList.add(new ChooseTargets(this, BattleMode.ChooseTargets));
      this.screenList.add(new EnforcedSwitch(this, BattleMode.EnforcedSwitch));
      this.screenList.add(new LevelUpScreen(this, BattleMode.LevelUp));
      this.screenList.add(new MegaEvolution(this));
      this.screenList.add(new ReplaceAttack(this));
      this.screenList.add(new YesNoForfeit(this));
      this.screenList.add(new YesNoReplaceMove(this));
      this.screenList.add(new WaitingScreen(this, BattleMode.Waiting));
      this.screenList.add(new ChooseAttack(this, BattleMode.MainMenu));
      if (this.bm.teamPokemon != null && this.bm.displayedEnemyPokemon != null) {
         this.pokemonOverlay = new OverlayNew(this);
      }

   }

   public void func_73866_w_() {
      super.func_73866_w_();
      if (this.bm.isSpectating) {
         this.addStopSpectateButton();
      }

      Iterator var1 = this.screenList.iterator();

      while(var1.hasNext()) {
         GuiScreen screen = (GuiScreen)var1.next();
         screen.func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
      }

      if (this.pokemonOverlay instanceof OverlayNew) {
         this.pokemonOverlay.func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
      }

   }

   private void addStopSpectateButton() {
      this.stopSpectateButton = new GuiButton(0, this.field_146294_l / 2 - 50, 0, 100, 20, I18n.func_135052_a("gui.spectate.stopspectate", new Object[0]));
      this.field_146292_n.add(this.stopSpectateButton);
   }

   public void restoreSettingsAndClose() {
      restoreSettingsAndCloseStatic(this.bm);
   }

   public static void restoreSettingsAndCloseStatic(@Nullable ClientBattleManager bm) {
      Minecraft mc = Minecraft.func_71410_x();
      if (bm != null) {
         bm.restoreSettingsAndClose();
      }

      mc.field_71439_g.func_71053_j();
      GuiPixelmonOverlay.isVisible = true;
      if (ServerStorageDisplay.bossDrops != null) {
         Minecraft.func_71410_x().func_147108_a(new GuiItemDrops());
      } else if (bm != null && !bm.evolveList.isEmpty()) {
         mc.func_147108_a(new GuiEvolve());
      } else if (ClientData.openMegaItemGui >= 0) {
         mc.func_147108_a(new GuiMegaItem(ClientData.openMegaItemGui > 0));
      } else {
         Pixelmon.network.sendToServer(new BattleGuiClosed());
      }

      BattleMusic.endBattleMusic();
      CursorHelper.setCursor(CursorHelper.DEFAULT_CURSOR);
   }

   public void func_146281_b() {
      super.func_146281_b();
      if (this.bm.battleEnded && !this.bm.hasNewAttacks()) {
         Pixelmon.network.sendToServer(new BattleGuiClosed());
      }

   }

   private void selectScreen() {
      Iterator var1 = this.screenList.iterator();

      while(var1.hasNext()) {
         BattleScreen bs = (BattleScreen)var1.next();
         if (bs.isScreen()) {
            this.currentScreen = bs;
            break;
         }
      }

   }

   public void selectScreenImmediate(BattleMode mode) {
      this.bm.mode = mode;
      this.selectScreen();
   }

   private void drawPokemonOverlays() {
      if (this.pokemonOverlay != null) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.pokemonOverlay.draw(this.field_146294_l, this.field_146295_m, this.guiWidth, this.guiHeight);
      }

   }

   public static void drawHealthBar(int x, int y, int width, int height, float health, int maxHealth) {
      GlStateManager.func_179091_B();
      GlStateManager.func_179142_g();
      GlStateManager.func_179094_E();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      GlStateManager.func_179090_x();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      int barWidth = width - 6;
      buffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(0.4F, 0.4F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)x, (double)(y + height), 0.0).func_181666_a(0.4F, 0.4F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)(y + height), 0.0).func_181666_a(0.4F, 0.4F, 0.4F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)y, 0.0).func_181666_a(0.4F, 0.4F, 0.4F, 1.0F).func_181675_d();
      float Percent = health / (float)maxHealth;
      if (Percent > 1.0F) {
         Percent = 1.0F;
      }

      float CurWidth = Percent * (float)barWidth;
      float r;
      float g;
      float b;
      if (health <= (float)(maxHealth / 5)) {
         r = 0.8F;
         g = 0.0F;
         b = 0.0F;
      } else if (health <= (float)(maxHealth / 2)) {
         r = 1.0F;
         g = 1.0F;
         b = 0.4F;
      } else {
         r = 0.2F;
         g = 1.0F;
         b = 0.2F;
      }

      buffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(r, g, b, 1.0F).func_181675_d();
      buffer.func_181662_b((double)x, (double)(y + height), 0.0).func_181666_a(r, g, b, 1.0F).func_181675_d();
      buffer.func_181662_b((double)((float)x + CurWidth), (double)(y + height), 0.0).func_181666_a(r, g, b, 1.0F).func_181675_d();
      buffer.func_181662_b((double)((float)x + CurWidth), (double)y, 0.0).func_181666_a(r, g, b, 1.0F).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179121_F();
      GlStateManager.func_179098_w();
      GlStateManager.func_179101_C();
      GlStateManager.func_179119_h();
   }

   public void drawButton(BattleMode mode, int x, int y, int buttonWidth, int buttonHeight, String string, int mouseX, int mouseY, int ind) {
      this.drawButton(mode, x, y, buttonWidth, buttonHeight, string, false, mouseX, mouseY, ind);
   }

   public void drawButton(BattleMode mode, int x, int y, int buttonWidth, int buttonHeight, String string, boolean isDisabled, int mouseX, int mouseY, int ind) {
      if (mode == BattleMode.MainMenu) {
         if (mouseX > x && mouseX < x + buttonWidth && mouseY > y && mouseY < y + buttonHeight) {
            GuiHelper.drawImageQuad((double)x, (double)y, (double)buttonWidth, (float)buttonHeight, 0.604687511920929, 0.3291666805744171, 0.7640625238418579, 0.40833333134651184, this.field_73735_i);
         }
      } else if (mode == BattleMode.ChooseAttack) {
         GuiHelper.drawImageQuad((double)x, (double)y, (double)buttonWidth, (float)buttonHeight, 0.3218750059604645, 0.3166666626930237, 0.614062488079071, 0.4208333194255829, this.field_73735_i);
         if (mouseX > x && mouseX < x + buttonWidth && mouseY > y && mouseY < y + buttonHeight) {
            GuiHelper.drawImageQuad((double)(x + 2), (double)(y + 2), (double)(buttonWidth - 5), (float)(buttonHeight - 4), 0.03593749925494194, 0.3229166567325592, 0.3125, 0.40625, this.field_73735_i);
            this.mouseOverButton = ind;
         }
      }

      if (!isDisabled) {
         this.func_73732_a(this.field_146297_k.field_71466_p, string, x + buttonWidth / 2, y + buttonHeight / 2 - 3, 16777215);
      } else {
         this.field_146297_k.field_71466_p.func_78276_b(string, x + buttonWidth / 2 - this.field_146297_k.field_71466_p.func_78256_a(string) / 2, y + buttonHeight / 2 - 3, 13421772);
         GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      }

   }

   public void func_146282_l() {
      if (Keyboard.getEventKeyState()) {
         int i = Keyboard.getEventKey();
         char c0 = Keyboard.getEventCharacter();
         this.func_73869_a(c0, i);
      }

      super.func_146282_l();
   }

   protected void func_73869_a(char key, int keyCode) {
      super.func_73869_a(key, keyCode);
      if (!GuiChatExtension.chatOpen && keyCode == 28 && this.bm.hasMoreMessages()) {
      }

      if (keyCode == 1 && this.bm.mode == BattleMode.ChooseTargets) {
         this.bm.selectedAttack = -1;
         this.bm.mode = BattleMode.MainMenu;
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      try {
         super.func_73864_a(mouseX, mouseY, mouseButton);
      } catch (IOException var5) {
      }

      if (this.currentScreen != null) {
         this.currentScreen.click(this.field_146294_l, this.field_146295_m, mouseX, mouseY);
      }

   }

   public void func_146278_c(int par1) {
   }

   public void func_146276_q_() {
   }

   public void func_73863_a(int mouseX, int mouseY, float par3) {
      super.func_73863_a(mouseX, mouseY, par3);
      this.func_146276_q_();
      if (this.bm.mode != BattleMode.YesNoReplaceMove && !this.bm.hasMoreMessages()) {
         if (this.bm.hasLevelUps()) {
            if (this.bm.mode != BattleMode.LevelUp) {
               this.bm.oldMode = this.bm.mode;
               this.bm.mode = BattleMode.LevelUp;
            }
         } else if (this.bm.hasNewAttacks() && this.bm.mode != BattleMode.ReplaceAttack) {
            this.bm.oldMode = this.bm.mode;
            this.bm.mode = BattleMode.ReplaceAttack;
         }
      }

      this.selectScreen();
      if (this.first) {
         this.first = false;
         if (ClientProxy.camera != null && !this.bm.battleEnded) {
            this.bm.setCameraToPlayer();
         }
      }

      if (this.isBattleComplete() && this.bm.battleType != EnumBattleType.Raid) {
         this.restoreSettingsAndClose();
      } else {
         RenderHelper.func_74518_a();
         GlStateManager.func_179147_l();
         if (this.bm.mode != BattleMode.LevelUp && this.bm.mode != BattleMode.ReplaceAttack && this.bm.mode != BattleMode.YesNoReplaceMove && (this.bm.mode == BattleMode.Waiting || this.bm.mode == BattleMode.MainMenu || this.bm.mode == BattleMode.ChooseAttack || this.bm.mode == BattleMode.ChooseTargets || this.bm.mode == BattleMode.EnforcedSwitch)) {
            this.drawPokemonOverlays();
         }

         if (this.currentScreen != null) {
            this.currentScreen.drawScreen(this.field_146294_l, this.field_146295_m, mouseX, mouseY);
         }

         if (this.bm.isSpectating) {
            if (this.stopSpectateButton == null) {
               this.addStopSpectateButton();
            }

            this.stopSpectateButton.func_191745_a(this.field_146297_k, mouseX, mouseY, 1.0F);
         }

         if (this.bm.afkOn && this.bm.afkTime <= 0) {
            this.bm.afkActive = true;
            this.bm.afkSelectMove();
            this.bm.resetAFKTime();
         }

         if (this.showGlobalInfo && this.bm.battleControllerIndex > -1) {
            GuiHelper.drawCenteredSquashedString(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.battle.turn", new Object[]{this.bm.battleTurn + 1}), false, 75.0, this.field_146294_l - 40, this.field_146295_m - 69, 16777215, false);
            String timeString;
            if (this.currentScreen instanceof ChooseAttack) {
               timeString = this.bm.afkOn ? I18n.func_135052_a("gui.battle.turntimer", new Object[]{this.bm.afkTime, this.bm.afkTime == 1 ? "" : "s"}) : I18n.func_135052_a("gui.battle.noturntimer", new Object[0]);
            } else {
               timeString = "Waiting...";
            }

            GuiHelper.drawCenteredSquashedString(this.field_146297_k.field_71466_p, timeString, false, 75.0, this.field_146294_l - 40, this.field_146295_m - 53, 16777215, false);
            GuiHelper.drawCenteredSquashedString(this.field_146297_k.field_71466_p, this.bm.weather == null ? I18n.func_135052_a("gui.battle.noweather", new Object[0]) : this.bm.weather.getLocalizedName(), false, 75.0, this.field_146294_l - 40, this.field_146295_m - 37, 16777215, false);
            GuiHelper.drawCenteredSquashedString(this.field_146297_k.field_71466_p, this.bm.terrain == null ? I18n.func_135052_a("gui.battle.noterrain", new Object[0]) : this.bm.terrain.getLocalizedName(), false, 75.0, this.field_146294_l - 40, this.field_146295_m - 21, 16777215, false);
         }

         this.showGlobalInfo = true;
         GlStateManager.func_179101_C();
         RenderHelper.func_74518_a();
         GlStateManager.func_179140_f();
         GlStateManager.func_179097_i();
         if (Pixelmon.devEnvironment && Minecraft.func_71410_x().field_71474_y.field_74330_P) {
            int centerW = this.field_146294_l / 2;
            int centerH = this.field_146295_m / 2;
            GuiHelper.drawCenteredString("x: " + mouseX + ", y: " + mouseY, (float)mouseX, (float)(mouseY - 29), 1048575);
            GuiHelper.drawCenteredString("xcenter: " + (mouseX - centerW), (float)mouseX, (float)(mouseY - 19), 1048575);
            GuiHelper.drawCenteredString("ycenter: " + (mouseY - centerH) + ", " + (this.field_146295_m - mouseY), (float)mouseX, (float)(mouseY - 9), 1048575);
            GuiHelper.drawCenteredString("width: " + this.field_146294_l + ", height: " + this.field_146295_m, (float)centerW, 20.0F, 1048575);
            GuiHelper.drawCenteredString("mode: " + this.bm.mode.name(), (float)centerW, 40.0F, 1048575);
         }

      }
   }

   private boolean isBattleComplete() {
      if (!this.bm.hasMoreMessages() && !this.bm.hasMoreTasks() && !this.bm.hasLevelUps() && !this.bm.hasNewAttacks() && !this.bm.choosingPokemon && this.bm.battleEnded) {
         ++this.shutdownDelay;
         return this.shutdownDelay > 15;
      } else {
         this.shutdownDelay = 0;
         return false;
      }
   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.currentScreen != null) {
         this.currentScreen.func_73876_c();
      }

      ++this.tick;
      if (this.tick++ % 100 == 1) {
         Pixelmon.network.sendToServer(new GuiBattleUpdatePacket());
      }

   }

   public int getGuiWidth() {
      return this.guiWidth;
   }

   public int getGuiHeight() {
      return this.guiHeight;
   }

   public boolean canSelectTarget(Attack attack) {
      AttackCategory attackCategory = attack.getAttackCategory();
      boolean attackHasZMove = attack.getMove().z != null;
      boolean usingZMove = this.bm.showZMoves;
      int enemyCount = this.bm.displayedEnemyPokemon != null ? this.bm.displayedEnemyPokemon.length : 0;
      int ourCount = this.bm.displayedOurPokemon != null ? this.bm.displayedOurPokemon.length : 0;
      int allyCount = this.bm.displayedAllyPokemon != null ? this.bm.displayedAllyPokemon.length : 0;
      if (attackCategory == AttackCategory.STATUS || !attackHasZMove || !usingZMove || enemyCount <= 1 && ourCount <= 1 && allyCount <= 0) {
         if ((attackCategory != AttackCategory.STATUS && this.bm.dynamaxing || this.bm.dynamax != null) && (enemyCount > 1 || ourCount > 1 || allyCount > 0)) {
            return true;
         } else {
            TargetingInfo info = attack.getMove().getTargetingInfo();
            if (info.hitsOppositeFoe && info.hitsAdjacentFoe && !info.hitsAdjacentAlly && !attack.isAttack("Me First")) {
               return false;
            } else if (info.hitsSelf && info.hitsAdjacentAlly && !info.hitsAll && this.bm.teamPokemon.length > 1) {
               return true;
            } else if (!info.hitsSelf && !info.hitsAdjacentFoe && this.bm.teamPokemon.length == 1) {
               return false;
            } else if (attack.isAttack("Curse") && !this.bm.getCurrentPokemon().getBaseStats().getTypeList().contains(EnumType.Ghost)) {
               return false;
            } else {
               return !info.hitsAll && !info.hitsSelf && (this.bm.teamPokemon.length > 1 || this.bm.displayedEnemyPokemon.length > 1);
            }
         }
      } else {
         return true;
      }
   }

   public boolean setTargeting(PixelmonInGui pig, Attack attack, int opponentTarget, int userTarget) {
      if (this.bm.dynamaxing || pig.pokemonUUID.equals(this.bm.dynamax)) {
         if (pig.gmaxFactor) {
            attack = MaxMoveConverter.getGMaxMoveFromAttack(attack, (PixelmonWrapper)null, pig.species, pig.species.getFormEnum(pig.form));
         } else {
            attack = MaxMoveConverter.getMaxMoveFromAttack(attack, (PixelmonWrapper)null);
         }
      }

      for(int i = 0; i < this.bm.targetted.length; ++i) {
         for(int j = 0; j < this.bm.targetted[i].length; ++j) {
            this.bm.targetted[i][j] = false;
         }
      }

      TargetingInfo info = attack.getMove().getTargetingInfo();
      AttackCategory attackCategory = attack.getAttackCategory();
      boolean attackHasZMove = attack.getMove().z != null;
      boolean usingZMove = this.bm.showZMoves;
      if (attackCategory != AttackCategory.STATUS && attackHasZMove && usingZMove) {
         if (opponentTarget == -1 && userTarget == -1 && !attack.isAttack("Last Resort") && info.hitsOppositeFoe && this.bm.currentPokemon < this.bm.targetted[1].length) {
            this.bm.targetted[1][this.bm.currentPokemon] = true;
            return false;
         }

         if (attack.isAttack("Last Resort")) {
            if (opponentTarget == -1 && userTarget == -1) {
               this.bm.targetted[0][this.bm.currentPokemon] = true;
            } else if (userTarget != -1 && userTarget == this.bm.currentPokemon) {
               this.bm.targetted[0][this.bm.currentPokemon] = true;
            }
         }

         if (opponentTarget != -1) {
            if (opponentTarget == this.bm.currentPokemon) {
               this.bm.targetted[1][this.bm.currentPokemon] = true;
            }

            if (opponentTarget == this.bm.currentPokemon + 1) {
               this.bm.targetted[1][this.bm.currentPokemon + 1] = true;
            }

            if (opponentTarget == this.bm.currentPokemon - 1) {
               this.bm.targetted[1][this.bm.currentPokemon - 1] = true;
            }
         }

         if (attack.isAttack("Clanging Scales")) {
            if (this.bm.currentPokemon < this.bm.targetted[1].length) {
               this.bm.targetted[1][this.bm.currentPokemon] = true;
            }

            if (this.bm.currentPokemon - 1 >= 0 && this.bm.currentPokemon - 1 < this.bm.targetted[1].length) {
               this.bm.targetted[1][this.bm.currentPokemon - 1] = true;
            }

            if (this.bm.currentPokemon + 1 < this.bm.targetted[1].length) {
               this.bm.targetted[1][this.bm.currentPokemon + 1] = true;
            }
         } else if (userTarget != -1) {
            if (userTarget == this.bm.currentPokemon + 1) {
               this.bm.targetted[0][this.bm.currentPokemon + 1] = true;
            }

            if (userTarget == this.bm.currentPokemon - 1) {
               this.bm.targetted[0][this.bm.currentPokemon - 1] = true;
            }

            if (userTarget == this.bm.currentPokemon) {
               this.bm.targetted[0][this.bm.currentPokemon] = true;
            }
         }
      } else if (info.hitsAll) {
         if (info.hitsOppositeFoe && this.bm.currentPokemon < this.bm.targetted[1].length) {
            this.bm.targetted[1][this.bm.currentPokemon] = true;
         }

         if (info.hitsAdjacentFoe) {
            if (this.bm.currentPokemon - 1 >= 0 && this.bm.currentPokemon - 1 < this.bm.targetted[1].length) {
               this.bm.targetted[1][this.bm.currentPokemon - 1] = true;
            }

            if (this.bm.currentPokemon + 1 < this.bm.targetted[1].length) {
               this.bm.targetted[1][this.bm.currentPokemon + 1] = true;
            }
         }

         if (info.hitsExtendedFoe) {
            if (this.bm.currentPokemon - 2 >= 0 && this.bm.currentPokemon - 2 < this.bm.targetted[1].length) {
               this.bm.targetted[1][this.bm.currentPokemon - 2] = true;
            }

            if (this.bm.currentPokemon + 2 < this.bm.targetted[1].length) {
               this.bm.targetted[1][this.bm.currentPokemon + 2] = true;
            }
         }

         if (info.hitsSelf) {
            this.bm.targetted[0][this.bm.currentPokemon] = true;
         }

         if (info.hitsAdjacentAlly) {
            if (this.bm.currentPokemon - 1 >= 0) {
               this.bm.targetted[0][this.bm.currentPokemon - 1] = true;
            }

            if (this.bm.currentPokemon + 1 < this.bm.targetted[0].length) {
               this.bm.targetted[0][this.bm.currentPokemon + 1] = true;
            }
         }

         if (info.hitsExtendedAlly) {
            if (this.bm.currentPokemon - 2 >= 0) {
               this.bm.targetted[0][this.bm.currentPokemon - 2] = true;
            }

            if (this.bm.currentPokemon + 2 < this.bm.targetted[0].length) {
               this.bm.targetted[0][this.bm.currentPokemon + 2] = true;
            }
         }

         if (info.hitsSelf && info.hitsAdjacentAlly && info.hitsExtendedAlly) {
            if (this.bm.currentPokemon - 3 >= 0) {
               this.bm.targetted[0][this.bm.currentPokemon - 3] = true;
            }

            if (this.bm.currentPokemon + 3 < this.bm.targetted[0].length) {
               this.bm.targetted[0][this.bm.currentPokemon + 3] = true;
            }
         }
      } else {
         if (userTarget != -1) {
            if (userTarget == this.bm.currentPokemon && info.hitsSelf) {
               this.bm.targetted[0][this.bm.currentPokemon] = true;
            }

            if (info.hitsAdjacentAlly) {
               if (userTarget == this.bm.currentPokemon + 1) {
                  this.bm.targetted[0][this.bm.currentPokemon + 1] = true;
               }

               if (userTarget == this.bm.currentPokemon - 1) {
                  this.bm.targetted[0][this.bm.currentPokemon - 1] = true;
               }
            }

            if (info.hitsExtendedAlly) {
               if (userTarget == this.bm.currentPokemon + 2) {
                  this.bm.targetted[0][this.bm.currentPokemon + 2] = true;
               }

               if (userTarget == this.bm.currentPokemon - 2) {
                  this.bm.targetted[0][this.bm.currentPokemon - 2] = true;
               }
            }
         }

         if (opponentTarget != -1) {
            if (this.bm.rules.battleType == EnumBattleType.Horde || this.bm.rules.battleType == EnumBattleType.Raid) {
               for(int i = 0; i < this.bm.targetted[1].length; ++i) {
                  if (i == opponentTarget) {
                     this.bm.targetted[1][i] = true;
                  }
               }
            }

            if (info.hitsOppositeFoe && opponentTarget == this.bm.currentPokemon) {
               this.bm.targetted[1][this.bm.currentPokemon] = true;
            }

            if (info.hitsAdjacentFoe) {
               if (opponentTarget == this.bm.currentPokemon + 1) {
                  this.bm.targetted[1][this.bm.currentPokemon + 1] = true;
               }

               if (opponentTarget == this.bm.currentPokemon - 1) {
                  this.bm.targetted[1][this.bm.currentPokemon - 1] = true;
               }
            }

            if (info.hitsExtendedFoe) {
               if (opponentTarget == this.bm.currentPokemon + 2) {
                  this.bm.targetted[1][this.bm.currentPokemon + 2] = true;
               }

               if (opponentTarget == this.bm.currentPokemon - 2) {
                  this.bm.targetted[1][this.bm.currentPokemon - 2] = true;
               }
            }
         }

         if (userTarget == -1 && opponentTarget == -1) {
            boolean isCurseUser = attack.isAttack("Curse") && !this.bm.getCurrentPokemon().getBaseStats().getTypeList().contains(EnumType.Ghost);
            if (!info.hitsSelf && !isCurseUser) {
               if (info.hitsOppositeFoe && this.bm.currentPokemon < this.bm.targetted[1].length) {
                  this.bm.targetted[1][this.bm.currentPokemon] = true;
               }
            } else {
               this.bm.targetted[0][this.bm.currentPokemon] = true;
            }
         }
      }

      if (userTarget >= 0) {
         return this.bm.targetted[0].length > userTarget && this.bm.targetted[0][userTarget];
      } else if (opponentTarget < 0) {
         return false;
      } else {
         return this.bm.targetted[1].length > opponentTarget && this.bm.targetted[1][opponentTarget];
      }
   }

   public boolean isTargeted(UUID uuid) {
      int i;
      for(i = 0; i < this.bm.teamPokemon.length; ++i) {
         if (Objects.equals(this.bm.teamPokemon[i], uuid)) {
            return this.bm.targetted[0].length > i && this.bm.targetted[0][i];
         }
      }

      for(i = 0; i < this.bm.displayedEnemyPokemon.length; ++i) {
         if (Objects.equals(this.bm.displayedEnemyPokemon[i].pokemonUUID, uuid)) {
            return this.bm.targetted[1].length > i && this.bm.targetted[1][i];
         }
      }

      return false;
   }

   public boolean showTargeting() {
      return this.bm.teamPokemon.length != 1 || this.bm.displayedEnemyPokemon.length != 1;
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146127_k == 0) {
         this.bm.endSpectate();
         Pixelmon.network.sendToServer(new RemoveSpectator(this.bm.battleControllerIndex, this.field_146297_k.field_71439_g.func_110124_au()));
      }

   }

   public void func_175273_b(Minecraft mcIn, int w, int h) {
      super.func_175273_b(mcIn, w, h);
      Iterator var4 = this.screenList.iterator();

      while(var4.hasNext()) {
         GuiScreen screen = (GuiScreen)var4.next();
         screen.func_175273_b(mcIn, w, h);
      }

      this.pokemonOverlay.func_175273_b(this.field_146297_k, this.field_146294_l, this.field_146295_m);
   }
}
