package com.pixelmonmod.pixelmon.storage.playerData;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.battles.CatchComboEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;

public class CaptureCombo {
   private EnumSpecies lastCapture;
   private int captureCount;

   public void onCapture(EntityPlayerMP player, EnumSpecies species) {
      if (PixelmonConfig.allowCatchCombo) {
         if (this.lastCapture == species) {
            ++this.captureCount;
         } else {
            this.captureCount = 1;
         }

         this.lastCapture = species;
         Pixelmon.EVENT_BUS.post(new CatchComboEvent.ComboIncrement(player, this.lastCapture, this.captureCount));
      } else {
         this.clearCombo();
      }

   }

   public int getCurrentCombo() {
      return this.captureCount;
   }

   public EnumSpecies getCurrentSpecies() {
      return this.lastCapture;
   }

   public void clearCombo() {
      this.lastCapture = null;
      this.captureCount = 0;
   }

   public int getCurrentThreshold() {
      for(int i = 0; i < PixelmonConfig.catchComboThresholds.size(); ++i) {
         if (this.captureCount <= (Integer)PixelmonConfig.catchComboThresholds.get(i)) {
            return i;
         }
      }

      return PixelmonConfig.catchComboThresholds.size();
   }

   public float getExpBouns() {
      int threshold = this.getCurrentThreshold();
      return PixelmonConfig.catchComboExpBonuses.size() > threshold ? (Float)PixelmonConfig.catchComboExpBonuses.get(threshold) : 1.0F;
   }

   public float getShinyModifier() {
      int threshold = this.getCurrentThreshold();
      return PixelmonConfig.catchComboShinyModifiers.size() > threshold ? (Float)PixelmonConfig.catchComboShinyModifiers.get(threshold) : 1.0F;
   }

   public int getPerfIVCount() {
      int threshold = this.getCurrentThreshold();
      return PixelmonConfig.catchComboPerfectIVs.size() > threshold ? (Integer)PixelmonConfig.catchComboPerfectIVs.get(threshold) : 0;
   }
}
