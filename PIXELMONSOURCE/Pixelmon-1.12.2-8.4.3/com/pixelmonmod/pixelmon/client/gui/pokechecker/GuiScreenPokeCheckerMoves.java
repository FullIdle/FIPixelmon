package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SwapMove;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

public class GuiScreenPokeCheckerMoves extends GuiScreenPokeChecker {
   private int selectedNumber = -1;
   private boolean move1 = true;
   private boolean move2 = false;
   private boolean move3 = false;
   private boolean move4 = false;

   GuiScreenPokeCheckerMoves(GuiScreenPokeChecker tab) {
      super(tab);
   }

   public GuiScreenPokeCheckerMoves(PokemonStorage storage, StoragePosition position, @Nullable GuiScreen parent) {
      super(storage, position, parent);
   }

   public void resetAll() {
      this.move1 = false;
      this.move2 = false;
      this.move3 = false;
      this.move4 = false;
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      if (!this.pokemon.isEgg()) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + this.pokemon.getLevel(), 10, -14, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " " + this.pokemon.getSpecies().getNationalPokedexNumber(), -30, -14, 16777215);
         Moveset moveset = this.pokemon.getMoveset();

         for(int i2 = 0; i2 < moveset.size(); ++i2) {
            if (this.pokemon.getMoveset().get(i2) != null) {
               this.func_73732_a(this.field_146297_k.field_71466_p, moveset.get(i2).getActualMove().getLocalizedName(), 130, -7 + i2 * 22, 16777215);
               this.func_73732_a(this.field_146297_k.field_71466_p, this.pokemon.getMoveset().get(i2).pp + "/" + this.pokemon.getMoveset().get(i2).getMaxPP(), 193, -5 + i2 * 22, 16777215);
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.types);
               float x = moveset.get(i2).getActualMove().getAttackType().textureX;
               float y = moveset.get(i2).getActualMove().getAttackType().textureY;
               GuiHelper.drawImageQuad(62.0, (double)(22 * i2 - 13), 18.0, 18.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), this.field_73735_i);
            }
         }
      } else {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " ???", 10, -14, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " ???", -30, -14, 16777215);
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.effects", new Object[0]), -10, 111, 16777215);
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.battle.description", new Object[0]), 107, 98, 16777215);
      this.drawSelection(mouseX, mouseY);
      this.drawSelectedRectBin(mouseX, mouseY);
      this.drawMoveDescription();
      this.drawBasePokemonInfo();
      GlStateManager.func_179084_k();
   }

   public void drawMoveDescription() {
      if (!this.pokemon.isEgg()) {
         if (this.move1 && this.pokemon.getMoveset().size() > 0) {
            this.drawMoveInfo(this.pokemon.getMoveset().get(0));
         }

         if (this.move2 && this.pokemon.getMoveset().size() > 1) {
            this.drawMoveInfo(this.pokemon.getMoveset().get(1));
         }

         if (this.move3 && this.pokemon.getMoveset().size() > 2) {
            this.drawMoveInfo(this.pokemon.getMoveset().get(2));
         }

         if (this.move4 && this.pokemon.getMoveset().size() > 3) {
            this.drawMoveInfo(this.pokemon.getMoveset().get(3));
         }
      }

   }

   public void switchMoves(int moveToChange2) {
      Pixelmon.network.sendToServer(new SwapMove(this.position, this.pokemon.getUUID(), this.selectedNumber, moveToChange2));
      this.selectedNumber = -1;
   }

   private void drawMoveInfo(Attack attack) {
      if (attack != null) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.battle.power", new Object[0]) + ":", -30, 128, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.battle.accuracy", new Object[0]) + ":", -30, 138, 16777215);
         int bpExtra = 0;
         int acExtra = 0;
         if (attack.getActualMove().getBasePower() >= 100) {
            bpExtra = this.field_146297_k.field_71466_p.func_78263_a('0');
         }

         if (attack.getActualMove().getAccuracy() >= 100) {
            acExtra = this.field_146297_k.field_71466_p.func_78263_a('0');
         }

         if (attack.getActualMove().getBasePower() > 0) {
            this.func_73731_b(this.field_146297_k.field_71466_p, "" + attack.getActualMove().getBasePower(), 30 - bpExtra, 128, 16777215);
         } else {
            this.func_73731_b(this.field_146297_k.field_71466_p, "--", 30 - bpExtra, 128, 16777215);
         }

         if (attack.getActualMove().getAccuracy() <= 0) {
            this.func_73731_b(this.field_146297_k.field_71466_p, "--", 30 - acExtra, 138, 16777215);
         } else {
            this.func_73731_b(this.field_146297_k.field_71466_p, "" + attack.getActualMove().getAccuracy(), 30 - acExtra, 138, 16777215);
         }

         this.func_73731_b(this.field_146297_k.field_71466_p, attack.getActualMove().getAttackCategory().getLocalizedName(), -30, 148, 16777215);
         this.field_146297_k.field_71466_p.func_78279_b(I18n.func_135052_a("attack." + attack.getActualMove().getAttackName().replace(" ", "_").toLowerCase() + ".description", new Object[0]), 60, 113, 145, 16777215);
      }
   }

   public void drawSelection(int i, int i1) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summaryMoves);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.pokemon.getMoveset().size() > 0 && i > this.field_146294_l / 2 - 31 && i < this.field_146294_l / 2 + 123 && i1 > this.field_146295_m / 2 - 100 && i1 < this.field_146295_m / 2 - 76 || this.move1) {
         this.func_73729_b(58, -17, 1, 231, 153, 24);
         this.resetAll();
         this.move1 = true;
      }

      if (this.pokemon.getMoveset().size() > 1 && i > this.field_146294_l / 2 - 31 && i < this.field_146294_l / 2 + 123 && i1 > this.field_146295_m / 2 - 77 && i1 < this.field_146295_m / 2 - 53 || this.move2) {
         this.func_73729_b(58, 6, 1, 231, 153, 24);
         this.resetAll();
         this.move2 = true;
      }

      if (this.pokemon.getMoveset().size() > 2 && i > this.field_146294_l / 2 - 31 && i < this.field_146294_l / 2 + 123 && i1 > this.field_146295_m / 2 - 54 && i1 < this.field_146295_m / 2 - 31 || this.move3) {
         this.func_73729_b(58, 28, 1, 231, 153, 24);
         this.resetAll();
         this.move3 = true;
      }

      if (this.pokemon.getMoveset().size() > 3 && i > this.field_146294_l / 2 - 31 && i < this.field_146294_l / 2 + 123 && i1 > this.field_146295_m / 2 - 32 && i1 < this.field_146295_m / 2 - 9 || this.move4) {
         this.func_73729_b(58, 50, 1, 231, 153, 24);
         this.resetAll();
         this.move4 = true;
      }

      this.drawSelectedRect();
   }

   protected void drawSelectedRect() {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summaryMoves);
      GlStateManager.func_179124_c(0.0F, 1.0F, 0.0F);
      if (this.selectedNumber == 0) {
         this.func_73729_b(58, -17, 1, 231, 153, 24);
      } else if (this.selectedNumber == 1) {
         this.func_73729_b(58, 6, 1, 231, 153, 24);
      } else if (this.selectedNumber == 2) {
         this.func_73729_b(58, 28, 1, 231, 153, 24);
      } else if (this.selectedNumber == 3) {
         this.func_73729_b(58, 50, 1, 231, 153, 24);
      }

      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
   }

   protected void drawSelectedRectBin(int i, int i1) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summaryMoves);
      GlStateManager.func_179124_c(1.0F, 0.0F, 0.0F);
      if (this.selectedNumber >= this.pokemon.getMoveset().size() && i > this.field_146294_l / 2 + 130 && i < this.field_146294_l / 2 + 158 && i1 > this.field_146295_m / 2 - 25 && i1 < this.field_146295_m / 2 + 9) {
         this.func_73729_b(220, 60, 230, 225, 26, 31);
      }

      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
   }

   protected int moveClicked(int i, int i1) {
      if (this.pokemon.isEgg()) {
         return -1;
      } else if (this.pokemon.getMoveset().size() > 0 && i > this.field_146294_l / 2 - 31 && i < this.field_146294_l / 2 + 123 && i1 > this.field_146295_m / 2 - 100 && i1 < this.field_146295_m / 2 - 76) {
         return 0;
      } else if (this.pokemon.getMoveset().size() > 1 && i > this.field_146294_l / 2 - 31 && i < this.field_146294_l / 2 + 123 && i1 > this.field_146295_m / 2 - 77 && i1 < this.field_146295_m / 2 - 53) {
         return 1;
      } else if (this.pokemon.getMoveset().size() > 2 && i > this.field_146294_l / 2 - 31 && i < this.field_146294_l / 2 + 123 && i1 > this.field_146295_m / 2 - 54 && i1 < this.field_146295_m / 2 - 31) {
         return 2;
      } else {
         return this.pokemon.getMoveset().size() > 3 && i > this.field_146294_l / 2 - 31 && i < this.field_146294_l / 2 + 123 && i1 > this.field_146295_m / 2 - 32 && i1 < this.field_146295_m / 2 - 9 ? 3 : -1;
      }
   }

   protected void attackClicked(int i, int i1) {
   }

   protected void selectMove(int i, int i1) {
      if (i > this.field_146294_l / 2 - 31 && i < this.field_146294_l / 2 + 123 && i1 > this.field_146295_m / 2 - 100 && i1 < this.field_146295_m / 2 - 9 && this.selectedNumber != this.moveClicked(i, i1)) {
         if (this.selectedNumber == -1) {
            this.selectedNumber = this.moveClicked(i, i1);
         } else if (this.selectedNumber != -1 && this.moveClicked(i, i1) != -1) {
            this.switchMoves(this.moveClicked(i, i1));
         }
      } else {
         this.selectedNumber = -1;
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      if (this.canDeleteMove() && this.selectedNumber >= 0 && mouseX > this.field_146294_l / 2 + 130 && mouseX < this.field_146294_l / 2 + 158 && mouseY > this.field_146295_m / 2 - 25 && mouseY < this.field_146295_m / 2 + 9) {
         this.field_146297_k.func_147108_a(new GuiScreenPokeCheckerWarningDeleteMove(this, this.pokemon, this.selectedNumber));
      }

      if (!this.pokemon.isEgg()) {
         this.attackClicked(mouseX, mouseY);
         this.selectMove(mouseX, mouseY);
      }

   }

   private boolean canDeleteMove() {
      return this.pokemon.getMoveset().size() > 1;
   }

   public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summaryMoves);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_73729_b((this.field_146294_l - this.xSize) / 2 - 40, (this.field_146295_m - this.ySize) / 2 - 25, 0, 0, 256, 205);
      if (this.selectedNumber >= 0 && this.canDeleteMove()) {
         this.func_73729_b((this.field_146294_l - this.xSize) / 2 + 220, (this.field_146295_m - this.ySize) / 2 + 60, 203, 225, 26, 31);
      }

      this.drawPokemonName();
      this.drawArrows(mouseX, mouseY);
   }
}
