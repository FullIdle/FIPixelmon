package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumMimikyu;

public class Disguise extends AbilityBase {
   public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (target.getSpecies() == EnumSpecies.Mimikyu && !target.getFormEnum().isTemporary() && !a.hasNoEffect(user, target) && !a.isAttack("Moongeist Beam", "Sunsteel Strike", "Photon Geyser", "Searing Sunraze Smash", "Menacing Moonraze Maelstrom", "Light That Burns the Sky")) {
         target.bc.sendToAll("pixelmon.abilities.disguise");
         target.setForm(target.getFormEnum() == EnumMimikyu.Disguised ? EnumMimikyu.Busted : (target.getFormEnum() == EnumMimikyu.Disguised_Spirit ? EnumMimikyu.Busted_Spirit : EnumMimikyu.Busted));
         target.bc.sendToAll("pixelmon.abilities.disguisebusted", target.getPokemonName());
         return target.getMaxHealth() / 8;
      } else {
         return damage;
      }
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.isAttack("Gastro Acid", "Worry Seed", "Simple Beam", "Role Play", "Skill Swap", "Entrainment")) {
         user.bc.sendToAll("pixelmon.battletext.movefailed");
         return false;
      } else {
         return true;
      }
   }

   public boolean allowsOutgoingAttack(PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.isAttack("Role Play", "Skill Swap", "Entrainment")) {
         user.bc.sendToAll("pixelmon.battletext.movefailed");
         return false;
      } else {
         return true;
      }
   }
}
