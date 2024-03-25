package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.comm.packetHandlers.ServerConfigList;

public class PixelmonServerConfig {
   public static int maxLevel;
   public static boolean allowCapturingOutsideBattle;
   public static boolean afkHandlerOn;
   public static boolean renderWildLevels;
   public static int afkTimerActivateSeconds;
   public static float ridingSpeedMultiplier;
   public static boolean allowShinyCharmFromPokedex;
   public static boolean allowExternalMoves;
   public static boolean universalTMs;
   public static boolean superUniversalTMs;

   public static void updateFromServer(ServerConfigList message) {
      maxLevel = message.maxLevel;
      renderWildLevels = message.renderWildLevels;
      allowCapturingOutsideBattle = message.allowCapturingOutsideBattle;
      afkHandlerOn = message.afkHandlerOn;
      afkTimerActivateSeconds = message.afkTimerActivateSeconds;
      ridingSpeedMultiplier = message.ridingSpeedMultiplier;
      allowShinyCharmFromPokedex = message.allowShinyCharmFromPokedex;
      allowExternalMoves = message.allowExternalMoves;
      universalTMs = message.universalTMs;
      superUniversalTMs = message.superUniversalTMs;
   }

   public static void load() {
      maxLevel = PixelmonConfig.maxLevel;
      allowCapturingOutsideBattle = PixelmonConfig.allowCapturingOutsideBattle;
      afkHandlerOn = PixelmonConfig.afkHandlerOn;
      renderWildLevels = PixelmonConfig.renderWildLevels;
      afkTimerActivateSeconds = PixelmonConfig.afkTimerActivateSeconds;
      ridingSpeedMultiplier = PixelmonConfig.ridingSpeedMultiplier;
      allowShinyCharmFromPokedex = PixelmonConfig.allowShinyCharmFromPokedex;
      universalTMs = PixelmonConfig.universalTMs;
      superUniversalTMs = PixelmonConfig.superUniversalTMs;
   }

   static {
      maxLevel = PixelmonConfig.maxLevel;
      allowCapturingOutsideBattle = PixelmonConfig.allowCapturingOutsideBattle;
      afkHandlerOn = PixelmonConfig.afkHandlerOn;
      renderWildLevels = PixelmonConfig.renderWildLevels;
      afkTimerActivateSeconds = PixelmonConfig.afkTimerActivateSeconds;
      ridingSpeedMultiplier = PixelmonConfig.ridingSpeedMultiplier;
      allowShinyCharmFromPokedex = PixelmonConfig.allowShinyCharmFromPokedex;
      allowExternalMoves = PixelmonConfig.allowExternalMoves;
      universalTMs = PixelmonConfig.universalTMs;
      superUniversalTMs = PixelmonConfig.superUniversalTMs;
   }
}
