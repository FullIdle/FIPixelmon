package com.pixelmonmod.tcg.duel.state;

public class CardSelectorResult {
   private boolean[] selection;
   private boolean isOpened;

   public boolean[] getSelection() {
      return this.selection;
   }

   public void setSelection(boolean[] selection) {
      this.selection = selection;
   }

   public boolean isOpened() {
      return this.isOpened;
   }

   public void setOpened(boolean opened) {
      this.isOpened = opened;
   }
}
