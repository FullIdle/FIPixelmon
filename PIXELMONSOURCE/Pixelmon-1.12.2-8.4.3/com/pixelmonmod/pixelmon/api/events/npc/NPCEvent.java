package com.pixelmonmod.pixelmon.api.events.npc;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class NPCEvent extends Event {
   public EntityNPC npc;
   public EnumNPCType type;
   public EntityPlayer player;

   protected NPCEvent() {
   }

   protected NPCEvent(EntityNPC npc, EnumNPCType type, EntityPlayer player) {
      this.npc = npc;
      this.type = type;
      this.player = player;
   }

   public static class EndBattle extends NPCEvent {
      public final BattleControllerBase bc;
      public final EnumBattleEndCause cause;
      public final boolean abnormal;
      private final ImmutableMap results;

      public EndBattle(BattleControllerBase bc, EnumBattleEndCause cause, boolean abnormal, Map results) {
         this.results = ImmutableMap.copyOf(results);
         this.npc = this.getNPCParticipant().trainer;
         this.player = this.getPlayerParticipant().player;
         this.type = EnumNPCType.Trainer;
         this.bc = bc;
         this.cause = cause;
         this.abnormal = abnormal;
      }

      public PlayerParticipant getPlayerParticipant() {
         UnmodifiableIterator var1 = this.results.keySet().iterator();

         BattleParticipant bp;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            bp = (BattleParticipant)var1.next();
         } while(!(bp instanceof PlayerParticipant));

         return (PlayerParticipant)bp;
      }

      public TrainerParticipant getNPCParticipant() {
         UnmodifiableIterator var1 = this.results.keySet().iterator();

         BattleParticipant bp;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            bp = (BattleParticipant)var1.next();
         } while(!(bp instanceof TrainerParticipant));

         return (TrainerParticipant)bp;
      }

      public BattleResults getPlayerResults() {
         return (BattleResults)this.results.get(this.getPlayerParticipant());
      }

      public BattleResults getNPCResults() {
         return (BattleResults)this.results.get(this.getNPCParticipant());
      }
   }

   @Cancelable
   public static class StartBattle extends NPCEvent {
      private final ArrayList participants;

      public StartBattle(ArrayList participants) {
         this.participants = participants;
         this.npc = this.getNPCParticipant().trainer;
         this.player = this.getPlayerParticipant().player;
         this.type = EnumNPCType.Trainer;
      }

      public PlayerParticipant getPlayerParticipant() {
         Iterator var1 = this.participants.iterator();

         BattleParticipant bp;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            bp = (BattleParticipant)var1.next();
         } while(!(bp instanceof PlayerParticipant));

         return (PlayerParticipant)bp;
      }

      public TrainerParticipant getNPCParticipant() {
         Iterator var1 = this.participants.iterator();

         BattleParticipant bp;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            bp = (BattleParticipant)var1.next();
         } while(!(bp instanceof TrainerParticipant));

         return (TrainerParticipant)bp;
      }
   }

   @Cancelable
   public static class Interact extends NPCEvent {
      public Interact(EntityNPC npc, EnumNPCType type, EntityPlayer player) {
         super(npc, type, player);
      }
   }
}
