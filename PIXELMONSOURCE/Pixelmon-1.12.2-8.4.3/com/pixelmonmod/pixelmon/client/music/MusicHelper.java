package com.pixelmonmod.pixelmon.client.music;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import paulscode.sound.SoundSystem;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(
   value = {Side.CLIENT},
   modid = "pixelmon"
)
public class MusicHelper {
   private static MusicTicker mcMusicTicker;
   private static SoundHandler mcSoundHandler;
   private static SoundManager mcSoundManager;
   private static SoundSystem soundSystem;
   private static Map invPlayingSounds;

   @SubscribeEvent
   public static void onSoundReloadEvent(SoundLoadEvent event) {
      if (Loader.instance().getLoaderState().ordinal() > LoaderState.INITIALIZATION.ordinal()) {
         CompletableFuture future = new CompletableFuture();
         ScheduledFuture scheduledFuture = ClientProxy.executor.scheduleWithFixedDelay(() -> {
            if ((Boolean)ReflectionHelper.getPrivateValue(SoundManager.class, mcSoundManager, "loaded", "field_148617_f")) {
               init(Minecraft.func_71410_x());
               future.complete((Object)null);
            }

         }, 10L, 10L, TimeUnit.MILLISECONDS);
         future.thenAccept((v) -> {
            scheduledFuture.cancel(false);
         });
      }
   }

   public static void init(Minecraft mc) {
      if (mcMusicTicker == null) {
         mcMusicTicker = mc.func_181535_r();
      }

      mcSoundHandler = mc.func_147118_V();
      mcSoundManager = (SoundManager)ReflectionHelper.getPrivateValue(SoundHandler.class, mcSoundHandler, "sndManager", "field_147694_f");
      soundSystem = (SoundSystem)ReflectionHelper.getPrivateValue(SoundManager.class, mcSoundManager, "sndSystem", "field_148620_e");
      invPlayingSounds = (Map)ReflectionHelper.getPrivateValue(SoundManager.class, mcSoundManager, "invPlayingSounds", "field_148630_i");
   }

   public static MusicTicker getMusicTicker() {
      return mcMusicTicker;
   }

   public static SoundHandler getSoundHandler() {
      return mcSoundHandler;
   }

   public static SoundManager getSoundManager() {
      return mcSoundManager;
   }

   public static SoundSystem getSoundSystem() {
      return soundSystem;
   }

   public static void fadeSoundToStop(ISound sound, long milis) {
      if (!sound.func_184364_b().func_188723_h()) {
         mcSoundManager.func_148602_b(sound);
      } else {
         if (mcSoundManager.func_148597_a(sound)) {
            fadeSoundToStopInternal(sound, milis, (Runnable)null);
         }

      }
   }

   public static void fadeSoundToStop(ISound sound, long milis, Runnable runnable) {
      if (!sound.func_184364_b().func_188723_h()) {
         mcSoundManager.func_148602_b(sound);
         runnable.run();
      } else {
         if (mcSoundManager.func_148597_a(sound)) {
            fadeSoundToStopInternal(sound, milis, runnable);
         } else {
            runnable.run();
         }

      }
   }

   private static void fadeSoundToStopInternal(ISound sound, long millis, Runnable runnable) {
      try {
         try {
            soundSystem.fadeOut(getInternalId(sound), (String)null, millis);
         } catch (Exception var5) {
            if (Pixelmon.devEnvironment) {
               var5.printStackTrace();
            }
         }

         ClientProxy.executor.schedule(() -> {
            mcSoundManager.func_148602_b(sound);
            if (runnable != null) {
               runnable.run();
            }

         }, millis, TimeUnit.MILLISECONDS);
      } catch (Exception var6) {
         mcSoundManager.func_148602_b(sound);
         if (runnable != null) {
            runnable.run();
         }
      }

   }

   public static void pause(ISound sound) {
      if (mcSoundManager.func_148597_a(sound)) {
         try {
            soundSystem.pause(getInternalId(sound));
         } catch (Exception var2) {
            if (Pixelmon.devEnvironment) {
               var2.printStackTrace();
            }
         }
      }

   }

   public static void resume(ISound sound) {
      try {
         soundSystem.play(getInternalId(sound));
      } catch (Exception var2) {
         if (Pixelmon.devEnvironment) {
            var2.printStackTrace();
         }
      }

   }

   @Nullable
   public static String getInternalId(ISound sound) {
      return (String)invPlayingSounds.get(sound);
   }
}
