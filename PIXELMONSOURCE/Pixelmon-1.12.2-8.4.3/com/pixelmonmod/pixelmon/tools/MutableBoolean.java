package com.pixelmonmod.pixelmon.tools;

public class MutableBoolean {
   private boolean value;

   public MutableBoolean() {
      this(false);
   }

   public MutableBoolean(boolean value) {
      this.value = value;
   }

   public MutableBoolean(Boolean value) {
      this.value = value;
   }

   public Boolean getValue() {
      return this.value;
   }

   public void setValue(boolean value) {
      this.value = value;
   }

   public void setFalse() {
      this.value = false;
   }

   public void setTrue() {
      this.value = true;
   }

   public void setValue(Boolean value) {
      this.value = value;
   }

   public boolean isTrue() {
      return this.value;
   }

   public boolean isFalse() {
      return !this.value;
   }

   public boolean booleanValue() {
      return this.value;
   }

   public Boolean toBoolean() {
      return this.booleanValue();
   }
}
