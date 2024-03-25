package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

public class GuiScreenPokeCheckerStats extends GuiScreenPokeChecker {
   private static final int HEX_WHITE = 16777215;
   private static final int HEX_DECREASE = 16724016;
   private static final int HEX_INCREASE = 65280;

   GuiScreenPokeCheckerStats(GuiScreenPokeChecker tab) {
      super(tab);
   }

   public GuiScreenPokeCheckerStats(PokemonStorage storage, StoragePosition position, @Nullable GuiScreen parent) {
      super(storage, position, parent);
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      int hexColor = 16777215;
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      if (!this.pokemon.isEgg()) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + this.pokemon.getLevel(), 10, -14, hexColor);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " " + this.pokemon.getSpecies().getNationalPokedexNumber(), -30, -14, hexColor);
         if (this.pokemon.getOriginalTrainer() != null) {
            this.func_73732_a(this.field_146297_k.field_71466_p, this.pokemon.getOriginalTrainer(), 8, 126, hexColor);
         }
      } else {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " ???", 10, -14, hexColor);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " ???", -30, -14, hexColor);
         this.func_73732_a(this.field_146297_k.field_71466_p, "???", 8, 126, hexColor);
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.ot", new Object[0]), -32, 111, hexColor);
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("nbt.hp.name", new Object[0]), 60, -12, hexColor);
      String strHP = String.valueOf(this.pokemon.getMaxHealth());
      if (this.pokemon.isEgg()) {
         strHP = "???";
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, strHP, 200 - strHP.length() * 3, -12, hexColor);
      if (this.pokemon.getNature().increasedStat == StatsType.Attack && !this.pokemon.isEgg()) {
         hexColor = 65280;
      } else if (this.pokemon.getNature().decreasedStat == StatsType.Attack && !this.pokemon.isEgg()) {
         hexColor = 16724016;
      } else {
         hexColor = 16777215;
      }

      String strATK = String.valueOf(this.pokemon.getStat(StatsType.Attack));
      if (this.pokemon.isEgg()) {
         strATK = "???";
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("nbt.attack.name", new Object[0]), 60, 9, hexColor);
      this.func_73731_b(this.field_146297_k.field_71466_p, strATK, 200 - strATK.length() * 3, 9, hexColor);
      if (this.pokemon.getNature().increasedStat == StatsType.Defence && !this.pokemon.isEgg()) {
         hexColor = 65280;
      } else if (this.pokemon.getNature().decreasedStat == StatsType.Defence && !this.pokemon.isEgg()) {
         hexColor = 16724016;
      } else {
         hexColor = 16777215;
      }

      String strDEF = String.valueOf(this.pokemon.getStat(StatsType.Defence));
      if (this.pokemon.isEgg()) {
         strDEF = "???";
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("nbt.defense.name", new Object[0]), 60, 28, hexColor);
      this.func_73731_b(this.field_146297_k.field_71466_p, strDEF, 200 - strDEF.length() * 3, 28, hexColor);
      if (this.pokemon.getNature().increasedStat == StatsType.SpecialAttack && !this.pokemon.isEgg()) {
         hexColor = 65280;
      } else if (this.pokemon.getNature().decreasedStat == StatsType.SpecialAttack && !this.pokemon.isEgg()) {
         hexColor = 16724016;
      } else {
         hexColor = 16777215;
      }

      String strSATK = String.valueOf(this.pokemon.getStat(StatsType.SpecialAttack));
      if (this.pokemon.isEgg()) {
         strSATK = "???";
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("nbt.spattack.name", new Object[0]), 60, 48, hexColor);
      this.func_73731_b(this.field_146297_k.field_71466_p, strSATK, 200 - strSATK.length() * 3, 48, hexColor);
      if (this.pokemon.getNature().increasedStat == StatsType.SpecialDefence && !this.pokemon.isEgg()) {
         hexColor = 65280;
      } else if (this.pokemon.getNature().decreasedStat == StatsType.SpecialDefence && !this.pokemon.isEgg()) {
         hexColor = 16724016;
      } else {
         hexColor = 16777215;
      }

      String strSDEF = String.valueOf(this.pokemon.getStat(StatsType.SpecialDefence));
      if (this.pokemon.isEgg()) {
         strSDEF = "???";
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("nbt.spdefense.name", new Object[0]), 60, 69, hexColor);
      this.func_73731_b(this.field_146297_k.field_71466_p, strSDEF, 200 - strSDEF.length() * 3, 69, hexColor);
      if (this.pokemon.getNature().increasedStat == StatsType.Speed && !this.pokemon.isEgg()) {
         hexColor = 65280;
      } else if (this.pokemon.getNature().decreasedStat == StatsType.Speed && !this.pokemon.isEgg()) {
         hexColor = 16724016;
      } else {
         hexColor = 16777215;
      }

      String strSPD = String.valueOf(this.pokemon.getStat(StatsType.Speed));
      if (this.pokemon.isEgg()) {
         strSPD = "???";
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("nbt.speed.name", new Object[0]), 60, 88, hexColor);
      this.func_73731_b(this.field_146297_k.field_71466_p, strSPD, 200 - strSPD.length() * 3, 88, hexColor);
      hexColor = 16777215;
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.happiness", new Object[0]), 70, 113, hexColor);
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.nature", new Object[0]), 157, 113, hexColor);
      this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.growth", new Object[0]), 8, 137, hexColor);
      if (!this.pokemon.isEgg()) {
         this.func_73732_a(this.field_146297_k.field_71466_p, String.valueOf(this.pokemon.getFriendship()), 95, 132, hexColor);
         EnumNature nature = this.pokemon.getBaseNature();
         EnumNature mintNature = this.pokemon.getMintNature();
         boolean hasMintNature = mintNature != null && mintNature != nature;
         if (hasMintNature) {
            this.func_73732_a(this.field_146297_k.field_71466_p, mintNature.getLocalizedName(), 174, 145, 11927491);
         }

         this.func_73732_a(this.field_146297_k.field_71466_p, nature.getLocalizedName(), 174, 132, -1);
         this.func_73732_a(this.field_146297_k.field_71466_p, this.pokemon.getGrowth().getLocalizedName(), 8, 150, -1);
      } else {
         this.func_73732_a(this.field_146297_k.field_71466_p, "???", 95, 132, hexColor);
         this.func_73732_a(this.field_146297_k.field_71466_p, "???", 174, 132, -1);
         this.func_73732_a(this.field_146297_k.field_71466_p, "???", 8, 150, -1);
      }

      this.drawBasePokemonInfo();
   }

   public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summaryStats);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_73729_b((this.field_146294_l - this.xSize) / 2 - 40, (this.field_146295_m - this.ySize) / 2 - 25, 0, 0, 256, 205);
      this.drawPokemonName();
      this.drawArrows(mouseX, mouseY);
   }

   public void drawHealthBar(int x, int y, int width, int height, Pokemon pokemon) {
      GlStateManager.func_179091_B();
      GlStateManager.func_179119_h();
      GlStateManager.func_179094_E();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      GlStateManager.func_179090_x();
      int barWidth = width - 6;
      buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      buffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(1.0F, 0.2F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)x, (double)(y + height), 0.0).func_181666_a(1.0F, 0.2F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)(y + height), 0.0).func_181666_a(1.0F, 0.2F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)y, 0.0).func_181666_a(1.0F, 0.2F, 0.2F, 1.0F).func_181675_d();
      if (!pokemon.isEgg()) {
         barWidth = (int)((float)pokemon.getHealth() / (float)pokemon.getMaxHealth() * ((float)width - 6.0F));
      } else {
         barWidth = (int)(1.0F * ((float)width - 6.0F));
      }

      barWidth = (int)((float)pokemon.getHealth() / (float)pokemon.getMaxHealth() * ((float)width - 6.0F));
      buffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(0.2F, 1.0F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)x, (double)(y + height), 0.0).func_181666_a(0.2F, 1.0F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)(y + height), 0.0).func_181666_a(0.2F, 1.0F, 0.2F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(x + barWidth), (double)y, 0.0).func_181666_a(0.2F, 1.0F, 0.2F, 1.0F).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179121_F();
      GlStateManager.func_179098_w();
      GlStateManager.func_179101_C();
      GlStateManager.func_179119_h();
   }
}
