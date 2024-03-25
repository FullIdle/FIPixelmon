package com.pixelmonmod.pixelmon.client.gui.chooseMoveset;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.comm.packetHandlers.chooseMoveset.ChooseMoveset;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;

public class GuiChooseMoveset extends GuiScreen implements IElementClicked {
   Pokemon pokemon;
   public static ArrayList mainAttackList;
   public static ArrayList chosenAttackList;
   int listTop;
   int listHeight;
   int listWidth;
   GuiAttackList mainList;
   GuiAttackList chosenList;

   public GuiChooseMoveset(Pokemon pokemon) {
      this.pokemon = pokemon;
      mainAttackList = pokemon.getBaseStats().getMovesUpToLevel(pokemon.getLevel());
      chosenAttackList = new ArrayList();
      this.listHeight = 150;
      this.listWidth = 90;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.listTop = this.field_146295_m / 2 - 58;
      this.mainList = new GuiAttackList(this, mainAttackList, this.listWidth, this.listHeight, this.listTop, this.field_146294_l / 2 - 3, this.field_146297_k);
      this.chosenList = new GuiAttackList(this, chosenAttackList, this.listWidth, this.listHeight, this.listTop, this.field_146294_l / 2 - 103, this.field_146297_k);
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
   }

   public void func_73863_a(int mouseX, int mouseY, float mFloat) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad(0.0, 0.0, (double)this.field_146294_l, (float)this.field_146295_m, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      String text = I18n.func_135052_a("gui.choosemoveset.title", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 110, 0);
      text = I18n.func_135052_a("gui.choosemoveset.chosen", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 - 65 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 92, 0);
      text = I18n.func_135052_a("gui.choosemoveset.full", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 35 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 92, 0);
      this.mainList.drawScreen(mouseX, mouseY, mFloat);
      this.chosenList.drawScreen(mouseX, mouseY, mFloat);
      GuiHelper.bindPokemonSprite(this.pokemon, this.field_146297_k);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 211), (double)(this.field_146295_m / 2 - 101), 84.0, 84.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      GuiHelper.drawAttackInfoBox(this.field_73735_i, this.field_146294_l, this.field_146295_m);

      int i;
      for(i = 0; i < mainAttackList.size(); ++i) {
         if (this.mainList.isMouseOver(i, mouseX, mouseY)) {
            GuiHelper.drawAttackInfoList((Attack)mainAttackList.get(i), this.field_146294_l, this.field_146295_m);
         }
      }

      for(i = 0; i < chosenAttackList.size(); ++i) {
         if (this.chosenList.isMouseOver(i, mouseX, mouseY)) {
            GuiHelper.drawAttackInfoList((Attack)chosenAttackList.get(i), this.field_146294_l, this.field_146295_m);
         }
      }

      super.func_73863_a(mouseX, mouseY, mFloat);
   }

   public void elementClicked(List list, int index) {
      Attack a;
      if (list == mainAttackList) {
         if (chosenAttackList.size() >= 4) {
            return;
         }

         a = (Attack)mainAttackList.get(index);
         mainAttackList.remove(a);
         chosenAttackList.add(a);
      } else {
         a = (Attack)chosenAttackList.get(index);
         chosenAttackList.remove(a);
         mainAttackList.add(a);
      }

   }

   protected void func_73869_a(char p_73869_1_, int p_73869_2_) {
      if (p_73869_2_ == 1 || p_73869_2_ == 28) {
         this.func_146284_a((GuiButton)null);
      }

   }

   protected void func_146284_a(GuiButton button) {
      Pixelmon.network.sendToServer(new ChooseMoveset(this.pokemon, chosenAttackList));
      this.field_146297_k.field_71439_g.func_71053_j();
   }

   public boolean func_73868_f() {
      return false;
   }
}
