package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class WaterBubble extends AbilityBase {
   public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return a.getType() == EnumType.Fire ? damage / 2 : damage;
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return a.getType() == EnumType.Water ? new int[]{power * 2, accuracy} : new int[]{power, accuracy};
   }

   public boolean allowsStatus(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (status == StatusType.Burn) {
         pokemon.bc.sendToAll("pixelmon.abilities.waterbubble", pokemon.getNickname());
         return false;
      } else {
         return true;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.getPrimaryStatus().type == StatusType.Burn) {
         pokemon.bc.sendToAll("pixelmon.abilities.waterbubblecure", pokemon.getNickname());
         pokemon.removeStatus(StatusType.Burn);
      }

   }
}
