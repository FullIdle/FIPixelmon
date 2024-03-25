package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.pixelmonmod.pixelmon.util.IEncodeable;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ClientNPCData implements IEncodeable {
   private String id;
   private String texture;

   public ClientNPCData(String id, String texture) {
      this.id = id;
      this.texture = texture;
   }

   public ClientNPCData(String texture) {
      this("", texture);
   }

   public ClientNPCData(ByteBuf buffer) {
      this.decodeInto(buffer);
   }

   public String getID() {
      return this.id;
   }

   public String getTexture() {
      return this.texture;
   }

   public void encodeInto(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.id);
      ByteBufUtils.writeUTF8String(buffer, this.texture);
   }

   public void decodeInto(ByteBuf buffer) {
      this.id = ByteBufUtils.readUTF8String(buffer);
      this.texture = ByteBufUtils.readUTF8String(buffer);
   }

   public int hashCode() {
      return this.texture.hashCode();
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         ClientNPCData other = (ClientNPCData)obj;
         if (this.texture == null) {
            if (other.texture != null) {
               return false;
            }
         } else if (!this.texture.equals(other.texture)) {
            return false;
         }

         return true;
      }
   }
}
