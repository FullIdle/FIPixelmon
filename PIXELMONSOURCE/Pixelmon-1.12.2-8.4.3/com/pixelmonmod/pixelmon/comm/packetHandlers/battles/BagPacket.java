package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BagPacket implements IMessage {
   public boolean fromPokemon;
   int itemIndex;
   int battleIndex;
   int additionalInfo;
   UUID pokemonUUID;
   UUID targetPokemonUUID;

   public BagPacket() {
   }

   public BagPacket(UUID pokemonUUID, int itemIndex, int battleIndex, int additionalInfo) {
      this.fromPokemon = false;
      this.pokemonUUID = pokemonUUID;
      this.itemIndex = itemIndex;
      this.battleIndex = battleIndex;
      this.additionalInfo = additionalInfo;
   }

   public BagPacket(UUID pokemonUUID, UUID targetPokemonUUID, int itemIndex, int battleIndex) {
      this.fromPokemon = true;
      this.pokemonUUID = pokemonUUID;
      this.targetPokemonUUID = targetPokemonUUID;
      this.itemIndex = itemIndex;
      this.battleIndex = battleIndex;
   }

   private void bagPacketFromPokemon(EntityPlayer player) {
      ItemStack usedStack = null;
      Iterator var3 = player.field_71071_by.field_70462_a.iterator();

      while(var3.hasNext()) {
         ItemStack i = (ItemStack)var3.next();
         if (i != null && Item.func_150891_b(i.func_77973_b()) == this.itemIndex) {
            usedStack = i;
            break;
         }
      }

      if (usedStack == null) {
         ChatHandler.sendChat(player, "bagpacket.itemnotfound");
      } else {
         BattleControllerBase bc = BattleRegistry.getBattle(this.battleIndex);
         if (bc == null) {
            ChatHandler.sendChat(player, "bagpacket.battlenotfound");
         } else {
            bc.setUseItem(this.pokemonUUID, player, usedStack, this.targetPokemonUUID);
         }
      }
   }

   private void bagPacket(EntityPlayer player) {
      ItemStack usedStack = null;
      Iterator var3 = player.field_71071_by.field_70462_a.iterator();

      while(var3.hasNext()) {
         ItemStack i = (ItemStack)var3.next();
         if (i != null && Item.func_150891_b(i.func_77973_b()) == this.itemIndex) {
            usedStack = i;
            break;
         }
      }

      if (usedStack == null) {
         ChatHandler.sendChat(player, "bagpacket.itemnotfound");
      } else {
         BattleControllerBase bc = BattleRegistry.getBattle(this.battleIndex);
         if (bc == null) {
            ChatHandler.sendChat(player, "bagpacket.battlenotfound");
         } else {
            if (bc.isWaiting()) {
               bc.setUseItem(this.pokemonUUID, player, usedStack, this.additionalInfo);
            }

         }
      }
   }

   public void fromBytes(ByteBuf buffer) {
      this.fromPokemon = buffer.readBoolean();
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.itemIndex = buffer.readInt();
      this.battleIndex = buffer.readInt();
      if (!this.fromPokemon) {
         this.additionalInfo = buffer.readInt();
      } else {
         this.targetPokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBoolean(this.fromPokemon);
      PixelmonMethods.toBytesUUID(buffer, this.pokemonUUID);
      buffer.writeInt(this.itemIndex);
      buffer.writeInt(this.battleIndex);
      if (!this.fromPokemon) {
         buffer.writeInt(this.additionalInfo);
      } else {
         PixelmonMethods.toBytesUUID(buffer, this.targetPokemonUUID);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(BagPacket message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_143004_u();
         if (!message.fromPokemon) {
            message.bagPacket(ctx.getServerHandler().field_147369_b);
         } else {
            message.bagPacketFromPokemon(ctx.getServerHandler().field_147369_b);
         }

      }
   }
}
