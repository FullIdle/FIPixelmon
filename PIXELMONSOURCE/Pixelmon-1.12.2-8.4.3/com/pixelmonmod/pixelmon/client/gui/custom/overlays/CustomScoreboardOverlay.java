package com.pixelmonmod.pixelmon.client.gui.custom.overlays;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import java.util.Collection;
import net.minecraft.client.gui.ScaledResolution;

public class CustomScoreboardOverlay {
   private static boolean enabled = false;
   private static String title;
   private static Collection scoreboardLines;
   private static Collection scores;
   private static ScoreboardLocation location;

   public static void draw(ScaledResolution scaledResolution) {
      switch (location) {
         case RIGHT_TOP:
            GuiHelper.drawScoreboard(3, ScoreboardJustification.RIGHT, scaledResolution.func_78326_a(), 1677721600, title, scoreboardLines, scores);
            break;
         case RIGHT_MIDDLE:
            GuiHelper.drawScoreboard(scaledResolution.func_78328_b() / 2 - (scoreboardLines.size() + 1) * 10 / 2, ScoreboardJustification.RIGHT, scaledResolution.func_78326_a(), 1677721600, title, scoreboardLines, scores);
            break;
         case RIGHT_BOTTOM:
            GuiHelper.drawScoreboard(scaledResolution.func_78328_b() - (scoreboardLines.size() + 1) * 10 - 75, ScoreboardJustification.RIGHT, scaledResolution.func_78326_a(), 1677721600, title, scoreboardLines, scores);
            break;
         case CENTER_TOP:
            GuiHelper.drawScoreboard(3, ScoreboardJustification.CENTER, scaledResolution.func_78326_a() / 2, 1677721600, title, scoreboardLines, scores);
            break;
         case LEFT_TOP:
            GuiHelper.drawScoreboard(3, ScoreboardJustification.LEFT, 3, 1677721600, title, scoreboardLines, scores);
            break;
         case LEFT_MIDDLE:
            GuiHelper.drawScoreboard(scaledResolution.func_78328_b() / 2 - (scoreboardLines.size() + 1) * 10 / 2, ScoreboardJustification.LEFT, 3, 1677721600, title, scoreboardLines, scores);
            break;
         case LEFT_BOTTOM:
            GuiHelper.drawScoreboard(scaledResolution.func_78328_b() - (scoreboardLines.size() + 1) * 10, ScoreboardJustification.LEFT, 3, 1677721600, title, scoreboardLines, scores);
      }

   }

   public static void populate(ScoreboardLocation location, String title, Collection scoreboardLines, Collection scores) {
      CustomScoreboardOverlay.location = location;
      CustomScoreboardOverlay.title = title;
      CustomScoreboardOverlay.scoreboardLines = scoreboardLines;
      CustomScoreboardOverlay.scores = scores;
   }

   public static void setLocation(ScoreboardLocation location) {
      CustomScoreboardOverlay.location = location;
   }

   public static void setEnabled(boolean enabled) {
      CustomScoreboardOverlay.enabled = enabled;
   }

   public static boolean isEnabled() {
      return enabled;
   }

   public static void resetBoard() {
      enabled = false;
      title = null;
      scoreboardLines = null;
      scores = null;
      location = null;
   }
}
