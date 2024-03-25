package com.pixelmonmod.pixelmon.client.gui.battles;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import java.util.UUID;

public class AttackData {
   public UUID pokemonUUID;
   public Attack attack;
   public boolean checkEvo;

   public AttackData(UUID pokemonUUID, Attack attack, boolean checkEvo) {
      this.pokemonUUID = pokemonUUID;
      this.attack = attack;
      this.checkEvo = checkEvo;
   }

   public boolean equals(Object compare) {
      if (compare instanceof AttackData) {
         AttackData compareData = (AttackData)compare;
         return !compareData.pokemonUUID.equals(this.pokemonUUID) ? false : compareData.attack.equals(this.attack);
      } else {
         return false;
      }
   }
}
