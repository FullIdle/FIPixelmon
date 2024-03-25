package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.pixelmonmod.pixelmon.util.IEncodeable;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ClientShopkeeperData implements IEncodeable {
   private String id;
   private List textures;
   private List names;

   public ClientShopkeeperData(String id, List textures, List names) {
      this.id = id;
      this.textures = textures;
      this.names = names;
   }

   public ClientShopkeeperData(String id) {
      this(id, new ArrayList(), new ArrayList());
   }

   public ClientShopkeeperData(ByteBuf buffer) {
      this.decodeInto(buffer);
   }

   public String getID() {
      return this.id;
   }

   public List getTextures() {
      return this.textures;
   }

   public List getNames() {
      return this.names;
   }

   public void encodeInto(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.id);
      ArrayHelper.encodeStringList(buffer, this.textures);
      ArrayHelper.encodeStringList(buffer, this.names);
   }

   public void decodeInto(ByteBuf buffer) {
      this.id = ByteBufUtils.readUTF8String(buffer);
      this.textures = ArrayHelper.decodeStringList(buffer);
      this.names = ArrayHelper.decodeStringList(buffer);
   }

   public int hashCode() {
      return this.id.hashCode();
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         ClientShopkeeperData other = (ClientShopkeeperData)obj;
         if (this.id == null) {
            if (other.id != null) {
               return false;
            }
         } else if (!this.id.equals(other.id)) {
            return false;
         }

         return true;
      }
   }
}
