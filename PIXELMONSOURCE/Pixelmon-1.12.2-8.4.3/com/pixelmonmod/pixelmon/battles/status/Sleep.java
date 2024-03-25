package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Guts;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;

public class Sleep extends StatusPersist {
   public transient int effectTurns;

   public Sleep() {
      this(RandomHelper.getRandomNumberBetween(1, 3));
   }

   public Sleep(int i) {
      super(StatusType.Sleep);
      this.effectTurns = -1;
      this.effectTurns = i;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.checkChance()) {
         sleep(user, target, user.attack, true);
      }

   }

   public static boolean sleep(PixelmonWrapper user, PixelmonWrapper target, Attack attack, boolean showMessage) {
      return (new Sleep()).addStatus(user, target, attack, showMessage, "pixelmon.effect.alreadysleeping", "pixelmon.effect.fallasleep");
   }

   public boolean isImmune(PixelmonWrapper pokemon) {
      if (pokemon.bc.rules.hasClause("sleep")) {
         PixelmonWrapper[] var2 = pokemon.getParticipant().allPokemon;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PixelmonWrapper pw = var2[var4];
            if (pw.getPrimaryStatus().type == StatusType.Sleep) {
               return true;
            }
         }
      }

      return uproarActive(pokemon);
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (uproarActive(user)) {
         this.effectTurns = 0;
      }

      if (this.effectTurns-- <= 0) {
         user.removeStatus((StatusBase)this);
         if (user.getBattleAbility().isAbility(Guts.class)) {
            int[] stats = user.getBattleStats().getBattleStats();
            int var10001 = StatsType.Attack.getStatIndex();
            stats[var10001] = (int)((double)stats[var10001] / 1.5);
            user.getBattleStats().setStatsForTurn(stats);
         }

         return true;
      } else {
         user.bc.sendToAll("pixelmon.status.stillsleeping", user.getNickname());
         return a.isAttack("Snore", "Sleep Talk");
      }
   }

   public void writeToNBT(NBTTagCompound nbt) {
      super.writeToNBT(nbt);
      nbt.func_74768_a("StatusSleepTurns", this.effectTurns);
   }

   public StatusPersist restoreFromNBT(NBTTagCompound nbt) {
      return nbt.func_74764_b("StatusSleepTurns") ? new Sleep(nbt.func_74762_e("StatusSleepTurns")) : new Sleep();
   }

   public static boolean uproarActive(PixelmonWrapper pokemon) {
      if (pokemon.bc == null) {
         return false;
      } else {
         Iterator var1 = pokemon.bc.getActiveUnfaintedPokemon().iterator();

         PixelmonWrapper pw;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            pw = (PixelmonWrapper)var1.next();
         } while(!pw.hasStatus(StatusType.Uproar));

         return true;
      }
   }

   public StatusBase copy() {
      return new Sleep(this.effectTurns);
   }

   public String getCureMessage() {
      return "pixelmon.status.wokeup";
   }

   public String getCureMessageItem() {
      return "pixelmon.status.sleepcureitem";
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         userChoice.raiseWeight(this.getWeightWithChance((float)userChoice.result.accuracy));
      }

   }
}
