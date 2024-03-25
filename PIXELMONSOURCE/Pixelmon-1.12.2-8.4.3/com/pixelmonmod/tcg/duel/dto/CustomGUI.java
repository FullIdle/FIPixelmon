package com.pixelmonmod.tcg.duel.dto;

import com.pixelmonmod.tcg.duel.state.CustomGUIResult;

public class CustomGUI {
   public String id;
   public CustomGUIResult defaultResult;

   public CustomGUI(String id, CustomGUIResult defaultResult) {
      this.id = id;
      this.defaultResult = defaultResult;
   }
}
