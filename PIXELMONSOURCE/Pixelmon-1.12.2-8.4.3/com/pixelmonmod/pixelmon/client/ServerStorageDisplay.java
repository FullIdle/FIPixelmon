package com.pixelmonmod.pixelmon.client;

import com.pixelmonmod.pixelmon.comm.packetHandlers.SelectPokemonListPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.StarterListPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops.ItemDropPacket;
import java.util.ArrayList;
import java.util.List;

public class ServerStorageDisplay {
   public static StarterListPacket starterListPacket;
   public static SelectPokemonListPacket selectPokemonListPacket;
   public static ItemDropPacket bossDrops;
   public static List editedPokemon = new ArrayList(6);
}
