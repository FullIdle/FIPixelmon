package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.client.gui.battles.rules.EnumRulesGuiState;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiBattleRulesPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateBattleQueryRules implements IMessage {
   EnumRulesGuiState state;

   public UpdateBattleQueryRules() {
   }

   public UpdateBattleQueryRules(EnumRulesGuiState state) {
      this.state = state;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.state.ordinal());
   }

   public void fromBytes(ByteBuf buf) {
      this.state = EnumRulesGuiState.getFromOrdinal(buf.readInt());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UpdateBattleQueryRules message, MessageContext ctx) {
         GuiScreen currentScreen = Minecraft.func_71410_x().field_71462_r;
         if (currentScreen instanceof GuiBattleRulesPlayer) {
            GuiBattleRulesPlayer rulesScreen = (GuiBattleRulesPlayer)currentScreen;
            rulesScreen.changeState(message.state);
         }

         return null;
      }
   }
}
