package com.pixelmonmod.tcg.duel.log;

import net.minecraft.util.ResourceLocation;

public enum DuelLogType {
   ATTACK(new ResourceLocation("tcg", "gui/game/log/actions/attack.png")),
   ABILITY(new ResourceLocation("tcg", "gui/game/log/actions/ability.png")),
   CONDITION(new ResourceLocation("tcg", "gui/game/log/actions/status.png")),
   KNOCKOUT(new ResourceLocation("tcg", "gui/game/log/actions/knockout.png")),
   ATTACH(new ResourceLocation("tcg", "gui/game/log/actions/attach.png")),
   DRAW(new ResourceLocation("tcg", "gui/game/log/actions/draw.png")),
   DISCARD(new ResourceLocation("tcg", "gui/game/log/actions/discard.png")),
   COIN(new ResourceLocation("tcg", "gui/game/log/actions/flip.png")),
   EVOLVE(new ResourceLocation("tcg", "gui/game/log/actions/evolve.png")),
   PLAY(new ResourceLocation("tcg", "gui/game/log/actions/play.png")),
   SWITCH(new ResourceLocation("tcg", "gui/game/log/actions/switch.png")),
   STARTGAME(new ResourceLocation("tcg", "gui/game/log/actions/startgame.png")),
   ENDGAME(new ResourceLocation("tcg", "gui/game/log/actions/endgame.png")),
   PASSTURN(new ResourceLocation("tcg", "gui/game/log/actions/passturn.png"));

   ResourceLocation r;

   private DuelLogType(ResourceLocation r) {
      this.r = r;
   }

   public ResourceLocation getResourceLocation() {
      return this.r;
   }
}
