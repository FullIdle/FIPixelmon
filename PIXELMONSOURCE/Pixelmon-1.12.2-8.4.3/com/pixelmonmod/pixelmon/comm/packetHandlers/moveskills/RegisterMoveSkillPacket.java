package com.pixelmonmod.pixelmon.comm.packetHandlers.moveskills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RegisterMoveSkillPacket implements IMessage {
   public MoveSkill moveSkill;
   public boolean unregister = false;

   public RegisterMoveSkillPacket() {
   }

   public RegisterMoveSkillPacket(MoveSkill moveSkill) {
      this.moveSkill = moveSkill;
   }

   public RegisterMoveSkillPacket(MoveSkill moveSkill, boolean unregister) {
      this.moveSkill = moveSkill;
      this.unregister = unregister;
   }

   public void fromBytes(ByteBuf buf) {
      this.moveSkill = new MoveSkill(ByteBufUtils.readUTF8String(buf));
      this.unregister = buf.readBoolean();
      if (!this.unregister) {
         this.moveSkill.setName(ByteBufUtils.readUTF8String(buf));
         int descCount = buf.readByte();

         for(int i = 0; i < descCount; ++i) {
            this.moveSkill.descriptions.add(ByteBufUtils.readUTF8String(buf));
         }

         this.moveSkill.sprite = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
         this.moveSkill.anyMoves = new ArrayList();
         int n = buf.readUnsignedByte();

         int i;
         for(i = 0; i < n; ++i) {
            this.moveSkill.anyMoves.add(Integer.valueOf(buf.readShort()));
         }

         this.moveSkill.combinationMoves = new ArrayList();
         n = buf.readUnsignedByte();

         for(i = 0; i < n; ++i) {
            this.moveSkill.combinationMoves.add(Integer.valueOf(buf.readShort()));
         }

         this.moveSkill.ableSpecs = new ArrayList();
         n = buf.readUnsignedByte();

         for(i = 0; i < n; ++i) {
            this.moveSkill.ableSpecs.add(new PokemonSpec(buf));
         }

         n = buf.readUnsignedByte();

         for(i = 0; i < n; ++i) {
            this.moveSkill.intrinsicSpecs.add(new PokemonSpec(buf));
         }

         int n = buf.readByte();

         for(i = 0; i < n; ++i) {
            this.moveSkill.behaviours.put(MoveSkill.EnumTargetType.values()[buf.readByte()], (pix, o) -> {
               return 1;
            });
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.moveSkill.id);
      buf.writeBoolean(this.unregister);
      if (!this.unregister) {
         ByteBufUtils.writeUTF8String(buf, this.moveSkill.name);
         buf.writeByte(this.moveSkill.descriptions.size());
         Iterator var2 = this.moveSkill.descriptions.iterator();

         while(var2.hasNext()) {
            String description = (String)var2.next();
            ByteBufUtils.writeUTF8String(buf, description);
         }

         ByteBufUtils.writeUTF8String(buf, this.moveSkill.sprite.toString());
         buf.writeByte(this.moveSkill.anyMoves.size());
         var2 = this.moveSkill.anyMoves.iterator();

         Integer moveID;
         while(var2.hasNext()) {
            moveID = (Integer)var2.next();
            buf.writeShort(moveID);
         }

         buf.writeByte(this.moveSkill.combinationMoves.size());
         var2 = this.moveSkill.combinationMoves.iterator();

         while(var2.hasNext()) {
            moveID = (Integer)var2.next();
            buf.writeShort(moveID);
         }

         buf.writeByte(this.moveSkill.ableSpecs.size());
         var2 = this.moveSkill.ableSpecs.iterator();

         PokemonSpec spec;
         while(var2.hasNext()) {
            spec = (PokemonSpec)var2.next();
            spec.toBytes(buf);
         }

         buf.writeByte(this.moveSkill.intrinsicSpecs.size());
         var2 = this.moveSkill.intrinsicSpecs.iterator();

         while(var2.hasNext()) {
            spec = (PokemonSpec)var2.next();
            spec.toBytes(buf);
         }

         buf.writeByte(this.moveSkill.behaviours.keySet().size());
         var2 = this.moveSkill.behaviours.keySet().iterator();

         while(var2.hasNext()) {
            MoveSkill.EnumTargetType targetType = (MoveSkill.EnumTargetType)var2.next();
            buf.writeByte((byte)targetType.ordinal());
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RegisterMoveSkillPacket message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            ClientProxy.moveSkills.removeIf((moveSkill) -> {
               return moveSkill.id.equals(message.moveSkill.id);
            });
            if (!message.unregister) {
               ClientProxy.moveSkills.add(message.moveSkill);
            }

         });
         return null;
      }
   }
}
