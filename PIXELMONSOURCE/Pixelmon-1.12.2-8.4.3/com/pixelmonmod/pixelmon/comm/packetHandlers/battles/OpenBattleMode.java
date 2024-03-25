package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpenBattleMode implements IMessage {
   BattleMode mode;
   int position = -1;

   public OpenBattleMode() {
   }

   public OpenBattleMode(BattleMode mode) {
      this.mode = mode;
   }

   public OpenBattleMode(BattleMode mode, int position) {
      this.mode = mode;
      this.position = position;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.mode.ordinal());
      buf.writeInt(this.position);
   }

   public void fromBytes(ByteBuf buf) {
      this.mode = BattleMode.values()[buf.readInt()];
      this.position = buf.readInt();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(OpenBattleMode message, MessageContext ctx) {
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(OpenBattleMode message) {
         ClientProxy.battleManager.mode = message.mode;
         ClientProxy.battleManager.choosingPokemon = true;
         ClientProxy.battleManager.teamPokemon = null;
         if (message.position != -1) {
            ClientProxy.battleManager.currentPokemon = message.position;
         }

         Minecraft.func_71410_x().func_147108_a(new GuiBattle());
      }
   }
}
