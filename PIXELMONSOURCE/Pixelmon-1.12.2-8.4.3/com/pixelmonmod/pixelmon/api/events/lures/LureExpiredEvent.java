package com.pixelmonmod.pixelmon.api.events.lures;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonItemsLures;
import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class LureExpiredEvent extends Event {
   public final EntityPlayerMP player;
   public final PlayerPartyStorage party;
   public final ItemStack lureStack;
   public final ItemLure lure;
   public ItemStack casingStack;

   public LureExpiredEvent(EntityPlayerMP player) {
      this.player = player;
      this.party = Pixelmon.storageManager.getParty(player);
      this.lureStack = this.party.getLureStack();
      this.lure = this.party.getLure();
      this.casingStack = new ItemStack(this.lure.strength == ItemLure.LureStrength.WEAK ? PixelmonItemsLures.weakLureCasing : PixelmonItemsLures.strongLureCasing);
   }

   public ItemLure.LureType getType() {
      return this.lure.type;
   }

   public ItemLure.LureStrength getStrength() {
      return this.lure.strength;
   }
}
