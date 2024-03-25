package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class WeatherChanger {
   public static boolean allowMultiplayerWeather = false;

   public static MoveSkill createRainMoveSkill() {
      MoveSkill moveSkill = createBaseMoveSkill("rain_dance", true).setName("pixelmon.moveskill.rain_dance.name").describe("pixelmon.moveskill.rain_dance.description1", "pixelmon.moveskill.rain_dance.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/raindance.png")).setAnyMoves("Rain Dance").setUsePP(true).setIntrinsicSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("Kyogre")}));
      return moveSkill;
   }

   public static MoveSkill createSunMoveSkill() {
      MoveSkill moveSkill = createBaseMoveSkill("sunny_day", false).setName("pixelmon.moveskill.sunny_day.name").describe("pixelmon.moveskill.sunny_day.description1", "pixelmon.moveskill.sunny_day.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/sunnyday.png")).setAnyMoves("Sunny Day").setUsePP(true).setIntrinsicSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("Groudon")}));
      return moveSkill;
   }

   private static MoveSkill createBaseMoveSkill(String id, boolean rain) {
      MoveSkill moveSkill = new MoveSkill(id);
      moveSkill.setDefaultCooldownTicks(6000);
      moveSkill.setBehaviourNoTarget((pixelmon) -> {
         if (!allowMultiplayerWeather && pixelmon.field_70170_p.func_73046_m().func_184103_al().func_72394_k() > 1 && pixelmon.field_70170_p.func_73046_m() instanceof DedicatedServer) {
            ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.RED, "pixelmon.moveskill.weather.nomultiplayer");
            return -1;
         } else {
            if (rain) {
               if (!pixelmon.field_70170_p.func_175678_i(pixelmon.func_180425_c())) {
                  ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.RED, "pixelmon.moveskill.weather.underground", pixelmon.func_145748_c_());
                  return -1;
               }

               if (pixelmon.field_70170_p.func_72896_J()) {
                  ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.RED, "pixelmon.moveskill.rain_dance.alreadyraining");
                  return -1;
               }
            } else {
               if (!pixelmon.field_70170_p.func_175678_i(pixelmon.func_180425_c())) {
                  ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.RED, "pixelmon.moveskill.weather.underground", pixelmon.func_145748_c_());
                  return -1;
               }

               if (!pixelmon.field_70170_p.func_72896_J()) {
                  ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.RED, "pixelmon.moveskill.sunny_day.alreadysunny");
                  return -1;
               }
            }

            pixelmon.field_70170_p.func_72912_H().func_76084_b(rain);
            if (pixelmon.getPokemonData().getSpecies() == EnumSpecies.Kyogre && rain) {
               pixelmon.field_70170_p.func_72912_H().func_76069_a(true);
               ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.WHITE, "pixelmon.moveskill.rain_dance.kyogreused", pixelmon.func_145748_c_());
            } else if (rain) {
               ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.WHITE, "pixelmon.moveskill.rain_dance.used");
            } else {
               ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.WHITE, "pixelmon.moveskill.sunny_day.used");
            }

            return moveSkill.cooldownTicks;
         }
      });
      return moveSkill;
   }
}
