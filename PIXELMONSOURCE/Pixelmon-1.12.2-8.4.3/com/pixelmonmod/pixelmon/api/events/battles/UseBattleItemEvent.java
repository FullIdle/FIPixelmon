package com.pixelmonmod.pixelmon.api.events.battles;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class UseBattleItemEvent extends Event {
   public final BattleControllerBase bcb;
   public final BattleParticipant participant;
   public final PixelmonWrapper user;
   public PixelmonWrapper target;
   public ItemStack stack;
   public int additionalInfo;

   public UseBattleItemEvent(PixelmonWrapper user, PixelmonWrapper target, ItemStack stack, int additionalInfo) {
      this.bcb = user.bc;
      this.participant = user.getParticipant();
      this.user = user;
      this.target = target;
      this.stack = stack;
      this.additionalInfo = additionalInfo;
   }
}
