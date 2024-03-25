package com.pixelmonmod.pixelmon.sounds;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

public class PixelSounds {
   public static SoundEvent pc;
   public static SoundEvent pokelootObtained;
   public static SoundEvent cameraShutter;
   public static SoundEvent healerActive;
   public static SoundEvent pokeballClose;
   public static SoundEvent pokeballRelease;
   public static SoundEvent pokeballCapture;
   public static SoundEvent pokeballCaptureSuccess;
   public static SoundEvent bellRing;
   public static SoundEvent ultraWormhole;
   public static SoundEvent ultraSpaceAmbient;
   public static EnumMap pixelmonSounds;
   public static Map pixelmonFormSounds;
   public static EnumMap battleMusics;
   public static MusicTicker.MusicType ULTRA_SPACE_MUSIC_TYPE;

   public static void registerSounds(IForgeRegistry registry) {
      String prefix = "pixelmon:";
      pc = registerSound(registry, prefix + "pixelmon.block.PC");
      pokelootObtained = registerSound(registry, prefix + "pixelmon.block.PokelootObtained");
      cameraShutter = registerSound(registry, prefix + "pixelmon.item.CameraShutter");
      healerActive = registerSound(registry, prefix + "pixelmon.block.healerActivate");
      pokeballClose = registerSound(registry, prefix + "pixelmon.block.PokeballClose");
      pokeballRelease = registerSound(registry, prefix + "pixelmon.block.PokeballRelease");
      pokeballCapture = registerSound(registry, prefix + "pixelmon.block.PokeballCapture");
      pokeballCaptureSuccess = registerSound(registry, prefix + "pixelmon.block.PokeballCaptureSuccess");
      bellRing = registerSound(registry, prefix + "pixelmon.block.bellRing");
      ultraWormhole = registerSound(registry, prefix + "pixelmon.item.UltraWormhole");
      ultraSpaceAmbient = registerSound(registry, prefix + "pixelmon.music.ultraspace");
      battleMusics = new EnumMap(BattleMusicType.class);
      BattleMusicType[] var2 = BattleMusicType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BattleMusicType type = var2[var4];
         battleMusics.put(type, registerSound(registry, prefix + "pixelmon.music.battle." + type.name().toLowerCase()));
      }

