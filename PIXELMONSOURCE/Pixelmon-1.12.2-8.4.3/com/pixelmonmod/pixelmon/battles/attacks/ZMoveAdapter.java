package com.pixelmonmod.pixelmon.battles.attacks;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class ZMoveAdapter implements JsonSerializer, JsonDeserializer {
   public JsonElement serialize(ZMove zMove, Type type, JsonSerializationContext ctx) {
      return ctx.serialize(zMove);
   }

   public ZMove deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      JsonObject jo = json.getAsJsonObject();
      ArrayList effects = new ArrayList();
      Iterator var6 = jo.get("effects").getAsJsonArray().iterator();

      while(var6.hasNext()) {
         JsonElement element = (JsonElement)var6.next();
         effects.add(EffectTypeAdapter.ADAPTER.deserialize(element, EffectBase.class, ctx));
      }

      ArrayList allowedPokemon = new ArrayList();
      Iterator var10 = jo.get("allowedPokemon").getAsJsonArray().iterator();

      while(var10.hasNext()) {
         JsonElement element = (JsonElement)var10.next();
         allowedPokemon.add(element.getAsString());
      }

      ZMove move = new ZMove(jo.get("crystal").getAsString(), jo.get("attackName").getAsString(), jo.get("basePower").getAsInt(), effects, allowedPokemon);
      return move;
   }
}
