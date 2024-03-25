package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class FriendGuard extends AbilityBase {
   public int modifyDamageTeammate(int damage, PixelmonWrapper user, PixelmonWrapper opponent, Attack a) {
      return (int)((double)damage * 0.75);
   }
}
