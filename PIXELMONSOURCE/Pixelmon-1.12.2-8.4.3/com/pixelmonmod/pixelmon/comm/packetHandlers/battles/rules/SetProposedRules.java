package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiBattleRulesPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetProposedRules implements IMessage {
   BattleRules rules;

   public SetProposedRules() {
   }

   public SetProposedRules(BattleRules rules) {
      this.rules = rules;
   }

   public void toBytes(ByteBuf buf) {
      this.rules.encodeInto(buf);
   }

   public void fromBytes(ByteBuf buf) {
      this.rules = new BattleRules(buf);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetProposedRules message, MessageContext ctx) {
         Minecraft mc = Minecraft.func_71410_x();
         GuiScreen currentScreen = mc.field_71462_r;
         if (currentScreen instanceof GuiBattleRulesPlayer) {
            GuiBattleRulesPlayer rulesScreen = (GuiBattleRulesPlayer)currentScreen;
            mc.func_152344_a(() -> {
               rulesScreen.setRules(message.rules);
            });
         }

         return null;
      }
   }
}
