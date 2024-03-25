package com.pixelmonmod.pixelmon.battles.attacks;

public enum Effectiveness {
   Normal(1.0F),
   Super(2.0F),
   Max(4.0F),
   Not(0.5F),
   Barely(0.25F),
   None(0.0F);

   public float value;

   private Effectiveness(float f) {
      this.value = f;
   }
}
