package com.pixelmonmod.pixelmon.battles.attacks;

import java.util.UUID;

public abstract class BattleMessageBase {
   public MessageType messageType;
   public boolean viewed = false;
   public UUID pokemonUUID;

   public abstract void process();
}
