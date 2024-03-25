package com.pixelmonmod.pixelmon.client.gui.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.chooseMoveset.GuiAttackList;
import com.pixelmonmod.pixelmon.client.gui.chooseMoveset.IElementClicked;
import com.pixelmonmod.pixelmon.comm.packetHandlers.NPCTeachMove;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCRelearner;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class GuiRelearner extends GuiScreen implements IElementClicked {
   Pokemon pokemon;
   NPCRelearner relearner;
   ItemStack cost;
   public static List attackList;
   int listTop;
   int listLeft;
   int listHeight;
   int listWidth;
   GuiAttackList attackListGui;

   public GuiRelearner(Pokemon pokemon, int npcId) {
      Optional entityNPCOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, npcId, NPCRelearner.class);
      if (!entityNPCOptional.isPresent()) {
         GuiHelper.closeScreen();
      } else {
         this.relearner = (NPCRelearner)entityNPCOptional.get();
         this.cost = this.relearner.getCost();
         this.pokemon = pokemon;
         attackList = NPCRelearner.getRelearnableMoves(pokemon);
         this.listHeight = 150;
         this.listWidth = 90;
      }
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.listTop = this.field_146295_m / 2 - 62;
      this.listLeft = this.field_146294_l / 2 - 40;
      this.attackListGui = new GuiAttackList(this, attackList, this.listWidth, this.listHeight, this.listTop, this.listLeft, this.field_146297_k);
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 50, 20, I18n.func_135052_a("gui.cancel.text", new Object[0])));
   }

   public void func_73863_a(int mouseX, int mouseY, float mFloat) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad(0.0, 0.0, (double)this.field_146294_l, (float)this.field_146295_m, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      String text = I18n.func_135052_a("gui.choosemoveset.relearnmove", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 110, 0);
      text = I18n.func_135052_a("gui.choosemoveset.choosemove", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 - 15 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 92, 0);
      this.attackListGui.drawScreen(mouseX, mouseY, mFloat);
      GuiHelper.bindPokemonSprite(this.pokemon, this.field_146297_k);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 211), (double)(this.field_146295_m / 2 - 98), 84.0, 84.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      GuiHelper.drawAttackInfoBox(this.field_73735_i, this.field_146294_l, this.field_146295_m);

      for(int i = 0; i < attackList.size(); ++i) {
         if (this.attackListGui.isMouseOver(i, mouseX, mouseY)) {
            GuiHelper.drawAttackInfoList((Attack)attackList.get(i), this.field_146294_l, this.field_146295_m);
         }
      }

      if (this.cost != null) {
         text = I18n.func_135052_a("gui.choosemoveset.cost", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 130 - this.field_146289_q.func_78256_a(text), this.field_146295_m / 2 + 60, 0);
         this.field_146296_j.func_180450_b(this.cost, this.field_146294_l / 2 + 130, this.field_146295_m / 2 + 55);
         this.field_146296_j.func_180453_a(this.field_146297_k.field_71466_p, this.cost, this.field_146294_l / 2 + 130, this.field_146295_m / 2 + 55, (String)null);
      }

      super.func_73863_a(mouseX, mouseY, mFloat);
   }

   public void elementClicked(List list, int index) {
      Attack a = (Attack)attackList.get(index);
      this.field_146297_k.field_71439_g.func_71053_j();
      Pixelmon.network.sendToServer(new NPCTeachMove(this.pokemon.getUUID(), a.getActualMove().getAttackId(), this.relearner.getId()));
   }

   public boolean func_73868_f() {
      return false;
   }

   protected void func_146284_a(GuiButton button) {
      this.field_146297_k.field_71439_g.func_71053_j();
   }
}
