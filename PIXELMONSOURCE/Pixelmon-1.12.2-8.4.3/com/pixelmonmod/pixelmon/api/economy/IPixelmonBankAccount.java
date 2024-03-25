package com.pixelmonmod.pixelmon.api.economy;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.UpdateClientPlayerData;
import java.util.UUID;
import net.minecraftforge.fml.common.FMLCommonHandler;

public interface IPixelmonBankAccount {
   int getMoney();

   void setMoney(int var1);

   int changeMoney(int var1);

   UUID getOwnerUUID();

   default void updatePlayer(int amount) {
      if (FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.getOwnerUUID()) != null) {
         Pixelmon.network.sendTo(new UpdateClientPlayerData(amount), FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.getOwnerUUID()));
      }
   }

   default void updatePlayer() {
      this.updatePlayer(this.getMoney());
   }
}
