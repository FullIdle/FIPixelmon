package com.pixelmonmod.tcg.api.card;

public enum CardModifier {
   Plus,
   Minus,
   Multiply,
   Divide;

   public static CardModifier parse(String modifier) {
      switch (modifier) {
         case "+":
            return Plus;
         case "-":
            return Minus;
         case "*":
         case "x":
            return Multiply;
         case "/":
            return Divide;
         default:
            return null;
      }
   }

   public int apply(int original, int modifierValue) {
      switch (this) {
         case Plus:
            return original + modifierValue;
         case Minus:
            return Math.max(0, original - modifierValue);
         case Multiply:
            return original * modifierValue;
         case Divide:
            return original / modifierValue;
         default:
            return 0;
      }
   }

   public String toString() {
      switch (this) {
         case Plus:
            return "+";
         case Minus:
            return "-";
         case Multiply:
            return "x";
         case Divide:
            return "/";
         default:
            return "";
      }
   }
}
