package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.gui.enums.EnumGui;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GenericGUIPacket implements IMessage {
   private GUITypes type;
   private boolean isOpen;
   private BlockPos blockPos;

   public GenericGUIPacket() {
   }

   public GenericGUIPacket(GUITypes type, boolean isOpen, BlockPos blockPos) {
      this.type = type;
      this.isOpen = isOpen;
      this.blockPos = blockPos;
   }

   public void fromBytes(ByteBuf buf) {
      this.type = GenericGUIPacket.GUITypes.values()[buf.readInt()];
      this.isOpen = buf.readBoolean();
      if (buf.readBoolean()) {
         this.blockPos = ByteBufTCG.readBlockPos(buf);
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.type.ordinal());
      buf.writeBoolean(this.isOpen);
      buf.writeBoolean(this.blockPos != null);
      if (this.blockPos != null) {
         ByteBufTCG.writeBlockPos(buf, this.blockPos);
      }

   }

   public static enum GUITypes {
      Deck,
      Battle,
      Spectate;
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(GenericGUIPacket message, MessageContext ctx) {
         EntityPlayerMP entityPlayer = ctx.getServerHandler().field_147369_b;
         entityPlayer.func_71121_q().func_152344_a(() -> {
            WorldServer w;
            TileEntityBattleController b;
            switch (message.type) {
               case Deck:
                  entityPlayer.openGui(TCG.instance, EnumGui.Deck.getIndex(), entityPlayer.func_130014_f_(), 0, 0, 0);
                  return;
               case Battle:
                  if (!message.isOpen) {
                     w = entityPlayer.func_71121_q();
                     b = (TileEntityBattleController)w.func_175625_s(message.blockPos);
                     if (b != null) {
                        PlayerServerState playerState = b.getGameServer().getPlayer(entityPlayer);
                        if (playerState != null) {
                           playerState.setInGUI(false);
                        }
                     }
                  }

                  return;
               case Spectate:
                  if (!message.isOpen) {
                     w = entityPlayer.func_71121_q();
                     b = (TileEntityBattleController)w.func_175625_s(message.blockPos);
                     if (b != null) {
                        b.getGameServer().getSpectators().remove(entityPlayer);
                     }
                  }

                  return;
               default:
            }
         });
         return null;
      }
   }
}
