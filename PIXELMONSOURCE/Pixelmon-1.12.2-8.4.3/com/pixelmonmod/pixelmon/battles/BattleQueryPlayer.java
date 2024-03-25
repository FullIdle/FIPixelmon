package com.pixelmonmod.pixelmon.battles;

import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.EnumBattleQueryResponse;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;

public class BattleQueryPlayer {
   public EntityPlayerMP player;
   public EntityPixelmon pokemon;
   public EnumBattleQueryResponse response;
   public int clauseVersionNumber;

   public BattleQueryPlayer(EntityPlayerMP player, EntityPixelmon pokemon) {
      this.player = player;
      this.pokemon = pokemon;
      this.response = EnumBattleQueryResponse.None;
   }

   public PlayerParticipant getParticipant() {
      return new PlayerParticipant(this.player, new EntityPixelmon[]{this.pokemon});
   }

   public PlayerParticipant getParticipant(EntityPixelmon second) {
      return new PlayerParticipant(this.player, new EntityPixelmon[]{this.pokemon, second});
   }
}
