package com.pixelmonmod.pixelmon.quests.client.rendering;

import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import com.pixelmonmod.pixelmon.quests.comm.QuestMarker;
import net.minecraft.util.ResourceLocation;

public class QuestResources {
   private static final GenericModelHolder marker_exclamation = new GenericModelHolder("quests/marker_exclamation.pqc");
   private static final GenericModelHolder marker_question = new GenericModelHolder("quests/marker_question.pqc");
   private static final ResourceLocation marker_exclamation_tex = new ResourceLocation("pixelmon", "textures/quests/exclamation.png");
   private static final ResourceLocation marker_question_tex = new ResourceLocation("pixelmon", "textures/quests/question.png");

   public static GenericModelHolder getMarkerModel(QuestMarker marker) {
      switch (marker.type) {
         case QUESTION:
            return marker_question;
         case EXCLAMATION:
            return marker_exclamation;
         default:
            return marker_exclamation;
      }
   }

   public static ResourceLocation getMarkerTexture(QuestProgressClient quest, QuestMarker marker) {
      switch (marker.type) {
         case QUESTION:
            return marker_question_tex;
         case EXCLAMATION:
            return marker_exclamation_tex;
         default:
            return marker_exclamation_tex;
      }
   }
}
