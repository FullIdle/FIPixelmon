package com.pixelmonmod.pixelmon.client.gui.starter;

public enum EnumShadow {
   Large(0.02F, 600, 120),
   Medium(0.1F, 400, 80),
   Small(0.2F, 300, 60);

   public float moveSpeedModifier;
   public int width;
   public int height;

   private EnumShadow(float moveSpeedModifier, int width, int height) {
      this.moveSpeedModifier = moveSpeedModifier;
      this.width = width;
      this.height = height;
   }
}
