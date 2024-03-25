package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import java.io.IOException;
import java.util.Optional;

public class AttackTypeAdapter extends TypeAdapter {
   public void write(JsonWriter out, Attack value) throws IOException {
      out.value(value.getActualMove().getAttackName());
   }

   public Attack read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
         return null;
      } else {
         String name = in.nextString();
         Optional optional = AttackBase.getAttackBaseFromEnglishName(name);
         if (optional.isPresent()) {
            return new Attack((AttackBase)optional.get());
         } else {
            Pixelmon.LOGGER.warn("Attack name invalid: " + name);
            return new Attack("Tackle");
         }
      }
   }
}
