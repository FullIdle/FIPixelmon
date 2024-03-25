package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicBounce;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.Iterator;

public class MagicCoat extends StatusBase {
   public MagicCoat() {
      super(StatusType.MagicCoat);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.status.applymagiccoat", user.getNickname());
      user.addStatus(new MagicCoat(), user);
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      Attack a = user.attack;
      return reflectMove(a, pokemon, user, "pixelmon.status.magiccoat");
   }

   public static boolean reflectMove(Attack a, PixelmonWrapper pokemon, PixelmonWrapper user, String message) {
      if (a.getAttackCategory() == AttackCategory.STATUS && !a.isAttack("Bestow", "Curse", "Guard Swap", "Heart Swap", "Helping Hand", "Lock-On", "Memento", "Mimic", "Power Swap", "Psych Up", "Psycho Shift", "Role Play", "Skill Swap", "Snatch", "Switcheroo", "Transform", "Trick", "Extreme Evoboost", "Sketch") && (!a.getMove().getTargetingInfo().hitsAll || !a.getMove().getTargetingInfo().hitsSelf)) {
         user.bc.sendToAll(message, pokemon.getNickname());
         pokemon.targetIndex = 0;
         boolean allowed = false;
         AbilityBase ability = user.getBattleAbility(pokemon);
         if (!(ability instanceof MagicBounce)) {
            allowed = ability.allowsIncomingAttack(user, pokemon, a);
         }

         if (allowed) {
            allowed = user.getUsableHeldItem().allowsIncomingAttack(user, pokemon, a);
         }

         if (allowed) {
            Iterator var6 = user.bc.getTeamPokemon(user).iterator();

            while(var6.hasNext()) {
               PixelmonWrapper ally = (PixelmonWrapper)var6.next();
               if (!ally.getBattleAbility().allowsIncomingAttackTeammate(ally, user, pokemon, a)) {
                  allowed = false;
                  break;
               }
            }
         }

         if (!allowed) {
            return true;
         } else if (!pokemon.getBattleAbility().allowsOutgoingAttack(pokemon, user, a)) {
            return true;
         } else if (a.hasNoEffect(pokemon, user)) {
            user.bc.sendToAll("pixelmon.battletext.noeffect", user.getNickname());
            return true;
         } else {
            Attack oldAttack = user.attack;
            user.attack = a;
            a.applyAttackEffect(pokemon, user);
            user.attack = oldAttack;
            return true;
         }
      } else {
         return false;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }
}
