package com.pixelmonmod.pixelmon.api.events.lures;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class LureExpiringEvent extends Event {
   public final EntityPlayerMP player;
   public final PlayerPartyStorage party;
   public final ItemStack lureStack;
   public final ItemLure lure;
   public int damage;

   public LureExpiringEvent(EntityPlayerMP player) {
      this.player = player;
      this.party = Pixelmon.storageManager.getParty(player);
      this.lureStack = this.party.getLureStack();
      this.lure = this.party.getLure();
      this.damage = 1;
   }

   public ItemLure.LureType getType() {
      return this.lure.type;
   }

   public ItemLure.LureStrength getStrength() {
      return this.lure.strength;
   }
}
