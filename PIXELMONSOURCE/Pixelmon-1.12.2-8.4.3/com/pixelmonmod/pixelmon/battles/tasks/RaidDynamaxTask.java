package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;

public class RaidDynamaxTask implements IBattleTask {
   public UUID playerUUID;

   /** @deprecated */
   @Deprecated
   public RaidDynamaxTask() {
   }

   public RaidDynamaxTask(UUID playerUUID) {
      this.playerUUID = playerUUID;
   }

   public boolean process(ClientBattleManager bm) {
      bm.dynamaxDisabled = !Minecraft.func_71410_x().field_71439_g.func_110124_au().equals(this.playerUUID);
      return false;
   }

   @Nullable
   public UUID getPokemonID() {
      return null;
   }

   public void fromBytes(ByteBuf buf) {
      this.playerUUID = UUIDHelper.readUUID(buf);
   }

   public void toBytes(ByteBuf buf) {
      UUIDHelper.writeUUID(this.playerUUID, buf);
   }
}
