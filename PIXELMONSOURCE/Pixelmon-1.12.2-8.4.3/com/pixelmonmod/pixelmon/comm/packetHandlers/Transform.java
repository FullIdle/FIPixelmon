package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.battles.status.Transformed;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Transform implements IMessage {
   public int pixelmonID;
   public EnumSpecies transformedModel;
   public String transformedTexture;
   public int transformedForm;

   public Transform() {
   }

   public Transform(int is, EnumSpecies newPokemon, String newTexture, IEnumForm newForm) {
      this.transformedModel = newPokemon;
      this.transformedTexture = newTexture;
      if (newForm.getFormSuffix().contains("gmax")) {
         this.transformedForm = newForm.getDefaultFromForm(newForm).getForm();
      } else {
         this.transformedForm = newForm.getForm();
      }

      this.pixelmonID = is;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pixelmonID = buffer.readInt();
      this.transformedModel = EnumSpecies.getFromDex(buffer.readInt());
      this.transformedTexture = ByteBufUtils.readUTF8String(buffer);
      this.transformedForm = buffer.readInt();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.pixelmonID);
      buffer.writeInt(this.transformedModel.getNationalPokedexInteger());
      ByteBufUtils.writeUTF8String(buffer, this.transformedTexture);
      buffer.writeInt(this.transformedForm);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(Transform message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            Transformed.applyToClientEntity(message);
         });
         return null;
      }
   }
}
