package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Thaw;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.forms.EnumShaymin;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

public class Freeze extends StatusPersist {
   public Freeze() {
      super(StatusType.Freeze);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.checkChance()) {
         freeze(user, target);
      }

   }

   public static boolean freeze(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasType(EnumType.Ice)) {
         return false;
      } else if (target.bc.globalStatusController.getWeather() instanceof Sunny) {
         return false;
      } else {
         TextComponentTranslation message = ChatHandler.getMessage("pixelmon.effect.frozesolid", target.getNickname());
         if (target.getPokemonName().equals(EnumSpecies.Shaymin.name) && target.entity.getPokemonData().getForm() == 1) {
            target.entity.setForm(EnumShaymin.LAND);
            user.bc.sendToAll("pixelmon.abilities.changeform", target.getNickname());
         }

         return target.addStatus(new Freeze(), user, message);
      }
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      for(int i = 0; i < a.getMove().effects.size(); ++i) {
         EffectBase e = (EffectBase)a.getMove().effects.get(i);
         if (e instanceof Thaw) {
            user.removeStatus((StatusBase)this);
            return true;
         }
      }

      if (RandomHelper.getRandomChance(20)) {
         user.removeStatus((StatusBase)this);
         return true;
      } else {
         user.bc.sendToAll("pixelmon.status.frozensolid", user.getNickname());
         return false;
      }
   }

   public void onDamageReceived(PixelmonWrapper userWrapper, PixelmonWrapper pokemon, Attack a, int damage, DamageTypeEnum damageType) {
      if (damageType == DamageTypeEnum.ATTACK && a != null && (a.getType() == EnumType.Fire || a.isAttack("Scald", "Steam Eruption"))) {
         pokemon.removeStatus((StatusBase)this);
      }

   }

   public StatusPersist restoreFromNBT(NBTTagCompound nbt) {
      return new Freeze();
   }

   public boolean isImmune(PixelmonWrapper pokemon) {
      return pokemon.hasType(EnumType.Ice);
   }

   public String getCureMessage() {
      return "pixelmon.status.breakice";
   }

   public String getCureMessageItem() {
      return "pixelmon.status.frozencureitem";
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      float weight = this.getWeightWithChance(100.0F);
      if (userChoice.isMiddleTier() && !userChoice.hitsAlly()) {
         Iterator var8 = userChoice.targets.iterator();

         while(var8.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var8.next();
            if (freeze(pw, target)) {
               userChoice.raiseWeight(weight);
            }
         }
      }

   }
}
