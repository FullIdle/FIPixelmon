package com.pixelmonmod.pixelmon.battles.controller.log;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;

public class BattleLog {
   private final BattleControllerBase bc;
   private int crashCount = 0;
   private final Instant battleStart = Instant.now();
   private final Map logs = new LinkedHashMap();
   private BattleTurnLog currentTurnLog;
   private Tuple lastError;

   public BattleLog(BattleControllerBase bc) {
      this.bc = bc;
      this.turnTick(0);
   }

   public void addEvent(BattleActionBase event) {
      this.currentTurnLog.registerAction(event);
   }

   public BattleActionBase getActionForPokemon(int turn, PixelmonWrapper pw) {
      BattleTurnLog log = (BattleTurnLog)this.logs.get(turn);
      return log != null ? log.getBattleActionFor(pw) : null;
   }

   public void turnTick(int turn) {
      this.logs.put(turn, this.currentTurnLog = new BattleTurnLog(turn));
   }

   public void onCrash(Exception exc, String error) {
      ++this.crashCount;
      this.lastError = new Tuple(exc, error);
      if (PixelmonConfig.printErrors) {
         System.err.println(error);
         exc.printStackTrace();
      }

      if (this.crashCount >= 3) {
         if (PixelmonConfig.printErrors) {
            System.out.println("===Too many errors detected in battle, force-ending===");
            this.bc.participants.stream().filter((participant) -> {
               return participant instanceof PlayerParticipant;
            }).forEach((participant) -> {
               ChatHandler.sendChat(participant.getEntity(), "pixelmon.general.battleerror");
            });
         }

         this.bc.endBattle(EnumBattleEndCause.FORCE);
      }

      try {
         this.exportLogFile();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public List getAllActions() {
      return (List)this.logs.values().stream().map((it) -> {
         return it.actions;
      }).flatMap(Collection::stream).collect(Collectors.toList());
   }

   public void exportLogFile() throws Exception {
      File file = new File("./logs/battle/", this.getFilename() + ".log");

      for(int count = 0; file.exists(); file = new File("./logs/battle/", this.getFilename() + "_" + count++ + ".log")) {
      }

      int maxNameLength = 15;
      String columnFormat = "%-" + maxNameLength + "." + maxNameLength + "s";
      StringBuilder sb = (new StringBuilder("Pixelmon Version ")).append(Pixelmon.getVersion()).append("\n");
      sb.append("Battle data ").append(this.battleStart.toString()).append("\n").append("\n");
      Iterator var6 = this.bc.participants.iterator();

      while(var6.hasNext()) {
         BattleParticipant participant = (BattleParticipant)var6.next();
         sb.append("Team #").append(participant.team).append(" ").append(participant.getType().toString()).append(" ").append(participant.getDisplayName()).append("\n");
         PixelmonWrapper[] var8 = participant.allPokemon;
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            PixelmonWrapper pw = var8[var10];
            NBTTagCompound tag = pw.pokemon.writeToNBT(new NBTTagCompound());
            sb.append("\t").append(tag.toString()).append(System.lineSeparator());
         }
      }

      sb.append("\n");
      var6 = this.logs.values().iterator();

      while(var6.hasNext()) {
         BattleTurnLog log = (BattleTurnLog)var6.next();
         sb.append(log.toString());
      }

      if (this.lastError != null) {
         sb.append((String)this.lastError.func_76340_b()).append('\n');
         sb.append(((Exception)this.lastError.func_76341_a()).getMessage()).append('\n');
         StackTraceElement[] var13 = ((Exception)this.lastError.func_76341_a()).getStackTrace();
         int var15 = var13.length;

         for(int var16 = 0; var16 < var15; ++var16) {
            StackTraceElement element = var13[var16];
            sb.append(element.toString()).append('\n');
         }
      }

      file.getParentFile().mkdir();
      file.createNewFile();
      Files.write(file.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
   }

   public String getFilename() {
      return "battle-" + this.battleStart.getEpochSecond();
   }

   private static class BattleTurnLog {
      private final List actions = new ArrayList();
      private final int turnNumber;

      public BattleTurnLog(int turnNumber) {
         this.turnNumber = turnNumber;
      }

      public void registerAction(BattleActionBase action) {
         this.actions.add(action);
      }

      public BattleActionBase getBattleActionFor(PixelmonWrapper wrapper) {
         Iterator var2 = this.actions.iterator();

         BattleActionBase action;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            action = (BattleActionBase)var2.next();
         } while(!Objects.equals(action.pokemon, wrapper));

         return action;
      }

      public String toString() {
         StringBuilder sb = new StringBuilder("Turn #" + this.turnNumber + "\n");

         for(Iterator var2 = this.actions.iterator(); var2.hasNext(); sb.append('\n')) {
            BattleActionBase action = (BattleActionBase)var2.next();
            sb.append(" - ");
            BattleParticipant participant = action.getParticipant();
            sb.append(participant.getType().toString()).append(" ").append(participant.getDisplayName()).append(" ");
            sb.append("pokemon=").append(action.pokemon.toString()).append(", ");
            sb.append("action=").append(action.actionType().toString());
            if (!(action instanceof AttackAction)) {
               if (action instanceof SwitchAction) {
                  sb.append(", switchingTo=").append(((SwitchAction)action).switchingTo);
               } else if (action instanceof BagItemAction) {
                  BagItemAction ba = (BagItemAction)action;
                  sb.append(", item=").append(ba.item == null ? "null" : ba.item.getRegistryName());
               }
            } else {
               sb.append(", attack=").append(((AttackAction)action).attack);
               if (action.pokemon.targets != null) {
                  sb.append(", targets=");
                  Iterator var5 = action.pokemon.targets.iterator();

                  while(var5.hasNext()) {
                     PixelmonWrapper target = (PixelmonWrapper)var5.next();
                     sb.append(target).append(" ");
                  }
               }

               sb.append(", results=").append(Arrays.toString(((AttackAction)action).moveResults));
            }
         }

         return sb.toString();
      }
   }
}
