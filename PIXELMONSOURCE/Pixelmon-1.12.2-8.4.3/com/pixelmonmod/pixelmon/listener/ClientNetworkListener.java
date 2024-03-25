package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ClientNetworkListener {
   @SubscribeEvent
   public static void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
      ClientStorageManager.load();
      ClientProxy.battleManager.battleEnded = true;
      Pixelmon.proxy.loadDefaultMoveSkills();
   }
}
