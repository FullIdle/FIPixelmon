package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Set;
import net.minecraft.util.text.TextComponentTranslation;

public class Mummy extends AbilityBase {
   private static final Set invalidAbilities = Sets.newHashSet(new String[]{"AsOne", "BattleBond", "Comatose", "Disguise", "GulpMissile", "IceFace", "Multitype", "PowerConstruct", "RKSSystem", "Schooling", "ShieldsDown", "StanceChange", "ZenMode", "Mummy"});

   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      AbilityBase userAbility = user.getBattleAbility(false);
      if (!invalidAbilities.contains(userAbility.getName())) {
         TextComponentTranslation abilityName = new TextComponentTranslation("ability.Mummy.name", new Object[0]);
         user.bc.sendToAll("pixelmon.abilities.activated", user.getNickname(), abilityName);
         user.bc.sendToAll("pixelmon.effect.entrainment", user.getNickname(), userAbility.getTranslatedName(), abilityName);
         user.setTempAbility(this);
      }

   }
}
