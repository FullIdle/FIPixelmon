package com.pixelmonmod.pixelmon.client.gui;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class GuiExtrasEditor extends GuiScreenDropDown {
   private final PixelExtrasData client;
   private int centerW;
   private int centerH;
   private float oldMouseX;
   private float oldMouseY;

   public GuiExtrasEditor() {
      this.centerW = this.field_146294_l / 2;
      this.centerH = this.field_146295_m / 2;
      this.field_146297_k = Minecraft.func_71410_x();
      this.client = PlayerExtraDataStore.get((EntityPlayer)this.field_146297_k.field_71439_g);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      this.field_146293_o.clear();
      this.centerW = this.field_146294_l / 2;
      this.centerH = this.field_146295_m / 2;
      ArrayList list;
      if (this.client.getAvailableHats().size() > 0) {
         this.field_146293_o.add(new GuiPixelLabel(I18n.func_135052_a("pixelmon.cosmetic.hat", new Object[0]), 0, this.centerW - 180, this.centerH - 100, 80, 30, 16777215));
         list = Lists.newArrayList(new PixelExtrasData.HatType[]{PixelExtrasData.HatType.NONE});
         list.addAll(this.client.getAvailableHats());
         this.addDropDown((new GuiDropDown(list, this.client.getHatType(), this.centerW - 180, this.centerH - 80, 80, 100)).setGetOptionString((hat) -> {
            return I18n.func_135052_a("pixelmon.cosmetic.hat." + hat.name().toLowerCase().replace("_", ""), new Object[0]);
         }).setOnSelected((hat) -> {
            this.client.setHatType(hat);
            this.client.setEnabled(PixelExtrasData.Category.HAT, true);
         }));
      }

      if (this.client.getAvailableSashs().size() > 0) {
         this.field_146293_o.add(new GuiPixelLabel(I18n.func_135052_a("pixelmon.cosmetic.sash", new Object[0]), 0, this.centerW - 180, this.centerH - 40, 80, 30, 16777215));
         list = Lists.newArrayList(new PixelExtrasData.SashType[]{PixelExtrasData.SashType.NONE});
         list.addAll(this.client.getAvailableSashs());
         this.addDropDown((new GuiDropDown(list, this.client.getSashType(), this.centerW - 180, this.centerH - 20, 80, 100)).setGetOptionString((sash) -> {
            return I18n.func_135052_a("pixelmon.cosmetic.sash." + sash.name().toLowerCase().replace("_", ""), new Object[0]);
         }).setOnSelected((sash) -> {
            this.client.setSashType(sash);
            this.client.setEnabled(PixelExtrasData.Category.SASH, true);
         }));
      }

      if (this.client.getAvailableRobes().size() > 0) {
         this.field_146293_o.add(new GuiPixelLabel(I18n.func_135052_a("pixelmon.cosmetic.robe", new Object[0]), 0, this.centerW + 100, this.centerH - 100, 80, 30, 16777215));
         list = Lists.newArrayList(new PixelExtrasData.RobeType[]{PixelExtrasData.RobeType.NONE});
         list.addAll(this.client.getAvailableRobes());
         this.addDropDown((new GuiDropDown(list, this.client.getRobeType(), this.centerW + 100, this.centerH - 80, 80, 100)).setGetOptionString((robe) -> {
            return I18n.func_135052_a("pixelmon.cosmetic.robe." + robe.name().toLowerCase().replace("_", ""), new Object[0]);
         }).setOnSelected((robe) -> {
            this.client.setRobeType(robe);
            this.client.setEnabled(PixelExtrasData.Category.ROBE, true);
         }));
      }

      if (this.client.getAvailableMonocles().size() > 0) {
         this.field_146293_o.add(new GuiPixelLabel(I18n.func_135052_a("pixelmon.cosmetic.monocle", new Object[0]), 0, this.centerW + 100, this.centerH - 40, 80, 30, 16777215));
         list = Lists.newArrayList(new PixelExtrasData.MonocleType[]{PixelExtrasData.MonocleType.NONE});
         list.addAll(this.client.getAvailableMonocles());
         this.addDropDown((new GuiDropDown(list, this.client.getMonocleType(), this.centerW + 100, this.centerH - 20, 80, 100)).setGetOptionString((monocle) -> {
            return I18n.func_135052_a("pixelmon.cosmetic.monocle." + monocle.name().toLowerCase().replace("_", ""), new Object[0]);
         }).setOnSelected((monocle) -> {
            this.client.setMonocleType(monocle);
            this.client.setEnabled(PixelExtrasData.Category.MONOCLE, true);
         }));
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiInventory.func_147046_a(this.centerW, this.centerH + this.centerH / 2, 80, (float)this.centerW - this.oldMouseX, (float)this.centerH - this.oldMouseY, this.field_146297_k.field_71439_g);
      super.func_73863_a(mouseX, mouseY, partialTicks);
      this.oldMouseX = (float)mouseX;
      this.oldMouseY = (float)mouseY;
   }

   protected void drawBackgroundUnderMenus(float partialTicks, int mouseX, int mouseY) {
   }

   public void func_146281_b() {
      PlayerExtraDataStore.saveAndSend();
   }

   public boolean func_73868_f() {
      return false;
   }
}
