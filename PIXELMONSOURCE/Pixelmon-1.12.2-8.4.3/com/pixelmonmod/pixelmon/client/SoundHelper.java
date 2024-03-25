package com.pixelmonmod.pixelmon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SoundHelper {
   public static void playButtonPressSound() {
      playSound(SoundEvents.field_187909_gi);
   }

   public static void playSound(SoundEvent event) {
      playSound(event, 1.0F, 1.0F);
   }

   public static void playSound(SoundEvent event, float volume, float pitch) {
      Minecraft mc = Minecraft.func_71410_x();
      if (mc.func_175606_aa() != null) {
         mc.field_71441_e.func_184156_a(mc.func_175606_aa().func_180425_c(), event, SoundCategory.MASTER, volume, pitch, false);
      } else {
         SoundHandler soundHandler = mc.func_147118_V();
         soundHandler.func_147682_a(PositionedSoundRecord.func_184371_a(event, pitch));
      }

   }
}
