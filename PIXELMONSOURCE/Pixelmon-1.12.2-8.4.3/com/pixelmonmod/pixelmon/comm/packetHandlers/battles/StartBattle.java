package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StartBattle implements IMessage {
   int battleIndex;
   ParticipantType[][] type;
   int afkActivate;
   int afkTurn;
   int catchCombo;
   BattleRules rules;

   public StartBattle() {
   }

   public StartBattle(int battleIndex, ParticipantType[][] type, BattleRules rules) {
      this(battleIndex, type, -1, -1, 0, rules);
   }

   public StartBattle(int battleIndex, ParticipantType[][] type, int afkActivate, int afkTurn, int catchCombo, BattleRules rules) {
      this.battleIndex = battleIndex;
      this.type = type;
      this.afkActivate = afkActivate;
      this.afkTurn = afkTurn;
      this.catchCombo = catchCombo;
      this.rules = rules;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.battleIndex);

      for(int i = 0; i < 2; ++i) {
         buffer.writeShort(this.type[i].length);
         ParticipantType[] var3 = this.type[i];
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ParticipantType p = var3[var5];
            buffer.writeInt(p.ordinal());
         }
      }

      buffer.writeInt(this.afkActivate);
      buffer.writeInt(this.afkTurn);
      buffer.writeInt(this.catchCombo);
      this.rules.encodeInto(buffer);
   }

   public void fromBytes(ByteBuf buffer) {
      this.battleIndex = buffer.readInt();
      this.type = new ParticipantType[2][];

      for(int i = 0; i < 2; ++i) {
         this.type[i] = new ParticipantType[buffer.readShort()];

         for(int j = 0; j < this.type[i].length; ++j) {
            this.type[i][j] = ParticipantType.get(buffer.readInt());
         }
      }

      this.afkActivate = buffer.readInt();
      this.afkTurn = buffer.readInt();
      this.catchCombo = buffer.readInt();
      this.rules = new BattleRules(buffer);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(StartBattle message, MessageContext ctx) {
         ClientProxy.battleManager.startBattle(message.battleIndex, message.type, message.afkActivate, message.afkTurn, message.rules);
         ClientProxy.battleManager.catchCombo = message.catchCombo;
         return null;
      }
   }
}
