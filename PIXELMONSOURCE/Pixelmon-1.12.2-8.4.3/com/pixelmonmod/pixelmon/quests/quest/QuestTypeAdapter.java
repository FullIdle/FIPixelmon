package com.pixelmonmod.pixelmon.quests.quest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class QuestTypeAdapter implements JsonDeserializer {
   public static Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

   public Quest deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      JsonObject obj = (JsonObject)element;

      try {
         return (Quest)gson.fromJson(obj, Quest.standardQuest);
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }
}
