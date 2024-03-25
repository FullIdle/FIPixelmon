package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.items.ItemPokemonEditor;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RespondPokemonEditorAllowed implements IMessage {
   UUID editingPlayer;
   boolean allowPokemonEditors;

   public RespondPokemonEditorAllowed() {
   }

   public RespondPokemonEditorAllowed(UUID editingPlayer, boolean allowPokemonEditors) {
      this.editingPlayer = editingPlayer;
      this.allowPokemonEditors = allowPokemonEditors;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.editingPlayer);
      buf.writeBoolean(this.allowPokemonEditors);
   }

   public void fromBytes(ByteBuf buf) {
      this.editingPlayer = PixelmonMethods.fromBytesUUID(buf);
      this.allowPokemonEditors = buf.readBoolean();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(RespondPokemonEditorAllowed message, MessageContext ctx) {
         MinecraftServer server = ctx.getServerHandler().field_147369_b.func_184102_h();
         EntityPlayerMP editingPlayer = server.func_184103_al().func_177451_a(message.editingPlayer);
         EntityPlayerMP editedPlayer = ctx.getServerHandler().field_147369_b;
         if (editingPlayer != null) {
            if (!ItemPokemonEditor.checkPermission(editingPlayer)) {
               return;
            }

            if (message.allowPokemonEditors) {
               PlayerPartyStorage party = Pixelmon.storageManager.getParty(editedPlayer);
               party.retrieveAll();
               Pixelmon.network.sendTo(new SetEditingPlayer(editingPlayer.func_110124_au(), editingPlayer.getDisplayNameString()), editedPlayer);
               OpenScreen.open(editedPlayer, EnumGuiScreen.EditedPlayer);
               Pixelmon.network.sendTo(new SetEditedPlayer(editedPlayer.func_110124_au(), editedPlayer.getDisplayNameString(), party.getAll()), editingPlayer);
               OpenScreen.open(editingPlayer, EnumGuiScreen.PokemonEditor);
            } else {
               ChatHandler.sendChat(editingPlayer, I18n.func_74838_a("gui.pokemoneditor.notallowedplayer"), editedPlayer.getDisplayNameString());
            }
         }

      }
   }
}
