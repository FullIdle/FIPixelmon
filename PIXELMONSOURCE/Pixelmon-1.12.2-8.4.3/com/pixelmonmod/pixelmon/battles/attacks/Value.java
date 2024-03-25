package com.pixelmonmod.pixelmon.battles.attacks;

public class Value {
   public int value = -1;
   public String stringValue = "";
   public ValueType type;

   public Value(int value, ValueType type) {
      this.value = value;
      this.type = type;
   }

   public Value(String stringValue, ValueType type) {
      this.stringValue = stringValue;
      this.type = type;
   }

   public String toString() {
      return this.type == ValueType.String ? "String - " + this.stringValue : "Number - " + this.value;
   }
}
