package com.pixelmonmod.pixelmon.battles.status;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Levitate;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class EntryHazard extends StatusBase {
   public static List ENTRY_HAZARDS = Lists.newArrayList(new String[]{"Spikes", "Stealth Rock", "Sticky Web", "Toxic Spikes"});
   private int maxLayers;
   protected transient int numLayers = 1;

   public EntryHazard(StatusType type, int maxLayers) {
      super(type);
      this.maxLayers = maxLayers;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0 || user.bc.simulateMode) {
         int message = false;
         EntryHazard hazard = (EntryHazard)target.getStatus(this.type);
         byte message;
         if (hazard == null) {
            target.addTeamStatus(this.getNewInstance(), user);
            message = 3;
         } else if (hazard.numLayers >= this.maxLayers) {
            message = 1;
         } else {
            if (!user.bc.simulateMode) {
               ++hazard.numLayers;
            }

            message = 2;
         }

         switch (message) {
            case 1:
               user.bc.sendToAll("pixelmon.effect.effectfailed");
               user.attack.moveResult.result = AttackResult.failed;
               break;
            case 2:
               user.bc.sendToAll(this.getMultiLayerMessage(), target.getNickname());
               break;
            case 3:
               user.bc.sendToAll(this.getFirstLayerMessage(), target.getNickname());
         }
      }

   }

   public int getNumLayers() {
      return this.numLayers;
   }

   public abstract EntryHazard getNewInstance();

   protected abstract String getFirstLayerMessage();

   protected String getMultiLayerMessage() {
      return this.getFirstLayerMessage();
   }

   protected abstract String getAffectedMessage();

   public boolean isTeamStatus() {
      return true;
   }

   public void applyEffectOnSwitch(PixelmonWrapper pw) {
      if (!this.isUnharmed(pw)) {
         pw.bc.sendToAll(this.getAffectedMessage(), pw.getNickname());
         this.doEffect(pw);
      }

   }

   protected void doEffect(PixelmonWrapper pw) {
      pw.doBattleDamage(pw, (float)this.getDamage(pw), DamageTypeEnum.STATUS);
   }

   public int getDamage(PixelmonWrapper pw) {
      return 0;
   }

   public StatusBase copy() {
      EntryHazard copy = this.getNewInstance();
      copy.numLayers = this.numLayers;
      return copy;
   }

   protected boolean isAirborne(PixelmonWrapper pw) {
      return !pw.isGrounded() && (pw.hasType(EnumType.Flying) || pw.getBattleAbility() instanceof Levitate && !this.ignoreLevitate(pw) || pw.getUsableHeldItem().getHeldItemType() == EnumHeldItems.airBalloon || pw.hasStatus(StatusType.MagnetRise, StatusType.Telekinesis));
   }

   private boolean ignoreLevitate(PixelmonWrapper pw) {
      if (pw.bc.turn >= pw.bc.turnList.size()) {
         return false;
      } else {
         PixelmonWrapper attacker = (PixelmonWrapper)pw.bc.turnList.get(pw.bc.turn);
         return AbilityBase.ignoreAbility(attacker, pw) && ((PixelmonWrapper)attacker.targets.get(0)).battlePosition == pw.battlePosition && this.wasForcedSwitch(attacker);
      }
   }

   private boolean wasForcedSwitch(PixelmonWrapper opponent) {
      return opponent.attack != null && opponent.attack.isAttack("Circle Throw", "Dragon Tail", "Roar", "Whirlwind") && (opponent.attack.moveResult.result == AttackResult.hit || opponent.attack.moveResult.result == AttackResult.proceed);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = pw.getParticipant().getOpponentPokemon().iterator();

      while(var7.hasNext()) {
         PixelmonWrapper opponent = (PixelmonWrapper)var7.next();
         EntryHazard hazard = (EntryHazard)opponent.getStatus(this.type);
         if (hazard != null && hazard.numLayers >= this.maxLayers) {
            userChoice.raiseWeight(-100.0F);
            return;
         }
      }

      int totalReserve = 0;

      BattleParticipant opponent;
      for(Iterator var11 = pw.getParticipant().getOpponents().iterator(); var11.hasNext(); totalReserve += opponent.countAblePokemon() - opponent.controlledPokemon.size()) {
         opponent = (BattleParticipant)var11.next();
      }

      userChoice.raiseWeight((float)(totalReserve * this.getAIWeight()));
   }

   public abstract int getAIWeight();

   protected boolean isUnharmed(PixelmonWrapper pw) {
      return pw.getHeldItem() == PixelmonItemsHeld.heavyDutyBoots;
   }
}
