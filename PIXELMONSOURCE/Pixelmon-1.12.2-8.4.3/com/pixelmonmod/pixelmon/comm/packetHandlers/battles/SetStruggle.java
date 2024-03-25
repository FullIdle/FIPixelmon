package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetStruggle implements IMessage {
   public boolean[][] targetting;
   public int battleIndex;
   public UUID pokemonUUID;

   public SetStruggle() {
   }

   public SetStruggle(UUID pokemonUUID, boolean[][] targetting, int battleIndex) {
      this.pokemonUUID = pokemonUUID;
      this.battleIndex = battleIndex;
      this.targetting = targetting;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
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

   }

   public void toBytes(ByteBuf buffer) {
      PixelmonMethods.toBytesUUID(buffer, this.pokemonUUID);
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

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetStruggle message, MessageContext ctx) {
         BattleControllerBase bc = BattleRegistry.getBattle(message.battleIndex);
         Iterator var4 = bc.participants.iterator();

         while(true) {
            BattleParticipant p;
            do {
               do {
                  if (!var4.hasNext()) {
                     return null;
                  }

                  p = (BattleParticipant)var4.next();
               } while(!(p instanceof PlayerParticipant));
            } while(!((PlayerParticipant)p).player.func_110124_au().equals(ctx.getServerHandler().field_147369_b.func_110124_au()));

            Iterator var6 = p.controlledPokemon.iterator();

            while(var6.hasNext()) {
               PixelmonWrapper pw = (PixelmonWrapper)var6.next();
               if (pw.getPokemonUUID().equals(message.pokemonUUID)) {
                  ArrayList targets = this.findTargets(message, bc, p);
                  pw.setStruggle(targets);
               }
            }
         }
      }

      private ArrayList findTargets(SetStruggle message, BattleControllerBase bc, BattleParticipant p) {
         ArrayList targets = new ArrayList();
         ArrayList teamPokemon = bc.getTeamPokemon(p);

         for(int i = 0; i < message.targetting[0].length; ++i) {
            if (message.targetting[0][i]) {
               targets.add(teamPokemon.get(i));
            }
         }

         ArrayList opponentPokemon = bc.getOpponentPokemon(p);

         for(int i = 0; i < message.targetting[1].length; ++i) {
            if (message.targetting[1][i]) {
               targets.add(opponentPokemon.get(i));
            }
         }

         return targets;
      }
   }
}
