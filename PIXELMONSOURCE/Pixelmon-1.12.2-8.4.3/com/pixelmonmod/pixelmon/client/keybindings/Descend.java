package com.pixelmonmod.pixelmon.client.keybindings;

import net.minecraft.client.settings.KeyBinding;

public class Descend extends KeyBinding {
   public static Descend Instance;

   public Descend() {
      super("key.descend", 46, "key.categories.pixelmon");
      Instance = this;
   }
}
