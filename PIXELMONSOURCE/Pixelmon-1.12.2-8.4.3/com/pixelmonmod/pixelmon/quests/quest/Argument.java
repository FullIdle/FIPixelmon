package com.pixelmonmod.pixelmon.quests.quest;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import java.util.function.Function;

public class Argument {
   private final String key;
   private final Function castFunction;
   private Object value;
   private Object defaultValue = null;
   private final boolean empty;

   private Argument(Object value) {
      this.key = null;
      this.castFunction = null;
      this.value = value;
      this.empty = false;
   }

   public static Argument of(Object value) {
      return new Argument(value);
   }

   private Argument(String key, Function castFunction) {
      if (key.startsWith("?") && key.endsWith("?")) {
         this.key = key.replace("?", "");
         this.castFunction = castFunction;
         this.value = null;
         this.empty = false;
      } else if (!key.isEmpty() && !key.equalsIgnoreCase("-")) {
         this.value = this.cast(castFunction, key);
         if (this.value instanceof String) {
            String potential = (String)this.value;
            if (potential.startsWith("?") && potential.endsWith("?")) {
               this.key = potential.replace("?", "");
               this.castFunction = castFunction;
               this.value = null;
               this.empty = false;
               return;
            }
         }

         this.key = null;
         this.castFunction = null;
         this.empty = false;
      } else {
         this.key = null;
         this.castFunction = null;
         this.value = null;
         this.empty = true;
      }

   }

   public static Argument from(String key, Function castFunction) {
      return new Argument(key, castFunction);
   }

   private Argument(String key, Function castFunction, Object defaultValue) {
      this.defaultValue = defaultValue;
      if (key.startsWith("?") && key.endsWith("?")) {
         this.key = key.replace("?", "");
         this.castFunction = castFunction;
         this.value = null;
      } else if (!key.isEmpty() && !key.equalsIgnoreCase("-")) {
         this.value = this.cast(castFunction, key);
         if (this.value instanceof String) {
            String potential = (String)this.value;
            if (potential.startsWith("?") && potential.endsWith("?")) {
               this.key = potential.replace("?", "");
               this.castFunction = castFunction;
               this.value = null;
               this.empty = false;
               return;
            }
         }

         this.key = null;
         this.castFunction = null;
      } else {
         this.key = null;
         this.castFunction = null;
         this.value = defaultValue;
      }

      this.empty = false;
   }

   public static Argument from(String key, Function castFunction, Object defaultValue) {
      return new Argument(key, castFunction, defaultValue);
   }

   public Object value(QuestProgress progress) {
      if (this.key != null && this.value == null) {
         String s = progress.getDataString(this.key);
         if (s != null) {
            return this.cast(s);
         } else {
            Long l = progress.getDataLong(this.key);
            return l != null ? this.cast(String.valueOf(l)) : null;
         }
      } else {
         return this.value;
      }
   }

   public boolean isPresent(QuestProgress progress) {
      return this.isEmpty() || this.value(progress) != null;
   }

   public boolean isEmpty() {
      return this.empty;
   }

   private Object cast(String in) {
      return this.cast(this.castFunction, in);
   }

   private Object cast(Function castFunction, String in) {
      try {
         return castFunction.apply(in);
      } catch (Exception var4) {
         Pixelmon.LOGGER.error("Failed to cast quest argument: " + in);
         return this.defaultValue;
      }
   }
}
