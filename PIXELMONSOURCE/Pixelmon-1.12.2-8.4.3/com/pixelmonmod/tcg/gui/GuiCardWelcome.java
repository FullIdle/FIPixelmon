package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.helper.CardHelper;
import net.minecraft.util.ResourceLocation;

public class GuiCardWelcome extends TCGGuiScreen {
   public static final int TEXTURE_WIDTH = 216;
   public static final int TEXTURE_HEIGHT = 335;

   protected void func_73864_a(int i, int j, int par3) {
      if (i > this.field_146294_l / 2 + 54 || i < this.field_146294_l / 2 - 83 || j > this.field_146295_m / 2 + 83 || j < this.field_146295_m / 2 - 83) {
         this.field_146297_k.field_71439_g.func_71053_j();
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      CardHelper.resetColour(1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/introduction.png"));
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 54), (double)(this.field_146295_m / 2 - 83), 108.0, 167.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      CardHelper.resetColour(1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(CardType.TRAINER.getIcon());
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 48), (double)(this.field_146295_m / 2 - 66), 27.0, 11.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      CardHelper.resetColour(1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/icons/" + CardRarity.SECRETRARE.getUnlocalizedName() + ".png"));
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 42), (double)(this.field_146295_m / 2 - 9), 8.0, 8.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      CardHelper.resetColour(1.0F);
      this.field_146297_k.field_71466_p.func_78264_a(true);
      String cardName = "Welcome!";
      this.field_146297_k.field_71466_p.func_78276_b(cardName, this.field_146294_l / 2 - 18, this.field_146295_m / 2 - 65, 2105376);
      CardHelper.resetColour(1.0F);
   }

   public boolean func_73868_f() {
      return false;
   }
}
