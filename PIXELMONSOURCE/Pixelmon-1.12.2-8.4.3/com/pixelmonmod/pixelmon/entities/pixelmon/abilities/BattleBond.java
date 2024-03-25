package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumGreninja;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;

public class BattleBond extends AbilityBase {
   public void tookDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user.getParticipant().ashNinja == null && user.getSpecies().equals(EnumSpecies.Greninja) && !user.getFormEnum().isTemporary() && user.getParticipant().getType() != ParticipantType.WildPokemon && target.isFainted() && target.getParticipant().hasMorePokemon() && !user.hasStatus(StatusType.Transformed)) {
         IEnumForm ash = ((EnumGreninja)user.getFormEnum()).getAshForm();
         if (ash != null) {
            user.getParticipant().ashNinja = user.getPokemonUUID();
            user.bc.sendToAll("pixelmon.battletext.ashgreninja.react", user.getParticipant().getDisplayName());
            user.evolution = new EvolutionQuery(user.entity, ash.getForm());
            user.setForm(ash);
         }
      }

   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user.getSpecies().equals(EnumSpecies.Greninja) && user.getFormEnum().isTemporary() && a.getMove().isAttack("Water Shuriken")) {
         power = (int)((double)power * 1.3333333333333333);
      }

      return new int[]{power, accuracy};
   }
}
