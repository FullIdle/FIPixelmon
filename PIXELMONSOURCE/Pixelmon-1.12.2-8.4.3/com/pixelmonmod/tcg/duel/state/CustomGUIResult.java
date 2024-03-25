package com.pixelmonmod.tcg.duel.state;

public class CustomGUIResult {
   private int[] result;
   private boolean isOpened;

   public int[] getResult() {
      return this.result;
   }

   public void setResult(int[] result) {
      this.result = result;
   }

   public boolean isOpened() {
      return this.isOpened;
   }

   public void setOpened(boolean opened) {
      this.isOpened = opened;
   }
}
