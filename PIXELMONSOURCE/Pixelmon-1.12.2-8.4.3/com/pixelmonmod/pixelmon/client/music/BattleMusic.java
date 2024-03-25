package com.pixelmonmod.pixelmon.client.music;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.sounds.BattleMusicType;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class BattleMusic {
   private static PositionedSoundRecord song;

   public static void startBattleMusic(BattleMusicType type, int index, long playtime, boolean repeat) {
      if (playtime == -1L) {
         endBattleMusic();
      } else {
         Minecraft mc = Minecraft.func_71410_x();
         VoidMusicTicker.replaceMusicTicker();
         if (isPlaying()) {
            mc.func_147118_V().func_147683_b(song);
            song = null;
         }

         SoundEvent soundEvent = (SoundEvent)PixelSounds.battleMusics.get(type);
         PositionedSoundRecord record = new FixedTrackPositionSoundRecord(soundEvent, index, SoundCategory.MUSIC, PixelmonConfig.battleMusicVolume, 1.0F, repeat, 0, AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
         song = record;
         mc.func_147118_V().func_147682_a(record);
      }
   }

   public static void endBattleMusic() {
      Minecraft mc = Minecraft.func_71410_x();
      if (isPlaying()) {
         MusicHelper.fadeSoundToStop(song, 2000L, VoidMusicTicker::restoreMusicTicker);
      } else if (mc.func_181535_r() instanceof VoidMusicTicker) {
         VoidMusicTicker.restoreMusicTicker();
      }

      song = null;
   }

   public static boolean isPlaying() {
      return song != null && MusicHelper.getSoundHandler().func_147692_c(song);
   }

   private static class FixedTrackPositionSoundRecord extends PositionedSoundRecord {
      int index;

      public FixedTrackPositionSoundRecord(SoundEvent soundIn, int index, SoundCategory categoryIn, float volumeIn, float pitchIn, boolean repeatIn, int repeatDelayIn, ISound.AttenuationType attenuationTypeIn, float xIn, float yIn, float zIn) {
         super(soundIn.func_187503_a(), categoryIn, volumeIn, pitchIn, repeatIn, repeatDelayIn, attenuationTypeIn, xIn, yIn, zIn);
         this.index = index;
      }

      public SoundEventAccessor func_184366_a(SoundHandler handler) {
         SoundEventAccessor accessor = super.func_184366_a(handler);
         if (this.index >= 0) {
            List accessorList = (List)ReflectionHelper.getPrivateValue(SoundEventAccessor.class, accessor, "accessorList", "field_188716_a");
            if (accessorList.size() > this.index) {
               this.field_184367_a = (Sound)((ISoundEventAccessor)accessorList.get(this.index)).func_148720_g();
            }
         }

         return accessor;
      }
   }
}
