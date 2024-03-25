package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleGui;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.LevelUp;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import com.pixelmonmod.pixelmon.util.helpers.CursorHelper;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class LevelUpScreen extends BattleGui {
   private EnumLevelStage drawLevelStage;

   public LevelUpScreen(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
      this.drawLevelStage = LevelUpScreen.EnumLevelStage.First;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.drawLevelStage = LevelUpScreen.EnumLevelStage.First;
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      CursorHelper.setCursor(CursorHelper.DEFAULT_CURSOR);
      this.drawBackground(width, height, mouseX, mouseY);
      this.drawButtons(mouseX, mouseY);
      Minecraft mc = Minecraft.func_71410_x();
      if (ClientProxy.camera != null) {
         this.parent.bm.setCameraToPlayer();
      }

      mc.field_71446_o.func_110577_a(GuiResources.levelUpPopup);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(width / 2 - 52), (double)(height / 2 - 66), 104.0, 113.0F, 0.0, 0.0, 0.40625, 0.44140625, this.field_73735_i);
      if (!this.bm.levelUpList.isEmpty()) {
         this.func_73731_b(mc.field_71466_p, I18n.func_135052_a("nbt.hp2.name", new Object[0]), width / 2 - 43, height / 2 - 54, 16777215);
         this.func_73731_b(mc.field_71466_p, I18n.func_135052_a("nbt.attack2.name", new Object[0]), width / 2 - 43, height / 2 - 38, 16777215);
         this.func_73731_b(mc.field_71466_p, I18n.func_135052_a("nbt.defense2.name", new Object[0]), width / 2 - 43, height / 2 - 22, 16777215);
         this.func_73731_b(mc.field_71466_p, I18n.func_135052_a("nbt.spattack2.name", new Object[0]), width / 2 - 43, height / 2 - 6, 16777215);
         this.func_73731_b(mc.field_71466_p, I18n.func_135052_a("nbt.spdefense2.name", new Object[0]), width / 2 - 43, height / 2 + 10, 16777215);
         this.func_73731_b(mc.field_71466_p, I18n.func_135052_a("nbt.speed2.name", new Object[0]), width / 2 - 43, height / 2 + 26, 16777215);
         LevelUp levelUp = (LevelUp)this.bm.levelUpList.get(0);
         if (this.drawLevelStage == LevelUpScreen.EnumLevelStage.First) {
            this.func_73731_b(mc.field_71466_p, "+" + (levelUp.statsLevel2.HP - levelUp.statsLevel1.HP), width / 2 + 25, height / 2 - 54, 16777215);
            this.func_73731_b(mc.field_71466_p, "+" + (levelUp.statsLevel2.Attack - levelUp.statsLevel1.Attack), width / 2 + 25, height / 2 - 38, 16777215);
            this.func_73731_b(mc.field_71466_p, "+" + (levelUp.statsLevel2.Defence - levelUp.statsLevel1.Defence), width / 2 + 25, height / 2 - 22, 16777215);
            this.func_73731_b(mc.field_71466_p, "+" + (levelUp.statsLevel2.SpecialAttack - levelUp.statsLevel1.SpecialAttack), width / 2 + 25, height / 2 - 6, 16777215);
            this.func_73731_b(mc.field_71466_p, "+" + (levelUp.statsLevel2.SpecialDefence - levelUp.statsLevel1.SpecialDefence), width / 2 + 25, height / 2 + 10, 16777215);
            this.func_73731_b(mc.field_71466_p, "+" + (levelUp.statsLevel2.Speed - levelUp.statsLevel1.Speed), width / 2 + 25, height / 2 + 26, 16777215);
         } else if (this.drawLevelStage == LevelUpScreen.EnumLevelStage.Second) {
            PixelmonStatsData stats = levelUp.statsLevel2;
            this.func_73731_b(mc.field_71466_p, "" + stats.HP, stats.HP < 100 ? width / 2 + 28 : width / 2 + 22, height / 2 - 54, 16777215);
            this.func_73731_b(mc.field_71466_p, "" + stats.Attack, stats.Attack < 100 ? width / 2 + 28 : width / 2 + 22, height / 2 - 38, 16777215);
            this.func_73731_b(mc.field_71466_p, "" + stats.Defence, stats.Defence < 100 ? width / 2 + 28 : width / 2 + 22, height / 2 - 22, 16777215);
            this.func_73731_b(mc.field_71466_p, "" + stats.SpecialAttack, stats.SpecialAttack < 100 ? width / 2 + 28 : width / 2 + 22, height / 2 - 6, 16777215);
            this.func_73731_b(mc.field_71466_p, "" + stats.SpecialDefence, stats.SpecialDefence < 100 ? width / 2 + 28 : width / 2 + 22, height / 2 + 10, 16777215);
            this.func_73731_b(mc.field_71466_p, "" + stats.Speed, stats.Speed < 100 ? width / 2 + 28 : width / 2 + 22, height / 2 + 26, 16777215);
         }

         UUID uuid = levelUp.pokemonUUID;
         if (this.bm != null) {
            PixelmonInGui pig = this.bm.getPokemon(uuid);
            if (pig != null) {
               String name = pig.getDisplayName();
               this.parent.battleLog.setActiveMessage(I18n.func_135052_a("gui.levelupscreen.lvlup", new Object[]{name, levelUp.level}));

               try {
                  this.parent.battleLog.drawElement(40, height - 80, Math.min(this.field_146294_l - 80, 260), 80, true);
               } catch (Exception var11) {
                  var11.printStackTrace();
               }
            } else {
               this.parent.restoreSettingsAndClose();
            }
         } else {
            this.parent.restoreSettingsAndClose();
         }
      }

   }

   public void func_73876_c() {
      this.parent.battleLog.func_73660_a();
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      if (!this.parent.battleLog.processUpDown(mouseX, mouseY)) {
         if (this.parent.battleLog.withinBounds(mouseX, mouseY) && this.parent.bm.hasMoreMessages()) {
            this.parent.battleLog.acknowledge();
         } else if (this.drawLevelStage == LevelUpScreen.EnumLevelStage.First) {
            this.drawLevelStage = LevelUpScreen.EnumLevelStage.Second;
         } else {
            try {
               LevelUp levelUp = (LevelUp)this.bm.levelUpList.remove(0);
               String name = this.bm.getPokemon(levelUp.pokemonUUID).getDisplayName();
               this.parent.battleLog.forceAddMessage(I18n.func_135052_a("gui.levelupscreen.lvlup", new Object[]{name, levelUp.level}));
            } catch (Exception var7) {
               this.bm.mode = this.bm.oldMode;
               this.bm.checkClearedMessages();
               return;
            }

            if (this.bm.hasLevelUps()) {
               this.drawLevelStage = LevelUpScreen.EnumLevelStage.First;
            } else if (this.bm.battleControllerIndex == -1 && !this.bm.hasNewAttacks()) {
               this.parent.restoreSettingsAndClose();
            } else {
               this.bm.mode = this.bm.oldMode;
               this.bm.checkClearedMessages();
            }
         }

      }
   }

   public boolean isScreen() {
      return super.isScreen() && this.bm.hasLevelUps();
   }

   public static enum EnumLevelStage {
      First,
      Second,
      Third;
   }
}
