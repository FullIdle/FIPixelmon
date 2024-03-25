package com.pixelmonmod.pixelmon.client.gui.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.chooseMoveset.GuiMoveList;
import com.pixelmonmod.pixelmon.client.gui.chooseMoveset.IElementClicked;
import com.pixelmonmod.pixelmon.comm.packetHandlers.NPCTeachMove;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.enums.EnumNPCTutorType;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class GuiTutor extends GuiScreen implements IElementClicked {
   public static List moveList;
   NPCTutor tutor;
   int listTop;
   int listLeft;
   int listHeight;
   int listWidth;
   GuiMoveList attackListGui;
   Pokemon pokemon;

   public GuiTutor(Pokemon data, int npcId) {
      Optional entityNPCOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, npcId, NPCTutor.class);
      if (!entityNPCOptional.isPresent()) {
         GuiHelper.closeScreen();
      } else {
         this.tutor = (NPCTutor)entityNPCOptional.get();
         this.pokemon = data;
         this.listHeight = 150;
         this.listWidth = 90;
      }
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      if (moveList.isEmpty()) {
         this.field_146297_k.field_71439_g.func_71053_j();
      } else {
         this.listTop = this.field_146295_m / 2 - 62;
         this.listLeft = this.field_146294_l / 2 - 40;
         this.attackListGui = new GuiMoveList(this, moveList, this.listWidth, this.listHeight, this.listTop, this.listLeft, this.field_146297_k);
         this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 50, 20, I18n.func_135052_a("gui.cancel.text", new Object[0])));
      }
   }

   public void func_73863_a(int mouseX, int mouseY, float mFloat) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GlStateManager.func_179124_c(0.9F, 0.9F, 0.9F);
      GuiHelper.drawImageQuad(0.0, 0.0, (double)this.field_146294_l, (float)this.field_146295_m, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      String text = this.tutor.getTutorType() == EnumNPCTutorType.TRANSFER ? I18n.func_135052_a("pixelmon.npc.transfername", new Object[0]) : I18n.func_135052_a("pixelmon.npc.tutorname", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 110, 0);
      text = I18n.func_135052_a("gui.choosemoveset.choosemove", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 92, 0);
      GuiHelper.bindPokemonSprite(this.pokemon, this.field_146297_k);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)((float)this.field_146294_l / 2.0F - 211.0F), (double)((float)this.field_146295_m / 2.0F - 98.0F), 84.0, 84.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      this.attackListGui.drawScreen(mouseX, mouseY, mFloat);
      GuiHelper.drawAttackInfoBox(this.field_73735_i, this.field_146294_l, this.field_146295_m);
      text = I18n.func_135052_a("gui.choosemoveset.cost", new Object[0]);
      int costTextWidth = this.field_146289_q.func_78256_a(text);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 125 - costTextWidth, this.field_146295_m / 2 + 60, 0);

      for(int i = 0; i < moveList.size(); ++i) {
         NPCTutor.LearnableMove learnableMove = (NPCTutor.LearnableMove)moveList.get(i);
         if (this.attackListGui.isMouseOver(i, mouseX, mouseY)) {
            drawAttackInfoList(learnableMove.attack(), this.field_146294_l, this.field_146295_m);
            List costList = learnableMove.costs();
            if (costList != null && !costList.isEmpty()) {
               int j = 0;

               for(Iterator var10 = costList.iterator(); var10.hasNext(); ++j) {
                  ItemStack item = (ItemStack)var10.next();
                  this.field_146296_j.func_180450_b(item, this.field_146294_l / 2 + 125 + j * 20, this.field_146295_m / 2 + 55);
                  this.field_146296_j.func_180453_a(this.field_146297_k.field_71466_p, item, this.field_146294_l / 2 + 125 + j * 20, this.field_146295_m / 2 + 55, (String)null);
               }
            }

            if (this.pokemon.getMoveset().hasAttack(learnableMove.attack())) {
               text = I18n.func_135052_a("gui.tutor.already", new Object[0]);
            } else if (learnableMove.learnable()) {
               text = I18n.func_135052_a("gui.tutor.canlearn", new Object[0]);
            } else {
               text = I18n.func_135052_a("gui.tutor.cantlearn", new Object[0]);
            }

            this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 125 - costTextWidth, this.field_146295_m / 2 + 75, 0);
         }
      }

      super.func_73863_a(mouseX, mouseY, mFloat);
   }

   public void elementClicked(List list, int index) {
      NPCTutor.LearnableMove learnableMove = (NPCTutor.LearnableMove)moveList.get(index);
      if (learnableMove.learnable() && !this.pokemon.getMoveset().hasAttack(learnableMove.attack())) {
         this.field_146297_k.field_71439_g.func_71053_j();
         Pixelmon.network.sendToServer(new NPCTeachMove(this.pokemon.getUUID(), learnableMove.attack().getAttackId(), this.tutor.getId()));
      }

   }

   protected void func_146284_a(GuiButton button) {
      if (button.field_146127_k == 1) {
         this.field_146297_k.field_71439_g.func_71053_j();
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public static void drawAttackInfoList(AttackBase a, int width, int height) {
      FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
      int y = height / 2 - 85;
      int x = width / 2 + 105;
      String powerString = I18n.func_135052_a("gui.choosemoveset.power", new Object[0]) + ": ";
      if (a.getBasePower() > 0) {
         powerString = powerString + a.getBasePower();
      } else {
         powerString = powerString + "--";
      }

      fontRenderer.func_78276_b(powerString, x, y + 3, 0);
      String accuracyString = I18n.func_135052_a("gui.battle.accuracy", new Object[0]) + ": ";
      if (a.getAccuracy() > 0) {
         accuracyString = accuracyString + a.getAccuracy();
      } else {
         accuracyString = accuracyString + "--";
      }

      fontRenderer.func_78276_b(accuracyString, x, y + 13, 0);
      fontRenderer.func_78276_b(I18n.func_135052_a("nbt.pp.name", new Object[0]) + " " + a.getPPBase() + "/" + a.getPPBase(), x, y + 23, 0);
      String typeString = I18n.func_135052_a("gui.battle.type", new Object[0]) + " ";
      fontRenderer.func_78276_b(typeString, x, y + 33, 0);
      fontRenderer.func_78276_b(a.getAttackType().getLocalizedName(), x + fontRenderer.func_78256_a(typeString), y + 33, a.getAttackType().getColor());
      String category = a.getAttackCategory().getLocalizedName();
      fontRenderer.func_78276_b(category, x, y + 43, 0);
      fontRenderer.func_78279_b(I18n.func_135052_a("attack." + a.getAttackName().replace(" ", "_").toLowerCase() + ".description", new Object[0]), x, y + 58, 95, 0);
   }
}
