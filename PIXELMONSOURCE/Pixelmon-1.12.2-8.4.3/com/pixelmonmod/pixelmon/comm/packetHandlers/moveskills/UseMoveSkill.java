package com.pixelmonmod.pixelmon.comm.packetHandlers.moveskills;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UseMoveSkill implements IMessage {
   int selectedPixelmon;
   MoveSkill moveSkill;
   int entityID;
   int x;
   int y;
   int z;
   int side;
   MoveSkill.EnumTargetType targetType;

   public UseMoveSkill() {
   }

   public UseMoveSkill(int selectedPixelmon, MoveSkill moveSkill) {
      this.selectedPixelmon = selectedPixelmon;
      this.moveSkill = moveSkill;
      this.targetType = MoveSkill.EnumTargetType.NOTHING;
   }

   public UseMoveSkill(int selectedPixelmon, MoveSkill moveSkill, int entityId, MoveSkill.EnumTargetType targetType) {
      this.selectedPixelmon = selectedPixelmon;
      this.moveSkill = moveSkill;
      this.entityID = entityId;
      this.targetType = targetType;
   }

   public UseMoveSkill(int selectedPixelmon, MoveSkill moveSkill, BlockPos pos, EnumFacing side) {
      this.selectedPixelmon = selectedPixelmon;
      this.moveSkill = moveSkill;
      this.x = pos.func_177958_n();
      this.y = pos.func_177956_o();
      this.z = pos.func_177952_p();
      this.side = side.func_176745_a();
      this.targetType = MoveSkill.EnumTargetType.BLOCK;
   }

   public void fromBytes(ByteBuf buf) {
      this.selectedPixelmon = buf.readByte();
      this.moveSkill = MoveSkill.getMoveSkillByID(ByteBufUtils.readUTF8String(buf));
      this.targetType = MoveSkill.EnumTargetType.values()[buf.readByte()];
      switch (this.targetType) {
         case MISC_ENTITY:
         case PLAYER:
         case POKEMON:
            this.entityID = buf.readInt();
            break;
         case BLOCK:
            this.x = buf.readInt();
            this.y = buf.readInt();
            this.z = buf.readInt();
            this.side = buf.readInt();
            this.targetType = MoveSkill.EnumTargetType.BLOCK;
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.selectedPixelmon);
      ByteBufUtils.writeUTF8String(buf, this.moveSkill.id);
      buf.writeByte(this.targetType.ordinal());
      switch (this.targetType) {
         case MISC_ENTITY:
         case PLAYER:
         case POKEMON:
            buf.writeInt(this.entityID);
            break;
         case BLOCK:
            buf.writeInt(this.x);
            buf.writeInt(this.y);
            buf.writeInt(this.z);
            buf.writeInt(this.side);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UseMoveSkill message, MessageContext ctx) {
         World world = ctx.getServerHandler().field_147369_b.field_70170_p;
         if (PixelmonConfig.allowExternalMoves) {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
            Pokemon pokemon = storage.get(message.selectedPixelmon);
            if (pokemon != null && pokemon.getPixelmonIfExists() != null) {
               EntityPixelmon pixelmon = pokemon.getPixelmonIfExists();
               if (pixelmon != null) {
                  MoveSkill moveSkill = message.moveSkill;
                  if (moveSkill == null || !MoveSkill.getMoveSkills(pixelmon.getPokemonData()).contains(moveSkill)) {
                     return;
                  }

                  if (pixelmon.getPokemonData().isMoveSkillCoolingDown(moveSkill)) {
                     return;
                  }

                  if (message.targetType == MoveSkill.EnumTargetType.BLOCK) {
                     pixelmon.setBlockTarget(message.x, message.y, message.z, EnumFacing.values()[message.side], moveSkill.id);
                  } else if (message.targetType != MoveSkill.EnumTargetType.NOTHING) {
                     Entity entity = world.func_73045_a(message.entityID);
                     if (entity instanceof EntityLivingBase) {
                        pixelmon.setAttackTarget((EntityLivingBase)entity, moveSkill.id);
                     }
                  } else {
                     moveSkill.onUsed(pixelmon, (Object)null, MoveSkill.EnumTargetType.NOTHING);
                  }
               }

            }
         }
      }
   }
}
