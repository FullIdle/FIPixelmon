package com.pixelmonmod.pixelmon.quests.listeners;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ItemListeners {
   @SubscribeEvent
   public void onApricornHarvest(ApricornEvent.PickApricorn event) throws InvalidQuestArgsException {
      if (!(event.player instanceof FakePlayer)) {
         EntityPlayerMP player = event.player;
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         pps.getQuestData(false).receive("APRICORN_HARVEST", event.getPickedStack());
      }

   }

   @SubscribeEvent
   public void onItemUse(PlayerInteractEvent.RightClickItem event) throws InvalidQuestArgsException {
      if (event.getSide() == Side.SERVER && event.getHand() == EnumHand.MAIN_HAND && !(event.getEntityPlayer() instanceof FakePlayer)) {
         EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         pps.getQuestData(false).receive("ITEM_USE", event.getItemStack());
      }

   }

   @SubscribeEvent
   public void onPickupItem(PlayerEvent.ItemPickupEvent event) throws InvalidQuestArgsException {
      if (event.player instanceof EntityPlayerMP && !(event.player instanceof FakePlayer)) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         pps.getQuestData(false).receive("ITEM_PICKUP", event.getStack());
      }

   }

   @SubscribeEvent
   public void onDropItem(ItemTossEvent event) throws InvalidQuestArgsException {
      if (event.getPlayer() instanceof EntityPlayerMP && !(event.getPlayer() instanceof FakePlayer)) {
         EntityPlayerMP player = (EntityPlayerMP)event.getPlayer();
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         pps.getQuestData(false).receive("ITEM_DROP", event.getEntityItem().func_92059_d());
      }

   }

   @SubscribeEvent
   public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) throws InvalidQuestArgsException {
      if (event.player instanceof EntityPlayerMP && !(event.player instanceof FakePlayer)) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         pps.getQuestData(true).receive("ITEM_CRAFT", event.crafting);
      }

   }

   @SubscribeEvent
   public void onItemSmelted(PlayerEvent.ItemSmeltedEvent event) throws InvalidQuestArgsException {
      if (event.player instanceof EntityPlayerMP && !(event.player instanceof FakePlayer)) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         pps.getQuestData(true).receive("ITEM_SMELT", event.smelting);
      }

   }

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) throws InvalidQuestArgsException {
      if (event.player instanceof EntityPlayerMP && !(event.player instanceof FakePlayer) && event.player.field_70170_p != null && event.player.field_70170_p.func_82737_E() % 40L == 0L) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         InventoryPlayer inventory = player.field_71071_by;
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         QuestData data = pps.getQuestData(false);
         ArrayList items = new ArrayList(inventory.field_70462_a);
         data.receiveMultipleInternal(new String[]{"ITEM_HAS", "ITEM_HAS_NOT"}, new Object[][]{{items}, {items}}, false);
      }

   }
}
