package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.enums.EnumMovesetGroup;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalRecords;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiScreenPokeCheckerTMs extends GuiScreenPokeChecker {
   private int selectedNumber = 0;
   private int scrollPos = 0;
   private EnumMovesetGroup selectedMoveset;

   public GuiScreenPokeCheckerTMs(GuiScreenPokeChecker tab) {
      super(tab);
      this.selectedMoveset = EnumMovesetGroup.LevelUp;
   }

   public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summaryTMs);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_73729_b((this.field_146294_l - this.xSize) / 2 - 40, (this.field_146295_m - this.ySize) / 2 - 25, 0, 0, 256, 205);
      this.drawPokemonName();
      this.drawArrows(mouseX, mouseY);
   }

   private void drawMoveInfo(AttackBase attack) {
      if (attack != null) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.battle.power", new Object[0]) + ":", -30, 128, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.battle.accuracy", new Object[0]) + ":", -30, 138, 16777215);
         int bpExtra = 0;
         int acExtra = 0;
         if (attack.getBasePower() >= 100) {
            bpExtra = this.field_146297_k.field_71466_p.func_78263_a('0');
         }

         if (attack.getAccuracy() >= 100) {
            acExtra = this.field_146297_k.field_71466_p.func_78263_a('0');
         }

         if (attack.getBasePower() > 0) {
            this.func_73731_b(this.field_146297_k.field_71466_p, "" + attack.getBasePower(), 30 - bpExtra, 128, 16777215);
         } else {
            this.func_73731_b(this.field_146297_k.field_71466_p, "--", 30 - bpExtra, 128, 16777215);
         }

         if (attack.getAccuracy() <= 0) {
            this.func_73731_b(this.field_146297_k.field_71466_p, "--", 30 - acExtra, 138, 16777215);
         } else {
            this.func_73731_b(this.field_146297_k.field_71466_p, "" + attack.getAccuracy(), 30 - acExtra, 138, 16777215);
         }

         this.func_73731_b(this.field_146297_k.field_71466_p, attack.getAttackCategory().getLocalizedName(), -30, 148, 16777215);
         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("attack." + attack.getAttackName().replace(" ", "_").toLowerCase() + ".name", new Object[0]), 137, 98, 16777215);
         this.field_146297_k.field_71466_p.func_78279_b(I18n.func_135052_a("attack." + attack.getAttackName().replace(" ", "_").toLowerCase() + ".description", new Object[0]), 60, 113, 145, 16777215);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.types);
         float x = attack.getAttackType().textureX;
         float y = attack.getAttackType().textureY;
         GuiHelper.drawImageQuad(0.0, 108.0, 18.0, 18.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), this.field_73735_i);
      }
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      if (!this.pokemon.isEgg()) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + this.pokemon.getLevel(), 10, -14, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " " + this.pokemon.getSpecies().getNationalPokedexNumber(), -30, -14, 16777215);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summaryTMs);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.func_73729_b(70 + 12 * this.selectedMoveset.getId(), -20, 0, 240, 12, 16);
         Map movesetGroupList = ClientStorageManager.pokedex.getMoveset(this.pokemon.getSpecies(), (byte)this.pokemon.getForm(), this.pokemon.getCustomTexture());
         List displayMoves = (List)movesetGroupList.get(this.selectedMoveset);
         if (displayMoves != null) {
            if (displayMoves.size() > 6) {
               float height = 85.0F / (float)displayMoves.size();
               GuiHelper.drawImageQuad(205.0, -4.0, 5.0, 90.0F, 0.0, 0.91796875, 0.0, 0.9375, this.field_73735_i);
               GuiHelper.drawImageQuad(205.0, (double)(-4.0F + height * (float)this.scrollPos), 5.0, height * 6.0F, 0.0, 0.87890625, 0.0, 0.8984375, this.field_73735_i);
               GuiHelper.drawImageQuad(205.0, (double)(-4.0F + height * (float)this.scrollPos), 5.0, 5.0F, 0.0, 0.859375, 0.0, 0.87890625, this.field_73735_i);
               GuiHelper.drawImageQuad(205.0, (double)(-4.0F + height * (float)(this.scrollPos + 6)), 5.0, 5.0F, 0.0, 0.8984375, 0.0, 0.91796875, this.field_73735_i);
            }

            for(int i = 0; i < 6; ++i) {
               if (displayMoves.size() > i + this.scrollPos) {
                  boolean shadow = this.selectedNumber != i + this.scrollPos;
                  ITechnicalMove move;
                  if (displayMoves.get(i + this.scrollPos) instanceof Gen8TechnicalRecords) {
                     move = (ITechnicalMove)displayMoves.get(i + this.scrollPos);
                     this.drawString(this.field_146297_k.field_71466_p, move.getAttack().getLocalizedName(), 100, i * 15, 16777215, shadow);
                     this.drawString(this.field_146297_k.field_71466_p, "TR " + move.getId(), 62, 15 * i, 16777215, shadow);
                  } else if (displayMoves.get(i + this.scrollPos) instanceof ITechnicalMove) {
                     move = (ITechnicalMove)displayMoves.get(i + this.scrollPos);
                     this.drawString(this.field_146297_k.field_71466_p, move.getAttack().getLocalizedName(), 100, i * 15, 16777215, shadow);
                     this.drawString(this.field_146297_k.field_71466_p, "TM " + move.getId(), 62, 15 * i, 16777215, shadow);
                  } else if (displayMoves.get(i + this.scrollPos) instanceof AttackBase) {
                     AttackBase move = (AttackBase)displayMoves.get(i + this.scrollPos);
                     this.drawString(this.field_146297_k.field_71466_p, move.getLocalizedName(), 62, i * 15, 16777215, shadow);
                  } else if (displayMoves.get(i + this.scrollPos) instanceof Pair) {
                     Pair p = (Pair)displayMoves.get(i + this.scrollPos);
                     this.drawString(this.field_146297_k.field_71466_p, ((AttackBase)p.getRight()).getLocalizedName(), 100, i * 15, 16777215, shadow);
                     this.drawString(this.field_146297_k.field_71466_p, "Lv " + p.getLeft(), 62, 15 * i, 16777215, shadow);
                  }
               }
            }

            if (displayMoves.size() > this.selectedNumber) {
               if (displayMoves.get(this.selectedNumber) instanceof ITechnicalMove) {
                  this.drawMoveInfo(((ITechnicalMove)displayMoves.get(this.selectedNumber)).getAttack());
               } else if (displayMoves.get(this.selectedNumber) instanceof AttackBase) {
                  this.drawMoveInfo((AttackBase)displayMoves.get(this.selectedNumber));
               } else if (displayMoves.get(this.selectedNumber) instanceof Pair) {
                  this.drawMoveInfo((AttackBase)((Pair)displayMoves.get(this.selectedNumber)).getRight());
               }
            }
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GuiHelper.drawScaledImage(GuiResources.rareCandy, 0.705, 58.5, -17.25, 16.0, 16.0, this.field_73735_i);
         GuiHelper.drawScaledImage(GuiResources.egg, 1.5, 64.0, -27.5, 16.0, 16.0, this.field_73735_i);
         this.func_73731_b(this.field_146297_k.field_71466_p, "1", 85, -16, ((List)movesetGroupList.get(EnumMovesetGroup.Kanto)).isEmpty() ? 10000536 : 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, "2", 97, -16, ((List)movesetGroupList.get(EnumMovesetGroup.Johto)).isEmpty() ? 10000536 : 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, "3", 109, -16, ((List)movesetGroupList.get(EnumMovesetGroup.Hoenn)).isEmpty() ? 10000536 : 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, "4", 121, -16, ((List)movesetGroupList.get(EnumMovesetGroup.Sinnoh)).isEmpty() ? 10000536 : 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, "5", 133, -16, ((List)movesetGroupList.get(EnumMovesetGroup.Unova)).isEmpty() ? 10000536 : 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, "6", 145, -16, ((List)movesetGroupList.get(EnumMovesetGroup.Kalos)).isEmpty() ? 10000536 : 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, "7", 157, -16, ((List)movesetGroupList.get(EnumMovesetGroup.Alola)).isEmpty() ? 10000536 : 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, "8", 169, -16, ((List)movesetGroupList.get(EnumMovesetGroup.Galar)).isEmpty() ? 10000536 : 16777215);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      } else {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " ???", 10, -14, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " ???", -30, -14, 16777215);
      }

      this.drawBasePokemonInfo();
      GlStateManager.func_179084_k();
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      int i = Mouse.getEventDWheel();
      List displayMoves = (List)ClientStorageManager.pokedex.getMoveset(this.pokemon.getSpecies(), (byte)this.pokemon.getForm(), this.pokemon.getCustomTexture()).get(this.selectedMoveset);
      if (displayMoves != null && displayMoves.size() > 7) {
         if (i > 0) {
            if (this.scrollPos - 1 != -1) {
               --this.scrollPos;
            }
         } else if (i < 0 && this.scrollPos + 1 != displayMoves.size() - 5) {
            ++this.scrollPos;
         }
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      int x = this.field_146294_l / 2 - mouseX;
      int y = this.field_146295_m / 2 - mouseY;
      Map check = ClientStorageManager.pokedex.getMoveset(this.pokemon.getSpecies(), (byte)this.pokemon.getForm(), this.pokemon.getCustomTexture());
      if (x < 30 && x > -121 && y < 102) {
         if (y > 88) {
            if (x > 18) {
               if (!((List)check.get(EnumMovesetGroup.LevelUp)).isEmpty()) {
                  this.selectedMoveset = EnumMovesetGroup.LevelUp;
                  this.scrollPos = 0;
                  this.selectedNumber = 0;
               }
            } else if (x > 6) {
               if (!((List)check.get(EnumMovesetGroup.Egg)).isEmpty()) {
                  this.selectedMoveset = EnumMovesetGroup.Egg;
                  this.scrollPos = 0;
                  this.selectedNumber = 0;
               }
            } else if (x > -6) {
               if (!((List)check.get(EnumMovesetGroup.Kanto)).isEmpty()) {
                  this.selectedMoveset = EnumMovesetGroup.Kanto;
                  this.scrollPos = 0;
                  this.selectedNumber = 0;
               }
            } else if (x > -18) {
               if (!((List)check.get(EnumMovesetGroup.Johto)).isEmpty()) {
                  this.selectedMoveset = EnumMovesetGroup.Johto;
                  this.scrollPos = 0;
                  this.selectedNumber = 0;
               }
            } else if (x > -30) {
               if (!((List)check.get(EnumMovesetGroup.Hoenn)).isEmpty()) {
                  this.selectedMoveset = EnumMovesetGroup.Hoenn;
                  this.scrollPos = 0;
                  this.selectedNumber = 0;
               }
            } else if (x > -42) {
               if (!((List)check.get(EnumMovesetGroup.Sinnoh)).isEmpty()) {
                  this.selectedMoveset = EnumMovesetGroup.Sinnoh;
                  this.scrollPos = 0;
                  this.selectedNumber = 0;
               }
            } else if (x > -54) {
               if (!((List)check.get(EnumMovesetGroup.Unova)).isEmpty()) {
                  this.selectedMoveset = EnumMovesetGroup.Unova;
                  this.scrollPos = 0;
                  this.selectedNumber = 0;
               }
            } else if (x > -66) {
               if (!((List)check.get(EnumMovesetGroup.Kalos)).isEmpty()) {
                  this.selectedMoveset = EnumMovesetGroup.Kalos;
                  this.scrollPos = 0;
                  this.selectedNumber = 0;
               }
            } else if (x > -78) {
               if (!((List)check.get(EnumMovesetGroup.Alola)).isEmpty()) {
                  this.selectedMoveset = EnumMovesetGroup.Alola;
                  this.scrollPos = 0;
                  this.selectedNumber = 0;
               }
            } else if (x > -90 && !((List)check.get(EnumMovesetGroup.Galar)).isEmpty()) {
               this.selectedMoveset = EnumMovesetGroup.Galar;
               this.scrollPos = 0;
               this.selectedNumber = 0;
            }
         } else if (y > 73) {
            this.selectedNumber = this.scrollPos;
         } else if (y > 58) {
            this.selectedNumber = this.scrollPos + 1;
         } else if (y > 43) {
            this.selectedNumber = this.scrollPos + 2;
         } else if (y > 28) {
            this.selectedNumber = this.scrollPos + 3;
         } else if (y > 13) {
            this.selectedNumber = this.scrollPos + 4;
         } else if (y > -2) {
            this.selectedNumber = this.scrollPos + 5;
         }
      }

   }

   public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color, boolean shadow) {
      fontRendererIn.func_175065_a(text, (float)x, (float)y, color, shadow);
   }
}
