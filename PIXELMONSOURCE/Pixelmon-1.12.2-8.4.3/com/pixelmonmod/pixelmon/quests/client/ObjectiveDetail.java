package com.pixelmonmod.pixelmon.quests.client;

public class ObjectiveDetail {
   private final String key;
   private final short quantityComplete;
   private final short totalQuantity;
   private final boolean complete;

   public ObjectiveDetail(String key, short quantityComplete, short totalQuantity, boolean complete) {
      this.key = key;
      this.quantityComplete = quantityComplete;
      this.totalQuantity = totalQuantity;
      this.complete = complete;
   }

   public String getKey() {
      return this.key;
   }

   public String applyPlaceholders(String text) {
      return text.replace("COUNT", Short.toString(this.quantityComplete)).replace("TOTAL", Short.toString(this.totalQuantity)).replace("#", Integer.toString(this.totalQuantity - this.quantityComplete));
   }

   public boolean isComplete() {
      return this.complete || this.quantityComplete == this.totalQuantity;
   }
}
