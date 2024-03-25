package com.pixelmonmod.pixelmon.battles.controller.log;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class AttackAction extends BattleActionBase {
   public final AttackBase attack;
   public final MoveResults[] moveResults;

   public AttackAction(int turn, int pokemonPosition, PixelmonWrapper pokemon, AttackBase attack, MoveResults[] moveResults) {
      super(turn, pokemonPosition, pokemon);
      this.moveResults = moveResults;
      this.attack = attack;
   }

   public BattleActionBase.ActionType actionType() {
      return BattleActionBase.ActionType.ATTACK;
   }
}
