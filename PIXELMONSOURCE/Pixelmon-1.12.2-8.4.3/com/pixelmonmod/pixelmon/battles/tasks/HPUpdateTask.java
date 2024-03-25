package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.tasks.DamageTask;
import com.pixelmonmod.pixelmon.client.gui.battles.tasks.HPTask;
import com.pixelmonmod.pixelmon.client.gui.battles.tasks.HealTask;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import javax.annotation.Nullable;

public class HPUpdateTask implements IBattleTask {
   private int healthDifference;
   private UUID pokemonUUID;
   private HPTask task;

   /** @deprecated */
   @Deprecated
   public HPUpdateTask() {
   }

   public HPUpdateTask(PixelmonWrapper user, int healthDifference) {
      this.pokemonUUID = user.getPokemonUUID();
      this.healthDifference = healthDifference;
   }

   public boolean process(ClientBattleManager bm) {
      if (this.task == null) {
         if (this.healthDifference <= 0) {
            this.task = new DamageTask((float)this.healthDifference, this.pokemonUUID);
         } else {
            this.task = new HealTask((float)this.healthDifference, this.pokemonUUID);
         }

         ClientBattleManager.TIMER.scheduleAtFixedRate(this.task, 0L, 8L);
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
      this.pokemonUUID = UUIDHelper.readUUID(buf);
      this.healthDifference = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      UUIDHelper.writeUUID(this.pokemonUUID, buf);
      buf.writeInt(this.healthDifference);
   }
}
