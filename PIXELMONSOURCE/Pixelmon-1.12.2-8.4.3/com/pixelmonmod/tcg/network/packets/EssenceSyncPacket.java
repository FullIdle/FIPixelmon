package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.helper.EssenceHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EssenceSyncPacket implements IMessage {
   private int grass;
   private int fire;
   private int water;
   private int lightning;
   private int fighting;
   private int psychic;
   private int colorless;
   private int darkness;
   private int metal;
   private int dragon;
   private int fairy;

   public EssenceSyncPacket() {
   }

   public EssenceSyncPacket(int grass, int fire, int water, int lightning, int fighting, int psychic, int colorless, int darkness, int metal, int dragon, int fairy) {
      this.grass = grass;
      this.fire = fire;
      this.water = water;
      this.lightning = lightning;
      this.fighting = fighting;
      this.psychic = psychic;
      this.colorless = colorless;
      this.darkness = darkness;
      this.dragon = dragon;
      this.fairy = fairy;
      this.metal = metal;
   }

   public EssenceSyncPacket(EntityPlayerMP player) {
      this.grass = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.GRASS);
      this.fire = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.FIRE);
      this.water = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.WATER);
      this.lightning = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.LIGHTNING);
      this.fighting = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.FIGHTING);
      this.psychic = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.PSYCHIC);
      this.colorless = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.COLORLESS);
      this.darkness = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.DARKNESS);
      this.dragon = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.DRAGON);
      this.fairy = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.FAIRY);
      this.metal = EssenceHelper.getPlayerEssenceFromEnergy(player, Energy.METAL);
   }

   public void fromBytes(ByteBuf buf) {
      this.grass = buf.readInt();
      this.fire = buf.readInt();
      this.water = buf.readInt();
      this.lightning = buf.readInt();
      this.fighting = buf.readInt();
      this.psychic = buf.readInt();
      this.colorless = buf.readInt();
      this.darkness = buf.readInt();
      this.metal = buf.readInt();
      this.dragon = buf.readInt();
      this.fairy = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.grass);
      buf.writeInt(this.fire);
      buf.writeInt(this.water);
      buf.writeInt(this.lightning);
      buf.writeInt(this.fighting);
      buf.writeInt(this.psychic);
      buf.writeInt(this.colorless);
      buf.writeInt(this.darkness);
      buf.writeInt(this.metal);
      buf.writeInt(this.dragon);
      buf.writeInt(this.fairy);
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(EssenceSyncPacket message, MessageContext ctx) {
         Minecraft mc = Minecraft.func_71410_x();
         EntityPlayer e = mc.field_71439_g;
         if (e != null) {
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.GRASS, message.grass);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.FIRE, message.fire);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.WATER, message.water);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.LIGHTNING, message.lightning);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.FIGHTING, message.fighting);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.PSYCHIC, message.psychic);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.COLORLESS, message.colorless);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.DARKNESS, message.darkness);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.METAL, message.metal);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.DRAGON, message.dragon);
            EssenceHelper.setPlayerEssenceFromEnergy(e, Energy.FAIRY, message.fairy);
         }

         return null;
      }
   }
}
