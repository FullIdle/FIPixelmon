package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import javax.annotation.Nullable;

public class FailedSwitchFleeTask implements IBattleTask {
   public boolean process(ClientBattleManager bm) {
      bm.mode = BattleMode.EnforcedSwitch;
      bm.enforcedFleeFailed = true;
      return false;
   }

   @Nullable
   public UUID getPokemonID() {
      return null;
   }

   public void toBytes(ByteBuf buffer) {
   }

   public void fromBytes(ByteBuf buffer) {
   }
}
