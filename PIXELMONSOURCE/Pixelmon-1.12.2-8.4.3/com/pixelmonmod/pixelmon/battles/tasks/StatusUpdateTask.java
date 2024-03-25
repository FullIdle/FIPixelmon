package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import javax.annotation.Nullable;

public class StatusUpdateTask implements IBattleTask {
   private int status = -1;
   private UUID pokemonUUID;

   /** @deprecated */
   @Deprecated
   public StatusUpdateTask() {
   }

   public StatusUpdateTask(UUID uuid, int status) {
      this.pokemonUUID = uuid;
      this.status = status;
   }

   public boolean process(ClientBattleManager bm) {
      PixelmonInGui pokemon = ClientProxy.battleManager.getPokemon(this.pokemonUUID);
      if (pokemon != null) {
         pokemon.status = this.status;
      }

      return false;
   }

   @Nullable
   public UUID getPokemonID() {
      return this.pokemonUUID;
   }

   public void fromBytes(ByteBuf buf) {
      this.pokemonUUID = UUIDHelper.readUUID(buf);
      this.status = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      UUIDHelper.writeUUID(this.pokemonUUID, buf);
      buf.writeInt(this.status);
   }
}