      pixelmonSounds = new EnumMap(EnumSpecies.class);
      pixelmonFormSounds = new HashMap();
      registerCries(registry);
      if (ULTRA_SPACE_MUSIC_TYPE == null && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
         ULTRA_SPACE_MUSIC_TYPE = (MusicTicker.MusicType)EnumHelper.addEnum(MusicTicker.MusicType.class, "ULTRA_SPACE", new Class[]{SoundEvent.class, Integer.TYPE, Integer.TYPE}, new Object[]{ultraSpaceAmbient, 0, 200});
      }

   }

   private static SoundEvent registerSound(IForgeRegistry registry, String soundNameIn) {
      ResourceLocation resourcelocation = new ResourceLocation(soundNameIn);
      SoundEvent event = (SoundEvent)(new SoundEvent(resourcelocation)).setRegistryName(resourcelocation);
      registry.register(event);
      return event;
   }

   private static void registerCries(IForgeRegistry registry) {
      String prefix = "pixelmon:pixelmon.mob.";
      InputStream istream = Pixelmon.proxy.getStreamForResourceLocation(new ResourceLocation("pixelmon:sounds.json"));
      BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(istream));
      JsonObject json = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
      EnumSpecies[] var5 = EnumSpecies.values();
      int var6 = var5.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         EnumSpecies species = var5[var7];
         String key = "pixelmon.mob." + species.name.toLowerCase();
         SoundEvent event;
         if (json.has(key)) {
            event = registerSound(registry, prefix + species.name.toLowerCase());
            pixelmonSounds.putIfAbsent(species, Maps.newEnumMap(BaseStats.SoundType.class));
            ((Map)pixelmonSounds.get(species)).put(BaseStats.SoundType.Neutral, event);
         }

         if (json.has(key + "F")) {
            event = registerSound(registry, prefix + species.name.toLowerCase() + "F");
            pixelmonSounds.putIfAbsent(species, Maps.newEnumMap(BaseStats.SoundType.class));
            ((Map)pixelmonSounds.get(species)).put(BaseStats.SoundType.Female, event);
         }

         if (json.has(key + "M")) {
            event = registerSound(registry, prefix + species.name.toLowerCase() + "M");
            pixelmonSounds.putIfAbsent(species, Maps.newEnumMap(BaseStats.SoundType.class));
            ((Map)pixelmonSounds.get(species)).put(BaseStats.SoundType.Male, event);
         }

         Iterator var18 = species.getPossibleForms(false).iterator();

         while(var18.hasNext()) {
            IEnumForm form = (IEnumForm)var18.next();
            if (!form.getFormSuffix().isEmpty()) {
               String suffix = form.getFormSuffix().startsWith("-") ? form.getFormSuffix().substring(1) : form.getFormSuffix();
               if (json.has(key + "." + suffix)) {
                  SoundEvent event = registerSound(registry, prefix + species.name.toLowerCase() + "." + suffix);
                  pixelmonFormSounds.putIfAbsent(species.getPokemonName() + form.getFormSuffix(), Maps.newEnumMap(BaseStats.SoundType.class));
                  ((Map)pixelmonFormSounds.get(species.getPokemonName() + form.getFormSuffix())).put(BaseStats.SoundType.Neutral, event);
               }
            }
         }
      }

      if (Pixelmon.devEnvironment) {
         Set cries = (Set)json.entrySet().stream().map(Map.Entry::getKey).filter((s) -> {
            return s.startsWith("pixelmon.mob");
         }).collect(Collectors.toSet());
         EnumSpecies[] var15 = EnumSpecies.values();
         var7 = var15.length;

         for(int var16 = 0; var16 < var7; ++var16) {
            EnumSpecies species = var15[var16];
            String key = "pixelmon.mob." + species.name.toLowerCase();
            cries.remove(key);
            cries.remove(key + "F");
            cries.remove(key + "M");
            Iterator var20 = species.getPossibleForms(false).iterator();

            while(var20.hasNext()) {
               IEnumForm form = (IEnumForm)var20.next();
               if (!form.getFormSuffix().isEmpty()) {
                  String suffix = form.getFormSuffix().startsWith("-") ? form.getFormSuffix().substring(1) : form.getFormSuffix();
                  cries.remove(key + "." + suffix);
               }
            }
         }

         cries.forEach((cry) -> {
            Pixelmon.LOGGER.warn("Found unlinked sound in sounds.json key: " + cry);
         });
      }

   }

   public static void linkPixelmonSounds() {
      EnumSpecies[] var0 = EnumSpecies.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumSpecies species = var0[var2];
         BaseStats stats = species.getBaseStats();
         if (pixelmonSounds.containsKey(species)) {
            Map map = (Map)pixelmonSounds.get(species);
            if (map.containsKey(BaseStats.SoundType.Neutral)) {
               stats.addSound(BaseStats.SoundType.Neutral, (SoundEvent)map.get(BaseStats.SoundType.Neutral));
            }

            if (map.containsKey(BaseStats.SoundType.Female)) {
               stats.addSound(BaseStats.SoundType.Female, (SoundEvent)map.get(BaseStats.SoundType.Female));
            }

            if (map.containsKey(BaseStats.SoundType.Male)) {
               stats.addSound(BaseStats.SoundType.Male, (SoundEvent)map.get(BaseStats.SoundType.Male));
            }
         }

         Iterator var7 = species.getPossibleForms(false).iterator();

         while(var7.hasNext()) {
            IEnumForm form = (IEnumForm)var7.next();
            if (!form.getFormSuffix().isEmpty() && pixelmonFormSounds.containsKey(species.getPokemonName() + form.getFormSuffix())) {
               species.getBaseStats(form).addSound(BaseStats.SoundType.Neutral, (SoundEvent)((Map)pixelmonFormSounds.get(species.getPokemonName() + form.getFormSuffix())).get(BaseStats.SoundType.Neutral));
            }
         }
      }

   }
}
