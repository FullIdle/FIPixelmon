package com.pixelmonmod.pixelmon.battles.rules.teamselection;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.CancelTeamSelect;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.CheckRulesVersionFixed;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.RejectTeamSelect;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.ShowTeamSelect;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TeamSelection {
   final int id;
   private BattleRules rules;
   private ParticipantSelection[] participants;
   private boolean showRules;
   public static final String FAINT_KEY = "f";
   public static final String EGG_KEY = "e";
   public static final String NONE_KEY = "n";

   TeamSelection(int id, BattleRules rules, boolean showRules, PartyStorage... storages) {
      this.id = id;
      this.rules = rules;
      this.showRules = showRules;
      this.participants = new ParticipantSelection[storages.length];

      for(int i = 0; i < this.participants.length; ++i) {
         this.participants[i] = new ParticipantSelection(storages[i]);
      }

   }

   boolean hasPlayer(EntityPlayerMP player) {
      ParticipantSelection[] var2 = this.participants;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ParticipantSelection p = var2[var4];
         if (p.storage instanceof PlayerPartyStorage && ((PlayerPartyStorage)p.storage).getPlayer() == player) {
            return true;
         }
      }

      return false;
   }

   void initializeClient() {
      ParticipantSelection cancelPart = null;
      boolean hasNPC = false;
      ParticipantSelection[] var3 = this.participants;
      int var4 = var3.length;

      int var5;
      ParticipantSelection p;
      for(var5 = 0; var5 < var4; ++var5) {
         p = var3[var5];
         if (p.isNPC) {
            hasNPC = true;
         } else {
            PartyStorage storage = p.storage;
            boolean hasPokemon = false;

            for(int i = 0; i < storage.getAll().length; ++i) {
               Pokemon current = storage.get(i);
               if (current == null) {
                  p.disabled[i] = "n";
               } else if (current.isEgg()) {
                  p.disabled[i] = "e";
               } else if (current.getHealth() <= 0 && !this.rules.fullHeal) {
                  p.disabled[i] = "f";
               }

               if (p.disabled[i] == null) {
                  p.disabled[i] = this.rules.validateSingle(current);
                  hasPokemon = hasPokemon || p.disabled[i] == null;
               }
            }

            if (!hasPokemon) {
               cancelPart = p;
            }
         }
      }

      if (cancelPart != null && !hasNPC) {
         this.cancelBattle(cancelPart);
      } else {
         var3 = this.participants;
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            p = var3[var5];
            if (!p.isNPC) {
               PlayerPartyStorage storage = (PlayerPartyStorage)p.storage;
               EntityPlayerMP player = storage.getPlayer();
               ParticipantSelection otherPart = this.getOther(p);
               List opponentPokemon = otherPart.storage.findAll((pokemonx) -> {
                  return pokemonx != null;
               });
               List opponentTeam = new ArrayList();
               Iterator var12 = opponentPokemon.iterator();

               while(var12.hasNext()) {
                  Pokemon pokemon = (Pokemon)var12.next();
                  opponentTeam.add(new TeamSelectPokemon(pokemon));
               }

               Object packet;
               if (otherPart.isNPC) {
                  packet = new ShowTeamSelect(this.id, p.disabled, opponentTeam, ((TrainerPartyStorage)otherPart.storage).getTrainer().getId(), ((TrainerPartyStorage)otherPart.storage).getTrainer().getName(player.field_71148_cg), this.rules, this.showRules);
               } else {
                  packet = new ShowTeamSelect(this.id, p.disabled, opponentTeam, ((PlayerPartyStorage)otherPart.storage).getPlayerUUID(), this.rules, this.showRules);
               }

               if (this.showRules) {
                  packet = new CheckRulesVersionFixed(BattleClauseRegistry.getClauseVersion(), (ShowTeamSelect)packet);
               }

               Pixelmon.network.sendTo((IMessage)packet, player);
            }
         }

      }
   }

   void startBattle() {
      ParticipantSelection npc = null;
      ParticipantSelection cancelPart = null;
      ParticipantSelection[] var3 = this.participants;
      int var4 = var3.length;

      ParticipantSelection p;
      for(int var5 = 0; var5 < var4; ++var5) {
         p = var3[var5];
         int index;
         if (p.isNPC) {
            npc = p;
            int[] teamIndices;
            if (!this.rules.teamPreview && this.rules.numPokemon >= 6) {
               teamIndices = new int[]{0, 1, 2, 3, 4, 5};
            } else {
               teamIndices = RandomHelper.getRandomDistinctNumbersBetween(0, 5, 6);
            }

            int[] var8 = teamIndices;
            int var9 = teamIndices.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               index = var8[var10];
               p.addTeamMember(index);
               if (this.rules.validateTeam(p.team) != null) {
                  p.removeTeamMember();
               }

               if (p.team.size() == this.rules.numPokemon) {
                  break;
               }
            }

            if (p.team.isEmpty()) {
               cancelPart = p;
               break;
            }
         } else {
            String initValidate = this.rules.validateTeam(p.team);
            if (p.team.isEmpty() || initValidate != null) {
               EntityPlayerMP player = ((PlayerPartyStorage)p.storage).getPlayer();
               if (player != null && initValidate != null) {
                  ChatHandler.sendChat(player, "gui.battlerules.teamviolatedforce", BattleClause.getLocalizedName(initValidate));
               }

               while(!p.team.isEmpty()) {
                  p.removeTeamMember();
                  if (this.rules.validateTeam(p.team) == null) {
                     break;
                  }
               }

               if (p.team.isEmpty()) {
                  int[] teamIndices = RandomHelper.getRandomDistinctNumbersBetween(0, 5, 6);
                  int[] var21 = teamIndices;
                  index = teamIndices.length;

                  for(int var12 = 0; var12 < index; ++var12) {
                     int index = var21[var12];
                     p.addTeamMember(index);
                     if (!p.team.isEmpty()) {
                        if (this.rules.validateTeam(p.team) == null) {
                           break;
                        }

                        p.removeTeamMember();
                     }
                  }
               }

               if (p.team.isEmpty()) {
                  cancelPart = p;
                  break;
               }
            }
         }
      }

      if (cancelPart != null) {
         this.cancelBattle(cancelPart);
      } else {
         Object part1;
         PlayerParticipant part2;
         if (npc == null) {
            part1 = this.getPlayerPart(this.participants[0]);
            part2 = this.getPlayerPart(this.participants[1]);
         } else {
            TrainerPartyStorage storage = (TrainerPartyStorage)npc.storage;
            p = this.getOther(npc);
            EntityPlayerMP player = ((PlayerPartyStorage)p.storage).getPlayer();
            if (!storage.getTrainer().canStartBattle(player, true)) {
               Pixelmon.network.sendTo(new CancelTeamSelect(), player);
               this.removeTeamSelect();
               return;
            }

            part1 = new TrainerParticipant(storage.getTrainer(), player, this.rules.battleType.numPokemon, npc.team);
            part2 = new PlayerParticipant(player, p.team, this.rules.battleType.numPokemon);
         }

         BattleRegistry.startBattle(new BattleParticipant[]{(BattleParticipant)part1}, new BattleParticipant[]{part2}, this.rules);
         this.removeTeamSelect();
      }
   }

   private void cancelBattle(ParticipantSelection cancelPart) {
      if (cancelPart != null) {
         String cancelName;
         int var5;
         if (!cancelPart.isNPC) {
            cancelName = ((PlayerPartyStorage)cancelPart.storage).getPlayerName();
         } else {
            EntityPlayerMP player = null;
            ParticipantSelection[] var4 = this.participants;
            var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               ParticipantSelection p = var4[var6];
               if (!p.isNPC) {
                  player = ((PlayerPartyStorage)p.storage).getPlayer();
                  break;
               }
            }

            String langCode = player == null ? "en_US" : player.field_71148_cg;
            cancelName = ((TrainerPartyStorage)cancelPart.storage).getTrainer().getName(langCode);
         }

         ParticipantSelection[] var9 = this.participants;
         int var11 = var9.length;

         for(var5 = 0; var5 < var11; ++var5) {
            ParticipantSelection p = var9[var5];
            if (!p.isNPC) {
               CancelTeamSelect packet = new CancelTeamSelect();
               EntityPlayerMP player = ((PlayerPartyStorage)p.storage).getPlayer();
               Pixelmon.network.sendTo(packet, player);
               if (p == cancelPart) {
                  ChatHandler.sendChat(player, "gui.battlerules.cancelselectyou");
               } else {
                  ChatHandler.sendChat(player, "gui.battlerules.cancelselect", cancelName);
               }
            }
         }

         this.removeTeamSelect();
      }

   }

   private PlayerParticipant getPlayerPart(ParticipantSelection p) {
      return new PlayerParticipant(((PlayerPartyStorage)p.storage).getPlayer(), p.team, this.rules.battleType.numPokemon);
   }

   private ParticipantSelection getOther(ParticipantSelection selection) {
      ParticipantSelection[] var2 = this.participants;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ParticipantSelection other = var2[var4];
         if (other != selection) {
            return other;
         }
      }

      return selection;
   }

   private ParticipantSelection getPlayer(EntityPlayerMP player) {
      ParticipantSelection[] var2 = this.participants;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ParticipantSelection p = var2[var4];
         if (!p.isNPC && ((PlayerPartyStorage)p.storage).getPlayer() == player) {
            return p;
         }
      }

      return null;
   }

   private boolean isReady() {
      ParticipantSelection[] var1 = this.participants;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ParticipantSelection p = var1[var3];
         if (!p.confirmed) {
            return false;
         }
      }

      return true;
   }

   public void registerTeamSelect(EntityPlayerMP player, int[] selection, boolean force) {
      ParticipantSelection ps = this.getPlayer(player);
      if (ps != null) {
         ps.setTeam(selection);

         while(ps.team.size() > this.rules.numPokemon) {
            ps.team.remove(ps.team.size() - 1);
         }

         String clauseID = this.rules.validateTeam(ps.team);
         if (ps.team.isEmpty()) {
            clauseID = "f";
         }

         if (clauseID != null && !force) {
            Pixelmon.network.sendTo(new RejectTeamSelect(clauseID), player);
         } else {
            ps.confirmed = true;
            if (this.isReady()) {
               this.startBattle();
            }
         }

      }
   }

   public void unregisterTeamSelect(EntityPlayerMP player) {
      ParticipantSelection ps = this.getPlayer(player);
      if (ps != null) {
         ps.confirmed = false;
      }

   }

   private void removeTeamSelect() {
      TeamSelectionList.removeSelection(this.id);
   }

   public static String[] getReservedKeys() {
      return new String[]{"f", "e", "n"};
   }
}
