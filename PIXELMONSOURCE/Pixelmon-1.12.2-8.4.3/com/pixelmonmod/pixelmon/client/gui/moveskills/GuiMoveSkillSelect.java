package com.pixelmonmod.pixelmon.client.gui.moveskills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;

public class GuiMoveSkillSelect extends GuiScreen {
   int slot;
   Pokemon pokemon = null;
   ArrayList moveSkills = new ArrayList();
   int currentlySelectedIndex = -1;
   long mouseStationaryTime = System.currentTimeMillis();
   int lastMouseX = 0;
   int lastMouseY = 0;

   public GuiMoveSkillSelect(int slot) {
      this.slot = slot;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.pokemon = ClientStorageManager.party.get(this.slot);
      if (this.pokemon == null) {
         this.field_146297_k.field_71439_g.func_71053_j();
      } else {
         this.moveSkills = ClientProxy.getMoveSkills(this.pokemon);
         if (GuiPixelmonOverlay.selectedMoveSkill != null) {
            MoveSkill selected = (MoveSkill)CollectionHelper.find(this.moveSkills, (moveSkill) -> {
               return moveSkill.id.equals(GuiPixelmonOverlay.selectedMoveSkill);
            });
            if (selected != null) {
               this.currentlySelectedIndex = this.moveSkills.indexOf(selected);
            }
         }

      }
   }

