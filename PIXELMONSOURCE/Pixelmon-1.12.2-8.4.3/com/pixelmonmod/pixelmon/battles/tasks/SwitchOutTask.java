package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.gui.battles.tasks.SwitchTask;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import javax.annotation.Nullable;

public class SwitchOutTask implements IBattleTask {
   UUID pokemonUUID;
   PixelmonInGui newPokemon;
   SwitchTask task;

   /** @deprecated */
   @Deprecated
   public SwitchOutTask() {
   }

   public SwitchOutTask(UUID pokemonUUID, PixelmonInGui newPokemon) {
      this.pokemonUUID = pokemonUUID;
      this.newPokemon = newPokemon;
   }

   public SwitchOutTask(UUID pix1UUID, PixelmonWrapper newPokemon) {
      this.pokemonUUID = pix1UUID;
      this.newPokemon = new PixelmonInGui(newPokemon);
   }

   public SwitchOutTask(UUID pix1UUID) {
      this.pokemonUUID = pix1UUID;
      this.newPokemon = null;
   }

   public boolean process(ClientBattleManager bm) {
      if (this.task == null) {
         this.task = new SwitchTask(this.pokemonUUID, this.newPokemon);
         ClientBattleManager.TIMER.scheduleAtFixedRate(this.task, 0L, 5L);
      }

      return !this.task.isDone();
   }

   @Nullable
   public UUID getPokemonID() {
      return this.pokemonUUID;
   }

   public boolean shouldRunParallel() {
      return true;
   }

   public void fromBytes(ByteBuf buf) {
      if (buf.readBoolean()) {
         this.pokemonUUID = new UUID(buf.readLong(), buf.readLong());
      }

      if (buf.readBoolean()) {
         this.newPokemon = new PixelmonInGui();
         this.newPokemon.decodeFrom(buf);
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.pokemonUUID != null);
      if (this.pokemonUUID != null) {
         PixelmonMethods.toBytesUUID(buf, this.pokemonUUID);
      }

      buf.writeBoolean(this.newPokemon != null);
      if (this.newPokemon != null) {
         this.newPokemon.encodeInto(buf);
      }

   }
}
