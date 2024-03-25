package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.Tier;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateClientRules implements IMessage {
   int clauseVersion = BattleClauseRegistry.getClauseVersion();
   List customClauses = BattleClauseRegistry.getClauseRegistry().getCustomClauses();
   List customTiers = BattleClauseRegistry.getTierRegistry().getCustomClauses();

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.clauseVersion);
      ArrayHelper.encodeList(buf, this.customClauses);
      ArrayHelper.encodeList(buf, this.customTiers);
   }

   public void fromBytes(ByteBuf buf) {
      this.clauseVersion = buf.readInt();
      this.customClauses = new ArrayList();
      int length = buf.readInt();

      int i;
      for(i = 0; i < length; ++i) {
         this.customClauses.add((new BattleClause(ByteBufUtils.readUTF8String(buf))).setDescription(ByteBufUtils.readUTF8String(buf)));
      }

      this.customTiers = new ArrayList();
      length = buf.readInt();

      for(i = 0; i < length; ++i) {
         this.customTiers.add((Tier)(new Tier(ByteBufUtils.readUTF8String(buf))).setDescription(ByteBufUtils.readUTF8String(buf)));
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UpdateClientRules message, MessageContext ctx) {
         if (!Minecraft.func_71410_x().func_71356_B()) {
            BattleClauseRegistry.getClauseRegistry().replaceCustomClauses(message.customClauses, message.clauseVersion);
            BattleClauseRegistry.getTierRegistry().replaceCustomClauses(message.customTiers, message.clauseVersion);
         }

         CheckRulesVersion.processStoredPacket(ctx);
         return null;
      }
   }
}
