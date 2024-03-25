package com.pixelmonmod.pixelmon.comm.packetHandlers.zygarde;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PowerConstruct;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumZygarde;
import com.pixelmonmod.pixelmon.items.ItemZygardeCube;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ZygardeCubePacket implements IMessage {
   boolean learnMove = false;
   int move;
   int cubeSlot;
   StoragePosition position;

   public ZygardeCubePacket() {
   }

   public ZygardeCubePacket(StoragePosition position, int cubeSlot, int move) {
      this.learnMove = true;
      this.move = move;
      this.cubeSlot = cubeSlot;
      this.position = position;
   }

   public ZygardeCubePacket(StoragePosition position, int cubeSlot) {
      this.learnMove = false;
      this.cubeSlot = cubeSlot;
      this.position = position;
   }

   public void fromBytes(ByteBuf buf) {
      this.learnMove = buf.readBoolean();
      if (this.learnMove) {
         this.move = buf.readInt();
      }

      this.cubeSlot = buf.readInt();
      this.position = StoragePosition.decode(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.learnMove);
      if (this.learnMove) {
         buf.writeInt(this.move);
      }

      buf.writeInt(this.cubeSlot);
      this.position.encode(buf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ZygardeCubePacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
         Pokemon pokemon = storage.get(message.position);
         ItemStack cube = player.field_71071_by.func_70301_a(message.cubeSlot);
         if (cube.func_77973_b() instanceof ItemZygardeCube && pokemon != null && pokemon.getSpecies() == EnumSpecies.Zygarde) {
            if (message.learnMove) {
               ItemZygardeCube.CoreType core = ItemZygardeCube.CoreType.fromIndex(message.move);
               if (ItemZygardeCube.hasCore(cube, core)) {
                  Optional opt = AttackBase.getAttackBaseFromEnglishName(core.getMoveName());
                  opt.ifPresent((attack) -> {
                     if (!pokemon.getMoveset().hasAttack(core.getMoveName())) {
                        if (pokemon.getMoveset().size() >= 4) {
                           LearnMoveController.sendLearnMove(player, pokemon.getUUID(), attack);
                        } else {
                           pokemon.getMoveset().add(new Attack(attack));
                           ChatHandler.sendFormattedChat(player, TextFormatting.GREEN, "pixelmon.stats.learnedmove", pokemon.getDisplayName(), attack.getTranslatedName());
                        }
                     } else {
                        ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.interaction.tmknown", pokemon.getDisplayName(), attack.getTranslatedName());
                     }

                  });
               }
            } else if (pokemon.getAbility() instanceof PowerConstruct) {
               EnumZygarde form = (EnumZygarde)pokemon.getFormEnum();
               pokemon.setForm(form == EnumZygarde.TEN_PERCENT ? EnumZygarde.FIFTY_PERCENT : EnumZygarde.TEN_PERCENT);
            }
         }

      }
   }
}
