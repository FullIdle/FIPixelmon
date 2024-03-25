package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChooseAttack implements IMessage {
   public int buttonId;
   public boolean[][] targetting;
   public int battleIndex;
   public UUID pokemonUUID;
   public boolean megaEvolving;
   public boolean dynamaxing;

   public ChooseAttack() {
   }

   public ChooseAttack(UUID pokemonUUID, boolean[][] targetting, int buttonId, int battleIndex, boolean megaEvolving, boolean dynamaxing) {
      this.pokemonUUID = pokemonUUID;
      this.buttonId = buttonId;
      this.battleIndex = battleIndex;
      this.targetting = ArrayHelper.deepCopy(targetting);
      this.megaEvolving = megaEvolving;
      this.dynamaxing = dynamaxing;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.buttonId = buffer.readInt();
      this.battleIndex = buffer.readInt();
      this.targetting = new boolean[buffer.readShort()][];

      int i;
      for(i = 0; i < this.targetting.length; ++i) {
         this.targetting[i] = new boolean[buffer.readShort()];
      }

      for(i = 0; i < this.targetting.length; ++i) {
         for(int j = 0; j < this.targetting[i].length; ++j) {
            this.targetting[i][j] = buffer.readBoolean();
         }
      }

      this.megaEvolving = buffer.readBoolean();
      this.dynamaxing = buffer.readBoolean();
   }

   public void toBytes(ByteBuf buffer) {
      PixelmonMethods.toBytesUUID(buffer, this.pokemonUUID);
      buffer.writeInt(this.buttonId);
      buffer.writeInt(this.battleIndex);
      buffer.writeShort(this.targetting.length);
      boolean[][] var2 = this.targetting;
      int var3 = var2.length;

      int var4;
      boolean[] aTargetting;
      for(var4 = 0; var4 < var3; ++var4) {
         aTargetting = var2[var4];
         buffer.writeShort(aTargetting.length);
      }

      var2 = this.targetting;
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         aTargetting = var2[var4];

         for(int j = 0; j < aTargetting.length; ++j) {
            buffer.writeBoolean(aTargetting[j]);
         }
      }

      buffer.writeBoolean(this.megaEvolving);
      buffer.writeBoolean(this.dynamaxing);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ChooseAttack message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         player.func_143004_u();
         BattleControllerBase bc = BattleRegistry.getBattle(message.battleIndex);
         if (bc == null) {
            return new ExitBattle();
         } else {
            BattleParticipant bp = bc.getParticipantForEntity(player);
            if (bp == null) {
               return new ExitBattle();
            } else {
               PixelmonWrapper pw = bp.getPokemonFromUUID(message.pokemonUUID);
               if (pw != null) {
                  BattleParticipant p = pw.getParticipant();
                  if (message.buttonId == -1) {
                     pw.chooseMove(p.getBattleAI().getNextMove(pw));
                  } else {
                     if (message.buttonId >= 4 || !message.megaEvolving && !message.dynamaxing) {
                        message.megaEvolving = false;
                        message.dynamaxing = false;
                     } else {
                        Attack attack = pw.getMoveset().get(message.buttonId);
                        if (attack == null || !attack.canUseMove()) {
                           bc.battleLog.onCrash(new Exception("Invalid attack"), "Invalid attack selected! player:" + player.func_70005_c_() + " pokemon:" + message.pokemonUUID.toString() + " index:" + message.buttonId);
                           return null;
                        }

                        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(ctx.getServerHandler().field_147369_b);
                        if (!storage.getMegaItem().canEvolve() && pw.getSpecies() != EnumSpecies.Rayquaza) {
                           message.megaEvolving = false;
                        }

                        if (!storage.getMegaItem().canDynamax()) {
                           message.dynamaxing = false;
                        }
                     }

                     ArrayList targets = this.findTargets(message, bc, p);
                     pw.setAttack(message.buttonId, targets, message.megaEvolving || message.dynamaxing);
                  }
               } else {
                  bc.battleLog.onCrash(new Exception("Invalid pokemon"), "Invalid pokemon selected! player:" + player.func_70005_c_() + " pokemon:" + message.pokemonUUID.toString());
               }

               return null;
            }
         }
      }

      private ArrayList findTargets(ChooseAttack message, BattleControllerBase bc, BattleParticipant p) {
         ArrayList targets = new ArrayList();
         ArrayList teamPokemon = bc.getTeamPokemon(p);

         for(int i = 0; i < message.targetting[0].length; ++i) {
            if (message.targetting[0][i] && i < teamPokemon.size()) {
               targets.add(teamPokemon.get(i));
            }
         }

         ArrayList opponentPokemon = bc.getOpponentPokemon(p);

         for(int i = 0; i < message.targetting[1].length; ++i) {
            if (message.targetting[1][i]) {
               if (bc.rules.battleType != EnumBattleType.Double && bc.rules.battleType != EnumBattleType.Single && bc.rules.battleType != EnumBattleType.Raid) {
                  Iterator var8 = opponentPokemon.iterator();

                  while(var8.hasNext()) {
                     PixelmonWrapper pokemon = (PixelmonWrapper)var8.next();
                     if (pokemon.battlePosition == i) {
                        targets.add(pokemon);
                     }
                  }
               } else if (i < opponentPokemon.size()) {
                  targets.add(opponentPokemon.get(i));
               }

               if (targets.isEmpty()) {
                  PixelmonWrapper fallback = (PixelmonWrapper)opponentPokemon.get(opponentPokemon.size() - 1);
                  if (!targets.contains(fallback)) {
                     targets.add(fallback);
                  }
                  break;
               }
            }
         }

         return targets;
      }
   }
}