   private int getIndexForPos(int mouseX, int mouseY) {
      mouseX -= this.field_146294_l / 2;
      mouseY = -1 * (mouseY - this.field_146295_m / 2);
      double quadrantAdjustment = mouseX >= 0 && mouseY >= 0 ? 0.0 : (mouseX >= 0 && mouseY <= 0 ? 6.283185307179586 : Math.PI);
      double r = Math.sqrt((double)(mouseX * mouseX + mouseY * mouseY));
      double theta = Math.atan(1.0 * (double)mouseY / (double)mouseX) + quadrantAdjustment;
      theta = ((theta - 1.5707963267948966) * 180.0 / Math.PI + 360.0) % 360.0;
      theta = 360.0 - theta;
      double wheelRadius = this.getWheelRadius();
      if (!(r < wheelRadius * 0.188) && !(r > wheelRadius * 0.9)) {
         if (this.moveSkills.isEmpty()) {
            return -1;
         } else if (this.moveSkills.size() == 1) {
            return 0;
         } else {
            double radialWidth = 360.0 / (double)this.moveSkills.size();

            for(int i = 0; i < this.moveSkills.size(); ++i) {
               if (i == 0) {
                  if (theta > 360.0 - radialWidth / 2.0 || theta < 0.0 + radialWidth / 2.0) {
                     return i;
                  }
               } else if (theta > (double)i * radialWidth - radialWidth / 2.0 && theta < (double)i * radialWidth + radialWidth / 2.0) {
                  return i;
               }
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public double getWheelRadius() {
      return 0.4 * (double)Math.min(this.field_146295_m, this.field_146294_l);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      int hovering = this.getIndexForPos(mouseX, mouseY);
      int focused = hovering;
      if (hovering == -1) {
         focused = this.currentlySelectedIndex;
      }

      float wheelRadius = (float)this.getWheelRadius();
      GlStateManager.func_179147_l();
      GlStateManager.func_179141_d();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.moveSkillWheel);
      GuiHelper.drawImageQuad((double)((float)(this.field_146294_l / 2) - wheelRadius), (double)((float)(this.field_146295_m / 2) - wheelRadius), (double)(wheelRadius * 2.0F), wheelRadius * 2.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.moveSkillWheelCenter);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2) - (double)wheelRadius * 0.2, (double)(this.field_146295_m / 2) - (double)wheelRadius * 0.2, (double)(wheelRadius * 0.4F), wheelRadius * 0.4F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      GuiHelper.bindPokemonSprite(this.pokemon, this.field_146297_k);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2) - (double)wheelRadius * 0.2, (double)(this.field_146295_m / 2) - (double)wheelRadius * 0.25, (double)(wheelRadius * 0.4F), wheelRadius * 0.4F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      int i;
      if (this.moveSkills.size() == 1) {
         this.drawMoveSkill(wheelRadius, 0.0F, (MoveSkill)this.moveSkills.get(0), true);
      } else {
         for(i = 0; i < this.moveSkills.size(); ++i) {
            this.drawMoveSkill(wheelRadius, 360.0F - (float)i * 360.0F / (float)this.moveSkills.size(), (MoveSkill)this.moveSkills.get(i), focused == i);
         }
      }

      if (focused != -1) {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.moveSkillPointer);
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b((float)(this.field_146294_l / 2), (float)(this.field_146295_m / 2), 0.0F);
         GlStateManager.func_179114_b(360.0F / (float)this.moveSkills.size() * (float)focused, 0.0F, 0.0F, 1.0F);
         GuiHelper.drawImageQuad((double)(-wheelRadius / 10.0F), (double)(-wheelRadius) * 0.4, (double)(wheelRadius / 5.0F), wheelRadius / 5.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         GlStateManager.func_179121_F();
      }

      if (this.moveSkills.size() > 1) {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pcPointerGrabbed);

         for(i = 0; i < this.moveSkills.size(); ++i) {
            float angle = 360.0F / (float)this.moveSkills.size() * ((float)i + (this.moveSkills.size() % 2 == 0 ? 0.5F : 0.0F));
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)(this.field_146294_l / 2), (float)(this.field_146295_m / 2), 0.0F);
            GlStateManager.func_179114_b(angle, 0.0F, 0.0F, 1.0F);
            GuiHelper.drawImageQuad((double)(-wheelRadius / 250.0F), (double)(wheelRadius * 0.2F), (double)(wheelRadius / 125.0F), wheelRadius * 0.7F, 0.45, 0.45, 0.55, 0.55, this.field_73735_i);
            GlStateManager.func_179121_F();
         }
      }

      if (mouseX == this.lastMouseX && mouseY == this.lastMouseY && System.currentTimeMillis() > this.mouseStationaryTime + 1250L && hovering != -1) {
         MoveSkill moveSkill = (MoveSkill)this.moveSkills.get(focused);
         StringBuilder str = new StringBuilder("§n" + I18n.func_135052_a(moveSkill.name, new Object[0]));
         Iterator var9 = moveSkill.descriptions.iterator();

         while(var9.hasNext()) {
            String description = (String)var9.next();
            str.append("\n\n§r").append(I18n.func_135052_a(description, new Object[0]));
         }

         List strings = this.field_146297_k.field_71466_p.func_78271_c(str.toString(), this.field_146294_l - mouseX - 10);
         float maxWidth = 0.0F;
         Iterator var11 = strings.iterator();

         while(var11.hasNext()) {
            String string = (String)var11.next();
            int width = this.field_146297_k.field_71466_p.func_78256_a(string);
            if ((float)width > maxWidth) {
               maxWidth = (float)width;
            }
         }

         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.button);
         GuiHelper.drawImageQuad((double)(mouseX - 9), (double)(mouseY - 16), (double)(maxWidth + 20.0F), (float)(strings.size() * 9 + 17), 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         this.field_146297_k.field_71466_p.func_78279_b(str.toString(), mouseX + 3, mouseY - 7, this.field_146294_l - mouseX - 10, 16777215);
      } else if (mouseX != this.lastMouseX || mouseY != this.lastMouseY) {
         this.mouseStationaryTime = System.currentTimeMillis();
         this.lastMouseX = mouseX;
         this.lastMouseY = mouseY;
      }

      GlStateManager.func_179084_k();
   }

   private void drawMoveSkill(float wheelRadius, float rotation, MoveSkill moveSkill, boolean select) {
      this.field_146297_k.field_71446_o.func_110577_a(moveSkill.sprite);
      float r = 0.6F * wheelRadius;
      int x = (int)((double)((float)this.field_146294_l / 2.0F) - (double)r * Math.cos((double)(rotation - 90.0F) * Math.PI / 180.0));
      int y = this.field_146295_m - (int)((double)((float)this.field_146295_m / 2.0F) - (double)r * Math.sin((double)(rotation - 90.0F) * Math.PI / 180.0));
      GuiHelper.drawImageQuad((double)((float)x - wheelRadius / 6.0F), (double)((float)y - wheelRadius / 6.0F), (double)(wheelRadius / 3.0F), wheelRadius / 3.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      if (select) {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.moveSkillSelect);
         GuiHelper.drawImageQuad((double)((float)x - wheelRadius / 6.0F), (double)((float)y - wheelRadius / 6.0F), (double)(wheelRadius / 3.0F), wheelRadius / 3.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int button) throws IOException {
      int clickedSpot = this.getIndexForPos(mouseX, mouseY);
      if (clickedSpot == -1) {
         this.field_146297_k.field_71439_g.func_71053_j();
      } else {
         GuiPixelmonOverlay.selectedMoveSkill = ((MoveSkill)this.moveSkills.get(clickedSpot)).id;
         this.field_146297_k.field_71439_g.func_184185_a(SoundEvents.field_187909_gi, 1.0F, 1.0F);
         this.field_146297_k.field_71439_g.func_71053_j();
      }

   }
}
