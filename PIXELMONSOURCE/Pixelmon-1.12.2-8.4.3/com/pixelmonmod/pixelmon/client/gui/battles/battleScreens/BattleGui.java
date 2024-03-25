package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class BattleGui extends BattleScreen {
   public static final ResourceLocation BACKGROUND = new ResourceLocation("pixelmon", "textures/gui/battle/background.png");
   public static final ResourceLocation DETAILS = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_details.png");
   public static final ResourceLocation MENU_BAG = new ResourceLocation("pixelmon", "textures/gui/battle/menu_bag.png");
   public static final ResourceLocation MENU_BAG_DISABLED = new ResourceLocation("pixelmon", "textures/gui/battle/menu_bag_disabled.png");
   public static final ResourceLocation MENU_BAG_ICON = new ResourceLocation("pixelmon", "textures/gui/battle/menu_bag_icon.png");
   public static final ResourceLocation MENU_POKE = new ResourceLocation("pixelmon", "textures/gui/battle/menu_poke.png");
   public static final ResourceLocation MENU_POKE_DISABLED = new ResourceLocation("pixelmon", "textures/gui/battle/menu_poke_disabled.png");
   public static final ResourceLocation MENU_POKE_ICON = new ResourceLocation("pixelmon", "textures/gui/battle/menu_poke_icon.png");
   public static final ResourceLocation MENU_RUN = new ResourceLocation("pixelmon", "textures/gui/battle/menu_run.png");
   public static final ResourceLocation MENU_RUN_DISABLED = new ResourceLocation("pixelmon", "textures/gui/battle/menu_run_disabled.png");
   public static final ResourceLocation MENU_RUN_ICON = new ResourceLocation("pixelmon", "textures/gui/battle/menu_run_icon.png");
   public List buttons = Lists.newArrayList();
   int chatArea;
   int detailsStart;

   public BattleGui(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public void func_73866_w_() {
      this.buttons.clear();
      this.buttons.add(new ActionButton(BattleGui.EnumBattleButton.BAG, this));
      this.buttons.add(new ActionButton(BattleGui.EnumBattleButton.POKE, this));
      this.buttons.add(new ActionButton(BattleGui.EnumBattleButton.RUN, this));
      if (ClientProxy.battleManager.rules.hasClause("bag")) {
         ((GuiButton)this.buttons.get(0)).field_146124_l = false;
      }

      if (!this.bm.canSwitch) {
         ((GuiButton)this.buttons.get(1)).field_146124_l = false;
      }

      if (!this.bm.canFlee || ClientProxy.battleManager.rules.hasClause("forfeit")) {
         ((GuiButton)this.buttons.get(2)).field_146124_l = false;
      }

   }

   public void drawBackground(int width, int height, int mouseX, int mouseY) {
      this.chatArea = width - 40 - 80;
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(BACKGROUND);
      GuiHelper.drawImage(0.0, (double)((float)height - 83.1F), (double)width, 83.0999984741211, this.field_73735_i);
      this.detailsStart = this.field_146294_l - 80;
      this.field_146297_k.field_71446_o.func_110577_a(DETAILS);
      GuiHelper.drawImage((double)this.detailsStart, (double)(height - 80), 80.0, 80.0, this.field_73735_i);
   }

   public void drawButtons(int mouseX, int mouseY) {
      ((GuiButton)this.buttons.get(1)).field_146124_l = this.bm.canSwitch;
      ((GuiButton)this.buttons.get(2)).field_146124_l = this.bm.canFlee && !ClientProxy.battleManager.rules.hasClause("forfeit");
      Iterator var3 = this.buttons.iterator();

      while(var3.hasNext()) {
         GuiButton button = (GuiButton)var3.next();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         button.func_191745_a(this.field_146297_k, mouseX, mouseY, 0.0F);
      }

   }

   public boolean handleButtonClick(int mouseX, int mouseY) {
      Iterator var3 = this.buttons.iterator();

      GuiButton button;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         button = (GuiButton)var3.next();
      } while(!(button instanceof ActionButton) || !button.func_146116_c(this.field_146297_k, mouseX, mouseY));

      this.bm.dynamaxing = false;
      this.bm.megaEvolving = false;
      this.bm.showZMoves = false;
      ActionButton action = (ActionButton)button;
      if (action.button == BattleGui.EnumBattleButton.RUN) {
         this.bm.selectRunAction(this.bm.getCurrentPokemon().pokemonUUID);
      } else {
         ClientProxy.battleManager.mode = action.button.getBattleMode();
      }

      return true;
   }

   public static enum EnumBattleButton {
      BAG,
      POKE,
      RUN;

      @Nonnull
      public BattleMode getBattleMode() {
         switch (this) {
            case BAG:
               return BattleMode.ChooseBag;
            case POKE:
               return BattleMode.ChoosePokemon;
            case RUN:
               return BattleMode.YesNoForfeit;
            default:
               return null;
         }
      }

      @Nonnull
      public ResourceLocation getTexture(boolean disabled) {
         switch (this) {
            case BAG:
               return disabled ? BattleGui.MENU_BAG_DISABLED : BattleGui.MENU_BAG;
            case POKE:
               return disabled ? BattleGui.MENU_POKE_DISABLED : BattleGui.MENU_POKE;
            case RUN:
               return disabled ? BattleGui.MENU_RUN_DISABLED : BattleGui.MENU_RUN;
            default:
               return null;
         }
      }

      @Nonnull
      public void drawButton(GuiScreen screen) {
         switch (this) {
            case BAG:
               GuiHelper.drawImage(-1.0, (double)this.getY(), 40.0, 24.0, 0.0F);
               break;
            case POKE:
               GuiHelper.drawImage(0.0, (double)this.getY(), 41.0, 26.0, 0.0F);
               break;
            case RUN:
               GuiHelper.drawImage(0.0, (double)this.getY(), 40.0, 24.0, 0.0F);
         }

      }

      public int getY() {
         GuiScreen screen = Minecraft.func_71410_x().field_71462_r;
         if (screen == null) {
            return 0;
         } else {
            switch (this) {
               case BAG:
                  return screen.field_146295_m - 72 - 8;
               case POKE:
                  return screen.field_146295_m - 48 - 5;
               case RUN:
                  return screen.field_146295_m - 24;
               default:
                  return 0;
            }
         }
      }

      @Nonnull
      public ResourceLocation getIcon() {
         switch (this) {
            case BAG:
               return BattleGui.MENU_BAG_ICON;
            case POKE:
               return BattleGui.MENU_POKE_ICON;
            case RUN:
               return BattleGui.MENU_RUN_ICON;
            default:
               return null;
         }
      }

      @Nonnull
      public void drawIcon(GuiScreen screen) {
         if (screen != null) {
            switch (this) {
               case BAG:
                  GuiHelper.drawImage(8.0, (double)(screen.field_146295_m - 72 - 3), 16.0, 14.0, 0.0F);
                  break;
               case POKE:
                  GuiHelper.drawImage(8.0, (double)(screen.field_146295_m - 48), 16.0, 16.0, 0.0F);
                  break;
               case RUN:
                  GuiHelper.drawImage(8.0, (double)(screen.field_146295_m - 24 + 5), 20.0, 14.0, 0.0F);
            }

         }
      }
   }

   public static class ActionButton extends GuiButton {
      public final EnumBattleButton button;
      public final BattleGui parent;
      private int hoveredFrames = 0;
      private boolean isPressed;

      public ActionButton(EnumBattleButton button, BattleGui parent) {
         super(button.ordinal(), 0, 0, button == BattleGui.EnumBattleButton.POKE ? 41 : 40, button == BattleGui.EnumBattleButton.POKE ? 26 : 24, "");
         this.button = button;
         this.field_146129_i = button.getY();
         this.parent = parent;
      }

      public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
         this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
         if (this.field_146123_n) {
            this.hoveredFrames = Math.min(3, this.hoveredFrames + 1);
         } else {
            this.hoveredFrames = Math.max(0, this.hoveredFrames - 1);
         }

         mc.field_71446_o.func_110577_a(this.button.getTexture(!this.field_146124_l));
         GlStateManager.func_179094_E();
         if ((this.parent.mode == BattleMode.MainMenu || this.parent.mode == BattleMode.ChooseAttack) && this.hoveredFrames > 0 && this.field_146124_l && this.field_146125_m) {
            GlStateManager.func_179109_b((float)(-this.hoveredFrames), 0.0F, 0.0F);
         }

         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
         GL11.glTexParameteri(3553, 10242, 33071);
         GL11.glTexParameteri(3553, 10243, 33071);
         this.button.drawButton(mc.field_71462_r);
         GlStateManager.func_179121_F();
         mc.field_71446_o.func_110577_a(this.button.getIcon());
         this.button.drawIcon(mc.field_71462_r);
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      }

      public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
         return this.isPressed = super.func_146116_c(mc, mouseX, mouseY);
      }

      public void func_146118_a(int mouseX, int mouseY) {
         this.isPressed = false;
      }
   }
}
