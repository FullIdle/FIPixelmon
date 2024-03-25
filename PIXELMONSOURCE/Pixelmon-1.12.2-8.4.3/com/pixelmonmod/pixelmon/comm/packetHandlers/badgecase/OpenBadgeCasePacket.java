package com.pixelmonmod.pixelmon.comm.packetHandlers.badgecase;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.badgecase.GuiBadgeCase;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.items.EnumBadgeCase;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpenBadgeCasePacket implements IMessage {
   private static final EnumUpdateType[] UPDATE_TYPES;
   String owner;
   boolean allowChanges;
   boolean custom;
   EnumBadgeCase color;
   List badges;
   Pokemon[] party = new Pokemon[6];

   public OpenBadgeCasePacket() {
   }

   public OpenBadgeCasePacket(String owner, boolean allowChanges, boolean custom, EnumBadgeCase color, List badges, List pokemon) {
      this.owner = owner;
      this.allowChanges = allowChanges;
      this.custom = custom;
      this.color = color;
      this.badges = badges;

      for(int i = 0; i < this.party.length; ++i) {
         if (pokemon.size() > i) {
            this.party[i] = (Pokemon)pokemon.get(i);
         }
      }

   }

   public void fromBytes(ByteBuf buf) {
      PacketBuffer buffer = new PacketBuffer(buf);
      this.owner = buffer.func_150789_c(30);
      this.allowChanges = buffer.readBoolean();
      this.custom = buffer.readBoolean();
      this.color = EnumBadgeCase.fromIndex(buffer.readByte());

      for(int i = 0; i < this.party.length; ++i) {
         if (buffer.readBoolean()) {
            this.party[i] = Pixelmon.pokemonFactory.create(UUID.randomUUID()).readFromByteBuffer(buffer, UPDATE_TYPES);
         } else {
            this.party[i] = null;
         }
      }

      this.badges = new ArrayList();

      try {
         NBTTagCompound superCompound = buffer.func_150793_b();
         NBTTagList list = superCompound.func_150295_c("list", 10);
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            NBTBase base = (NBTBase)var5.next();
            this.badges.add(new ItemStack((NBTTagCompound)base));
         }

      } catch (IOException var7) {
         throw new RuntimeException(var7);
      }
   }

   public void toBytes(ByteBuf buf) {
      PacketBuffer buffer = new PacketBuffer(buf);
      buffer.func_180714_a(this.owner);
      buffer.writeBoolean(this.allowChanges);
      buffer.writeBoolean(this.custom);
      buffer.writeByte(this.color.ordinal());

      for(int i = 0; i < this.party.length; ++i) {
         buffer.writeBoolean(this.party[i] != null);
         if (this.party[i] != null) {
            this.party[i].writeToByteBuffer(buffer, UPDATE_TYPES);
         }
      }

      NBTTagCompound superCompound = new NBTTagCompound();
      NBTTagList list = new NBTTagList();
      Iterator var5 = this.badges.iterator();

      while(var5.hasNext()) {
         ItemStack stack = (ItemStack)var5.next();
         list.func_74742_a(stack.func_77955_b(new NBTTagCompound()));
      }

      superCompound.func_74782_a("list", list);
      buffer.func_150786_a(superCompound);
   }

   static {
      UPDATE_TYPES = new EnumUpdateType[]{EnumUpdateType.Nickname, EnumUpdateType.Name, EnumUpdateType.Form, EnumUpdateType.Egg, EnumUpdateType.Texture};
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(OpenBadgeCasePacket message, MessageContext ctx) {
         this.onClient(message, ctx);
      }

      @SideOnly(Side.CLIENT)
      public void onClient(OpenBadgeCasePacket message, MessageContext ctx) {
         if (Minecraft.func_71410_x().field_71462_r instanceof GuiBadgeCase) {
            ((GuiBadgeCase)Minecraft.func_71410_x().field_71462_r).updateBadgeCase(message.owner, message.allowChanges, message.custom, message.color, message.badges, message.party);
         } else {
            Minecraft.func_71410_x().func_147108_a(new GuiBadgeCase(message.owner, message.allowChanges, message.custom, message.color, message.badges, message.party));
         }

      }
   }
}
