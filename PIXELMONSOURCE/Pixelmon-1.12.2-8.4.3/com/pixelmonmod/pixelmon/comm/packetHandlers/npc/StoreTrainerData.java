package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StoreTrainerData implements IMessage {
   int trainerId;
   SetTrainerData data;
   ItemStack[] drops;
   EnumStoreTrainerDataType dataType;

   public StoreTrainerData() {
   }

   public StoreTrainerData(int trainerId, SetTrainerData data) {
      this.trainerId = trainerId;
      this.data = data;
      this.dataType = EnumStoreTrainerDataType.CHAT;
   }

   public StoreTrainerData(int currentTrainerID, ItemStack[] drops) {
      this.trainerId = currentTrainerID;
      this.drops = drops;
      this.dataType = EnumStoreTrainerDataType.DROPS;
   }

   public void fromBytes(ByteBuf buf) {
      this.trainerId = buf.readInt();
      this.dataType = EnumStoreTrainerDataType.values()[buf.readInt()];
      if (this.dataType == EnumStoreTrainerDataType.CHAT) {
         this.data = new SetTrainerData();
         this.data.decodeInto(buf);
      } else {
         this.drops = new ItemStack[buf.readInt()];

         for(int i = 0; i < this.drops.length; ++i) {
            this.drops[i] = ByteBufUtils.readItemStack(buf);
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.trainerId);
      buf.writeInt(this.dataType.ordinal());
      if (this.dataType == EnumStoreTrainerDataType.CHAT) {
         this.data.encodeInto(buf);
      } else {
         buf.writeInt(this.drops.length);
         ItemStack[] var2 = this.drops;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack stack = var2[var4];
            ByteBufUtils.writeItemStack(buf, stack);
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(StoreTrainerData message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (!ItemNPCEditor.checkPermission(player)) {
            return null;
         } else {
            Optional entityNPCOptional = EntityNPC.locateNPCServer(player.field_70170_p, message.trainerId, NPCTrainer.class);
            if (!entityNPCOptional.isPresent()) {
               return null;
            } else {
               NPCTrainer trainer = (NPCTrainer)entityNPCOptional.get();
               if (message.dataType == EnumStoreTrainerDataType.CHAT) {
                  trainer.update(message.data);
               } else {
                  trainer.updateDrops(message.drops);
               }

               return null;
            }
         }
      }
   }
}
