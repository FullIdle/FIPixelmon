package com.pixelmonmod.pixelmon.client.gui.statueEditor;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.RenderStatue;
import com.pixelmonmod.pixelmon.comm.packetHandlers.statueEditor.EnumStatuePacketMode;
import com.pixelmonmod.pixelmon.comm.packetHandlers.statueEditor.StatuePacketServer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumStatueTextureType;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;

public class GuiStatueEditor extends GuiScreenDropDown {
   public static UUID statueId;
   private EntityStatue statue;
   GuiButton animationGuiButton;
   GuiButton modelGuiButton;
   GuiTextField tbName;
   GuiTextField tbLabel;
   GuiTextField tbFrame;
   int controlWidth = 320;
   int controlLeft;
   int controlHeight = 90;
   int controlTop;
   ModelBase lastModel;
   private GuiDropDown formDropDown;
   int lastFrame;
   static float spinCount = 0.0F;

   public GuiStatueEditor(int entityid) {
      Keyboard.enableRepeatEvents(true);
      Entity entity = Minecraft.func_71410_x().field_71441_e.func_73045_a(entityid);
      if (entity instanceof EntityStatue) {
         EntityStatue statue = (EntityStatue)entity;
         if (Objects.equals(statue.func_110124_au(), statueId)) {
            this.statue = statue;
         }
      }

   }

