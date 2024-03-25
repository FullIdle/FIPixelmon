package com.pixelmonmod.pixelmon.client.music;

import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VoidMusicTicker extends MusicTicker {
   public static MusicTicker mcMusicTicker = Minecraft.func_71410_x().func_181535_r();
   private static ISound pausedMusic;

   public VoidMusicTicker(Minecraft mcIn) {
      super(mcIn);
   }

   public void func_73660_a() {
   }

   public static void replaceMusicTicker() {
      Minecraft mc = Minecraft.func_71410_x();
      if (!(mc.func_181535_r() instanceof VoidMusicTicker)) {
         mcMusicTicker = mc.func_181535_r();
      }

      ReflectionHelper.setPrivateValue(Minecraft.class, mc, new VoidMusicTicker(mc), "mcMusicTicker", "field_147126_aw");
      ISound music = getCurrentMusic();
      if (music != null && mc.func_147118_V().func_147692_c(music)) {
         pausedMusic = music;
         MusicHelper.pause(music);
      }

   }

   public static void restoreMusicTicker() {
      ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.func_71410_x(), mcMusicTicker, "mcMusicTicker", "field_147126_aw");
      if (pausedMusic != null) {
         MusicHelper.resume(pausedMusic);
         pausedMusic = null;
      }

   }

   public static ISound getCurrentMusic() {
      return mcMusicTicker != null ? (ISound)ReflectionHelper.getPrivateValue(MusicTicker.class, mcMusicTicker, "currentMusic", "field_147678_c") : null;
   }
}
