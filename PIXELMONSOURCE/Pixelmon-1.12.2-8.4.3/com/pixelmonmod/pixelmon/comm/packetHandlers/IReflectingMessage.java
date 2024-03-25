package com.pixelmonmod.pixelmon.comm.packetHandlers;

import io.netty.buffer.ByteBuf;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public interface IReflectingMessage extends IMessage {
   default void fromBytes(ByteBuf buff) {
      Field[] fields = this.getClass().getFields();
      Field[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         if (this.isValidField(field)) {
            try {
               Object value = null;
               if (field.getType() == Boolean.class) {
                  value = buff.readBoolean();
               } else if (field.getType() == Integer.class) {
                  value = buff.readInt();
               } else if (field.getType() == String.class) {
                  value = ByteBufUtils.readUTF8String(buff);
               } else if (field.getType() == Short.class) {
                  value = buff.readShort();
               } else {
                  if (field.getType() != Byte.class) {
                     throw new IllegalArgumentException("The fuck is this variable");
                  }

                  value = buff.readByte();
               }

               field.set(this, value);
            } catch (IllegalArgumentException | IllegalAccessException | IndexOutOfBoundsException var8) {
               var8.printStackTrace();
            }
         }
      }

   }

   default void toBytes(ByteBuf buff) {
      Field[] fields = this.getClass().getFields();
      Field[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         if (this.isValidField(field)) {
            try {
               if (field.getType() == Boolean.class) {
                  buff.writeBoolean(field.getBoolean(this));
               } else if (field.getType() == Integer.class) {
                  buff.writeInt(field.getInt(this));
               } else if (field.getType() == String.class) {
                  ByteBufUtils.writeUTF8String(buff, (String)field.get(this));
               } else if (field.getType() == Short.class) {
                  buff.writeShort(field.getShort(this));
               } else {
                  if (field.getType() != Byte.class) {
                     throw new IllegalArgumentException("The fuck is this variable");
                  }

                  buff.writeByte(field.getByte(this));
               }
            } catch (IllegalArgumentException | IllegalAccessException | IndexOutOfBoundsException var8) {
               var8.printStackTrace();
            }
         }
      }

   }

   default boolean isValidField(Field field) {
      boolean isPublic = Modifier.isPublic(field.getModifiers());
      boolean isStatic = Modifier.isStatic(field.getModifiers());
      return isPublic && !isStatic;
   }
}
