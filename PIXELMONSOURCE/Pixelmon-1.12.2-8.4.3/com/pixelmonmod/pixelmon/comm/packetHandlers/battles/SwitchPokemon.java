package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SwitchPokemon implements IMessage {
   UUID newPokemonUUID = null;
   int battleControllerIndex;
   UUID switchingPokemonUUID;
   boolean happensInstantly;

   public SwitchPokemon() {
   }

   public SwitchPokemon(UUID newPokemonUUID, int battleControllerIndex, UUID switchingPokemonUUID, boolean happensInstantly) {
      this.newPokemonUUID = newPokemonUUID;
      this.battleControllerIndex = battleControllerIndex;
      this.switchingPokemonUUID = switchingPokemonUUID;
      this.happensInstantly = happensInstantly;
   }

   public void fromBytes(ByteBuf buffer) {
      if (buffer.readBoolean()) {
         this.newPokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      }

      this.battleControllerIndex = buffer.readInt();
      this.switchingPokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.happensInstantly = buffer.readBoolean();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBoolean(this.newPokemonUUID != null);
      if (this.newPokemonUUID != null) {
         PixelmonMethods.toBytesUUID(buffer, this.newPokemonUUID);
      }

      buffer.writeInt(this.battleControllerIndex);
      PixelmonMethods.toBytesUUID(buffer, this.switchingPokemonUUID);
      buffer.writeBoolean(this.happensInstantly);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SwitchPokemon message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            BattleControllerBase bc = BattleRegistry.getBattle(message.battleControllerIndex);
            if (bc != null) {
               PlayerParticipant p = bc.getPlayer((EntityPlayer)player);
               if (p != null) {
                  UUID newPokemonUUID;
                  if (message.newPokemonUUID == null) {
                     PixelmonWrapper switching = p.getPokemonFromUUID(message.switchingPokemonUUID);
                     if (switching == null) {
                        return;
                     }

                     newPokemonUUID = p.getBattleAI().getNextSwitch(switching);
                  } else {
                     newPokemonUUID = message.newPokemonUUID;
                  }

                  bc.switchPokemon(message.switchingPokemonUUID, newPokemonUUID, message.happensInstantly);
               }
            }
         });
         return null;
      }
   }
}
