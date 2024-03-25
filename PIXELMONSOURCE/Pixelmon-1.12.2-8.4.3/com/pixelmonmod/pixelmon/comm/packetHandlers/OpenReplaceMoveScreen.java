package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiEvolve;
import com.pixelmonmod.pixelmon.client.gui.battles.AttackData;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpenReplaceMoveScreen implements IMessage {
   UUID pokemonUUID;
   int attackId;
   boolean checkEvo;

   /** @deprecated */
   @Deprecated
   public OpenReplaceMoveScreen() {
   }

   public OpenReplaceMoveScreen(UUID pokemonUUID, AttackBase move) {
      this(pokemonUUID, move.getAttackId(), false);
   }

   public OpenReplaceMoveScreen(UUID pokemonUUID, AttackBase move, boolean checkEvo) {
      this(pokemonUUID, move.getAttackId(), checkEvo);
   }

   public OpenReplaceMoveScreen(UUID pokemonUUID, int attackId, boolean checkEvo) {
      this.pokemonUUID = pokemonUUID;
      this.attackId = attackId;
      this.checkEvo = checkEvo;
   }

   public void fromBytes(ByteBuf buf) {
      this.pokemonUUID = UUIDHelper.readUUID(buf);
      this.attackId = buf.readInt();
      this.checkEvo = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      UUIDHelper.writeUUID(this.pokemonUUID, buf);
      buf.writeInt(this.attackId);
      buf.writeBoolean(this.checkEvo);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(OpenReplaceMoveScreen message, MessageContext ctx) {
         this.processData(message);
      }

      @SideOnly(Side.CLIENT)
      private void processData(OpenReplaceMoveScreen message) {
         AttackData attackData = new AttackData(message.pokemonUUID, new Attack(message.attackId), message.checkEvo);
         if (!ClientProxy.battleManager.newAttackList.contains(attackData)) {
            ClientProxy.battleManager.newAttackList.add(attackData);
            if (!(Minecraft.func_71410_x().field_71462_r instanceof GuiBattle) && !(Minecraft.func_71410_x().field_71462_r instanceof GuiEvolve)) {
               Minecraft.func_71410_x().func_147108_a(new GuiBattle());
            }

         }
      }
   }
}
