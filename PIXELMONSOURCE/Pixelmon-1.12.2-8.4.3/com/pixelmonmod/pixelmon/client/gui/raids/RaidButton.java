package com.pixelmonmod.pixelmon.client.gui.raids;

import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.client.SoundHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import java.awt.Color;
import java.util.function.Consumer;

public class RaidButton {
   private final String text;
   private final RaidData raid;
   private final Consumer onClick;
   private final double x;
   private final double y;
   private final double z;
   private final double w;
   private final double h;
   private boolean visible = true;
   private boolean enabled = true;

   public RaidButton(double x, double y, double z, double w, double h, String text, RaidData raid, Consumer onClick) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
      this.h = h;
      this.text = text;
      this.raid = raid;
      this.onClick = onClick;
   }

   public void draw(double mouseX, double mouseY, float partialTicks) {
      if (this.visible) {
         boolean hover = this.isWithin(mouseX, mouseY);
         GuiHelper.drawRectWithSemicircleEnds(this.x, this.y, this.z, this.w, this.h, 20, this.isInteractable() ? (hover ? Color.BLACK : Color.WHITE) : Color.WHITE);
         GuiHelper.drawScaledCenteredString(this.text, (float)(this.x + this.w / 2.0), (float)(this.y + this.h * 0.255), this.isInteractable() ? (hover ? Color.WHITE.getRGB() : Color.BLACK.getRGB()) : Color.GRAY.getRGB(), (float)this.h * 1.2F);
      }

   }

   public boolean isWithin(double x, double y) {
      double r = this.h / 2.0;
      return x >= this.x - r && y >= this.y && x <= this.x + this.w + r && y <= this.y + this.h;
   }

   public void onClick(double mouseX, double mouseY, int button) {
      if (this.isInteractable() && this.isWithin(mouseX, mouseY)) {
         this.onClick.accept(button);
         SoundHelper.playButtonPressSound();
      }

   }

   public boolean isInteractable() {
      return this.enabled && this.visible;
   }

   public RaidButton setVisible(boolean visible) {
      this.visible = visible;
      return this;
   }

   public RaidButton setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }
}
