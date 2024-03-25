package com.pixelmonmod.pixelmon.quests.listeners;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

public class TickListeners {
   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) throws InvalidQuestArgsException {
      if (event.phase != Phase.START) {
         if (event.side == Side.SERVER) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
            QuestData data = pps.getQuestData(false);
            if (player.field_70170_p != null && player.field_70170_p.func_82737_E() % 40L == 0L) {
               data.receiveMultiple(new String[]{"STRUCTURE", "ENTITY_VICINITY", "TILEENTITY_VICINITY", "TEST_DATA", "FLAG"}, new Object[][]{new Object[0], new Object[0], new Object[0], new Object[0], new Object[0]});
            }

            data.receiveMultiple(new String[]{"WORLD_TIME", "SERVER_TIME", "TIMER", "RANDOM", "FOLLOWTHROUGH", "DATE", "QUERY", "ABSOLUTE_POSITION", "NPC_TIMED_INSERTER", "DEX_VALUES_SPEC_INSERTER", "TYPE_VALUES_SPEC_INSERTER", "NAME_INSERTER", "STRING_INSERTER", "INTEGER_INSERTER", "DECIMAL_INSERTER", "TYPING_INSERTER", "BOOLEAN_INSERTER", "CONCATENATE_INSERTER", "OPERATION_INSERTER"}, new Object[][]{{player.func_71121_q().func_72820_D() % 24000L}, new Object[0], new Object[0], new Object[0], new Object[0], new Object[0], new Object[0], {player.func_180425_c(), player.field_71093_bK}, new Object[0], new Object[0], new Object[0], new Object[0], new Object[0], new Object[0], new Object[0], new Object[0], new Object[0], new Object[0], new Object[0], new Object[0]});
         }

      }
   }
}
