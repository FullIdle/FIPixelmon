package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Set;
import net.minecraft.util.text.TextComponentTranslation;

public class WanderingSpirit extends AbilityBase {
   public static Set noAbilityChange = Sets.newHashSet(new String[]{"AsOne", "BattleBond", "Comatose", "Disguise", "FlowerGift", "GulpMissile", "HungerSwitch", "IceFace", "Illusion", "Imposter", "Multitype", "NeutralizingGas", "PowerConstruct", "ShieldsDown", "Receiver", "RKSSystem", "Schooling", "StanceChange", "ZenMode", "WonderGuard", "PowerOfAlchemy"});

   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      AbilityBase other = user.getBattleAbility();
      AbilityBase self = target.getBattleAbility();
      if (!noAbilityChange.contains(other.getName())) {
         TextComponentTranslation abilityName = new TextComponentTranslation("ability.WanderingSpirit.name", new Object[0]);
         user.bc.sendToAll("pixelmon.abilities.activated", user.getNickname(), abilityName);
         user.bc.sendToAll("pixelmon.effect.entrainment", user.getNickname(), other.getTranslatedName(), self.getTranslatedName());
         user.bc.sendToAll("pixelmon.effect.entrainment", target.getNickname(), self.getTranslatedName(), other.getTranslatedName());
         user.setTempAbility(self);
         target.setTempAbility(other);
      }

   }
}