   public void func_73866_w_() {
      if (this.statue == null) {
         GuiHelper.closeScreen();
      } else {
         EnumSpecies species = this.statue.getSpecies();
         if (species == null) {
            GuiHelper.closeScreen();
         } else {
            super.func_73866_w_();
            this.controlLeft = this.field_146294_l / 2 - this.controlWidth / 2;
            this.controlTop = this.field_146295_m - this.controlHeight - 10;
            int buttonHeight = 20;
            int textFieldHeight = 17;
            this.field_146292_n.add(new GuiButton(0, this.controlLeft + 137, this.controlTop + 8, 80, buttonHeight, I18n.func_135052_a("gui.update.text", new Object[0])));
            this.field_146292_n.add(new GuiButton(2, this.controlLeft + 137, this.controlTop + 30, 80, buttonHeight, I18n.func_135052_a("gui.animate.text", new Object[0])));
            List textures = new ArrayList(Arrays.asList(EnumStatueTextureType.values()));
            textures.remove(EnumStatueTextureType.Special);
            ITranslatable current = this.statue.getTextureType();
            this.addDropDown((new GuiDropDown(textures, current, this.controlLeft + 228, this.controlTop - 15, 80, 120)).setGetOptionString(ITranslatable::getLocalizedName).setOnSelected((texture) -> {
               Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetTextureType, statueId, texture.toString()));
            }).setInactiveTop(this.controlTop + 20));
            this.addDropDown((new GuiDropDown(EnumGrowth.orderedList, this.statue.getGrowth(), this.controlLeft + 45, this.controlTop + 4, 80, 150)).setGetOptionString(ITranslatable::getLocalizedName).setOnSelected((growth) -> {
               Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetGrowth, statueId, growth.toString()));
            }).setInactiveTop(this.controlTop + 35));
            List forms = Lists.newArrayList(this.statue.getSpecies().getPossibleForms(true));
            if (EnumSpecies.genderForm.contains(this.statue.getSpecies()) || EnumSpecies.mfTextured.contains(this.statue.getSpecies())) {
               if (!forms.contains(Gender.Male)) {
                  forms.add(Gender.Male);
               }

               if (!forms.contains(Gender.Female)) {
                  forms.add(Gender.Female);
               }
            }

            int width = Math.min(150, Math.max(80, forms.stream().mapToInt((f) -> {
               return this.field_146297_k.field_71466_p.func_78256_a(f.getLocalizedName());
            }).max().orElse(0)));
            this.formDropDown = this.addDropDown((new GuiDropDown(forms, this.statue.getFormEnum(), this.controlLeft + 228, this.controlTop + 46 - Math.min(120, forms.size() * 6), width, 150)).setGetOptionString(ITranslatable::getLocalizedName).setOnSelected((form) -> {
               if (form instanceof Gender) {
                  Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetGender, statueId, ((Gender)form).ordinal()));
               } else {
                  Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetForm, statueId, form.getForm()));
                  if (this.statue.getIsFlying()) {
                     this.changeFlying(false);
                  }
               }

               this.resetSpecialTexture();
            }).setInactiveTop(this.controlTop + 44));
            this.field_146292_n.add(new GuiButton(1, this.controlLeft + this.controlWidth - 55, this.field_146295_m - 31, 50, buttonHeight, I18n.func_135052_a("gui.save.text", new Object[0])));
            this.tbFrame = null;
            if (this.statue.isSmd() && this.statue.getFrameCount() > 0) {
               int animTop = this.controlTop + 73;
               List anims = this.statue.getAllAnimations();
               List animStrings = new ArrayList();
               Iterator var11 = anims.iterator();

               while(var11.hasNext()) {
                  AnimationType anim = (AnimationType)var11.next();
                  animStrings.add(anim.toString().toLowerCase());
               }

               this.addDropDown((new GuiDropDown(animStrings, this.statue.getAnimation().toString().toLowerCase(), this.controlLeft + 48, animTop + Math.max(-16, (anims.size() - 1) * -10), 60, 100)).setGetOptionString((animation) -> {
                  return I18n.func_135052_a("gui.model." + animation, new Object[0]);
               }).setOnSelected(this::selectAnimation).setInactiveTop(this.controlTop + 73));
               this.tbFrame = new GuiTextField(9, this.field_146297_k.field_71466_p, this.controlLeft + 158, this.controlTop + 70, 40, textFieldHeight);
               this.tbFrame.func_146180_a(this.statue.getAnimationFrame() + "");
               this.lastFrame = this.statue.getAnimationFrame();
            }

            if (this.statue.hasFlyingModel()) {
               String extra = this.statue.getIsFlying() ? "flying" : "standing";
               this.modelGuiButton = new GuiButton(10, this.controlLeft + 137, this.controlTop + 30, 80, buttonHeight, I18n.func_135052_a("gui.model." + extra, new Object[0]));
               this.field_146292_n.add(this.modelGuiButton);
            }

            this.tbName = (new GuiTabCompleteTextField(6, this.field_146297_k.field_71466_p, this.controlLeft + 45, this.controlTop + 10, 90, textFieldHeight)).setCompletions(EnumSpecies.getNameList());
            this.tbName.func_146180_a(this.statue.getSpecies().getLocalizedName());
            this.tbLabel = new GuiTextField(7, this.field_146297_k.field_71466_p, this.controlLeft + 45, this.controlTop + 51, 160, textFieldHeight);
            this.tbLabel.func_146180_a(this.statue.getLabel());
            this.lastModel = this.statue.getModel();
            this.checkForm();
         }
      }
   }

   private void selectAnimation(String nextAnim) {
      this.statue.setAnimation(AnimationType.getTypeFor(nextAnim));
      Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetAnimation, statueId, nextAnim));
   }

   protected void func_73869_a(char key, int keyCode) throws IOException {
      int eventKey = Keyboard.getEventKey();
      if (eventKey != 1 && eventKey != 28) {
         if (this.tbName == null) {
            this.func_73866_w_();
         }

         this.tbName.func_146201_a(key, keyCode);
         this.tbLabel.func_146201_a(key, keyCode);
         if (this.tbFrame != null) {
            this.tbFrame.func_146201_a(key, keyCode);
            if (this.tbFrame.func_146206_l()) {
               int frame = 0;
               if (!this.tbFrame.func_146179_b().isEmpty()) {
                  try {
                     frame = Integer.parseInt(this.tbFrame.func_146179_b());
                  } catch (NumberFormatException var6) {
                     this.tbFrame.func_146180_a("");
                  }
               }

               if (frame != this.lastFrame) {
                  if (frame < 0) {
                     frame = 0;
                     this.tbFrame.func_146180_a("");
                  } else if (frame >= this.statue.getFrameCount()) {
                     frame = this.statue.getFrameCount() - 1;
                     this.tbFrame.func_146180_a(String.valueOf(frame));
                  }

                  if (frame != this.lastFrame) {
                     this.statue.setAnimationFrame(frame);
                     Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetAnimationFrame, statueId, frame));
                     this.lastFrame = frame;
                  }
               }
            }
         }

      } else {
         this.closeScreen();
      }
   }

   private void closeScreen() {
      if (!this.tbName.func_146179_b().equalsIgnoreCase(this.statue.getSpecies().name)) {
         EnumSpecies e = EnumSpecies.getFromNameAnyCase(this.tbName.func_146179_b());
         if (e != null && e != this.statue.getSpecies()) {
            Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetName, statueId, e.name));
         }
      }

      if (!this.tbLabel.func_146179_b().equalsIgnoreCase(this.statue.getLabel())) {
         Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetLabel, statueId, this.tbLabel.func_146179_b()));
      }

      GuiHelper.closeScreen();
      this.field_146297_k.func_71381_h();
   }

   protected void mouseClickedUnderMenus(int x, int y, int mouseButton) throws IOException {
      if (this.tbName == null) {
         this.func_73866_w_();
      }

      this.tbName.func_146192_a(x, y, mouseButton);
      this.tbLabel.func_146192_a(x, y, mouseButton);
      if (this.tbFrame != null) {
         this.tbFrame.func_146192_a(x, y, mouseButton);
      }

   }

   protected void func_146284_a(GuiButton guiButton) throws IOException {
      super.func_146284_a(guiButton);
      if (this.tbName == null) {
         this.func_73866_w_();
      }

      if (guiButton.field_146127_k == 0) {
         EnumSpecies e = EnumSpecies.getFromNameAnyCase(this.tbName.func_146179_b());
         if (e != null) {
            this.resetSpecialTexture();
            Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetName, statueId, e.name));
         } else {
            this.tbName.func_146180_a(I18n.func_135052_a("pixelmon." + this.statue.getSpecies().name.toLowerCase() + ".name", new Object[0]));
         }
      } else if (guiButton.field_146127_k == 1) {
         this.closeScreen();
      } else if (guiButton.field_146127_k == 2) {
         boolean shouldAnimate = !this.statue.getShouldAnimate();
         Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetShouldAnimate, statueId, shouldAnimate));
      } else if (guiButton.field_146127_k == 10) {
         this.changeFlying(!this.statue.getIsFlying());
      }

   }

   private void changeFlying(boolean isFlying) {
      this.statue.setIsFlying(isFlying);
      Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetModelStanding, statueId, isFlying));
      String extra = isFlying ? "flying" : "standing";
      if (this.modelGuiButton != null) {
         this.modelGuiButton.field_146126_j = I18n.func_135052_a("gui.model." + extra, new Object[0]);
      }

   }

   protected void drawBackgroundUnderMenus(float p_146976_1_, int p_146976_2_, int par3) {
      this.func_73733_a(this.controlLeft, this.controlTop, this.controlLeft + this.controlWidth, this.controlTop + this.controlHeight, -1713512995, -1713512995);
      if (this.statue.getModel() != this.lastModel) {
         ScaledResolution scaledresolution = new ScaledResolution(this.field_146297_k);
         int i = scaledresolution.func_78326_a();
         int j = scaledresolution.func_78328_b();
         this.func_146280_a(this.field_146297_k, i, j);
         this.checkForm();
         this.lastModel = this.statue.getModel();
      }

      String text = I18n.func_135052_a("gui.trainereditor.name", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.controlLeft + 40 - this.field_146297_k.field_71466_p.func_78256_a(text), this.controlTop + 16, 5592405);
      text = I18n.func_135052_a("gui.trainereditor.growth", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.controlLeft + 40 - this.field_146297_k.field_71466_p.func_78256_a(text), this.controlTop + 36, 5592405);
      if (this.tbName != null) {
         this.tbName.func_146194_f();
      }

      text = I18n.func_135052_a("gui.label.text", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.controlLeft + 40 - this.field_146297_k.field_71466_p.func_78256_a(text), this.controlTop + 56, 5592405);
      if (this.tbLabel != null) {
         this.tbLabel.func_146194_f();
      }

      text = I18n.func_135052_a("gui.texture.text", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.controlLeft + 268 - this.field_146297_k.field_71466_p.func_78256_a(text) / 2, this.controlTop + 10, 5592405);
      if (this.tbFrame != null) {
         text = I18n.func_135052_a("gui.model.animation", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(text, this.controlLeft + 46 - this.field_146297_k.field_71466_p.func_78256_a(text), this.controlTop + 74, 5592405);
         this.tbFrame.func_146194_f();
         text = I18n.func_135052_a("gui.model.frame", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(text, this.controlLeft + 150 - this.field_146297_k.field_71466_p.func_78256_a(text), this.controlTop + 74, 5592405);
         text = "/" + (this.statue.getFrameCount() - 1);
         this.field_146297_k.field_71466_p.func_78276_b(text, this.controlLeft + 200, this.controlTop + 74, 5592405);
      }

      if (this.formDropDown.getVisible()) {
         text = I18n.func_135052_a("gui.trainereditor.form", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(text, this.controlLeft + 268 - this.field_146297_k.field_71466_p.func_78256_a(text) / 2, this.controlTop + 34, 5592405);
      }

      EntityStatue ep = this.statue;
      if (ep != null && ep.getModel() != null) {
         drawEntityToScreen(this.field_146294_l / 2 - 10, this.field_146295_m / 2 + 20, 200, 200, ep, (float)par3, true);
      }

   }

   public static void drawEntityToScreen(int x, int y, int w, int l, EntityStatue e, float pt, boolean spin) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179142_g();
      GlStateManager.func_179126_j();
      GlStateManager.func_179109_b((float)x, (float)y, 100.0F);
      float eheight = (float)l / e.field_70131_O / 4.0F;
      float ewidth = (float)l / e.field_70130_N / 4.0F;
      float scalar = eheight > ewidth ? eheight : ewidth;
      GlStateManager.func_179152_a(scalar, scalar, scalar);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      if (spin) {
         GlStateManager.func_179114_b(spinCount += 0.66F, 0.0F, 1.0F, 0.0F);
      }

      RenderHelper.func_74519_b();

      try {
         RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
         Render entityClassRenderObject = renderManager.func_78715_a(EntityStatue.class);
         RenderStatue rp = (RenderStatue)entityClassRenderObject;
         rp.renderStatue(e, 0.0, e.func_70033_W(), 0.0, 0.0F, pt, true);
         renderManager.field_78735_i = 180.0F;
      } catch (Exception var13) {
      }

      GlStateManager.func_179121_F();
   }

   public boolean func_73868_f() {
      return false;
   }

   public void func_146276_q_() {
      this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, -1725816286, -1725816286);
   }

   private void checkForm() {
      if (this.statue.getSpecies() != null) {
         if (this.statue.getSpecies().getNumForms(false) <= 0 && !this.statue.getSpecies().hasMega() && !EnumSpecies.genderForm.contains(this.statue.getSpecies()) && !EnumSpecies.mfTextured.contains(this.statue.getSpecies())) {
            if (this.formDropDown.getVisible()) {
               this.formDropDown.setVisible(false);
               if (this.statue.getForm() != -1) {
                  this.statue.setForm(-1);
               }
            }
         } else {
            this.formDropDown.setVisible(true);
         }
      }

   }

   private void resetSpecialTexture() {
      if (this.statue.getTextureType() == EnumStatueTextureType.Special) {
         EnumStatueTextureType t = EnumStatueTextureType.OriginalTexture;
         ((GuiButton)this.field_146292_n.get(1)).field_146126_j = I18n.func_135052_a("enum.statuetex." + t.toString().toLowerCase(), new Object[0]);
         this.statue.setTextureType(t);
         Pixelmon.network.sendToServer(new StatuePacketServer(EnumStatuePacketMode.SetTextureType, statueId, t.toString()));
      }

   }
}
