package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IBattleTask extends IMessage {
   Handler HANDLER = new Handler();

   boolean process(ClientBattleManager var1);

   @Nullable
   UUID getPokemonID();

   default boolean shouldRunParallel() {
      return false;
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(IBattleTask message, MessageContext ctx) {
         if (ClientProxy.battleManager != null && ClientProxy.battleManager.displayedOurPokemon != null) {
            ClientProxy.battleManager.addBattleMessage(message);
         }
      }
   }
}
