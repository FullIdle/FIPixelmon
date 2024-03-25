package com.pixelmonmod.pixelmon.client.gui.pc;

import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import java.awt.Rectangle;

public class SlotPC {
   public StoragePosition position;
   public int x;
   public int y;
   public int length;

   public SlotPC(int x, int y, StoragePosition position) {
      this.position = position;
      this.x = x;
      this.y = y;
      this.length = 30;
   }

   public Rectangle getBounds() {
      return new Rectangle(this.x, this.y, this.length, this.length);
   }
}
