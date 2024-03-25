package com.pixelmonmod.pixelmon.quests.quest;

import net.minecraft.util.ResourceLocation;

public enum StageIcon {
   EXCLAMATION_MARK(new ResourceLocation("pixelmon", "textures/gui/quests/exclamation_mark.png")),
   QUESTION_MARK(new ResourceLocation("pixelmon", "textures/gui/quests/question_mark.png")),
   FOUR_POINTED_STAR(new ResourceLocation("pixelmon", "textures/gui/quests/four_pointed_star.png")),
   SPEECH_BUBBLE(new ResourceLocation("pixelmon", "textures/gui/quests/speech_bubble.png")),
   GEAR(new ResourceLocation("pixelmon", "textures/gui/quests/gear.png")),
   POKEBALL(new ResourceLocation("pixelmon", "textures/gui/quests/pokeball.png")),
   CLOCK(new ResourceLocation("pixelmon", "textures/gui/quests/clock.png"));

   private final ResourceLocation resource;

   private StageIcon(ResourceLocation resource) {
      this.resource = resource;
   }

   public ResourceLocation getResource() {
      return this.resource;
   }

   public static StageIcon getIcon(byte ordinal) {
      return ordinal >= 0 && ordinal < values().length ? values()[ordinal] : EXCLAMATION_MARK;
   }
}
