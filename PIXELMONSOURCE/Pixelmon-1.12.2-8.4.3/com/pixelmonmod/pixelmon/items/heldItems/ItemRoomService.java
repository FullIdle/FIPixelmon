package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.TrickRoom;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemRoomService extends HeldItem {
   public ItemRoomService() {
      super(EnumHeldItems.other, "room_service");
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      for(int i = 0; i < newPokemon.bc.globalStatusController.getGlobalStatusSize(); ++i) {
         if (newPokemon.bc.globalStatusController.getGlobalStatus(i) instanceof TrickRoom) {
            newPokemon.getBattleStats().modifyStat(-1, (StatsType)StatsType.Speed);
            newPokemon.bc.sendToAll("pixelmon.helditems.room_service", newPokemon.getNickname());
            newPokemon.consumeItem();
         }
      }

   }

   public void onGlobalStatusAdded(PixelmonWrapper itemHolder, GlobalStatusBase globalStatus) {
      if (globalStatus.type == StatusType.TrickRoom) {
         itemHolder.getBattleStats().modifyStat(-1, (StatsType)StatsType.Speed);
         itemHolder.bc.sendToAll("item.pixelmon.room_service", itemHolder.getNickname());
         itemHolder.consumeItem();
      }

   }
}
