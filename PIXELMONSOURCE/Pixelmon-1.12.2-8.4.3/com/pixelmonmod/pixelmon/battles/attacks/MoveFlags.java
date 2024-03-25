package com.pixelmonmod.pixelmon.battles.attacks;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class MoveFlags {
   public final boolean authentic;
   public final boolean sound;
   public static final transient MoveFlags DEFAULT = new MoveFlags();
   public static final Adapter ADAPTER = new Adapter();

   private MoveFlags() {
      this.authentic = false;
      this.sound = false;
   }

   private MoveFlags(boolean authentic, boolean sound) {
      this.authentic = authentic;
      this.sound = sound;
   }

   // $FF: synthetic method
   MoveFlags(Object x0) {
      this();
   }

   // $FF: synthetic method
   MoveFlags(boolean x0, boolean x1, Object x2) {
      this(x0, x1);
   }

   public static class Adapter extends TypeAdapter {
      public void write(JsonWriter out, MoveFlags value) throws IOException {
         out.beginObject();
         if (value.authentic) {
            out.name("authentic");
            out.value(true);
         }

         if (value.sound) {
            out.name("sound");
            out.value(true);
         }

         out.endObject();
      }

      public MoveFlags read(JsonReader reader) throws IOException {
         reader.beginObject();
         boolean authentic = false;
         boolean sound = false;

         while(reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token != JsonToken.NAME) {
               return new MoveFlags();
            }

            String fieldname = reader.nextName();
            if (fieldname.equals("authentic")) {
               authentic = reader.nextBoolean();
            } else if (fieldname.equals("sound")) {
               sound = reader.nextBoolean();
            }
         }

         reader.endObject();
         return new MoveFlags(authentic, sound);
      }
   }
}
