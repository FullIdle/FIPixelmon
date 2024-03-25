package com.pixelmonmod.pixelmon.entities.pixelmon.drops;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.DropEvent;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops.ItemDropMode;
import com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops.ItemDropPacket;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity8HoldsItems;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;

public class DropItemQueryList {
   public static List queryList = Collections.synchronizedList(new ArrayList());

   public static void register(Entity8HoldsItems pixelmon, ArrayList givenDrops, EntityPlayerMP player) {
      DropEvent event = new DropEvent(player, pixelmon, pixelmon.isBossPokemon() ? ItemDropMode.Boss : ItemDropMode.NormalPokemon, givenDrops);
      if (!Pixelmon.EVENT_BUS.post(event)) {
         givenDrops = new ArrayList(event.getDrops());
         DropItemQuery diq = new DropItemQuery(new Vec3d(pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v), player.func_110124_au(), givenDrops);
         queryList.add(diq);
         if (pixelmon.isBossPokemon()) {
            Pixelmon.network.sendTo(new ItemDropPacket(ItemDropMode.Boss, givenDrops), player);
         } else {
            Pixelmon.network.sendTo(new ItemDropPacket(ItemDropMode.NormalPokemon, ChatHandler.getMessage("gui.guiItemDrops.beatPokemon", pixelmon.getNickname()), givenDrops), player);
         }

      }
   }

   public static void register(NPCTrainer npc, ArrayList givenDrops, EntityPlayerMP player) {
      DropEvent event = new DropEvent(player, npc, npc.isGymLeader ? ItemDropMode.Other : ItemDropMode.NormalTrainer, givenDrops);
      if (!Pixelmon.EVENT_BUS.post(event)) {
         givenDrops = new ArrayList(event.getDrops());
         DropItemQuery diq = new DropItemQuery(new Vec3d(npc.field_70165_t, npc.field_70163_u, npc.field_70161_v), player.func_110124_au(), givenDrops);
         queryList.add(diq);
         ItemDropPacket p;
         if (npc.isGymLeader) {
            p = new ItemDropPacket(ChatHandler.getMessage("pixelmon.entitytrainer.dropitemstitle", npc.getName(player.field_71148_cg)), givenDrops);
         } else {
            p = new ItemDropPacket(ItemDropMode.NormalTrainer, givenDrops);
         }

         Pixelmon.network.sendTo(p, player);
      }
   }

   public static void takeAllItems(EntityPlayerMP player) {
      for(int i = 0; i < queryList.size(); ++i) {
         DropItemQuery query = (DropItemQuery)queryList.get(i);
         if (query.playerUUID.equals(player.func_110124_au())) {
            Iterator var3 = query.droppedItemList.iterator();

            while(var3.hasNext()) {
               DroppedItem droppedItem = (DroppedItem)var3.next();
               DropItemHelper.giveItemStack(player, droppedItem.itemStack, false);
            }
         }
      }

      removeQuery(player);
   }

   public static void dropAllItems(EntityPlayerMP player) {
      if (!PixelmonConfig.deleteUnwantedDrops) {
         for(int i = 0; i < queryList.size(); ++i) {
            if (((DropItemQuery)queryList.get(i)).playerUUID.equals(player.func_110124_au())) {
               Iterator var2 = ((DropItemQuery)queryList.get(i)).droppedItemList.iterator();

               while(var2.hasNext()) {
                  DroppedItem droppedItem = (DroppedItem)var2.next();
                  boolean var10003 = droppedItem.rarity != EnumBossMode.NotBoss;
                  DropItemHelper.dropItemOnGround(((DropItemQuery)queryList.get(i)).position, player, droppedItem.itemStack, var10003, false);
               }
            }
         }
      }

      removeQuery(player);
   }

   public static void takeItem(EntityPlayerMP player, int itemID) {
      for(int i = 0; i < queryList.size(); ++i) {
         DropItemQuery query = (DropItemQuery)queryList.get(i);
         if (query.playerUUID.equals(player.func_110124_au())) {
            for(int j = 0; j < query.droppedItemList.size(); ++j) {
               DroppedItem droppedItem = (DroppedItem)query.droppedItemList.get(j);
               if (droppedItem.id == itemID) {
                  DropItemHelper.giveItemStack(player, droppedItem.itemStack, false);
                  query.droppedItemList.remove(j);
                  if (query.droppedItemList.isEmpty()) {
                     removeQuery(player);
                  }

                  return;
               }
            }
         }
      }

   }

   public static void removeQuery(EntityPlayerMP player) {
      for(int i = 0; i < queryList.size(); ++i) {
         if (((DropItemQuery)queryList.get(i)).playerUUID.equals(player.func_110124_au())) {
            queryList.remove(i);
            --i;
         }
      }

   }
}
