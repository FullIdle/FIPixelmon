package com.pixelmonmod.pixelmon.quests.quest;

public class Context {
   private final Object[] values;

   public Context(Object[] values) {
      this.values = values;
   }

   public Object get(int index) {
      return this.get(index, (Object)null);
   }

   public Object get(int index, Object defaultValue) {
      return index >= 0 && index < this.values.length ? this.values[index] : defaultValue;
   }

   public int size() {
      return this.values.length;
   }
}
