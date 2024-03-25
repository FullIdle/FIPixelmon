package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Disable;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import net.minecraft.util.text.TextComponentTranslation;

public class CursedBody extends AbilityBase {
   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (!user.hasStatus(StatusType.Disable) && !target.hasStatus(StatusType.Substitute) && RandomHelper.getRandomChance(0.3F)) {
         TextComponentTranslation message = ChatHandler.getMessage("pixelmon.abilities.cursedbody", target.getNickname(), user.getNickname(), a.getMove().getTranslatedName());
         user.addStatus(new Disable(a), target, message);
      }

   }
}
