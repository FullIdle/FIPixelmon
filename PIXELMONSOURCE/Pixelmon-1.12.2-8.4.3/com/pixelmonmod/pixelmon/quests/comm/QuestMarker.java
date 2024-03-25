package com.pixelmonmod.pixelmon.quests.comm;

import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.util.math.BlockPos;

public class QuestMarker {
   public float x;
   public float y;
   public float z;
   public UUID uuid;
   public int dim;
   public boolean specialColor;
   public QuestColor color;
   public Type type;

   public QuestMarker(float x, float y, float z, UUID uuid, int dim, QuestColor color, Type type) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.uuid = uuid;
      this.dim = dim;
      this.color = color;
      this.type = type;
   }

   public QuestMarker(BlockPos pos, UUID uuid, int dim, QuestColor color, Type type) {
      this((float)pos.func_177958_n(), (float)pos.func_177956_o(), (float)pos.func_177952_p(), uuid, dim, color, type);
   }

   public QuestMarker(double x, double y, double z, UUID uuid, int dim, QuestColor color, Type type) {
      this((float)x, (float)y, (float)z, uuid, dim, color, type);
   }

   public QuestMarker(float x, float y, float z, int dim, QuestColor color, Type type) {
      this(x, y, z, (UUID)null, dim, color, type);
   }

   public QuestMarker(UUID uuid, int dim, QuestColor color, Type type) {
      this(0.0F, 0.0F, 0.0F, uuid, dim, color, type);
   }

   public QuestMarker(BlockPos pos, int dim, QuestColor color, Type type) {
      this((float)pos.func_177958_n(), (float)pos.func_177956_o(), (float)pos.func_177952_p(), (UUID)null, dim, color, type);
   }

   public QuestMarker(double x, double y, double z, int dim, QuestColor color, Type type) {
      this((float)x, (float)y, (float)z, (UUID)null, dim, color, type);
   }

   public QuestMarker(ByteBuf buf) {
      this.read(buf);
   }

   public void read(ByteBuf buf) {
      if (buf.readBoolean()) {
         this.uuid = UUIDHelper.readUUID(buf);
      } else {
         this.x = buf.readFloat();
         this.y = buf.readFloat();
         this.z = buf.readFloat();
      }

      this.dim = buf.readInt();
      this.specialColor = buf.readBoolean();
      if (this.specialColor) {
         this.color = new QuestColor(buf.readInt(), 0, 0);
      } else {
         this.color = new QuestColor(buf.readInt());
      }

      this.type = QuestMarker.Type.values()[buf.readByte()];
   }

   public void write(ByteBuf buf) {
      if (this.uuid != null) {
         buf.writeBoolean(true);
         UUIDHelper.writeUUID(this.uuid, buf);
      } else {
         buf.writeBoolean(false);
         buf.writeFloat(this.x);
         buf.writeFloat(this.y);
         buf.writeFloat(this.z);
      }

      buf.writeInt(this.dim);
      buf.writeBoolean(this.color.getR() < 0);
      buf.writeInt(this.color.getR() < 0 ? this.color.getR() : this.color.getRGB());
      buf.writeByte(this.type.ordinal());
   }

   public QuestColor getColor(long worldTime) {
      if (this.specialColor && this.color.getR() == -1) {
         long mod = worldTime % 120L;
         if (mod > 60L) {
            mod -= (mod - 60L) * 2L;
         }

         mod = (long)((int)((double)mod * 1.5));
         return new QuestColor(28, 65 + (int)mod, 138);
      } else {
         return this.color;
      }
   }

   public static enum Type {
      EXCLAMATION,
      QUESTION;
   }
}
