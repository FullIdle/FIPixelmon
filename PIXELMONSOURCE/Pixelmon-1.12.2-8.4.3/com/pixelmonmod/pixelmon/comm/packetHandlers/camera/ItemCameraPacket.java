package com.pixelmonmod.pixelmon.comm.packetHandlers.camera;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CameraEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ItemCameraPacket implements IMessage {
   public int entityId;

   public ItemCameraPacket() {
   }

   public ItemCameraPacket(int entityID) {
      this.entityId = entityID;
   }

   public void fromBytes(ByteBuf buf) {
      this.entityId = ByteBufUtils.readVarInt(buf, 4);
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeVarInt(buf, this.entityId, 4);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ItemCameraPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         player.func_184102_h().func_152344_a(() -> {
            EntityPixelmon pixelmon = (EntityPixelmon)((EntityPixelmon)player.field_70170_p.func_73045_a(message.entityId));
            if (pixelmon != null) {
               if (!PixelmonConfig.allowMultiplePhotosOfSamePokemon && pixelmon.cameraCapturedPlayers.contains(player.func_110124_au()) && !Pixelmon.EVENT_BUS.post(new CameraEvent.DuplicatePhoto(player, pixelmon))) {
                  player.func_145747_a(new TextComponentTranslation("camera.error.samepokemon", new Object[]{Entity1Base.getLocalizedName(pixelmon.getBaseStats().getPokemonName())}));
               } else if (Pixelmon.EVENT_BUS.post(new CameraEvent.ConsumeFilm(player, pixelmon)) || player.field_71071_by.func_174925_a(PixelmonItems.filmItem, 0, 1, (NBTTagCompound)null) == 1) {
                  if (!PixelmonConfig.allowMultiplePhotosOfSamePokemon) {
                     pixelmon.cameraCapturedPlayers.add(player.func_110124_au());
                  }

                  ItemStack photo = ItemPixelmonSprite.getPhoto(pixelmon.getPokemonData());
                  CameraEvent.TakePhoto event = new CameraEvent.TakePhoto(player, pixelmon, photo);
                  if (!Pixelmon.EVENT_BUS.post(event)) {
                     player.field_71071_by.func_70441_a(event.photo);
                     pixelmon.field_70170_p.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, PixelSounds.cameraShutter, SoundCategory.AMBIENT, 1.0F, 1.0F);
                  }
               }
            }

         });
         return null;
      }
   }
}
