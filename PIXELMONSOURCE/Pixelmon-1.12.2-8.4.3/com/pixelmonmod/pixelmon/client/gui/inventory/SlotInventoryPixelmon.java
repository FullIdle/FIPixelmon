package com.pixelmonmod.pixelmon.client.gui.inventory;

import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.pc.SlotPC;
import java.awt.Rectangle;

public class SlotInventoryPixelmon extends SlotPC {
   private static final int HELD_X_OFFSET = 19;
   public int heldItemX;
   public int heldItemY;

   public SlotInventoryPixelmon(int x, int y, StoragePosition position) {
      super(x, y, position);
      this.length = 16;
      this.heldItemX = x + 19;
      this.heldItemY = y;
   }

   public Rectangle getHeldItemBounds() {
      return new Rectangle(this.heldItemX, this.heldItemY, this.length, this.length);
   }

   void setX(int newX) {
      this.x = newX;
      this.heldItemX = this.x + 19;
   }
}
