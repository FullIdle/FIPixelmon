package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class EnforcedSwitchTask implements IBattleMessage {
   public int switchPosition = -1;

   /** @deprecated */
   @Deprecated
   public EnforcedSwitchTask() {
   }

   public EnforcedSwitchTask(int switchPosition) {
      this.switchPosition = switchPosition;
   }

   public ITextComponent getMessage() {
      return new TextComponentTranslation("battlecontroller.switch", new Object[0]);
   }

   public boolean process(ClientBattleManager bm) {
      bm.mode = BattleMode.EnforcedSwitch;
      bm.oldMode = BattleMode.MainMenu;
      bm.currentPokemon = this.switchPosition;
      bm.enforcedFleeFailed = false;
      return false;
   }

   @Nullable
   public UUID getPokemonID() {
      return null;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.switchPosition);
   }

   public void fromBytes(ByteBuf buffer) {
      this.switchPosition = buffer.readInt();
   }
}
